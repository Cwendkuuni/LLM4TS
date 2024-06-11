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
 private static final String CURRENT_SCHEMA = "original_schema";
 private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=test_schema";
 private static final String QUERY_SQL = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 private Connection connection = mock(Connection.class);
 private OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 private JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 private SessionExtensionPoint obOracleSessionExtension = PowerMockito.spy(new OBOracleSessionExtension());

 @Before
 public void setup() throws SQLException {
 when(connection.getClass()).thenReturn(OceanBaseConnection.class);
 when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(QUERY_SQL, String.class)).thenReturn(CURRENT_SCHEMA);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(CONNECTION_ID));
 PowerMockito.doReturn(jdbcOperationsUtil).when(obOracleSessionExtension, "getJdbcOperationsUtil", connection);
 }

 @Test
 public void testKillQuery() throws SQLException {
 obOracleSessionExtension.killQuery(connection, CONNECTION_ID);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getJdbcOperationsUtil", connection);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getCurrentSchema", connection);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getJdbcOperationsUtil").withArguments(connection);
 PowerMockito.verifyPrivate(jdbcOperationsUtil).update(ALTER_SQL);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String result = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(CURRENT_SCHEMA, result);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getJdbcOperationsUtil", connection);
 PowerMockito.verifyPrivate(jdbcOperationsUtil).queryForObject(QUERY_SQL, String.class);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String result = obOracleSessionExtension.getConnectionId(connection);
 assertEquals(CONNECTION_ID, result);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getJdbcOperationsUtil", connection);
 PowerMockito.verifyPrivate(jdbcOperationsUtil).queryForObject(QUERY_SQL, String.class);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getConnectionId", oceanBaseConnection);
 }

 @Test
 public void testGetConnectionIdOceanBaseConnection() throws SQLException {
 when(connection.unwrap(OceanBaseConnection.class)).thenReturn(null);
 String result = obOracleSessionExtension.getConnectionId(connection);
 assertNull(result);
 PowerMockito.verifyPrivate(obOracleSessionExtension).invoke("getJdbcOperationsUtil", connection);
 PowerMockito.verifyPrivate(jdbcOperationsUtil).queryForObject(QUERY_SQL, String.class);
 PowerMockito.verifyPrivate(obOracleSessionExtension, never()).invoke("getConnectionId", oceanBaseConnection);
 }
}