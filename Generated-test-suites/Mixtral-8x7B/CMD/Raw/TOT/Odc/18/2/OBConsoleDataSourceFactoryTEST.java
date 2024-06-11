import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");
connectionConfig.setDefaultSchema("testdb");

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
driverManagerDataSource.setUrl(jdbcUrl);
driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
driverManagerDataSource.setUsername(dataSourceFactory.getUsername());
driverManagerDataSource.setPassword(dataSourceFactory.getPassword());
assertEquals(dataSourceFactory.getUsername(), driverManagerDataSource.getUsername());
assertEquals(dataSourceFactory.getPassword(), driverManagerDataSource.getPassword());
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(dataSourceFactory.getUsername(), clone.getUsername());
assertEquals(dataSourceFactory.getPassword(), clone.getPassword());
assertEquals(dataSourceFactory.getHost(), clone.getHost());
assertEquals(dataSourceFactory.getPort(), clone.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), clone.getDefaultSchema());
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
int newPort = 4406;
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
newParams.put("param1", "value1");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
}

@Test
public void testGetParameters() {
Map<String, String> params = dataSourceFactory.getParameters();
assertNotNull(params);
assertTrue(params.isEmpty());
}

}