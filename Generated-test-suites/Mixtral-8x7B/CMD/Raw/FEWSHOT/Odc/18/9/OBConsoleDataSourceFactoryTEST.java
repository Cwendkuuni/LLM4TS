import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

@Test
public void testGetUsername() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("testUser");
connectionConfig.setDialectType(DialectType.OB_ORACLE);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals("\"testUser\"", factory.getUsername());
}

@Test
public void testGetPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("testPassword");
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals("testPassword", factory.getPassword());
}

@Test
public void testGetConnectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setType(ConnectType.STANDALONE);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals(ConnectType.STANDALONE, factory.getConnectType());
}

@Test
public void testGetJdbcUrl() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("testHost");
connectionConfig.setPort(1234);
connectionConfig.setDefaultSchema("testSchema");
Map<String, String> parameters = new HashMap<>();
parameters.put("param1", "value1");
connectionConfig.setParameters(parameters);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals("jdbc:oceanbase:oracle://testHost:1234/testSchema?param1=value1", factory.getJdbcUrl());
}

@Test
public void testGetJdbcParams() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBTenantEndpoint endpoint = new OBTenantEndpoint();
endpoint.setProxyHost("proxyHost");
endpoint.setProxyPort(5678);
connectionConfig.setEndpoint(endpoint);
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("keyStoreFilePath");
sslFileEntry.setKeyStoreFilePassword("keyStoreFilePassword");
sslConfig.setSslFileEntry(sslFileEntry);
connectionConfig.setSslConfig(sslConfig);
Map<String, String> jdbcUrlParameters = new HashMap<>();
jdbcUrlParameters.put("key2", "value2");
connectionConfig.getJdbcUrlParameters().putAll(jdbcUrlParameters);
Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertEquals("64000000", result.get("maxAllowedPacket"));
assertEquals("true", result.get("allowMultiQueries"));
assertEquals("5000", result.get("connectTimeout"));
assertEquals(DEFAULT_ZERO_DATE_TIME_BEHAVIOR, result.get("zeroDateTimeBehavior"));
assertEquals("true", result.get("noDatetimeStringSync"));
assertEquals("false", result.get("jdbcCompliantTruncation"));
assertEquals("false", result.get("sendConnectionAttributes"));
assertEquals("proxyHost", result.get("socksProxyHost"));
assertEquals("5678", result.get("socksProxyPort"));
assertEquals("true", result.get("useSSL"));
assertEquals("true", result.get("trustServerCertificate"));
assertEquals("keyStoreFilePath", result.get("trustStore"));
assertEquals("keyStoreFilePassword", result.get("trustStorePassword"));
assertEquals("keyStoreFilePath", result.get("keyStore"));
assertEquals("keyStoreFilePassword", result.get("keyStorePassword"));
assertEquals("false", result.get("trustServerCertificate"));
assertEquals("value2", result.get("key2"));
}

@Test
public void testGetDataSource() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("testHost");
connectionConfig.setPort(1234);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
SingleConnectionDataSource dataSource = new OBConsoleDataSourceFactory(connectionConfig, true).getDataSource();
assertEquals("testHost", dataSource.getUrl());
assertEquals("testUser", dataSource.getUsername());
assertEquals("testPassword", dataSource.getPassword());
assertEquals(ConnectionInitializer.class, dataSource.getInitializerTypes().get(0));
}

@Test
public void testGetDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
assertEquals(DialectType.OB_ORACLE, factory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("testHost");
connectionConfig.setPort(1234);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
CloneableDataSourceFactory clonedFactory = factory.deepCopy();
assertEquals(factory.getUsername(), clonedFactory.getUsername());
assertEquals(factory.getPassword(), clonedFactory.getPassword());
assertEquals(factory.getHost(), clonedFactory.getHost());
assertEquals(factory.getPort(), clonedFactory.getPort());
}

@Test
public void testResetUsername() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("testUser");
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetUsername(value -> "newTestUser");
assertEquals("newTestUser", factory.getUsername());
}

@Test
public void testResetPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("testPassword");
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetPassword(value -> "newTestPassword");
assertEquals("newTestPassword", factory.getPassword());
}

@Test
public void testResetHost() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("testHost");
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetHost(value -> "newTestHost");
assertEquals("newTestHost", factory.getHost());
}

@Test
public void testResetPort() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPort(1234);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetPort(value -> 5678);
assertEquals(5678, factory.getPort());
}

@Test
public void testResetSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDefaultSchema("testSchema");
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetSchema(value -> "newTestSchema");
assertEquals("newTestSchema", factory.getDefaultSchema());
}

@Test
public void testResetParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
Map<String, String> parameters = new HashMap<>();
parameters.put("param1", "value1");
connectionConfig.setParameters(parameters);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
Map<String, String> newParameters = new HashMap<>();
newParameters.put("param1", "newValue1");
newParameters.put("param2", "value2");
factory.resetParameters(value -> newParameters);
assertEquals("newValue1", factory.getParameters().get("param1"));
assertEquals("value2", factory.getParameters().get("param2"));
}

}