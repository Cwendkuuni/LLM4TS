import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoint;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

 private OBOracleSessionExtension obOracleSessionExtension;
 private JdbcOperations jdbcOperations;
 private Connection connection;

 @Before
 public void setup() {
 DataSource dataSource = new SingleConnectionDataSource("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");
 jdbcOperations = mock(JdbcOperations.class);
 connection = mock(Connection.class);
 when(dataSource.getConnection()).thenReturn(connection);
 when(jdbcOperations.getJdbcTemplate()).thenReturn(connection);
 obOracleSessionExtension = new OBOracleSessionExtension();
 obOracleSessionExtension.setJdbcOperationsUtil(new JdbcOperationsUtil(jdbcOperations));
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "1";
 obOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testschema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
 String schemaName = "";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetConnectionIdWithException() throws SQLException {
 when(jdbcOperations.queryForObject(mock(String.class), mock(Class.class))).thenThrow(new SQLException());
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertEquals(null, connectionId);
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }
}