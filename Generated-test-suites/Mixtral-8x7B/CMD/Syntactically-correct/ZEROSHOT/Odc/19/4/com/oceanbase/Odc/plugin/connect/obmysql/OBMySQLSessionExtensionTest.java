import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoint;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

 @Before
 public void setup() {
 DriverManagerDataSource dataSource = new DriverManagerDataSource();
 dataSource.setDriverClassName("com.oceanbase.jdbc.OceanBaseDriver");
 dataSource.setUrl("jdbc:oceanbase://127.0.0.1:2881/dbname");
 dataSource.setUsername("username");
 dataSource.setPassword("password");
 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
 when(jdbcTemplate.queryForObject(("SELECT DATABASE()"), String.class)).thenReturn("testdb");
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = mock(Connection.class);
 OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
 obMySQLSessionExtension.killQuery(connection, "1");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = mock(Connection.class);
 when(connection.getCatalog()).thenReturn("testdb");
 OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
 obMySQLSessionExtension.switchSchema(connection, "testdb");
 Assert.assertEquals(obMySQLSessionExtension.getCurrentSchema(connection), "testdb");
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = mock(Connection.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 when(jdbcTemplate.queryForObject(("SELECT DATABASE()"), String.class)).thenReturn("testdb");
 when(connection.getMetaData()).thenReturn(jdbcTemplate);
 OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
 Assert.assertEquals(obMySQLSessionExtension.getCurrentSchema(connection), "testdb");
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = mock(Connection.class);
 OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);
 when(connection.getClass()).thenReturn(OceanBaseConnection.class);
 when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
 OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
 Assert.assertEquals(obMySQLSessionExtension.getConnectionId(connection), "12345");
 }

 @Test
 public void testGetConnectionId_withoutServerThreadId() throws SQLException {
 Connection connection = mock(Connection.class);
 when(connection.getClass()).thenReturn(Connection.class);
 when(connection.unwrap(OceanBaseConnection.class)).thenThrow(new SQLException());
 OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
 Assert.assertNull(obMySQLSessionExtension.getConnectionId(connection));
 }
}