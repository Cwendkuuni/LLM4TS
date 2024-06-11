import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock
private JdbcOperationsUtil jdbcOperationsUtil;

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

private Connection connection;

@Before
public void setup() {
connection = Mockito.mock(Connection.class);
when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "12345";
obOracleSessionExtension.killQuery(connection, connectionId);
Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).execute(anyString());
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "test_schema";
String currentSchema = "test_schema2";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
obOracleSessionExtension.switchSchema(connection, schemaName);
String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getQuoteIdentifier(schemaName);
Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).update(alterSql);
}

@Test
public void testSwitchSchema_sameSchema() throws SQLException {
String schemaName = "test_schema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obOracleSessionExtension.switchSchema(connection, schemaName);
Mockito.verify(jdbcOperationsUtil, Mockito.times(0)).update(anyString());
}

@Test
public void testGetCurrentSchema() {
when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("test_schema");
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals("test_schema", currentSchema);
}

@Test
public void testGetConnectionId() {
when(connection instanceof OceanBaseConnection).thenReturn(true);
Statement statement = Mockito.mock(Statement.class);
when(connection.createStatement()).thenReturn(statement);
when(statement.executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
when(statement.executeQuery(anyString()).next()).thenReturn(true).thenReturn(false);
when(statement.executeQuery(anyString()).getString(1)).thenReturn("12345");
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals("12345", connectionId);
}

@Test
public void testGetConnectionId_exception() {
when(connection instanceof OceanBaseConnection).thenReturn(false);
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals(null, connectionId);
}
}