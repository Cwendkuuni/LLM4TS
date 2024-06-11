import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.PluginManager;
import org.pf4j.SimplePluginManager;
import org.pf4j.junit.AbstractJUnit4SpringJdbcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class OBMySQLSessionExtensionTest extends AbstractJUnit4SpringJdbcTest {

 @Autowired
 private DataSource dataSource;

 private PluginManager pluginManager;

 @Before
 public void setup() {
 pluginManager = new SimplePluginManager();
 pluginManager.loadPlugins();
 }

 @Override
 public void configureDataSourceBuilder(EmbeddedDatabaseBuilder builder) {
 super.configureDataSourceBuilder(builder);
 builder.setType(EmbeddedDatabaseType.H2);
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = dataSource.getConnection();
 String connectionId = "12345";
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 extension.killQuery(connection, connectionId);
 Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = dataSource.getConnection();
 String schemaName = "testSchema";
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 extension.switchSchema(connection, schemaName);
 Mockito.verify(connection).setCatalog(schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = dataSource.getConnection();
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(currentSchema, jdbcTemplate.queryForObject("SELECT DATABASE()", String.class));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = dataSource.getConnection();
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 String connectionId = extension.getConnectionId(connection);
 assertEquals(connectionId, String.valueOf(((JdbcTemplate) jdbcTemplate).getConnection().getClientInfo("serverThreadId")));
 }

 @Test
 public void testGetConnectionIdWithException() throws SQLException {
 Connection connection = mock(Connection.class);
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Mockito.when(connection.createStatement()).thenThrow(new SQLException());
 String connectionId = extension.getConnectionId(connection);
 assertEquals(connectionId, null);
 }
}