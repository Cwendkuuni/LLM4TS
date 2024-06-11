import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtilsODC;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

import lombok.NonNull;
import lombok.Setter;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testUser";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
String expectedUsername = "testUser";
assertEquals(expectedUsername, dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
String expectedPassword = "testPassword";
assertEquals(expectedPassword, dataSourceFactory.getPassword());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("testUser", dataSource.getUsername());
assertEquals("testPassword", dataSource.getPassword());
assertEquals("jdbc:mysql://localhost:3306/testUser", dataSource.getUrl());
}

@Test
public void testGetDialectType() {
DialectType dialectType = DialectType.OB_MYSQL;
assertEquals(dialectType, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory copy = dataSourceFactory.deepCopy();
assertNotSame(dataSourceFactory, copy);
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
assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
}

@Test
public void testResetPort() {
int newPort = 4444;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
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

@Test
public void testGetConnectType() {
ConnectType connectType = ConnectType.CONSOLE;
assertEquals(connectType, dataSourceFactory.getConnectType());
}

@Test(expected = IllegalArgumentException.class)
public void testResetUsernameNull() {
dataSourceFactory.resetUsername(value -> null);
}

@Test(expected = IllegalArgumentException.class)
public void testResetPasswordNull() {
dataSourceFactory.resetPassword(value -> null);
}

@Test(expected = IllegalArgumentException.class)
public void testResetHostNull() {
dataSourceFactory.resetHost(value -> null);
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegative() {
dataSourceFactory.resetPort(value -> -1);
}

@Test
public void testGetDefaultSchema() {
String defaultSchema = "testSchema";
dataSourceFactory.setDefaultSchema(defaultSchema);
String expectedDefaultSchema = "testSchema";
assertEquals(expectedDefaultSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testParameters() {
Map<String, String> parameters = new HashMap<>();
parameters.put("key", "value");
dataSourceFactory.setParameters(parameters);
Map<String, String> expectedParameters = new HashMap<>();
expectedParameters.put("key", "value");
assertEquals(expectedParameters, dataSourceFactory.getParameters());
}

@Test
public void testGetParameters() {
Map<String, String> parameters = new HashMap<>();
parameters.put("key", "value");
dataSourceFactory.setParameters(parameters);
Map<String, String> expectedParameters = new HashMap<>();
expectedParameters.put("key", "value");
assertEquals(expectedParameters, dataSourceFactory.getParameters());
}

@Test
public void testGetSSLConfig() {
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
sslConfig.setKeyStoreFilePath("path/to/keystore.p12");
sslConfig.setKeyStoreFilePassword("password");
dataSourceFactory.setSSLConfig(sslConfig);
assertNotNull(dataSourceFactory.getSSLConfig());
}

@Test
public void testGetSSLFileEntry() {
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("path/to/keystore.p12");
sslFileEntry.setKeyStoreFilePassword("password");
dataSourceFactory.setSSLFileEntry(sslFileEntry);
assertNotNull(dataSourceFactory.getSSLFileEntry());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("maxAllowedPacket", "64000000");
jdbcParams.put("allowMultiQueries", "true");
jdbcParams.put("connectTimeout", "5000");
jdbcParams.put("zeroDateTimeBehavior", "DEFAULT_ZERO_DATE_TIME_BEHAVIOR");
jdbcParams.put("noDatetimeStringSync", "true");
jdbcParams.put("jdbcCompliantTruncation", "false");
dataSourceFactory.setJdbcUrlParameters(jdbcParams);
Map<String, String> expectedJdbcParams = new HashMap<>();
expectedJdbcParams.put("maxAllowedPacket", "64000000");
expectedJdbcParams.put("allowMultiQueries", "true");
expectedJdbcParams.put("connectTimeout", "5000");
expectedJdbcParams.put("zeroDateTimeBehavior", "DEFAULT_ZERO_DATE_TIME_BEHAVIOR");
expectedJdbcParams.put("noDatetimeStringSync", "true");
expectedJdbcParams.put("jdbcCompliantTruncation", "false");
assertEquals(expectedJdbcParams, dataSourceFactory.getJdbcParams());
}

@Test
public void testGetJdbcParamsNull() {
Map<String, String> jdbcParams = null;
dataSourceFactory.setJdbcUrlParameters(jdbcParams);
Map<String, String> expectedJdbcParams = new HashMap<>();
assertEquals(expectedJdbcParams, dataSourceFactory.getJdbcParams());
}

@Test
public void testGetDbUser() {
String dbUser = "testUser";
assertEquals(dbUser, OBConsoleDataSourceFactory.getDbUser(new ConnectionConfig().setUsername(dbUser)));
}

@Test
public void testGetDbUserWithQuotes() {
String dbUser = "testUser";
ConnectionConfig connectionConfig = new ConnectionConfig().setUsername(dbUser);
connectionConfig.setDialectType(DialectType.OB_ORACLE);
String expectedDbUser = "\"testUser\"";
assertEquals(expectedDbUser, OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

@Test
public void testGetDbUserWithTenant() {
String dbUser = "testUser";
String tenantName = "testTenant";
String expectedDbUser = dbUser + "@" + tenantName;
ConnectionConfig connectionConfig = new ConnectionConfig().setUsername(dbUser).setTenantName(tenantName);
assertEquals(expectedDbUser, OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

@Test
public void testGetDbUserWithCluster() {
String dbUser = "testUser";
String clusterName = "testCluster";
String expectedDbUser = dbUser + "#" + clusterName;
ConnectionConfig connectionConfig = new ConnectionConfig().setUsername(dbUser).setClusterName(clusterName);
assertEquals(expectedDbUser, OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

@Test
public void testGetDbUserWithTenantAndCluster() {
String dbUser = "testUser";
String tenantName = "testTenant";
String clusterName = "testCluster";
String expectedDbUser = dbUser + "@" + tenantName + "#" + clusterName;
ConnectionConfig connectionConfig = new ConnectionConfig().setUsername(dbUser).setTenantName(tenantName)
.setClusterName(clusterName);
assertEquals(expectedDbUser, OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

@Test
public void testGetPassword() {
String password = "testPassword";
assertEquals(password, OBConsoleDataSourceFactory.getPassword(new ConnectionConfig().setPassword(password)));
}

@Test
public void testGetJdbcParamsWithSocksProxy() {
Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("socksProxyHost", "localhost");
jdbcParams.put("socksProxyPort", "1234");
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setEndpoint(
new ConnectionConfig.Endpoint().setProxyHost("localhost").setProxyPort(1234))).containsKey("socksProxyHost"));
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setEndpoint(
new ConnectionConfig.Endpoint().setProxyHost("localhost").setProxyPort(1234))).containsKey("socksProxyPort"));
}

@Test
public void testGetJdbcParamsWithSSL() {
Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("useSSL", "true");
jdbcParams.put("trustStore", "path/to/truststore.p12");
jdbcParams.put("trustStorePassword", "password");
jdbcParams.put("keyStore", "path/to/keystore.p12");
jdbcParams.put("keyStorePassword", "password");
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setSslConfig(
new SSLConfig().setEnabled(true).setTrustStoreFilePath("path/to/truststore.p12")
.setTrustStoreFilePassword("password").setKeyStoreFilePath("path/to/keystore.p12")
.setKeyStoreFilePassword("password"))).containsKey("useSSL"));
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setSslConfig(
new SSLConfig().setEnabled(true).setTrustStoreFilePath("path/to/truststore.p12")
.setTrustStoreFilePassword("password").setKeyStoreFilePath("path/to/keystore.p12")
.setKeyStoreFilePassword("password"))).containsKey("trustStore"));
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setSslConfig(
new SSLConfig().setEnabled(true).setTrustStoreFilePath("path/to/truststore.p12")
.setTrustStoreFilePassword("password").setKeyStoreFilePath("path/to/keystore.p12")
.setKeyStoreFilePassword("password"))).containsKey("trustStorePassword"));
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setSslConfig(
new SSLConfig().setEnabled(true).setTrustStoreFilePath("path/to/truststore.p12")
.setTrustStoreFilePassword("password").setKeyStoreFilePath("path/to/keystore.p12")
.setKeyStoreFilePassword("password"))).containsKey("keyStore"));
assertTrue(OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig().setSslConfig(
new SSLConfig().setEnabled(true).setTrustStoreFilePath("path/to/truststore.p12")
.setTrustStoreFilePassword("password").setKeyStoreFilePath("path/to/keystore.p12")
.setKeyStoreFilePassword("password"))).containsKey("keyStorePassword"));
}
}