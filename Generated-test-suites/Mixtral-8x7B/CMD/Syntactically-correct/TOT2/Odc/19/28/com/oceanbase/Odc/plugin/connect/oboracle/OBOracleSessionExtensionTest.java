import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock
private JdbcOperationsUtil jdbcOperationsUtil;

@InjectMocks
private OBOracleSessionExtension oBOracleSessionExtension;

private Connection connection;

@Before
public void setup() {
connection = mock(Connection.class);
}

@Test
public void testKillQuery_withValidConnectionId_shouldExecuteKillQuery() throws SQLException {
oBOracleSessionExtension.killQuery(connection, "123456");
verify(jdbcOperationsUtil).getJdbcOperations(connection);
verify(jdbcOperationsUtil).execute(eq("KILL QUERY 123456"));
}

@Test
public void testSwitchSchema_withSameSchemaName_shouldDoNothing() throws SQLException {
oBOracleSessionExtension.switchSchema(connection, "SCHEMA_A");
verify(jdbcOperationsUtil, never()).update(anyString());
}

@Test
public void testSwitchSchema_withDifferentSchemaName_shouldAlterSession() throws SQLException {
when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.JdbcOperations.class));
oBOracleSessionExtension.switchSchema(connection, "SCHEMA_B");
verify(jdbcOperationsUtil).getJdbcOperations(connection);
verify(jdbcOperationsUtil.getJdbcOperations(connection)).update(eq("ALTER SESSION SET CURRENT_SCHEMA=SCHEMA_B"));
}

@Test
public void testGetCurrentSchema_withConnection_shouldReturnCurrentSchema() {
when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.JdbcOperations.class));
when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenReturn("SCHEMA_A");
assertEquals("SCHEMA_A", oBOracleSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId_withConnection_shouldReturnConnectionId() {
when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.JdbcOperations.class));
when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new RuntimeException());
when(connection instanceof OceanBaseConnection).thenReturn(true);
OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(123456L);
when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
when(connection.getClass().getDeclaredField("oceanBaseConnection")).thenReturn(oceanBaseConnection.getClass().getDeclaredField("oceanBaseConnection"));
when(connection.getClass().getDeclaredField("oceanBaseConnection").get(connection)).thenReturn(oceanBaseConnection);
assertEquals("123456", oBOracleSessionExtension.getConnectionId(connection));
}
}