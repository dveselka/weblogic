/* Copyright (c) 2012,2013, Oracle and/or its affiliates. All rights reserved.  */
package dave.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAVEN_ARCHETYPE_SAMPLE_BANKI")
@NamedQuery(name = "findAllBanks", query = "SELECT name FROM Bank b")
public class Bank {
  @Id
  private String name;
  
  private float amount;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
