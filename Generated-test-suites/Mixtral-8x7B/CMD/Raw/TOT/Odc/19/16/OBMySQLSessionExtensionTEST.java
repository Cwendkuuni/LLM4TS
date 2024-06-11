import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

 private OBMySQLSessionExtension obMySQLSessionExtension;
 private Connection connection;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 connection = mock(Connection.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 obMySQLSessionExtension.killQuery(connection, "queryId");
 verify(connection).createStatement();
 verify(connection).execute("KILL QUERY queryId");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 obMySQLSessionExtension.switchSchema(connection, "newSchema");
 verify(connection).setCatalog("newSchema");
 }

 @Test
 public void testSwitchSchemaWhenSameSchema() throws SQLException {
 when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("existingSchema");
 obMySQLSessionExtension.switchSchema(connection, "existingSchema");
 verify(connection, never()).setCatalog(anyString());
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 when(connection.createStatement()).thenThrow(new SQLException());
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(connection.getClass().getMethod("getServerThreadId")).thenReturn(Integer.class.getMethod("intValue"));

 String connectionId = obMySQLSessionExtension.getConnectionId(connection);

 verify(connection).createStatement();
 verify(connection).getClass();
 verify(connection).getServerThreadId();

 assertEquals(Integer.toString(((OceanBaseConnection) connection).getServerThreadId()), connectionId);
 }

 @Test
 public void testGetConnectionIdWhenNoException() throws SQLException {
 when(connection.createStatement()).thenReturn(mock(Connection.class));

 String connectionId = obMySQLSessionExtension.getConnectionId(connection);

 verify(connection, never()).getServerThreadId();
 assertEquals(connectionId, connection.createStatement().toString());
 }
}