/* Copyright (c) 2012,2013, Oracle and/or its affiliates. All rights reserved.  */
package dave.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAVEN_ARCHETYPE_SAMPLE_ACCOUNT")
@NamedQuery(name = "findAllAccounts", query = "SELECT a FROM Account a")
public class Account {
  @Id
  private String name;
  
  private float amount;

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
  
  
}
