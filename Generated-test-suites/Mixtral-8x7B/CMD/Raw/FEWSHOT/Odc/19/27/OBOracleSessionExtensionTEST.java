package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@org.powermock.modules.junit4.PowerMockRunner
public class OBOracleSessionExtensionTest {

 private static final String CONNECTION_ID = "12345";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CURRENT_SCHEMA = "current_schema";
 private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(SCHEMA_NAME);
 private static final String QUERY_SQL = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 private static final String QUERY_FOR_OBJECT_EXCEPTION_MESSAGE = "queryForObject returned null, but exception was expected";
 private Connection connection;
 private SessionExtensionPoint obOracleSessionExtension;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 JdbcOperationsUtil.setJdbcOperations(connection, JdbcOperationsUtil.getJdbcOperations(connection));
 }

 @Test
 public void testKillQuery() {
 obOracleSessionExtension.killQuery(connection, CONNECTION_ID);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 PowerMockito.doNothing().when(connection).createStatement();
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(ALTER_SQL)).thenReturn(1);
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaException() throws SQLException {
 PowerMockito.doNothing().when(connection).createStatement();
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(ALTER_SQL)).thenThrow(new SQLException(QUERY_FOR_OBJECT_EXCEPTION_MESSAGE));
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testGetCurrentSchema() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(QUERY_SQL, String.class)).thenReturn(CURRENT_SCHEMA);
 assertEquals(CURRENT_SCHEMA, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetCurrentSchemaException() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(QUERY_SQL, String.class)).thenThrow(new RuntimeException(QUERY_FOR_OBJECT_EXCEPTION_MESSAGE));
 assertNull(obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(QUERY_SQL, String.class)).thenReturn(CONNECTION_ID);
 assertEquals(CONNECTION_ID, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdOceanBaseConnection() {
 OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(67890);
 when(connection.getClass()).thenReturn(OceanBaseConnection.class);
 when(connection.getClass().cast(connection)).thenReturn(oceanBaseConnection);
 assertEquals("67890", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdException() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(QUERY_SQL, String.class)).thenThrow(new RuntimeException(QUERY_FOR_OBJECT_EXCEPTION_MESSAGE));
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}