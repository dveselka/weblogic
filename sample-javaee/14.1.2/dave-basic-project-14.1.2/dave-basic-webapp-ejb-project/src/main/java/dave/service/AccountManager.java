/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import javax.ejb.Local;
import dave.entity.Account;

/**
 * EJB Business Interface - Local interface for CDI injection
 */
@Local
public interface AccountManager {

  public void depositOnAccount(String name, float amount);
  
  public Account findAccount(String name);
  
}

