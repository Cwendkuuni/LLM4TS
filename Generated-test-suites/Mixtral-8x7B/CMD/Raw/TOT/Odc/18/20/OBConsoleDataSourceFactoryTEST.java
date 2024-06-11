import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:oceanbase:oracle:@127.0.0.1:20000:my_schema", jdbcUrl);
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testGetDataSource() throws SQLException {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
Connection connection = dataSource.getConnection();
assertTrue(connection.isValid(1));
assertEquals("oceanbase:oracle", connection.getMetaData().getDatabaseProductName());
}

@Test
public void testResetUsername() {
dataSourceFactory.resetUsername(value -> "new_username");
assertEquals("new_username", dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
dataSourceFactory.resetPassword(value -> "new_password");
assertEquals("new_password", dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
dataSourceFactory.resetHost(value -> "127.0.0.2");
assertEquals("127.0.0.2", dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
dataSourceFactory.resetPort(value -> 30000);
assertEquals(Integer.valueOf(30000), dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
dataSourceFactory.resetSchema(value -> "new_schema");
assertEquals("new_schema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("new_param", "new_value");
dataSourceFactory.resetParameters(value -> newParameters);
assertEquals(newParameters, dataSourceFactory.getParameters());
}

@Test(expected = NotImplementedException.class)
public void testCloneNotImplemented() {
dataSourceFactory.deepCopy();
}

private ConnectionConfig createConnectionConfig() {
ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.OB_ORACLE);
config.setUsername("existing_username");
config.setPassword("existing_password");
config.setHost("127.0.0.1");
config.setPort(20000);
config.setDefaultSchema("my_schema");
return config;
}

}