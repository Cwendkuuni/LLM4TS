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
import org.springframework.jdbc.core.JdbcOperations;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock private PluginWrapper pluginWrapper;
@Mock private JdbcOperations jdbcOperations;
@InjectMocks private OBOracleSessionExtension obOracleSessionExtension;

private Connection connection;

@Before
public void setup() {
connection = mock(Connection.class);
JdbcOperationsUtil.setJdbcOperations(connection, jdbcOperations);
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
verify(jdbcOperations).execute(eq("KILL QUERY " + connectionId));
}

@Test
public void testSwitchSchema_sameSchema() {
when(jdbcOperations.queryForObject(any(), eq(String.class))).thenReturn("test_schema");
obOracleSessionExtension.switchSchema(connection, "test_schema");
verify(jdbcOperations).update(eq("ALTER SESSION SET CURRENT_SCHEMA=test_schema"));
}

@Test
public void testSwitchSchema_differentSchema() throws SQLException {
when(jdbcOperations.queryForObject(any(), eq(String.class))).thenReturn("test_schema1");
obOracleSessionExtension.switchSchema(connection, "test_schema2");
verify(jdbcOperations).update(eq("ALTER SESSION SET CURRENT_SCHEMA=test_schema2"));
}

@Test
public void testGetCurrentSchema() {
when(jdbcOperations.queryForObject(any(), eq(String.class))).thenReturn("test_schema");
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
verify(jdbcOperations).queryForObject(any(), eq(String.class));
assert Objects.equals(currentSchema, "test_schema");
}

@Test
public void testGetConnectionId_connectionSupportsUserenv() {
when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
.thenReturn(mock(java.sql.ResultSet.class));
String connectionId = obOracleSessionExtension.getConnectionId(connection);
verify(connection).createStatement();
verify(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).close();
assert Objects.nonNull(connectionId);
}

@Test
public void testGetConnectionId_connectionDoesNotSupportUserenv() {
when(connection.getClass().getName()).thenReturn("java.sql.Connection");
when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
.thenThrow(mock(SQLException.class));
String connectionId = obOracleSessionExtension.getConnectionId(connection);
verify(connection).createStatement();
verify(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).close();
verify(pluginWrapper).getPlugin(any());
assert Objects.nonNull(connectionId);
}
}