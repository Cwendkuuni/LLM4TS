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
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock
private PluginWrapper pluginWrapper;

@Mock
private Connection connection;

@Mock
private JdbcTemplate jdbcTemplate;

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

@Before
public void setup() {
JdbcOperationsUtil.setJdbcOperations(connection, jdbcTemplate);
}

@Test
public void testKillQuery() throws Exception {
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
verify(jdbcTemplate, times(1)).execute("KILL QUERY " + connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "test_schema";
String currentSchema = "default_schema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
obOracleSessionExtension.switchSchema(connection, schemaName);
String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getQuotedIdentifier(schemaName);
verify(jdbcTemplate, times(1)).update(alterSql);
}

@Test
public void testSwitchSchema_sameSchema() throws SQLException {
String schemaName = "test_schema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obOracleSessionExtension.switchSchema(connection, schemaName);
verify(jdbcTemplate, times(0)).update(anyString());
}

@Test
public void testGetCurrentSchema() throws Exception {
when(jdbcTemplate.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
.thenReturn("test_schema");
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals("test_schema", currentSchema);
}

@Test
public void testGetConnectionId_connectionIsNull() {
String connectionId = obOracleSessionExtension.getConnectionId(null);
assertNull(connectionId);
}

@Test
public void testGetConnectionId_connectionIsOceanBaseConnection() {
long threadId = 123456;
when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
when(connection.getServerThreadId()).thenReturn(threadId);
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals(Long.toString(threadId), connectionId);
}

@Test(expected = SQLException.class)
public void testGetConnectionId_connectionIsNotOceanBaseConnection() throws Exception {
when(connection.getClass().getName()).thenReturn(Connection.class.getName());
obOracleSessionExtension.getConnectionId(connection);
}
}