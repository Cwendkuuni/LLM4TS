package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

@org.powermock.modules.junit4.PowerMockRunner
public class OBOracleSessionExtensionTest {

 private static final String CONNECTION_ID = "123456";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CURRENT_SCHEMA = "original_schema";
 private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(SCHEMA_NAME);
 private static final String QUERY_SQL = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 private static final String QUERY_FOR_OBJECT_EXCEPTION_MSG = "Expected exception when calling queryForObject()";
 private static final String GET_JDBC_OPERATIONS_EXCEPTION_MSG = "Expected exception when calling getJdbcOperations()";
 private Connection connection;
 private JdbcOperationsUtil jdbcOperationsUtil;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 }

 @Test
 public void testKillQuery() {
 String connectionId = "123";
 JdbcOperationsUtil.execute = (conn, sql) -> {};
 OBOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void testKillQueryWithException() {
 String connectionId = "123";
 JdbcOperationsUtil.execute = (conn, sql) -> {
 throw new RuntimeException();
 };
 assertThrows(RuntimeException.class, () -> OBOracleSessionExtension.killQuery(connection, connectionId));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 OBOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testSwitchSchemaWithException() throws SQLException {
 when(jdbcOperationsUtil.update(ALTER_SQL)).thenThrow(new SQLException(GET_JDBC_OPERATIONS_EXCEPTION_MSG));
 assertThrows(SQLException.class, () -> OBOracleSessionExtension.switchSchema(connection, SCHEMA_NAME));
 }

 @Test
 public void testGetCurrentSchema() {
 when(jdbcOperationsUtil.queryForObject(QUERY_SQL, String.class)).thenReturn(CURRENT_SCHEMA);
 String currentSchema = OBOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(CURRENT_SCHEMA, currentSchema);
 }

 @Test
 public void testGetCurrentSchemaWithException() {
 when(jdbcOperationsUtil.queryForObject(QUERY_SQL, String.class)).thenThrow(new RuntimeException(QUERY_FOR_OBJECT_EXCEPTION_MSG));
 assertThrows(RuntimeException.class, () -> OBOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 when(jdbcOperationsUtil.queryForObject(QUERY_SQL, String.class)).thenReturn(CONNECTION_ID);
 String connectionId = OBOracleSessionExtension.getConnectionId(connection);
 assertEquals(CONNECTION_ID, connectionId);
 }

 @Test
 public void testGetConnectionIdWithException() {
 when(jdbcOperationsUtil.queryForObject(QUERY_SQL, String.class)).thenThrow(new RuntimeException(QUERY_FOR_OBJECT_EXCEPTION_MSG));
 when(connection instanceof OceanBaseConnection)
 .thenReturn(true);
 OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(123456);
 when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
 when(connection.equals(oceanBaseConnection)).thenReturn(true);
 String connectionId = OBOracleSessionExtension.getConnectionId(connection);
 assertEquals(CONNECTION_ID, connectionId);
 }
}