import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

private OBOracleSessionExtension obOracleSessionExtension;
private Connection connection;
private JdbcTemplate jdbcTemplate;

@Before
public void setup() {
connection = mock(Connection.class);
jdbcTemplate = mock(JdbcTemplate.class);
obOracleSessionExtension = new OBOracleSessionExtension() {
@Override
public JdbcTemplate getJdbcOperations(Connection connection) {
return jdbcTemplate;
}
};
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
Mockito.verify(jdbcTemplate).execute(anyString());
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "testSchema";
obOracleSessionExtension.switchSchema(connection, schemaName);
String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getQuoteIdentifier(schemaName);
Mockito.verify(jdbcTemplate).update(alterSql);
}

@Test
public void testGetCurrentSchema() {
when(jdbcTemplate.queryForObject(anyString(), Mockito.eq(String.class))).thenReturn("testSchema");
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals("testSchema", currentSchema);
}

@Test
public void testGetConnectionId() {
when(jdbcTemplate.queryForObject(anyString(), Mockito.eq(String.class))).thenThrow(new RuntimeException());
when(connection instanceof OceanBaseConnection).thenReturn(true);
OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(1234L);
when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
assertEquals("1234", connectionId);
}
}