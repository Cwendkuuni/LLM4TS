import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
when(jdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenReturn(jdbcOperationsUtil);
}

// Test for killQuery method
@Test
public void testKillQuery() throws SQLException {
obOracleSessionExtension.killQuery(connection, "connectionId");
assertEquals("KILL QUERY " + "connectionId", jdbcOperationsUtil.getJdbcOperations(connection).getLastExecutedQuery());
}

// Test for switchSchema method with typical use case
@Test
public void testSwitchSchemaTypicalUseCase() throws SQLException {
obOracleSessionExtension.switchSchema(connection, "schemaName");
// Add assertions to check for successful schema switch
}

// Test for switchSchema method with edge case - schemaName is null
@Test(expected = SQLException.class)
public void testSwitchSchemaEdgeCaseSchemaNameIsNull() throws SQLException {
obOracleSessionExtension.switchSchema(connection, null);
}

// Test for switchSchema method with exception handling scenario
@Test(expected = SQLException.class)
public void testSwitchSchemaExceptionHandlingScenario() throws SQLException {
when(jdbcOperationsUtil.getJdbcOperations(connection).update(anyString())).thenThrow(new SQLException());
obOracleSessionExtension.switchSchema(connection, "schemaName");
}

// Test for getCurrentSchema method with typical use case
@Test
public void testGetCurrentSchemaTypicalUseCase() {
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
// Add assertions to check for correct schema name retrieval
}

// Test for getConnectionId method with typical use case
@Test
public void testGetConnectionIdTypicalUseCase() {
String connectionId = obOracleSessionExtension.getConnectionId(connection);
// Add assertions to check for correct connection id retrieval
}

// Test for getConnectionId method with error scenario
@Test
public void testGetConnectionIdErrorScenario() {
when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new RuntimeException());
String connectionId = obOracleSessionExtension.getConnectionId(connection);
// Add assertions to check for correct error handling
}
}