/**
 * @author Copyright (c) 2010,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.sql.DataSourceDefinition;

import dave.entity.Account;

@Stateless
//Data Source defined for JPA. It assume the derby database is started up and listen to localhost:1527
@DataSourceDefinition(name = "java:module/env/mavenArchetypeDataSource", className = "org.apache.derby.jdbc.ClientXADataSource", portNumber = 1527, serverName = "localhost", databaseName = "examples", user = "examples", password = "examples", properties={"create=true", "weblogic.TestTableName=SQL SELECT 1 FROM SYS.SYSTABLES"})
public class AccountManagerImpl implements AccountManager {

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
