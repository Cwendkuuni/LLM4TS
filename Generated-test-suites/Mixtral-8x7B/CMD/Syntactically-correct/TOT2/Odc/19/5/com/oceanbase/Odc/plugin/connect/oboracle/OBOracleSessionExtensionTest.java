import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private PluginWrapper pluginWrapper;

 @Mock
 private Connection connection;

 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtensionSpy;

 @Before
 public void setup() {
 mockStatic(JdbcOperationsUtil.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123456";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 obOracleSessionExtensionSpy.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = "initial_schema";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 when(obOracleSessionExtensionSpy.getCurrentSchema(connection)).thenReturn(currentSchema);
 obOracleSessionExtensionSpy.switchSchema(connection, schemaName);
 }

 @Test
 public void testSwitchSchemaWhenCurrentSchemaIsTheSame() throws SQLException {
 String schemaName = "test_schema";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 when(obOracleSessionExtensionSpy.getCurrentSchema(connection)).thenReturn(schemaName);
 obOracleSessionExtensionSpy.switchSchema(connection, schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWhenSQLExceptionOccurs() throws SQLException {
 String schemaName = "test_schema";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException());
 obOracleSessionExtensionSpy.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String expectedSchema = "test_schema";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
 .thenReturn(expectedSchema);
 String actualSchema = obOracleSessionExtensionSpy.getCurrentSchema(connection);
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String expectedConnectionId = "123456";
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 when(connection.getClass().getName().contains("OceanBaseConnection")).thenReturn(true);
 when(connection.unwrap(OceanBaseConnection.class)).thenReturn(mock(OceanBaseConnection.class));
 when(mock(OceanBaseConnection.class).getServerThreadId()).thenReturn(Integer.parseInt(expectedConnectionId));
 String actualConnectionId = obOracleSessionExtensionSpy.getConnectionId(connection);
 assertEquals(expectedConnectionId, actualConnectionId);
 }

 @Test
 public void testGetConnectionIdWhenNoConnectionIdIsAvailable() throws SQLException {
 when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(JdbcOperationsUtil.createJdbcOperations());
 when(connection.getClass().getName().contains("OceanBaseConnection")).thenReturn(false);
 String actualConnectionId = obOracleSessionExtensionSpy.getConnectionId(connection);
 assertEquals(Optional.empty(), Optional.ofNullable(actualConnectionId));
 }
}