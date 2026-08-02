package dave.graphql.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import dave.graphql.repository.AccountRepository;

/**
 * Builds and caches the GraphQL engine at application startup.
 * Data fetchers delegate to the AccountRepository EJB for JPA queries.
 */
@ApplicationScoped
public class GraphQLSchemaConfig {

    @EJB
    private AccountRepository accountRepository;

    private GraphQL graphQL;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();

        InputStream schemaStream = getClass().getResourceAsStream("/graphql/schema.graphqls");
        Reader reader = new InputStreamReader(schemaStream, StandardCharsets.UTF_8);

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(reader);

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder
                .dataFetcher("account", env -> {
                    String name = env.getArgument("name");
                    return accountRepository.findByName(name);
                })
                .dataFetcher("accounts", env -> accountRepository.findAll())
            )
            .build();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
