import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

 private PluginWrapper pluginWrapper;
 private OBOracleSessionExtension obOracleSessionExtension;
 private Connection connection;
 private JdbcTemplate jdbcTemplate;

 @Before
 public void setup() {
 pluginWrapper = mock(PluginWrapper.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 connection = mock(Connection.class);
 jdbcTemplate = new JdbcTemplate();
 JdbcOperationsUtil.setJdbcOperations(connection, jdbcTemplate);
 }

 @Test
 public void testKillQuery() throws SQLException {
 obOracleSessionExtension.killQuery(connection, "connectionId");
 verify(jdbcTemplate).execute("KILL QUERY connectionId");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, "schemaName");
 String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=schemaName";
 verify(jdbcTemplate).update(alterSql);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 when(jdbcTemplate.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
 .thenReturn("currentSchema");
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 verify(jdbcTemplate).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
 assertEquals("currentSchema", currentSchema);
 }

 @Test
 public void testGetConnectionId() {
 when(connection.getClass().getName()).thenReturn("OceanBaseConnection");
 when(connection.getClass().getMethod("getServerThreadId")).thenReturn(Integer.class.getMethod("intValue"));
 when(connection.getClass().getMethod("getServerThreadId").invoke(connection)).thenReturn(12345);
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertEquals("12345", connectionId);
 }

 @Test
 public void testGetConnectionIdException() {
 when(connection.getClass().getName()).thenReturn("Connection");
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertEquals(null, connectionId);
 }
}