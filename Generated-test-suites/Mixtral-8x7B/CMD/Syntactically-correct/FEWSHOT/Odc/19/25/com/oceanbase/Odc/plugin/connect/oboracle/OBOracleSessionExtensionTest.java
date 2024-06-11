package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

 private Connection mockConnection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setup() {
 mockConnection = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenReturn(mock(
 JdbcOperationsUtil.class));
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123456";
 obOracleSessionExtension.killQuery(mockConnection, connectionId);
 verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute(anyString());
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(mockConnection, schemaName);
 verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).update(anyString());
 }

 @Test
 public void testGetCurrentSchema() {
 String expected = "test_schema";
 when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), any()))
 .thenReturn(expected);
 String actual = obOracleSessionExtension.getCurrentSchema(mockConnection);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetConnectionId() {
 String expected = "123456";
 when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), any()))
 .thenThrow(new RuntimeException());
 when(mockConnection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
 when(
 ((OceanBaseConnection) mockConnection).getServerThreadId()
 ).thenReturn(Integer.parseInt(expected));
 String actual = obOracleSessionExtension.getConnectionId(mockConnection);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetConnectionIdWithException() {
 when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), any()))
 .thenThrow(new RuntimeException());
 when(mockConnection.getClass().getName()).thenReturn("other.Connection");
 String actual = obOracleSessionExtension.getConnectionId(mockConnection);
 assertNull(actual);
 }
}