/**
 * JNDI Tree Browser - Lists all JNDI bindings
 */
package dave.client;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class JndiTreeBrowser {

    private static final String WEBLOGIC_JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
    private static final String PROVIDER_URL = "t3://localhost:7001";
    
    public static void main(String[] args) {
        JndiTreeBrowser browser = new JndiTreeBrowser();
        try {
            browser.listJndiTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void listJndiTree() throws NamingException {
        Context context = getInitialContext();
        
        System.out.println("========================================");
        System.out.println("JNDI Tree Browser");
        System.out.println("========================================\n");
        
        // List common JNDI contexts
        String[] contexts = {"", "java:global", "java:app", "java:module"};
        
        for (String ctx : contexts) {
            try {
                System.out.println("\n--- Listing: " + (ctx.isEmpty() ? "Root Context" : ctx) + " ---");
                listContext(context, ctx);
            } catch (NamingException e) {
                System.out.println("Cannot list context '" + ctx + "': " + e.getMessage());
            }
        }
        
        context.close();
    }
    
    private void listContext(Context context, String name) throws NamingException {
        try {
            NamingEnumeration<NameClassPair> list;
            
            if (name.isEmpty()) {
                list = context.list("");
            } else {
                Context subContext = (Context) context.lookup(name);
                list = subContext.list("");
            }
            
            while (list.hasMore()) {
                NameClassPair pair = list.next();
                String fullName = name.isEmpty() ? pair.getName() : name + "/" + pair.getName();
                System.out.println("  " + fullName + " (" + pair.getClassName() + ")");
                
                // Try to list if it's a context
                if (pair.getClassName().contains("Context")) {
                    try {
                        listSubContext(context, fullName, 1);
                    } catch (Exception e) {
                        // Skip if can't list
                    }
                }
            }
        } catch (NamingException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
    
    private void listSubContext(Context context, String name, int depth) throws NamingException {
        if (depth > 2) return; // Limit recursion depth
        
        try {
            Context subContext = (Context) context.lookup(name);
            NamingEnumeration<NameClassPair> list = subContext.list("");
            
            String indent = "  " + "  ".repeat(depth);
            
            while (list.hasMore()) {
                NameClassPair pair = list.next();
                String fullName = name + "/" + pair.getName();
                System.out.println(indent + fullName + " (" + pair.getClassName() + ")");
            }
        } catch (Exception e) {
            // Skip
        }
    }
    
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
