package dave.graphql.repository;

import java.util.List;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dave.graphql.entity.Account;

/**
 * Reads account data from the shared Derby database used by dave-basic-webapp-ejb-project.
 * The datasource connects to the same examples DB on Derby port 1527.
 */
@Stateless
@DataSourceDefinition(
    name = "java:module/env/graphqlDataSource",
    className = "org.apache.derby.jdbc.ClientXADataSource",
    portNumber = 1527,
    serverName = "localhost",
    databaseName = "examples",
    user = "examples",
    password = "examples",
    properties = {"create=true", "weblogic.TestTableName=SQL SELECT 1 FROM SYS.SYSTABLES"}
)
public class AccountRepository {

    @PersistenceContext(unitName = "GraphQLPU")
    private EntityManager em;

    public Account findByName(String name) {
        return em.find(Account.class, name);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findAll() {
        return em.createNamedQuery("graphql.findAllAccounts").getResultList();
    }
}
