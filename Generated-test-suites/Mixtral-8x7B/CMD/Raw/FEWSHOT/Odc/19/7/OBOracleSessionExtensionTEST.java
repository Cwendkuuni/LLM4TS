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
 private OceanBaseConnection obConnection;

 private OBOracleSessionExtension extension;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 extension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "12345";
 extension.killQuery(connection, connectionId);
 verify(jdbcOperationsUtil, times(1)).execute(eq("KILL QUERY " + connectionId), any(Connection.class));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 extension.switchSchema(connection, schemaName);
 String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
 verify(jdbcOperationsUtil, times(1)).update(eq(alterSql), any(Connection.class));
 }

 @Test
 public void testGetCurrentSchema() {
 String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
 String currentSchema = "test_schema";
 when(jdbcOperationsUtil.queryForObject(eq(querySql), eq(String.class), any(Connection.class)))
 .thenReturn(currentSchema);
 assertEquals(currentSchema, extension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = "67890";
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));
 assertEquals(connectionId, extension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionId_exception() {
 when(connection instanceof OceanBaseConnection).thenReturn(false);
 assertNull(extension.getConnectionId(connection));
 }
}