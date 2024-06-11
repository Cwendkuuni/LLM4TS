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

 @Test
 public void testSwitchSchema() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString()))
 .thenReturn(1);

 obOracleSessionExtension.switchSchema(connection, "schemaName");
 }

 @Test
 public void testSwitchSchema_whenCurrentSchemaEqualsTargetSchema() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, "schemaName");
 }

 @Test
 public void testGetCurrentSchema() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
 PowerMockito.any()))
 .thenReturn("currentSchema");

 assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
 PowerMockito.any()))
 .thenThrow(new RuntimeException());

 when(connection instanceof OceanBaseConnection)
 .thenReturn(true);

 assertEquals("serverThreadId", obOracleSessionExtension.getConnectionId(connection));

 when(connection instanceof OceanBaseConnection)
 .thenReturn(false);

 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}