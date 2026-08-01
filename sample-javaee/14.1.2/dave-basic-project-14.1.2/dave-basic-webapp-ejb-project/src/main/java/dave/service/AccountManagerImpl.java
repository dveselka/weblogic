/**
 * @author Copyright (c) 2010,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.sql.DataSourceDefinition;

import dave.entity.Account;

/**
 * Option 1: Use name attribute to define the EJB name
 * The JNDI name will be: java:global/<app-name>/<ejb-name>!<interface>
 * 
 * Option 2: Use mappedName for explicit JNDI binding (WebLogic-specific)
 * mappedName = "ejb/AccountManager"
 */
@Stateless(name = "AccountManagerEJB")
@Local(AccountManager.class)
@Remote(AccountManagerRemote.class)
//Data Source defined for JPA. It assume the derby database is started up and listen to localhost:1527
@DataSourceDefinition(name = "java:module/env/mavenArchetypeDataSource", className = "org.apache.derby.jdbc.ClientXADataSource", portNumber = 1527, serverName = "localhost", databaseName = "examples", user = "examples", password = "examples", properties={"create=true", "weblogic.TestTableName=SQL SELECT 1 FROM SYS.SYSTABLES"})
public class AccountManagerImpl implements AccountManager, AccountManagerRemote {

  private static final Logger LOGGER = Logger.getLogger(AccountManagerImpl.class.getName());

  @PersistenceContext
  private EntityManager em;
  
  public void depositOnAccount(String name, float amount) {
    String serverName = System.getProperty("weblogic.Name", "unknown-server");
    LOGGER.info("[" + serverName + "] depositOnAccount called for account='" + name + "', amount=" + amount);

    Account account = em.find(Account.class, name);
    if (account == null) {
      account = new Account();
      account.setName(name);
      LOGGER.info("[" + serverName + "] Creating new account record for '" + name + "'");
    }

    account.setAmount(account.getAmount() + amount);
    em.persist(account);
    LOGGER.info("[" + serverName + "] Account '" + name + "' balance updated to " + account.getAmount());
  }
  
  public Account findAccount(String name) {
    String serverName = System.getProperty("weblogic.Name", "unknown-server");
    LOGGER.info("[" + serverName + "] findAccount called for account='" + name + "'");

    Account account = em.find(Account.class, name);
    if (account == null) {
      LOGGER.info("[" + serverName + "] Account not found for '" + name + "'");
    } else {
      LOGGER.info("[" + serverName + "] Account found for '" + name + "' with balance=" + account.getAmount());
    }
    return account;
  }
}
