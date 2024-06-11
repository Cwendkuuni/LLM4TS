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

 private Connection connection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 obOracleSessionExtension.killQuery(connection, "connectionId");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(
 "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)))
 .thenReturn(1);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testSwitchSchemaWhenCurrentSchemaIsTheSame() throws SQLException {
 String schemaName = "testSchema";
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
 String.class)).thenReturn("currentSchema");
 assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual",
 String.class)).thenThrow(new SQLException());
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1234L);
 assertEquals("1234", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWhenQueryForObjectIsSuccess() throws SQLException {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual",
 String.class)).thenReturn("connectionId");
 assertEquals("connectionId", obOracleSessionExtension.getConnectionId(connection));
 }
}