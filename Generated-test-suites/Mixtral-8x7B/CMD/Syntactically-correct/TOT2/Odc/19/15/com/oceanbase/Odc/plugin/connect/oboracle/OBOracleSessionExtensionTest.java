import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock
private Connection connection;

@Mock
private JdbcOperationsUtil jdbcOperationsUtil;

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

@Before
public void setup() {
jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.jdbcOperationsUtil = jdbcOperationsUtil;
}

// Test for killQuery method
@Test
public void testKillQuery() throws SQLException {
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
verify(jdbcOperationsUtil, times(1)).execute(anyString());
}

// Test for switchSchema method with typical use case
@Test
public void testSwitchSchemaTypicalUseCase() throws SQLException {
String schemaName = "testSchema";
obOracleSessionExtension.switchSchema(connection, schemaName);
verify(jdbcOperationsUtil, times(1)).update(anyString());
}

// Test for switchSchema method with edge case - schemaName is null
@Test(expected = SQLException.class)
public void testSwitchSchemaEdgeCaseSchemaNameIsNull() throws SQLException {
obOracleSessionExtension.switchSchema(connection, null);
}

// Test for switchSchema method with exception handling scenario
@Test
public void testSwitchSchemaExceptionScenario() throws SQLException {
when(jdbcOperationsUtil.update(anyString())).thenThrow(new SQLException());
obOracleSessionExtension.switchSchema(connection, "testSchema");
verify(jdbcOperationsUtil, times(1)).update(anyString());
}

// Test for getCurrentSchema method with typical use case
@Test
public void testGetCurrentSchemaTypicalUseCase() {
String expectedSchema = "testSchema";
when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
verify(jdbcOperationsUtil, times(1)).queryForObject(anyString(), eq(String.class));
assert(Objects.equals(expectedSchema, actualSchema));
}

// Test for getCurrentSchema method with exception handling scenario
@Test
public void testGetCurrentSchemaExceptionScenario() {
when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
obOracleSessionExtension.getCurrentSchema(connection);
verify(jdbcOperationsUtil, times(1)).queryForObject(anyString(), eq(String.class));
}

// Test for getConnectionId method with typical use case
@Test
public void testGetConnectionIdTypicalUseCase() {
String expectedConnectionId = "123456";
when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);
String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
verify(jdbcOperationsUtil, times(1)).queryForObject(anyString(), eq(String.class));
assert(Objects.equals(expectedConnectionId, actualConnectionId));
}

// Test for getConnectionId method with exception handling scenario
@Test
public void testGetConnectionIdExceptionScenario() {
when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
verify(jdbcOperationsUtil, times(1)).queryForObject(anyString(), eq(String.class));
assert(actualConnectionId == null);
}

// Test for getConnectionId method with OracleConnection
@Test
public void testGetConnectionIdOracleConnection() throws SQLException {
long threadId = 123456;
Connection oracleConnection = mock(OceanBaseConnection.class);
when(oracleConnection.getServerThreadId()).thenReturn(threadId);
String actualConnectionId = obOracleSessionExtension.getConnectionId(oracleConnection);
verify(oracleConnection, times(1)).getServerThreadId();
assert(Objects.equals(String.valueOf(threadId), actualConnectionId));
}
}