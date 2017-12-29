package org.dave.jms.destination;

import java.text.NumberFormat;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Copyright (c) 2011,2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Message driven bean used to process messages.
 */

@MessageDriven(
  mappedName = "myJmsQueue",
  activationConfig = {
    @ActivationConfigProperty(
      propertyName  = "destinationType", 
      propertyValue = "javax.jms.Queue"),   
      
    @ActivationConfigProperty(
      propertyName  = "acknowledgeMode",
      propertyValue = "Auto-acknowledge")
  }
)
public class QueueMDB implements MessageListener {
    
  private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();    
  
  @Resource
  private MessageDrivenContext mdctx;
  
  public QueueMDB() {
    
  }
  
  public void onMessage(Message message) {
    try{
      System.out.println("The money has been deposited to " + message.getStringProperty("name") +  ", for the amount of " + currencyFormatter.format(message.getFloatProperty("amount")));
        
    } catch (Exception e) {
      e.printStackTrace();
      mdctx.setRollbackOnly();
    }
  }

}
