import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

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
 // Add assertions if needed
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String currentSchema = "currentSchema";
 String newSchema = "newSchema";
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(StringUtils.quoteOracleIdentifier(newSchema)))
 .thenReturn(1);
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
 obOracleSessionExtension.switchSchema(connection, newSchema);
 // Add assertions if needed
 }

 @Test
 public void testSwitchSchemaNoChange() throws SQLException {
 String currentSchema = "currentSchema";
 String newSchema = "currentSchema";
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
 obOracleSessionExtension.switchSchema(connection, newSchema);
 // Add assertions if needed
 }

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = "currentSchema";
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
 String.class)).thenReturn(currentSchema);
 assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = "connectionId";
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual",
 String.class)).thenReturn(connectionId);
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdMockOceanBaseConnection() throws SQLException {
 long threadId = 1234;
 String connectionId = String.valueOf(threadId);
 OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(threadId);
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual",
 String.class)).thenReturn(null);
 when(oceanBaseConnection.getServerThreadId() + "").thenReturn(connectionId);
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdException() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual",
 String.class)).thenThrow(new SQLException("Test exception"));
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}