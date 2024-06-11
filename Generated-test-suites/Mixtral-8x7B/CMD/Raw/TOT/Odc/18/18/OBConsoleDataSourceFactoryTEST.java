import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setUp() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");
connectionConfig.setDefaultSchema("testdb");

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetDataSource() {
DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
assertEquals("localhost", obConsoleDataSourceFactory.getHost());
assertEquals(new Integer(3306), obConsoleDataSourceFactory.getPort());
assertEquals("root", obConsoleDataSourceFactory.getUsername());
assertEquals("password", obConsoleDataSourceFactory.getPassword());
assertEquals("testdb", obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
assertEquals("jdbc:mysql://localhost:3306/testdb?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&maxAllowedPacket=64000000", jdbcUrl);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = obConsoleDataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(obConsoleDataSourceFactory.getDialectType(), clone.getDialectType());
assertEquals(obConsoleDataSourceFactory.getHost(), clone.getHost());
assertEquals(obConsoleDataSourceFactory.getPort(), clone.getPort());
assertEquals(obConsoleDataSourceFactory.getUsername(), clone.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), clone.getPassword());
assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), clone.getDefaultSchema());
}

@Test
public void testResetUsername() {
String newUsername = "newUser";
obConsoleDataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
obConsoleDataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "127.0.0.1";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 3307;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
obConsoleDataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
}

@Test
public void testGetParameters() {
Map<String, String> params = obConsoleDataSourceFactory.getParameters();
assertEquals(0, params.size());
}

}