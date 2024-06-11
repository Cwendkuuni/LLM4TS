import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtilsTest;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 connectionConfig = new ConnectionConfig();
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 eventPublisher = mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher,
 connectionExtensionPoint);
 }

 @Test
 public void testGetJdbcUrl() {
 // Given
 String host = "localhost";
 Integer port = 3306;
 String defaultSchema = "testDB";
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);

 // When
 String result = dataSourceFactory.getJdbcUrl();

 // Then
 String expected = connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters);
 assertEquals(expected, result);
 }

 @Test
 public void testGetUsername() {
 // Given
 String username = "testUser";
 String tenantName = "testTenant";
 String clusterName = "testCluster";
 DialectType dialectType = DialectType.OB_ORACLE;
 connectionConfig.setUsername(username);
 connectionConfig.setOBTenantName(tenantName);
 connectionConfig.setClusterName(clusterName);
 connectionConfig.setDialectType(dialectType);

 // When
 String result = dataSourceFactory.getUsername();

 // Then
 String expected = "\"" + username + "\"@" + tenantName;
 if (StringUtils.hasLength(clusterName)) {
 expected += "#" + clusterName;
 }
 assertEquals(expected, result);
 }

 @Test
 public void testGetPassword() {
 // Given
 String password = "testPassword";
 connectionConfig.setPassword(password);

 // When
 String result = dataSourceFactory.getPassword();

 // Then
 assertEquals(password, result);
 }

 @Test
 public void testGetJdbcParams() {
 // Given
 Map<String, String> jdbcUrlParams = new HashMap<>();
 jdbcUrlParams.put("maxAllowedPacket", "64000000");
 jdbcUrlParams.put("allowMultiQueries", "true");
 jdbcUrlParams.put("connectTimeout", "5000");
 jdbcUrlParams.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 jdbcUrlParams.put("noDatetimeStringSync", "true");
 jdbcUrlParams.put("jdbcCompliantTruncation", "false");
 jdbcUrlParams.put("sendConnectionAttributes", "false");
 connectionConfig.setEndpoint(new OBTenantEndpoint("proxyHost", 1234));
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keyStoreFilePath");
 sslFileEntry.setKeyStoreFilePassword("keyStoreFilePassword");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);
 connectionConfig.getJdbcUrlParameters().put("key", "value");

 // When
 Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 // Then
 assertEquals(jdbcUrlParams, result);
 }

 @Test
 public void testGetDataSource() {
 // Given
 String jdbcUrl = "jdbcUrl";
 String username = "username";
 String password = "password";
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
 dataSource.setEventPublisher(eventPublisher);
 dataSource.setUrl(jdbcUrl);
 dataSource.setUsername(username);
 dataSource.setPassword(password);
 dataSource.setDriverClassName("driverClassName");
 Properties properties = new Properties();
 properties.setProperty("allowLoadLocalInfile", "false");
 properties.setProperty("allowUrlInLocalInfile", "false");
 properties.setProperty("allowLoadLocalInfileInPath", "");
 properties.setProperty("autoDeserialize", "false");
 dataSource.setConnectionProperties(properties);
 dataSource.setAutoCommit(true);
 List<ConnectionInitializer> initializers = Arrays.asList(new ConnectionInitializer() {
 }, new ConnectionInitializer() {
 });
 SessionCreatedInitializer sessionCreatedInitializer = new SessionCreatedInitializer(connectionConfig);
 dataSource.addInitializer(sessionCreatedInitializer);

 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn(jdbcUrl);
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("driverClassName");
 when(connectionConfig.getUsername()).thenReturn(username);
 when(connectionConfig.getPassword()).thenReturn(password);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
 when(eventPublisher.publish(any())).thenReturn(null);

 // When
 DataSource result = dataSourceFactory.getDataSource();

 // Then
 assertEquals(dataSource, result);
 }

 @Test
 public void testGetDialectType() {
 // Given
 DialectType dialectType = DialectType.OB_ORACLE;
 connectionConfig.setDialectType(dialectType);

 // When
 DialectType result = dataSourceFactory.getDialectType();

 // Then
 assertEquals(dialectType, result);
 }

 @Test
 public void testDeepCopy() {
 // Given
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);

 // When
 OBConsoleDataSourceFactory copy = original.deepCopy();

 // Then
 assertNotSame(original, copy);
 assertEquals(original.connectionConfig, copy.connectionConfig);
 assertEquals(original.autoCommit, copy.autoCommit);
 }

 @Test
 public void testResetUsername() {
 // Given
 String originalUsername = "originalUsername";
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = v -> newUsername;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetUsername(mapper);

 // Then
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 // Given
 String originalPassword = "originalPassword";
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = v -> newPassword;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetPassword(mapper);

 // Then
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 // Given
 String originalHost = "originalHost";
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = v -> newHost;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetHost(mapper);

 // Then
 assertEquals(newHost, dataSourceFactory.host);
 }

 @Test
 public void testResetPort() {
 // Given
 Integer originalPort = 1234;
 Integer newPort = 5678;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = v -> newPort;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetPort(mapper);

 // Then
 assertEquals(newPort, dataSourceFactory.port);
 }

 @Test
 public void testResetSchema() {
 // Given
 String originalSchema = "originalSchema";
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = v -> newSchema;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetSchema(mapper);

 // Then
 assertEquals(newSchema, dataSourceFactory.defaultSchema);
 }

 @Test
 public void testResetParameters() {
 // Given
 Map<String, String> originalParameters = new HashMap<>();
 originalParameters.put("param1", "value1");
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "newValue1");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = v -> newParameters;
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 dataSourceFactory.resetParameters(mapper);

 // Then
 assertEquals(newParameters, dataSourceFactory.parameters);
 }

}