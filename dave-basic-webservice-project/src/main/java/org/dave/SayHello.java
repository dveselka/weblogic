package org.dave;

// Import the standard JWS annotation interfaces

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

// Standard JWS annotation that specifies that the name of the Web Service is
// "Simple" and the targetNamespace used in the generated WSDL is 
// "http://example.org"
@WebService(name="SayHello", targetNamespace="http://example.org", serviceName = "SayHello")

// Standard JWS annotation that specifies the mapping of the service onto the
// SOAP message protocol.
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT,
             use=SOAPBinding.Use.LITERAL,
             parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)

/**
 * This JWS file forms the basis of a simple WebLogic Web Service with a 
 * single operation: sayHello
 *
 * @author Copyright (c) 1999,2013, Oracle and/or its affiliates. All rights reserved.
 */

public class SayHello {

  // Required constructor

  public SayHello() {}

  // Standard JWS annotation that specifies that the method should be exposed
  // as a public operation.  Because the annotation does not include the
  // member-value "operationName", the public name of the operation is the
  // same as the method name: sayHello.

  @WebMethod()
  public String sayHello(String message) {
    System.out.println("sayHello:" + message);
    return "Here is the message: '" + message + "'";
  }
}
