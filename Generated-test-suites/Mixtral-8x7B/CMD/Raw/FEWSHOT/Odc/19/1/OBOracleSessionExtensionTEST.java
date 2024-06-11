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

@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
public class OBOracleSessionExtensionTest {

 @Extension
 @Slf4j
 public static class TestOBOracleSessionExtension extends OBOracleSessionExtension {
 }

 private Connection connection;
 private TestOBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 obOracleSessionExtension = new TestOBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 obOracleSessionExtension.killQuery(connection, "123");
 PowerMockito.verifyStatic(JdbcOperationsUtil.class);
 JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY 123");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString())).thenReturn(1);
 obOracleSessionExtension.switchSchema(connection, "schema1");
 PowerMockito.verifyStatic(StringUtils.class);
 StringUtils.quoteOracleIdentifier("schema1");
 PowerMockito.verifyStatic(JdbcOperationsUtil.class);
 JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=schema1");
 }

 @Test
 public void testGetCurrentSchema() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
 .thenReturn("schema1");
 assertEquals("schema1", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
 .thenThrow(new RuntimeException());
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
 when(obConnection.getServerThreadId()).thenReturn(123);
 when(connection = obConnection).thenReturn(obConnection);
 assertEquals("123", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdNoException() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
 .thenReturn("123");
 assertEquals("123", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdNull() {
 PowerMockito.mockStatic(JdbcOperationsUtil.class);
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
 .thenThrow(new RuntimeException());
 when(connection instanceof OceanBaseConnection).thenReturn(false);
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}