import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
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
public void setUp() {
Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("maxAllowedPacket", "64000000");
jdbcParams.put("allowMultiQueries", "true");
jdbcParams.put("connectTimeout", "5000");
jdbcParams.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
jdbcParams.put("noDatetimeStringSync", "true");
jdbcParams.put("jdbcCompliantTruncation", "false");
jdbcParams.put("sendConnectionAttributes", "false");

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setJdbcUrlParameters(jdbcParams);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("localhost", dataSource.getConnection().getMetaData().getURL());
assertEquals("root", dataSource.getConnection().getMetaData().getUserName());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
assertNotNull(clonedDataSourceFactory);
assertEquals(dataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
}

@Test
public void testResetUsername() {
String newUsername = "newUsername";
dataSourceFactory.resetUsername(value -> newUsername);
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals(newUsername, dataSource.getConnection().getMetaData().getUserName());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
dataSourceFactory.resetPassword(value -> newPassword);
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals(newPassword, dataSource.getConnection().getMetaData().getUserName());
}

@Test
public void testResetHost() {
String newHost = "127.0.0.1";
dataSourceFactory.resetHost(value -> newHost);
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals(newHost, dataSource.getConnection().getMetaData().getURL());
}

@Test
public void testResetPort() {
int newPort = 3307;
dataSourceFactory.resetPort(value -> newPort);
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals("jdbc:mysql://127.0.0.1:" + newPort, dataSource.getConnection().getMetaData().getURL());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals(newSchema, dataSource.getConnection().getMetaData().getURL().split("\\?")[0].split(":")[2]);
}

@Test
public void testResetParameters() {
Map<String, String> newJdbcParams = new HashMap<>();
newJdbcParams.put("maxAllowedPacket", "128000000");
dataSourceFactory.resetParameters(value -> newJdbcParams);
DataSource dataSource = dataSourceFactory.getDataSource();
Properties props = dataSource.getConnection().getConnectionProperties();
assertEquals("128000000", props.getProperty("maxAllowedPacket"));
}

@Test
public void testGetUsername() {
assertEquals("root", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("password", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertTrue(jdbcUrl.contains("localhost"));
assertTrue(jdbcUrl.contains("3306"));
assertTrue(jdbcUrl.contains("testdb"));
}

@Test
public void testGetPassword_EmptyPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertNull(dataSourceFactory.getPassword());
}

@Test
public void testGetJdbcUrl_EmptyDefaultSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDefaultSchema("");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertFalse(dataSourceFactory.getJdbcUrl().contains("defaultSchema"));
}

}