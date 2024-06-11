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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private Connection connection;

 @Mock
 private JdbcOperationsUtil jdbcOperationsUtil;

 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtension = spy(new OBOracleSessionExtension());

 @Before
 public void setup() {
 doNothing().when(obOracleSessionExtension).killQuery(any(Connection.class), anyString());
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(jdbcOperationsUtil);
 when(obOracleSessionExtension.getConnectionId(connection)).thenReturn(jdbcOperationsUtil);
 }

 @Test
 public void testKillQuery() throws Exception {
 String connectionId = "123456";
 obOracleSessionExtension.killQuery(connection, connectionId);
 verify(jdbcOperationsUtil, times(1)).execute(anyString());
 }

 @Test
 public void testSwitchSchemaTypicalUseCase() throws SQLException {
 String schemaName = "testSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 verify(jdbcOperationsUtil, times(1)).update(anyString());
 }

 @Test
 public void testSwitchSchemaSameSchema() throws SQLException {
 String schemaName = "currentSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 verify(jdbcOperationsUtil, times(0)).update(anyString());
 }

 @Test
 public void testGetCurrentSchema() throws Exception {
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("testSchema");
 String schemaName = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals("testSchema", schemaName);
 }

 @Test
 public void testGetConnectionId() throws Exception {
 when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(mock(Exception.class));
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertEquals("obc-server-thread-id", connectionId);
 }
}