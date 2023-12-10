/**
 * @author Copyright (c) 2010,2013, Oracle and/or its affiliates. All rights reserved.
 *  
 */
package dave.service;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dave.entity.Account;
import oracle.jdbc.OracleConnection;
import oracle.sql.ArrayDescriptor;
import oracle.sql.SQLName;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Stateless
//Data Source defined for JPA. It assume the derby database is started up and listen to localhost:1527
@DataSourceDefinition(name = "java:module/env/mavenArchetypeDataSource", className = "org.apache.derby.jdbc.ClientXADataSource", portNumber = 1527, serverName = "localhost", databaseName = "examples", user = "examples", password = "examples", properties = {
		"create=true", "weblogic.TestTableName=SQL SELECT 1 FROM SYS.SYSTABLES" })
public class AccountManagerImpl implements AccountManager {

	@PersistenceContext
	private EntityManager em;

	public void depositOnAccount(String name, float amount) {
		Account account = em.find(Account.class, name);
		if (account == null) {
			account = new Account();
			account.setName(name);
		}
		account.setAmount(account.getAmount() + amount);
		em.persist(account);
	}

	public Account findAccount(String name) {

		em.getTransaction().begin();
		Connection connection = em.unwrap(java.sql.Connection.class);

		try {
			useDeprecatedOracleConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			useOracleConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return em.find(Account.class, name);
	}

	private void useDeprecatedOracleConnection(java.sql.Connection connection) throws SQLException {

		OracleConnection oracleConnection = null;

		if (connection.isWrapperFor(OracleConnection.class)) {
			oracleConnection = connection.unwrap(OracleConnection.class);
		} else {
			// recover, not an oracle connection
		}

		Object[] reportArray = new Object[3];
		STRUCT[] struct = new STRUCT[1];

		ArrayDescriptor arrayDescriptor = new ArrayDescriptor(
				new SQLName("T_REPORT_TABLE", (OracleConnection) connection), connection);
		StructDescriptor structDescriptor = StructDescriptor.createDescriptor("R_REPORT_OBJECT", connection);

		oracle.sql.ARRAY reportsArray = new oracle.sql.ARRAY(arrayDescriptor, connection, struct);

	}

	private void useOracleConnection(java.sql.Connection connection) throws SQLException {

		OracleConnection oracleConnection = null;

		if (connection.isWrapperFor(OracleConnection.class)) {
			oracleConnection = connection.unwrap(OracleConnection.class);
		} else {
			// recover, not an oracle connection
		}

		Object[] reportArray = new Object[3];
		Struct[] struct = new Struct[1];

		int arrayIndex = 0;

		struct[arrayIndex++] = connection.createStruct("R_REPORT_OBJECT", reportArray);

		Array reportsArray = ((OracleConnection) connection).createOracleArray("T_REPORT_TABLE", struct);

	}
}
