import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

private OBOracleSessionExtension obOracleSessionExtension;
private JdbcOperations jdbcOperations;
private Connection connection;

@Before
public void setup() {
jdbcOperations = mock(JdbcOperations.class);
connection = mock(Connection.class);
obOracleSessionExtension = new OBOracleSessionExtension();
JdbcOperationsUtil.setJdbcOperations(connection, jdbcOperations);
}

@Test
public void testKillQuery() throws Exception {
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
Mockito.verify(jdbcOperations).execute(anyString());
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "testSchema";
String currentSchema = "currentSchema";
when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn(currentSchema);
obOracleSessionExtension.switchSchema(connection, schemaName);
Mockito.verify(jdbcOperations).update(anyString());
}

@Test
public void testSwitchSchemaNoChange() throws SQLException {
String schemaName = "currentSchema";
String currentSchema = "currentSchema";
when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn(currentSchema);
obOracleSessionExtension.switchSchema(connection, schemaName);
Mockito.verify(jdbcOperations, Mockito.never()).update(anyString());
}

@Test
public void testGetCurrentSchema() {
String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("testSchema");
assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() {
String querySql = "select userenv('sessionid') from dual";
when(jdbcOperations.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
when(connection instanceof OceanBaseConnection).thenReturn(true);
OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(123456L);
when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
when(connection.getClass().cast(connection)).thenReturn(oceanBaseConnection);
assertEquals("123456", obOracleSessionExtension.getConnectionId(connection));
}
}