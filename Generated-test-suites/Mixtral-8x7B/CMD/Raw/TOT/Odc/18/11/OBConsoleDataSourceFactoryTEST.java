import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
// Initialize the dataSourceFactory with some default values
dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
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
// Add assertions based on the expected username
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertNotNull(password);
// Add assertions based on the expected password
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
// Add assertions based on the expected DataSource behavior
SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
assertEquals(dataSourceFactory.getUsername(), singleConnectionDataSource.getUsername());
assertEquals(dataSourceFactory.getPassword(), singleConnectionDataSource.getPassword());
assertEquals(dataSourceFactory.getJdbcUrl(), singleConnectionDataSource.getUrl());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
// Add assertions based on the expected clone behavior
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
assertEquals(newHost, dataSourceFactory.host);
}

@Test
public void testResetPort() {
int newPort = 1234;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.port.intValue());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.defaultSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.parameters);
}

}