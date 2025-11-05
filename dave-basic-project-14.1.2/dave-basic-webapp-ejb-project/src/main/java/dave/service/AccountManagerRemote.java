/**
 * @author Copyright (c) 2008,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import javax.ejb.Remote;
import dave.entity.Account;

/**
 * EJB Business Interface
 */
public interface AccountManagerRemote {

  public void depositOnAccount(String name, float amount);
  
  public Account findAccount(String name);
  
}

