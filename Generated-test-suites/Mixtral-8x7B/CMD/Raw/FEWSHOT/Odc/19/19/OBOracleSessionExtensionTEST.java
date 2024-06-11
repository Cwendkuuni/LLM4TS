package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 private OBOracleSessionExtension obOracleSessionExtension;

 private Connection connection;

 @Before
 public void setup() {
 obOracleSessionExtension = new OBOracleSessionExtension();
 connection = mock(Connection.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 obOracleSessionExtension.killQuery(connection, "connectionId");
 }

 @Test(expected = SQLException.class)
 public void testKillQueryNullConnection() throws SQLException {
 obOracleSessionExtension.killQuery(null, "connectionId");
 }

 @Test(expected = SQLException.class)
 public void testKillQueryNullConnectionId() throws SQLException {
 obOracleSessionExtension.killQuery(connection, null);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(JdbcOperationsUtil.escapeSql("ALTER SESSION SET CURRENT_SCHEMA=?",
 "com.oceanbase.odc.common.util.StringUtils"))).thenReturn(1);
 obOracleSessionExtension.switchSchema(connection, "schemaName");
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaNullConnection() throws SQLException {
 obOracleSessionExtension.switchSchema(null, "schemaName");
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaNullSchemaName() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, null);
 }

 @Test
 public void testGetCurrentSchema() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(JdbcOperationsUtil.escapeSql("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
 "com.oceanbase.odc.common.util.JdbcOperationsUtil") , String.class)).thenReturn("currentSchema");
 assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetCurrentSchemaNull() {
 assertNull(obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345L);
 assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdJdbc() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(JdbcOperationsUtil.escapeSql("select userenv('sessionid') from dual",
 "com.oceanbase.odc.common.util.JdbcOperationsUtil"), String.class)).thenReturn("jdbcConnectionId");
 assertEquals("jdbcConnectionId", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdNull() {
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}