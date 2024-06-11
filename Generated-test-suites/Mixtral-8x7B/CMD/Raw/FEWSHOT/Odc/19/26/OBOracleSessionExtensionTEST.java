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
 private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(SCHEMA_NAME);
 private static final String QUERY_SQL = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 private Connection connection;
 private SessionExtensionPoint extension;

 @Before
 public void setup() throws SQLException {
 connection = mock(Connection.class);
 extension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() {
 String connectionId = "123";
 JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(ALTER_SQL)).thenReturn(1);
 PowerMockito.mockStatic(StringUtils.class);
 when(StringUtils.quoteOracleIdentifier(SCHEMA_NAME)).thenReturn(SCHEMA_NAME);
 extension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testSwitchSchemaWhenSchemaIsCurrent() throws SQLException {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(getCurrentSchema(connection)).thenReturn(SCHEMA_NAME);
 extension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testGetCurrentSchema() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(QUERY_SQL, String.class)).thenReturn(CURRENT_SCHEMA);
 assertEquals(CURRENT_SCHEMA, extension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(connection.getClass()).thenReturn(OceanBaseConnection.class);
 when(connection.getClass().getConstructor(Connection.class)).thenReturn(oceanBaseConnection.getClass().getConstructor(Connection.class));
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.parseInt(CONNECTION_ID));
 assertEquals(CONNECTION_ID, extension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWhenException() throws SQLException {
 when(connection.getClass()).thenReturn(Connection.class);
 assertEquals(null, extension.getConnectionId(connection));
 }
}