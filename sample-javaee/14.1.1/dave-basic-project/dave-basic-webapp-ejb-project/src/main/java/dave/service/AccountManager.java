/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import dave.entity.Account;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * EJB Business Interface
 */
@Local
@Remote
public interface AccountManager {

  public void depositOnAccount(String name, float amount);
  
  public Account findAccount(String name);
  
}
