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

@org.powermock.modules.junit4.PowerMockRunner
public class OBOracleSessionExtensionTest {

 private static final String CONNECTION_ID = "12345";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CURRENT_SCHEMA = "current_schema";
 private static final String QUOTED_SCHEMA = "\"test_schema\"";
 private Connection connection;
 private SessionExtensionPoint obOracleSessionExtension;

 @Before
 public void setUp() {
 connection = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "1234";
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString()))
 .thenReturn(0);
 obOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test(expected = SQLException.class)
 public void testKillQueryWithException() throws SQLException {
 String connectionId = "1234";
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString()))
 .thenThrow(new SQLException());
 obOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString()))
 .thenReturn(1);
 PowerMockito.when(getCurrentSchema(connection)).thenReturn(CURRENT_SCHEMA);
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 assertEquals(SCHEMA_NAME, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class));
 }

 @Test
 public void testSwitchSchemaWithSameSchema() throws SQLException {
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString())).thenReturn(1);
 PowerMockito.when(getCurrentSchema(connection)).thenReturn(SCHEMA_NAME);
 obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
 assertEquals(SCHEMA_NAME, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class));
 }

 @Test
 public void testGetCurrentSchema() {
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
 .thenReturn(CURRENT_SCHEMA);
 assertEquals(CURRENT_SCHEMA, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
 .thenReturn(CONNECTION_ID);
 assertEquals(CONNECTION_ID, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithException() throws SQLException {
 PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
 .thenThrow(new SQLException());
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 assertEquals(CONNECTION_ID, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testQuoteOracleIdentifier() {
 assertEquals(QUOTED_SCHEMA, StringUtils.quoteOracleIdentifier(SCHEMA_NAME));
 }
}

class StringUtilsTest {

 @Test
 public void testQuoteOracleIdentifier() {
 assertEquals("\"test\"", StringUtils.quoteOracleIdentifier("test"));
 assertEquals("\"test_with_underscore\"", StringUtils.quoteOracleIdentifier("test_with_underscore"));
 }
}