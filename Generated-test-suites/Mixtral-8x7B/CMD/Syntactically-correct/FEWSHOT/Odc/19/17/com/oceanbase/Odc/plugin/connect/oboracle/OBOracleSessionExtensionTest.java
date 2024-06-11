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

@SuppressWarnings("rawtypes")
public class OBOracleSessionExtensionTest {

 private Connection connection;
 private JdbcOperationsUtil jdbcOperationsUtil;
 private SessionExtensionPoint obOracleSessionExtension;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "12345";
 obOracleSessionExtension.killQuery(connection, connectionId);
 PowerMockito.verifyStatic(jdbcOperationsUtil);
 jdbcOperationsUtil.execute(PowerMockito.eq("KILL QUERY " + connectionId));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 String currentSchema = "currentSchema";
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
 PowerMockito.verifyStatic(jdbcOperationsUtil);
 jdbcOperationsUtil.update(PowerMockito.eq(alterSql));
 }

 @Test
 public void testSwitchSchema_sameSchema() throws SQLException {
 String schemaName = "testSchema";
 String currentSchema = schemaName;
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 PowerMockito.verifyStatic(jdbcOperationsUtil, PowerMockito.never());
 jdbcOperationsUtil.update(PowerMockito.anyString());
 }

 @Test
 public void testGetCurrentSchema() {
 String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 String currentSchema = "currentSchema";
 when(jdbcOperationsUtil.queryForObject(querySql, String.class, connection)).thenReturn(currentSchema);
 assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = "12345";
 when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class, connection))
 .thenReturn(connectionId);
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionId_exception() throws SQLException {
 PowerMockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
 String connectionId = null;
 when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class, connection))
 .thenThrow(new SQLException());
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
 }
}