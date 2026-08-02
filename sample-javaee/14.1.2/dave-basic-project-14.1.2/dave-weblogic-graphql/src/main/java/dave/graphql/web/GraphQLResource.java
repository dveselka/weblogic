package dave.graphql.web;

import com.fasterxml.jackson.core.type.TypeReference;
import graphql.ExecutionInput;
import graphql.ExecutionResult;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import dave.graphql.config.GraphQLSchemaConfig;

@Path("/graphql")
public class GraphQLResource {

    @Inject
    private GraphQLSchemaConfig schemaConfig;

    /**
     * Executes a GraphQL query. Request body: {"query": "...", "variables": {...}, "operationName": "..."}
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response execute(String body) {
        try {
            Map<String, Object> request = schemaConfig.getObjectMapper()
                .readValue(body, new TypeReference<Map<String, Object>>() {});

            String query = (String) request.get("query");
            if (query == null || query.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"errors\":[{\"message\":\"Missing 'query' field\"}]}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) request.get("variables");
            String operationName = (String) request.get("operationName");

            ExecutionInput.Builder inputBuilder = ExecutionInput.newExecutionInput().query(query);
            if (variables != null) {
                inputBuilder.variables(variables);
            }
            if (operationName != null) {
                inputBuilder.operationName(operationName);
            }

            ExecutionResult result = schemaConfig.getGraphQL().execute(inputBuilder.build());
            String json = schemaConfig.getObjectMapper().writeValueAsString(result.toSpecification());

            return Response.ok(json).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            String error = "{\"errors\":[{\"message\":\"" + e.getMessage().replace("\"", "'") + "\"}]}";
            return Response.serverError().entity(error).type(MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * Returns an embedded GraphiQL UI for interactive query testing.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response graphiql() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <title>GraphiQL - WebLogic GraphQL</title>
              <link rel="stylesheet" href="https://unpkg.com/graphiql/graphiql.min.css" />
            </head>
            <body style="margin: 0; height: 100vh; overflow: hidden;">
              <div id="graphiql" style="height: 100vh;"></div>
              <script crossorigin src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
              <script crossorigin src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
              <script crossorigin src="https://unpkg.com/graphiql/graphiql.min.js"></script>
              <script>
                const fetcher = GraphiQL.createFetcher({ url: window.location.pathname });
                const root = ReactDOM.createRoot(document.getElementById('graphiql'));
                root.render(React.createElement(GraphiQL, { fetcher: fetcher }));
              </script>
            </body>
            </html>
            """;
        return Response.ok(html).type(MediaType.TEXT_HTML).build();
    }
}
