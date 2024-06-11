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
when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
}

// Test for killQuery method
@Test
public void testKillQuery() throws SQLException {
obOracleSessionExtension.killQuery(connection, "connectionId");
}

// Test for switchSchema method with valid schema name
@Test
public void testSwitchSchemaValid() throws SQLException {
String schemaName = "testSchema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("differentSchema");
obOracleSessionExtension.switchSchema(connection, schemaName);
assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
}

// Test for switchSchema method with same schema name
@Test
public void testSwitchSchemaSame() throws SQLException {
String schemaName = "testSchema";
when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obOracleSessionExtension.switchSchema(connection, schemaName);
assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
}

// Test for getCurrentSchema method
@Test
public void testGetCurrentSchema() {
String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testSchema");
assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
}

// Test for getConnectionId method with successful query
@Test
public void testGetConnectionIdSuccess() {
String connectionId = "12345";
when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class))
.thenReturn(connectionId);
assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
}

// Test for getConnectionId method with OceanBaseConnection and successful thread id extraction
@Test
public void testGetConnectionIdOceanBaseConnectionSuccess() {
long threadId = 12345;
when(connection instanceof OceanBaseConnection).thenReturn(true);
when( ((OceanBaseConnection) connection).getServerThreadId()).thenReturn(threadId);
assertEquals(threadId + "", obOracleSessionExtension.getConnectionId(connection));
}

// Test for getConnectionId method with exception during query
@Test
public void testGetConnectionIdQueryException() {
when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class))
.thenThrow(new RuntimeException());
assertEquals(null, obOracleSessionExtension.getConnectionId(connection));
}

// Test for getConnectionId method with OceanBaseConnection but unsuccessful thread id extraction
@Test
public void testGetConnectionIdOceanBaseConnectionFailed() {
when(connection instanceof OceanBaseConnection).thenReturn(true);
when( ((OceanBaseConnection) connection).getServerThreadId()).thenReturn(-1);
assertEquals(null, obOracleSessionExtension.getConnectionId(connection));
}
}