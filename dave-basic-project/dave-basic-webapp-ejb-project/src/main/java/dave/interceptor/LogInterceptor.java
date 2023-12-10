/* Copyright (c) 2012,2013, Oracle and/or its affiliates. All rights reserved.  */
package dave.interceptor;

import java.text.NumberFormat;
import javax.interceptor.Interceptor;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import dave.service.AccountBean;

/**
 * Interceptor Class
 * 
 */
@OnDeposit
@Interceptor
public class LogInterceptor {

  private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
  
  @AroundInvoke
  public Object doLog(InvocationContext ctx) throws Exception {
    AccountBean account = (AccountBean)ctx.getTarget(); 
    String msg = String.format("[%s] The money has been deposited to %s, the balance of the account is %s",
                               this.getClass().getName(),
                               account.getName(),
                               currencyFormatter.format(account.getAmount()));
    System.out.println(msg);
    return ctx.proceed();
  }
}
