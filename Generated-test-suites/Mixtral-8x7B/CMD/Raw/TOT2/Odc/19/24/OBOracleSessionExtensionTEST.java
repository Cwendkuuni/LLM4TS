import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

@Mock
private JdbcOperationsUtil jdbcOperationsUtil;

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

@Test
@DisplayName("Test killQuery method with valid connectionId")
public void testKillQueryWithValidConnectionId() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
Mockito.verify(jdbcOperationsUtil).execute(Mockito.eq("KILL QUERY " + connectionId), Mockito.any(Connection.class));
}

@Test
@DisplayName("Test killQuery method with null connectionId")
public void testKillQueryWithNullConnectionId() {
Connection connection = Mockito.mock(Connection.class);
assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
}

@Test
@DisplayName("Test switchSchema method with changing schema")
public void testSwitchSchemaWithChangingSchema() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
String schemaName = "newSchema";
String currentSchema = "oldSchema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
obOracleSessionExtension.switchSchema(connection, schemaName);
String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.quoteOracleIdentifier(schemaName);
Mockito.verify(jdbcOperationsUtil).update(Mockito.eq(alterSql), Mockito.any(Connection.class));
}

@Test
@DisplayName("Test switchSchema method with same schema")
public void testSwitchSchemaWithSameSchema() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
String schemaName = "oldSchema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obOracleSessionExtension.switchSchema(connection, schemaName);
Mockito.verifyNoInteractions(jdbcOperationsUtil);
}

@Test
@DisplayName("Test switchSchema method with null schemaName")
public void testSwitchSchemaWithNullSchemaName() {
Connection connection = Mockito.mock(Connection.class);
assertThrows(NullPointerException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
}

@Test
@DisplayName("Test getCurrentSchema method with valid connection")
public void testGetCurrentSchemaWithValidConnection() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
String schemaName = "testSchema";
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);
assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
}

@Test
@DisplayName("Test getCurrentSchema method with null connection")
public void testGetCurrentSchemaWithNullConnection() {
assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
}

@Test
@DisplayName("Test getConnectionId method with valid connection")
public void testGetConnectionIdWithValidConnection() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
String connectionId = "123456";
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
}

@Test
@DisplayName("Test getConnectionId method with null connection")
public void testGetConnectionIdWithNullConnection() {
Connection connection = null;
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(connection));
}

@Test
@DisplayName("Test getConnectionId method with exception")
public void testGetConnectionIdWithException() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals("Connection-" + ((OceanBaseConnection) connection).getServerThreadId(), connectionId);
}
}