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

 private static final String CONNECTION_ID = "123456";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CURRENT_SCHEMA = "current_schema";
 private static final String QUOTED_SCHEMA = "\"test_schema\"";
 private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=\"" + SCHEMA_NAME + "\"";
 private Connection connection;
 private SessionExtensionPoint obOracleSessionExtension;

 @Before
 public void setUp() throws Exception {
 connection = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 PowerMockito.mockStatic(StringUtils.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_connection_id";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 obOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 when(StringUtils.quoteOracleIdentifier(SCHEMA_NAME)).thenReturn(QUOTED_SCHEMA);
 when(getCurrentSchema(connection)).thenReturn(CURRENT_SCHEMA);
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testSwitchSchema_whenCurrentSchemaEqualsTargetSchema_thenDoNothing() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 when(getCurrentSchema(connection)).thenReturn(SCHEMA_NAME);
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(
 "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(CURRENT_SCHEMA);
 assertEquals(CURRENT_SCHEMA, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
 when(
 ((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123456);
 assertEquals(CONNECTION_ID, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionId_whenConnectionIsNotInstanceOfOceanBaseConnection_thenReturnNull() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(com.oceanbase.odc.common.util.JdbcOperations.class));
 when(connection.getClass().getName()).thenReturn(Connection.class.getName());
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }

}