/**
 * External Java EJB Client for AccountManager
 */
package dave.client;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dave.service.AccountManagerRemote;
import dave.entity.Account;

public class AccountManagerClient {

    private static final String WEBLOGIC_JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
    private static final String PROVIDER_URL = "t3://localhost:7001";
    
    /**
     * JNDI lookup patterns for WebLogic:
     * 
     * Pattern 1 (Portable Global JNDI):
     * java:global/<app-name>/<module-name>/<bean-name>!<fully-qualified-interface-name>
     * Example: java:global/basicWebappEjb/AccountManagerImpl!dave.service.AccountManagerRemote
     * 
     * Pattern 2 (WebLogic-specific):
     * <ejb-name>#<fully-qualified-interface-name>
     * Example: AccountManagerImpl#dave.service.AccountManagerRemote
     * 
     * Pattern 3 (Application-scoped):
     * java:app/<module-name>/<bean-name>!<fully-qualified-interface-name>
     */
    // Multiple JNDI names to try - WebLogic registers EJBs with multiple patterns
    private static final String[] JNDI_NAMES = {
        "java:global/basicWebappEjb/AccountManagerImpl!dave.service.AccountManagerRemote",
        "java:global/basicWebappEjb/AccountManagerImpl",
        "basicWebappEjb/AccountManagerImpl!dave.service.AccountManagerRemote",
        "ejb/AccountManagerImpl#dave.service.AccountManagerRemote"
    };
    
    public static void main(String[] args) {
        AccountManagerClient client = new AccountManagerClient();
        try {
            client.testEJB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testEJB() throws NamingException {
        Context context = null;
        try {
            // Get initial context
            context = getInitialContext();
            
            // Try multiple JNDI names
            AccountManagerRemote accountManager = null;
            String successfulJndiName = null;
            
            for (String jndiName : JNDI_NAMES) {
                try {
                    System.out.println("Trying JNDI name: " + jndiName);
                    accountManager = (AccountManagerRemote) context.lookup(jndiName);
                    successfulJndiName = jndiName;
                    System.out.println("✓ Successfully looked up AccountManagerRemote EJB at: " + jndiName);
                    break;
                } catch (NamingException e) {
                    System.out.println("✗ Failed: " + e.getMessage());
                }
            }
            
            if (accountManager == null) {
                throw new NamingException("Could not find EJB with any of the attempted JNDI names");
            }
            
            // Test deposit
            String accountName = "john.doe";
            float depositAmount = 100.50f;
            
            System.out.println("Depositing $" + depositAmount + " to account: " + accountName);
            accountManager.depositOnAccount(accountName, depositAmount);
            
            // Test find
            Account account = accountManager.findAccount(accountName);
            if (account != null) {
                System.out.println("Account found: " + account.getName() + 
                                   ", Balance: $" + account.getAmount());
            } else {
                System.out.println("Account not found");
            }
            
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Create InitialContext for WebLogic Server
     */
    private Context getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, WEBLOGIC_JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        
        // Add credentials - required for WebLogic security
        env.put(Context.SECURITY_PRINCIPAL, "weblogic");
        env.put(Context.SECURITY_CREDENTIALS, "weblogic123");
        
        return new InitialContext(env);
    }
}
