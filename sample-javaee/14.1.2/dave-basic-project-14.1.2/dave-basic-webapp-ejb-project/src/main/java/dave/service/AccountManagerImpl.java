/**
 * @author Copyright (c) 2010,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

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

  @PersistenceContext
  private EntityManager em;
  
  public void depositOnAccount(String name, float amount) {
    Account account = em.find(Account.class, name);
    if (account == null) {
      account = new Account();
      account.setName(name);
    }
    account.setAmount(account.getAmount() + amount);
    em.persist(account);
  }
  
  public Account findAccount(String name) {
    return em.find(Account.class, name);
  }
}
