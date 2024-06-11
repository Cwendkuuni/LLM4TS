import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDefaultSchema("testdb");

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
assertEquals("localhost", ((SingleConnectionDataSource) dataSource).getUrl().getHost());
assertEquals(3306, ((SingleConnectionDataSource) dataSource).getUrl().getPort());
assertEquals("testuser", ((SingleConnectionDataSource) dataSource).getUsername());
assertEquals("testpassword", ((SingleConnectionDataSource) dataSource).getPassword());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copiedDataSourceFactory = dataSourceFactory.deepCopy();
assertNotNull(copiedDataSourceFactory);
assertEquals(dataSourceFactory.getDialectType(), copiedDataSourceFactory.getDialectType());
assertEquals(dataSourceFactory.getUsername(), copiedDataSourceFactory.getUsername());
assertEquals(dataSourceFactory.getPassword(), copiedDataSourceFactory.getPassword());
}

@Test
public void testResetUsername() {
String newUsername = "newusername";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newpassword";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newhost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getJdbcUrl().split(":")[1].split("/")[0]);
}

@Test
public void testResetPort() {
int newPort = 4444;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, Integer.parseInt(dataSourceFactory.getJdbcUrl().split(":")[1].split("/")[1]));
}

@Test
public void testResetSchema() {
String newSchema = "newschema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getJdbcUrl().split("/")[3]);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("testKey", "testValue");
dataSourceFactory.resetParameters(value -> newParams);
Properties properties = dataSourceFactory.getDataSource().getConnectionProperties();
assertEquals("testValue", properties.get("testKey"));
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testdb?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword="
+ "&useSSL=false&zeroDateTimeBehavior=convertToNull";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig());
assertNotNull(jdbcParams);
assertEquals(16, jdbcParams.size());
}

@Test
public void testGetPassword_withEmptyPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals("", dataSourceFactory.getPassword());
}

@Test
public void testGetUsername_withObOracleDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setUsername("testuser");
String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
assertEquals("\"testuser\"@?", username);
}

@Test
public void testGetUsername_withObTenantName() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testuser");
connectionConfig.setOBTenantName("testtenant");
String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
assertEquals("testuser@testtenant", username);
}

@Test
public void testGetUsername_withTenantName() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testuser");
connectionConfig.setTenantName("testtenant");
String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
assertEquals("testuser@testtenant", username);
}

@Test
public void testGetUsername_withClusterName() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testuser");
connectionConfig.setClusterName("testcluster");
String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
assertEquals("testuser@?#testcluster", username);
}

@Test
public void testGetPassword_withObOracleDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setPassword("testpassword");
assertEquals("testpassword", OBConsoleDataSourceFactory.getPassword(connectionConfig));
}

@Test
public void testGetJdbcParams_withSslConfig() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.getSslConfig().setEnabled(true);
connectionConfig.getSslConfig().setKeyStoreFilePassword("testpassword");
connectionConfig.getSslConfig().setTrustStoreFilePassword("testpassword");
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertEquals("true", jdbcParams.get("useSSL"));
assertEquals("true", jdbcParams.get("trustServerCertificate"));
assertEquals("testpassword", jdbcParams.get("trustStorePassword"));
assertEquals("testpassword", jdbcParams.get("keyStorePassword"));
}

@Test
public void testGetJdbcParams_withEndpoint() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setEndpoint(new OBTenantEndpoint());
connectionConfig.getEndpoint().setProxyHost("proxyhost");
connectionConfig.getEndpoint().setProxyPort(8080);
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertEquals("proxyhost", jdbcParams.get("socksProxyHost"));
assertEquals("8080", jdbcParams.get("socksProxyPort"));
}

@Test
public void testGetJdbcUrl_withoutDefaultSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withEmptyHost() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql:///?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withEmptyPort() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(0);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost/?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withNonDefaultDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.ORACLE);
connectionConfig.setHost("localhost");
connectionConfig.setPort(1521);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:oracle:thin:@localhost:1521:?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withCustomJdbcUrlParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
Map<String, String> customJdbcUrlParameters = new HashMap<>();
customJdbcUrlParameters.put("testKey", "testValue");
connectionConfig.setJdbcUrlParameters(customJdbcUrlParameters);
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&testKey=testValue&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withCustomJdbcUrlParametersAndSsl() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.getSslConfig().setEnabled(true);
connectionConfig.getSslConfig().setKeyStoreFilePassword("testpassword");
connectionConfig.getSslConfig().setTrustStoreFilePassword("testpassword");
Map<String, String> customJdbcUrlParameters = new HashMap<>();
customJdbcUrlParameters.put("testKey", "testValue");
connectionConfig.setJdbcUrlParameters(customJdbcUrlParameters);
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=testpassword&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&testKey=testValue&trustStore=&trustStorePassword=testpassword&useSSL=true&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}

@Test
public void testGetJdbcUrl_withCustomJdbcUrlParametersAndEndpoint() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setEndpoint(new OBTenantEndpoint());
connectionConfig.getEndpoint().setProxyHost("proxyhost");
connectionConfig.getEndpoint().setProxyPort(8080);
Map<String, String> customJdbcUrlParameters = new HashMap<>();
customJdbcUrlParameters.put("testKey", "testValue");
connectionConfig.setJdbcUrlParameters(customJdbcUrlParameters);
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&socksProxyHost=proxyhost&socksProxyPort=8080&testKey=testValue&trustStore=&trustStorePassword=&useSSL=false&zeroDateTimeBehavior=convertToNull",
jdbcUrl);
}
}