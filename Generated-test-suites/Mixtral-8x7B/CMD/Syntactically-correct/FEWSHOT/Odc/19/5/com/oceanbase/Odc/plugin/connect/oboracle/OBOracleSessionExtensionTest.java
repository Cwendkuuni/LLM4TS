import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("rawtypes")
public class OBOracleSessionExtensionTest {

 private static final String CONNECTION_ID = "123456";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CURRENT_SCHEMA = "default_schema";
 private static final String QUOTED_SCHEMA_NAME = "\"test_schema\"";
 private Connection connection = mock(Connection.class);
 private OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 private JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 private StringUtils stringUtils = mock(StringUtils.class);
 private SessionExtensionPoint sessionExtensionPoint = new OBOracleSessionExtension();

 @Before
 public void setup() throws Exception {
 PowerMockito.whenNew(JdbcOperationsUtil.class).withNoArguments().thenReturn(jdbcOperationsUtil);
 PowerMockito.whenNew(StringUtils.class).withNoArguments().thenReturn(stringUtils);
 when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
 when(connection.isClosed()).thenReturn(false);
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
 when(stringUtils.quoteOracleIdentifier(SCHEMA_NAME)).thenReturn(QUOTED_SCHEMA_NAME);
 when(stringUtils.quoteOracleIdentifier(CURRENT_SCHEMA)).thenReturn(QUOTED_SCHEMA_NAME);
 when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
 String.class)).thenReturn(CURRENT_SCHEMA);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.parseInt(CONNECTION_ID));
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123";
 sessionExtensionPoint.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 sessionExtensionPoint.switchSchema(connection, SCHEMA_NAME);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(CURRENT_SCHEMA, currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 assertEquals(CONNECTION_ID, connectionId);
 }

 @Test
 public void testGetConnectionIdWithException() throws SQLException {
 when(connection.getClass().getName()).thenReturn("not_oceanbase_connection");
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 assertNull(connectionId);
 }
}