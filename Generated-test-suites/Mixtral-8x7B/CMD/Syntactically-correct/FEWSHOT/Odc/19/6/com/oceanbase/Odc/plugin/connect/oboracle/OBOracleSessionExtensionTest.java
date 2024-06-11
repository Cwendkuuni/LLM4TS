import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

 @Mock
 private Connection connection;

 @Mock
 private JdbcOperationsUtil jdbcOperationsUtil;

 @Mock
 private OceanBaseConnection oceanBaseConnection;

 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 obOracleSessionExtension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123456";
 obOracleSessionExtension.killQuery(connection, connectionId);
 verify(jdbcOperationsUtil, times(1)).execute(eq("KILL QUERY " + connectionId), any(Connection.class));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("currentSchema");
 String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 verify(jdbcOperationsUtil, times(1)).update(eq(alterSql), any(Connection.class));
 }

 @Test
 public void testGetCurrentSchema() {
 String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(eq(querySql), any(Class.class), any(Connection.class)))
 .thenReturn("testSchema");
 assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 String querySql = "select userenv('sessionid') from dual";
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(jdbcOperationsUtil.queryForObject(eq(querySql), any(Class.class), any(Connection.class)))
 .thenReturn("connectionId");
 assertEquals("connectionId", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() {
 String connectionId = "12345";
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.parseInt(connectionId));
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(oceanBaseConnection));
 }
}