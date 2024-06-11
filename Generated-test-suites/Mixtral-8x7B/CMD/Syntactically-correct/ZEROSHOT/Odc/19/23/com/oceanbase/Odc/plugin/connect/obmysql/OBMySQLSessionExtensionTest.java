import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

@Mock
private PluginWrapper pluginWrapper;

@Mock
private Connection connection;

@Mock
private JdbcOperations jdbcOperations;

@InjectMocks
@Extension
@Slf4j
private OBMySQLSessionExtension obmysqlSessionExtension = spy(new OBMySQLSessionExtension());

@Before
public void setup() {
obmysqlSessionExtension.setPluginWrapper(pluginWrapper);
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "1";
obmysqlSessionExtension.killQuery(connection, connectionId);
verify(jdbcOperations, times(1)).execute(anyString());
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "test";
when(obmysqlSessionExtension.getCurrentSchema(connection)).thenReturn("default");
doNothing().when(connection).setCatalog(schemaName);
obmysqlSessionExtension.switchSchema(connection, schemaName);
verify(connection, times(1)).setCatalog(schemaName);
}

@Test
public void testGetCurrentSchema() {
when(jdbcOperations.queryForObject(anyString(), any())).thenReturn("test");
assertEquals("test", obmysqlSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() {
when(jdbcOperations.queryForObject(anyString(), any())).thenThrow(mock(Exception.class));
when(connection instanceof OceanBaseConnection).thenReturn(true);
OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(1);
when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
when(connection.getClass().cast(connection)).thenReturn(oceanBaseConnection);
assertEquals("1", obmysqlSessionExtension.getConnectionId(connection));
}
}