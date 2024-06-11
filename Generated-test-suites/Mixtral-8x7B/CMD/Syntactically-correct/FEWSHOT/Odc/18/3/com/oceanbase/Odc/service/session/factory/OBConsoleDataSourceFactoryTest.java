import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private EventPublisher eventPublisher = mock(EventPublisher.class);

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
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
 assertTrue(jdbcUrl.contains("localhost"));
 assertTrue(jdbcUrl.contains("3306"));
 assertTrue(jdbcUrl.contains("testdb"));
 }

 @Test
 public void testGetUsername() {
 assertEquals(connectionConfig.getUsername(), dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals(connectionConfig.getPassword(), dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals(connectionConfig.getHost(), dataSource.getUrl());
 assertEquals(connectionConfig.getUsername(), dataSource.getUsername());
 assertEquals(connectionConfig.getPassword(), dataSource.getPassword());
 assertEquals(DialectType.OB_MYSQL.getDriverClassName(), dataSource.getDriverClassName());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(connectionConfig.getDialectType(), dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copy);
 assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
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
 assertEquals(newHost, dataSourceFactory.host);
 }

 @Test
 public void testResetPort() {
 int newPort = 4444;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.port);
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

 @Test
 public void testGetDefaultSchema() {
 assertEquals("testdb", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
 assertFalse(jdbcParams.isEmpty());
 assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
 assertTrue(jdbcParams.containsKey("allowMultiQueries"));
 assertTrue(jdbcParams.containsKey("connectTimeout"));
 assertTrue(jdbcParams.containsKey("zeroDateTimeBehavior"));
 assertTrue(jdbcParams.containsKey("noDatetimeStringSync"));
 assertTrue(jdbcParams.containsKey("jdbcCompliantTruncation"));
 }

 @Test
 public void testGetDbUser() {
 assertEquals("root", OBConsoleDataSourceFactory.getDbUser(connectionConfig));
 }

 @Test
 public void testSslConfig() {
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 sslConfig.setKeyStoreFilePath("keystore.p12");
 sslConfig.setKeyStoreFilePassword("password");
 connectionConfig.setSslConfig(sslConfig);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 Map<String, String> properties = dataSource.getConnectionProperties();
 assertTrue(properties.containsKey("useSSL"));
 assertTrue(properties.containsKey("trustServerCertificate"));
 assertTrue(properties.containsKey("trustStore"));
 assertTrue(properties.containsKey("trustStorePassword"));
 assertTrue(properties.containsKey("keyStore"));
 assertTrue(properties.containsKey("keyStorePassword"));
 }

 @Test
 public void testNoSslConfig() {
 connectionConfig.setSslConfig(null);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 Map<String, String> properties = dataSource.getConnectionProperties();
 assertTrue(properties.containsKey("useSSL"));
 assertFalse(Boolean.parseBoolean(properties.get("useSSL")));
 }

 @Test
 public void testGetEndpoint() {
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setProxyHost("proxy.com");
 endpoint.setProxyPort(8080);
 connectionConfig.setEndpoint(endpoint);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 Map<String, String> properties = dataSource.getConnectionProperties();
 assertTrue(properties.containsKey("socksProxyHost"));
 assertTrue(properties.containsKey("socksProxyPort"));
 assertEquals("proxy.com", properties.get("socksProxyHost"));
 assertEquals("8080", properties.get("socksProxyPort"));
 }

 @Test
 public void testNoEndpoint() {
 connectionConfig.setEndpoint(null);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 Map<String, String> properties = dataSource.getConnectionProperties();
 assertFalse(properties.containsKey("socksProxyHost"));
 assertFalse(properties.containsKey("socksProxyPort"));
 }

 @Test
 public void testAddInitializers() {
 List<ConnectionInitializer> initializers = Collections.singletonList(new SessionCreatedInitializer(connectionConfig));
 dataSourceFactory.setInitConnection(true);
 dataSourceFactory.setConnectionExtensionPoint(new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> jdbcUrlParams) {
 return null;
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return initializers;
 }

 @Override
 public String getDriverClassName() {
 return null;
 }
 });

 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.getInitializers().contains(initializers.get(0)));
 }

 @Test
 public void testNoInitializers() {
 List<ConnectionInitializer> initializers = Collections.emptyList();
 dataSourceFactory.setInitConnection(true);
 dataSourceFactory.setConnectionExtensionPoint(new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> jdbcUrlParams) {
 return null;
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return initializers;
 }

 @Override
 public String getDriverClassName() {
 return null;
 }
 });

 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.getInitializers().isEmpty());
 }

 @Test
 public void testDisableAutoCommit() {
 connectionConfig.setAutoCommit(false);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertFalse(dataSource.isAutoCommit());
 }

 @Test
 public void testEnableAutoCommit() {
 connectionConfig.setAutoCommit(true);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.isAutoCommit());
 }

 @Test
 public void testDisableConnectionInitialization() {
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 dataSourceFactory.setInitConnection(false);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.getInitializers().isEmpty());
 }

 @Test
 public void testConnectionInitializationWithEmptyInitializers() {
 List<ConnectionInitializer> initializers = Collections.emptyList();
 dataSourceFactory.setInitConnection(true);
 dataSourceFactory.setConnectionExtensionPoint(new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> jdbcUrlParams) {
 return null;
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return initializers;
 }

 @Override
 public String getDriverClassName() {
 return null;
 }
 });
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.getInitializers().isEmpty());
 }

 @Test
 public void testConnectionInitializationWithInitializers() {
 List<ConnectionInitializer> initializers = Arrays.asList(new SessionCreatedInitializer(connectionConfig),
 new SessionCreatedInitializer(connectionConfig));
 dataSourceFactory.setInitConnection(true);
 dataSourceFactory.setConnectionExtensionPoint(new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> jdbcUrlParams) {
 return null;
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return initializers;
 }

 @Override
 public String getDriverClassName() {
 return null;
 }
 });
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals(initializers.size(), dataSource.getInitializers().size());
 }

 @Test
 public void testDisableEventPublisher() {
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 dataSourceFactory.setEventPublisher(null);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertNull(dataSource.getEventPublisher());
 }

 @Test
 public void testEnableEventPublisher() {
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 dataSourceFactory.setEventPublisher(eventPublisher);
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertSame(eventPublisher, dataSource.getEventPublisher());
 }

 @Test
 public void testDisableConnectionFactoryClone() {
 dataSourceFactory = spy(new OBConsoleDataSourceFactory(connectionConfig, true));
 doReturn(null).when(dataSourceFactory).deepCopy();
 OBConsoleDataSourceFactory clone = dataSourceFactory.clone();
 assertSame(dataSourceFactory, clone);
 }

 @Test
 public void testEnableConnectionFactoryClone() {
 OBConsoleDataSourceFactory dataSourceFactorySpy = spy(new OBConsoleDataSourceFactory(connectionConfig, true));
 OBConsoleDataSourceFactory clone = dataSourceFactorySpy.clone();
 assertNotSame(dataSourceFactorySpy, clone);
 }
}