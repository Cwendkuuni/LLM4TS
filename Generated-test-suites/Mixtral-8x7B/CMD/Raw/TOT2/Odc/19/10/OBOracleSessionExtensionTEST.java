import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OBOracleSessionExtensionTest {

 private PluginManager pluginManager;
 private ApplicationContext applicationContext;
 private Connection connection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setUp() {
 applicationContext = new AnnotationConfigApplicationContext();
 pluginManager = new SpringPluginManager(applicationContext);
 pluginManager.loadPlugins();
 pluginManager.startPlugins();
 obOracleSessionExtension = pluginManager.getExtensionPoint(SessionExtensionPoint.class, "com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension");
 connection = mock(Connection.class);
 }

 @Test
 public void shouldKillQuery() throws SQLException {
 String connectionId = "123456";
 doReturn(JdbcOperationsUtil.getJdbcOperations(connection)).when(obOracleSessionExtension).getJdbcOperations(connection);
 obOracleSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void shouldNotSwitchSchemaIfSame() throws SQLException {
 String schemaName = "test_schema";
 doReturn("test_schema").when(obOracleSessionExtension).getCurrentSchema(connection);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void shouldSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 doReturn("other_schema").when(obOracleSessionExtension).getCurrentSchema(connection);
 doReturn(JdbcOperationsUtil.getJdbcOperations(connection)).when(obOracleSessionExtension).getJdbcOperations(connection);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void shouldReturnConnectionId() throws SQLException, ExecutionException {
 doReturn(JdbcOperationsUtil.getJdbcOperations(connection)).when(obOracleSessionExtension).getJdbcOperations(connection);
 assertEquals("1", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void shouldReturnNullIfNoConnectionId() throws SQLException, ExecutionException {
 if (connection instanceof OceanBaseConnection) {
 ((OceanBaseConnection) connection).setServerThreadId(null);
 }
 doReturn(JdbcOperationsUtil.getJdbcOperations(connection)).when(obOracleSessionExtension).getJdbcOperations(connection);
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}