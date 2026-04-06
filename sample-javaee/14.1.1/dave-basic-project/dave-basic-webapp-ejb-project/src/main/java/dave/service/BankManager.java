/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import dave.entity.Bank;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * EJB Business Interface
 */
@Local
@Remote
public interface BankManager {

  public Bank findBank(String name);
  
}
