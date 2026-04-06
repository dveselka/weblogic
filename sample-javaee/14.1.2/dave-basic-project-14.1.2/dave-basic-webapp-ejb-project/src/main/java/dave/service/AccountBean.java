/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import java.sql.Connection;
import java.text.NumberFormat;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

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
    try {
      depositJms();
    } catch (JMSException e) {
      e.printStackTrace();
      msg = "Deposit successful, but failed to send JMS message: " + e.getMessage();
    }
  }


  @Resource(mappedName = "myConnectionFactory")
  private ConnectionFactory connectionFactory;

  @Resource(mappedName = "myJmsQueue")
  private Queue accountQueue;

  public void depositJms() throws JMSException{
    try{
      javax.jms.Connection connection = connectionFactory.createConnection();
      Session accountSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer messageProducer = accountSession.createProducer(accountQueue);
      
      Message message = accountSession.createMessage();
      message.setStringProperty("name", name);
      message.setFloatProperty("amount", amount);
      
      messageProducer.send(message);
    } catch (JMSException e) {
      e.printStackTrace();
      throw e;
    }
    
    msg = "A message with the deposit request has been sent, please check the console output of the server to verify it was received";
  }

}
