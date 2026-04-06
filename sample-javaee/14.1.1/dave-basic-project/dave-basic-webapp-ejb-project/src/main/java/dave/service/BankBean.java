/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import dave.entity.Account;
import dave.entity.Bank;
import dave.interceptor.OnDeposit;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.NumberFormat;

/**
 * CDI Managed Bean Class
 */
@Named
@RequestScoped
public class BankBean {
    
  private String name;
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Inject
  private BankManager bankManager;

  public Bank findBank(String name) {
    Bank bank = bankManager.findBank(name);
   System.out.println("Found bank" + bank.getName());
   return bank;
  }
}
