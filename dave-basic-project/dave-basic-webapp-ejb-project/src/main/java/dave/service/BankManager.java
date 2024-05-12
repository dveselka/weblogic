/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import dave.entity.Bank;

/**
 * EJB Business Interface
 */
public interface BankManager {

  public Bank findBank(String name);
  
}
