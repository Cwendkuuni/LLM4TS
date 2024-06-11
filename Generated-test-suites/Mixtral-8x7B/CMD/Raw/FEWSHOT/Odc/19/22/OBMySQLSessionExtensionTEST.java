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
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private OceanBaseConnection oceanBaseConnection;
 private JdbcOperationsUtil jdbcOperationsUtil;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 oceanBaseConnection = mock(OceanBaseConnection.class);
 jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "12345";
 OBMySQLSessionExtension.killQuery(connection, connectionId);
 assertEquals("KILL QUERY " + connectionId, jdbcOperationsUtil.getLastExecutedQuery());
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = "original_schema";
 when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
 OBMySQLSessionExtension.switchSchema(connection, schemaName);
 assertEquals(schemaName, JdbcOperationsUtil.getLastExecutedQuery());
 }

 @Test
 public void testSwitchSchema_sameSchema() throws SQLException {
 String schemaName = "original_schema";
 String currentSchema = "original_schema";
 when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
 OBMySQLSessionExtension.switchSchema(connection, schemaName);
 assertEquals(null, JdbcOperationsUtil.getLastExecutedQuery());
 }

 @Test
 public void testGetCurrentSchema() {
 String querySql = "SELECT DATABASE()";
 String currentSchema = "test_schema";
 when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(currentSchema);
 assertEquals(currentSchema, OBMySQLSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = "12345";
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.parseInt(connectionId));
 assertEquals(connectionId, OBMySQLSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionId_null() {
 when(connection.getClass()).thenReturn(Connection.class);
 assertNull(OBMySQLSessionExtension.getConnectionId(connection));
 }
}