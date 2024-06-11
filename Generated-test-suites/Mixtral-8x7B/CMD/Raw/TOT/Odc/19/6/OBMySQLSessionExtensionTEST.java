import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

 private OBMySQLSessionExtension obmysqlSessionExtension;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 obmysqlSessionExtension = new OBMySQLSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 // Create a mock Connection object
 Connection connection = Mockito.mock(Connection.class);

 // Define the behavior of the connection object
 Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

 // Call the method under test
 obmysqlSessionExtension.killQuery(connection, "12345");

 // Verify the expected SQL statement was executed
 Mockito.verify(connection.createStatement()).execute("KILL QUERY 12345");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 // Create a mock Connection object
 Connection connection = Mockito.mock(Connection.class);

 // Define the behavior of the connection object
 when(connection.getCatalog()).thenReturn("initialSchema");

 // Call the method under test
 obmysqlSessionExtension.switchSchema(connection, "newSchema");

 // Verify that the catalog was set to the new schema
 verify(connection).setCatalog("newSchema");
 }

 @Test
 public void testSwitchSchema_sameSchema() throws SQLException {
 // Create a mock Connection object
 Connection connection = Mockito.mock(Connection.class);

 // Define the behavior of the connection object
 when(connection.getCatalog()).thenReturn("newSchema");

 // Call the method under test
 obmysqlSessionExtension.switchSchema(connection, "newSchema");

 // Verify that the method does not modify the catalog if it's already set to the new schema
 verify(connection, never()).setCatalog(anyString());
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 // Create a mock Connection object
 Connection connection = Mockito.mock(Connection.class);

 // Define the behavior of the connection object
 when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
 when(connection.getCatalog()).thenReturn("testSchema");
 when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
 when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
 when(connection.createStatement().executeQuery(anyString()).getString(1)).thenReturn("testSchema");

 // Call the method under test
 String result = obmysqlSessionExtension.getCurrentSchema(connection);

 // Verify the result
 assertEquals("testSchema", result);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 // Create a mock OceanBaseConnection object
 OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);

 // Call the method under test
 String result = obmysqlSessionExtension.getConnectionId(oceanBaseConnection);

 // Verify the result
 assertEquals("12345", result);
 }

 @Test
 public void testGetConnectionId_noConnectionId() {
 // Call the method under test
 String result = obmysqlSessionExtension.getConnectionId(Mockito.mock(Connection.class));

 // Verify that the method returns null if the connection does not have a connection ID
 Assertions.assertNull(result);
 }
}