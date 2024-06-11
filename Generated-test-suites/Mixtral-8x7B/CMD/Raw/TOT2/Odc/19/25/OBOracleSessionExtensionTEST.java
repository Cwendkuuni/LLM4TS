import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private JdbcOperationsUtil jdbcOperationsUtil;

 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtension;

 private Connection connection;

 @Before
 public void setup() {
 connection = mock(Connection.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123456";
 obOracleSessionExtension.killQuery(connection, connectionId);
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 jdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 String currentSchema = "currentSchema";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(currentSchema);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
 jdbcOperationsUtil.update(alterSql);
 }

 @Test
 public void testSwitchSchemaSameSchema() throws SQLException {
 String schemaName = "currentSchema";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(schemaName);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 String schemaName = "testSchema";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String querySql = "select userenv('sessionid') from dual";
 String connectionId = "123456";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(connectionId);
 String connectionIdResult = obOracleSessionExtension.getConnectionId(connection);
 assertEquals(connectionId, connectionIdResult);
 }

 @Test
 public void testGetConnectionIdException() throws SQLException {
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new SQLException());
 String connectionIdResult = obOracleSessionExtension.getConnectionId(connection);
 assertNull(connectionIdResult);
 }

 @Test
 public void testGetConnectionIdOceanBaseConnection() throws SQLException {
 String connectionId = "123456";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new SQLException());
 ((OceanBaseConnection) connection).setServerThreadId(Integer.parseInt(connectionId));
 String connectionIdResult = obOracleSessionExtension.getConnectionId(connection);
 assertEquals(connectionId, connectionIdResult);
 }
}