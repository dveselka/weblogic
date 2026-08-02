package dave.graphql.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAVEN_ARCHETYPE_SAMPLE_ACCOUNT")
@NamedQuery(name = "graphql.findAllAccounts", query = "SELECT a FROM Account a")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

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
