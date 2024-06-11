import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

@Mock
private Connection connection;

private OBOracleSessionExtension obOracleSessionExtension;

@BeforeEach
public void setup() {
obOracleSessionExtension = new OBOracleSessionExtension();
}

@Test
public void testKillQuery() throws SQLException {
obOracleSessionExtension.killQuery(connection, "connectionId");
Mockito.verify(connection).createStatement().execute("KILL QUERY connectionId");
}

@Test
public void testSwitchSchema_withSameSchemaName_shouldReturn() throws SQLException {
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("testSchema");
obOracleSessionExtension.switchSchema(connection, "testSchema");
Mockito.verifyNoMoreInteractions(connection);
}

@Test
public void testSwitchSchema_withDifferentSchemaName() throws SQLException {
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("oldSchema");
when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
obOracleSessionExtension.switchSchema(connection, "newSchema");
Mockito.verify(connection.createStatement()).execute("ALTER SESSION SET CURRENT_SCHEMA=newSchema");
}

@Test
public void testGetCurrentSchema_whenQuerySuccessful() {
when(obOracleSessionExtension.getConnectionId(connection)).thenReturn("connectionId");
when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals("connectionId", currentSchema);
}

@Test
public void testGetConnectionId_whenConnectionIsInstanceOfOceanBaseConnection() {
OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
when(connection.getInstance()).thenReturn(oceanBaseConnection);
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals("12345", connectionId);
}

@Test
public void testGetConnectionId_whenConnectionIsNotInstanceOfOceanBaseConnection() {
when(connection.getClass()).thenReturn(Object.class);
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertNull(connectionId);
}
}