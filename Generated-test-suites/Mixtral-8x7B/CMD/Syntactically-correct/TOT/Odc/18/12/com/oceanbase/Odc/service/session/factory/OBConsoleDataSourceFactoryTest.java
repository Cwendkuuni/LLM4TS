import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
// Initialize the dataSourceFactory with some default values
dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
// Add assertions based on the expected format of the JDBC URL
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertNotNull(username);
// Add assertions based on the expected format of the username
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertNotNull(password);
// Add assertions based on the expected format of the password
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertNotNull(dialectType);
// Add assertions based on the expected DialectType
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
// Add assertions based on the expected DataSource behavior
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
// Add assertions to compare the original and cloned dataSourceFactory instances
}

@Test
public void testResetUsername() {
String newUsername = "new_username";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "new_password";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "new_host";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
}

@Test
public void testResetPort() {
int newPort = 1234;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
}

@Test
public void testResetSchema() {
String newSchema = "new_schema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getJdbcParams(dataSourceFactory.connectionConfig));
}

private ConnectionConfig getConnectionConfig() {
ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.OB_MYSQL);
config.setHost("localhost");
config.setPort(3306);
config.setUsername("username");
config.setPassword("password");
config.setDefaultSchema("test_schema");
config.setSslConfig(new SSLConfig());
return config;
}

}