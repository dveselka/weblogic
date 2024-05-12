/**
 * @author Copyright (c) 2010,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import dave.entity.Account;
import dave.entity.Bank;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
//Data Source defined for JPA. It assume the derby database is started up and listen to localhost:1527
@DataSourceDefinition(name = "java:module/env/mavenArchetypeDataSource", className = "org.apache.derby.jdbc.ClientXADataSource", portNumber = 1527, serverName = "localhost", databaseName = "examples", user = "examples", password = "examples", properties={"create=true", "weblogic.TestTableName=SQL SELECT 1 FROM SYS.SYSTABLES"})
public class BankManagerImpl implements BankManager {

  @PersistenceContext
  private EntityManager em;
  
  public Bank findBank(String name) {
    Bank bank = em.find(Bank.class, name);
    if (bank == null) {
      bank = new Bank();
      bank.setName(name);
    }
    bank.setName(bank.getName());
    em.persist(bank);

    return  bank;
  }

}
