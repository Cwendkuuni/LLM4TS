import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

private ConnectionExtensionPoint connectionExtensionPoint;

private ConnectionConfig connectionConfig;

private EventPublisher eventPublisher;

@Before
public void setup() {
connectionExtensionPoint = mock(ConnectionExtensionPoint.class);

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
connectionConfig.setHost("testHost");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testSchema");

eventPublisher = mock(EventPublisher.class);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher, connectionExtensionPoint);
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);

SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
assertEquals(singleConnectionDataSource.getUrl(), dataSourceFactory.getJdbcUrl());
assertEquals(singleConnectionDataSource.getUsername(), dataSourceFactory.getUsername());
assertEquals(singleConnectionDataSource.getPassword(), dataSourceFactory.getPassword());
assertEquals(singleConnectionDataSource.isAutoCommit(), dataSourceFactory.isAutoCommit());
}

@Test
public void testIsAutoCommit() {
assertTrue(dataSourceFactory.isAutoCommit());

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, eventPublisher, connectionExtensionPoint);
assertFalse(dataSourceFactory.isAutoCommit());
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertNotNull(username);
assertEquals(username, "testUser");
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertNotNull(password);
assertEquals(password, "testPassword");
}

@Test
public void testGetConnectType() {
ConnectType connectType = dataSourceFactory.getConnectType();
assertNotNull(connectType);
assertEquals(connectType, ConnectType.CONSOLE);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copiedDataSourceFactory = dataSourceFactory.deepCopy();
assertNotNull(copiedDataSourceFactory);

assertEquals(copiedDataSourceFactory.getUsername(), dataSourceFactory.getUsername());
assertEquals(copiedDataSourceFactory.getPassword(), dataSourceFactory.getPassword());
assertEquals(copiedDataSourceFactory.getJdbcUrl(), dataSourceFactory.getJdbcUrl());
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
int newPort = 4444;
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

private void stubConnectionExtensionPointGetJdbcUrl() {
when(connectionExtensionPoint.generateJdbcUrl(dataSourceFactory.getHost(), dataSourceFactory.getPort(),
dataSourceFactory.getDefaultSchema(), dataSourceFactory.getParameters()))
.thenReturn("jdbc:test://" + dataSourceFactory.getHost() + ":" + dataSourceFactory.getPort()
+ "/" + dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

assertNotNull(jdbcParams);
assertEquals(jdbcParams.get("maxAllowedPacket"), "64000000");
assertEquals(jdbcParams.get("allowMultiQueries"), "true");
assertEquals(jdbcParams.get("connectTimeout"), "5000");
assertEquals(jdbcParams.get("zeroDateTimeBehavior"), DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
assertEquals(jdbcParams.get("noDatetimeStringSync"), "true");
assertEquals(jdbcParams.get("jdbcCompliantTruncation"), "false");
}

@Test
public void testGetUsernameWithObOracleDialectType() {
ConnectionConfig obOracleConnectionConfig = new ConnectionConfig();
obOracleConnectionConfig.setDialectType(DialectType.OB_ORACLE);
obOracleConnectionConfig.setUsername("obOracleUser");
obOracleConnectionConfig.setTenantName("tenant1");
obOracleConnectionConfig.setClusterName("cluster1");

String obOracleUsername = OBConsoleDataSourceFactory.getUsername(obOracleConnectionConfig);

assertEquals(obOracleUsername, "\"obOracleUser\"@tenant1#cluster1");
}

@Test
public void testGetPasswordWithObOracleDialectType() {
String password = "testPassword";
String obOraclePassword = OBConsoleDataSourceFactory.getPassword(connectionConfig);

assertEquals(obOraclePassword, password);
}

@Test
public void testGetJdbcParamsWithSslConfigEnabled() {
ConnectionConfig connectionConfigWithSslEnabled = new ConnectionConfig();
connectionConfigWithSslEnabled.setDialectType(DialectType.OB_MYSQL);
connectionConfigWithSslEnabled.setUsername("testUser");
connectionConfigWithSslEnabled.setPassword("testPassword");
connectionConfigWithSslEnabled.setHost("testHost");
connectionConfigWithSslEnabled.setPort(3306);
connectionConfigWithSslEnabled.setDefaultSchema("testSchema");

SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
sslConfig.setKeyStoreFilePath("path/to/keystore.p12");
sslConfig.setKeyStoreFilePassword("password");
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("path/to/keystore.p12");
sslFileEntry.setKeyStoreFilePassword("password");
sslConfig.setSslFileEntry(sslFileEntry);
connectionConfigWithSslEnabled.setSslConfig(sslConfig);

Map<String, String> jdbcParamsWithSslEnabled = OBConsoleDataSourceFactory.getJdbcParams(connectionConfigWithSslEnabled);

assertNotNull(jdbcParamsWithSslEnabled);
assertEquals(jdbcParamsWithSslEnabled.get("useSSL"), "true");
assertEquals(jdbcParamsWithSslEnabled.get("trustServerCertificate"), "true");
assertEquals(jdbcParamsWithSslEnabled.get("trustStore"), "path/to/keystore.p12");
assertEquals(jdbcParamsWithSslEnabled.get("trustStorePassword"), "password");
assertEquals(jdbcParamsWithSslEnabled.get("keyStore"), "path/to/keystore.p12");
assertEquals(jdbcParamsWithSslEnabled.get("keyStorePassword"), "password");
}

@Test
public void testGetJdbcParamsWithSslConfigDisabled() {
ConnectionConfig connectionConfigWithSslDisabled = new ConnectionConfig();
connectionConfigWithSslDisabled.setDialectType(DialectType.OB_MYSQL);
connectionConfigWithSslDisabled.setUsername("testUser");
connectionConfigWithSslDisabled.setPassword("testPassword");
connectionConfigWithSslDisabled.setHost("testHost");
connectionConfigWithSslDisabled.setPort(3306);
connectionConfigWithSslDisabled.setDefaultSchema("testSchema");

Map<String, String> jdbcParamsWithSslDisabled = OBConsoleDataSourceFactory.getJdbcParams(connectionConfigWithSslDisabled);

assertNotNull(jdbcParamsWithSslDisabled);
assertEquals(jdbcParamsWithSslDisabled.get("useSSL"), "false");
}

@Test
public void testGetJdbcUrlWithProxy() {
ConnectionConfig connectionConfigWithProxy = new ConnectionConfig();
connectionConfigWithProxy.setDialectType(DialectType.OB_MYSQL);
connectionConfigWithProxy.setUsername("testUser");
connectionConfigWithProxy.setPassword("testPassword");
connectionConfigWithProxy.setHost("testHost");
connectionConfigWithProxy.setPort(3306);
connectionConfigWithProxy.setDefaultSchema("testSchema");

OBTenantEndpoint endpoint = new OBTenantEndpoint();
endpoint.setProxyHost("proxyHost");
endpoint.setProxyPort(8080);
connectionConfigWithProxy.setEndpoint(endpoint);

stubConnectionExtensionPointGetJdbcUrl();
String jdbcUrlWithProxy = dataSourceFactory.getJdbcUrl();

assertEquals(jdbcUrlWithProxy, "jdbc:test://testHost:3306/testSchema?socksProxyHost=proxyHost&socksProxyPort=8080");
}

@Test
public void testGetJdbcUrlWithoutProxy() {
stubConnectionExtensionPointGetJdbcUrl();
String jdbcUrlWithoutProxy = dataSourceFactory.getJdbcUrl();

assertEquals(jdbcUrlWithoutProxy, "jdbc:test://testHost:3306/testSchema");
}
}