import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

@Test
public void testKillQuery() throws SQLException {
Connection connection = mock(Connection.class);
String connectionId = "1234";
obOracleSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
Connection connection = mock(Connection.class);
String schemaName = "testSchema";
obOracleSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchema() throws SQLException {
Connection connection = mock(Connection.class);
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
.thenReturn("testSchema");
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals("testSchema", currentSchema);
}

@Test
public void testGetConnectionId() throws SQLException {
Connection connection = mock(Connection.class);
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
.thenThrow(new SQLException());
when(connection instanceof OceanBaseConnection)
.thenReturn(true);
OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId())
.thenReturn(5678);
when(connection.getClass())
.thenReturn(oceanBaseConnection.getClass());
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals("5678", connectionId);
}

@Test
public void testGetConnectionIdException() {
Connection connection = mock(Connection.class);
when(connection instanceof OceanBaseConnection)
.thenReturn(false);
assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));
}
}