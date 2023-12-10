/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package org.dave.jsf;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * CDI Managed Bean Class
 */
@Named
@RequestScoped
public class AccountBean{

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

  @Resource(mappedName = "myConnectionFactory")
  private ConnectionFactory connectionFactory;

  @Resource(mappedName = "myJmsQueue")
  private Queue accountQueue;

  public void deposit() throws JMSException{
    try{
      Connection connection = connectionFactory.createConnection();
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
