import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/odc?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&connectTimeout=5000&maxAllowedPacket=64000000&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false", jdbcUrl);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertEquals("localhost:3306", ((SingleConnectionDataSource) dataSource).getConnection().getMetaData().getURL());
assertEquals("root", ((SingleConnectionDataSource) dataSource).getConnection().getMetaData().getUserName());
assertTrue(((SingleConnectionDataSource) dataSource).isAutoCommit());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory copy = dataSourceFactory.deepCopy();
assertEquals(dataSourceFactory.getDialectType(), copy.getDialectType());
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getHost(), copy.getHost());
assertEquals(dataSourceFactory.getPort(), copy.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(), copy.getParameters());
}

@Test
public void testResetUsername() {
String newUsername = "newUser";
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
int newPort = 8080;
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
newParams.put("param2", "value2");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
}

@Test
public void testGetUsername_withSchema() {
ConnectionConfig connectionConfig = createConnectionConfig();
connectionConfig.setDefaultSchema("testSchema");
OBConsoleDataSourceFactory dataSourceFactoryWithSchema = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals("root@testSchema", dataSourceFactoryWithSchema.getUsername());
}

@Test
public void testGetJdbcUrl_withSchema() {
ConnectionConfig connectionConfig = createConnectionConfig();
connectionConfig.setDefaultSchema("testSchema");
OBConsoleDataSourceFactory dataSourceFactoryWithSchema = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactoryWithSchema.getJdbcUrl();
assertTrue(jdbcUrl.contains("testSchema"));
}

@Test
public void testGetDataSource_withSchema() {
ConnectionConfig connectionConfig = createConnectionConfig();
connectionConfig.setDefaultSchema("testSchema");
OBConsoleDataSourceFactory dataSourceFactoryWithSchema = new OBConsoleDataSourceFactory(connectionConfig, true);
DataSource dataSource = dataSourceFactoryWithSchema.getDataSource();
assertTrue(((SingleConnectionDataSource) dataSource).getConnection().getMetaData().getURL().contains("testSchema"));
}

private ConnectionConfig createConnectionConfig() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("root");
connectionConfig.setPassword("root");
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setSslConfig(null);
return connectionConfig;
}

}