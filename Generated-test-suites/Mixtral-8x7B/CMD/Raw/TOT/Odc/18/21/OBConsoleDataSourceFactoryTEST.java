import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:oceanbase:oceanbase://host:port/defaultSchema", jdbcUrl);
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertEquals("username@tenantName#clusterName", username);
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertEquals("password", password);
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_CONSOLE, dialectType);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals("oceanbase", dataSource.getDriverClassName());
assertEquals("jdbc:oceanbase:oceanbase://host:port/defaultSchema", dataSource.getUrl());
assertEquals("username@tenantName#clusterName", dataSource.getUsername());
assertEquals("password", dataSource.getPassword());
assertTrue(dataSource instanceof SingleConnectionDataSource);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copyDataSourceFactory = dataSourceFactory.deepCopy();
assertFalse(dataSourceFactory == copyDataSourceFactory);
assertEquals(dataSourceFactory.getUsername(), copyDataSourceFactory.getUsername());
assertEquals(dataSourceFactory.getPassword(), copyDataSourceFactory.getPassword());
assertEquals(dataSourceFactory.getHost(), copyDataSourceFactory.getHost());
assertEquals(dataSourceFactory.getPort(), copyDataSourceFactory.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copyDataSourceFactory.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(), copyDataSourceFactory.getParameters());
}

@Test
public void testResetUsername() {
String newUsername = "newUsername";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 1234;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
}

private ConnectionConfig getConnectionConfig() {
ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.OB_CONSOLE);
config.setUsername("username");
config.setPassword("password");
config.setHost("host");
config.setPort(5000);
config.setDefaultSchema("defaultSchema");
Map<String, Object> params = new HashMap<>();
params.put("key", "value");
config.setParameters(params);
return config;
}

}