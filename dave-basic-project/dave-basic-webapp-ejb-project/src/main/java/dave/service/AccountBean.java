/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import java.text.NumberFormat;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dave.entity.Account;
import dave.interceptor.OnDeposit;

/**
 * CDI Managed Bean Class
 */
@Named
@RequestScoped
public class AccountBean{
    
  private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();  

  private String name;
  
  private float amount;
  
  private String msg;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Inject
  private AccountManager accountManager;

  @OnDeposit
  public void deposit() {
    accountManager.depositOnAccount(name, amount);
    Account account = accountManager.findAccount(name);
    msg = "The money has been deposited to " + account.getName() + ", the balance of the account is " + currencyFormatter.format(account.getAmount());
  }
}
