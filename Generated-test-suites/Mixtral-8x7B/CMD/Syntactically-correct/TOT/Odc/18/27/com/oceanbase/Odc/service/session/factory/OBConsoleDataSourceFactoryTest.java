import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private OBConsoleDataSourceFactory dataSourceFactory;
 private MockedStatic<ConnectionPluginUtil> connectionPluginUtilMock;

 @Before
 public void setUp() {
 connectionPluginUtilMock = mockStatic(ConnectionPluginUtil.class);
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("key", "value");
 connectionPluginUtilMock.when(ConnectionPluginUtil::getConnectionExtension)
 .withArguments(DialectType.OB_MYSQL)
 .thenReturn(connectionExtensionPoint);
 Mockito.when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), any()))
 .thenReturn("jdbc:mock://mock-host:mock-port/mock-schema");
 Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.jdbc.Driver");
 }

 @Test
 public void testGetJdbcUrl() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 assertEquals("jdbc:mock://mock-host:mock-port/mock-schema", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetDataSource() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 SingleConnectionDataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 assertEquals("com.mock.jdbc.Driver", dataSource.getDriverClassName());
 assertEquals("jdbc:mock://mock-host:mock-port/mock-schema", dataSource.getUrl());
 assertEquals(createValidConnectionConfig().getUsername(), dataSource.getUsername());
 assertEquals(createValidConnectionConfig().getPassword(), dataSource.getPassword());
 Properties properties = new Properties();
 properties.setProperty("key", "value");
 assertEquals(properties, dataSource.getConnectionProperties());
 }

 @Test
 public void testGetDialectType() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
 assertEquals(dataSourceFactory.getUsername(), deepCopy.getUsername());
 assertEquals(dataSourceFactory.getPassword(), deepCopy.getPassword());
 assertEquals(dataSourceFactory.getHost(), deepCopy.getHost());
 assertEquals(dataSourceFactory.getPort(), deepCopy.getPort());
 assertEquals(dataSourceFactory.getDefaultSchema(), deepCopy.getDefaultSchema());
 assertEquals(dataSourceFactory.getParameters(), deepCopy.getParameters());
 }

 @Test
 public void testResetUsername() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 String newUsername = "new_username";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 String newPassword = "new_password";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 String newHost = "new_host";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 int newPort = 1234;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 String newSchema = "new_schema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), null, true);
 Map<String, String> newParameters = Collections.singletonMap("key", "new_value");
 dataSourceFactory.resetParameters(value -> newParameters);
 assertEquals(newParameters, dataSourceFactory.getParameters());
 }

 private ConnectionConfig createValidConnectionConfig() {
 ConnectionConfig config = new ConnectionConfig();
 config.setUsername("username");
 config.setPassword("password");
 config.setHost("mock-host");
 config.setPort(1234);
 config.setDialectType(DialectType.OB_MYSQL);
 config.setDefaultSchema("mock-schema");
 config.setEndpoint(new OBTenantEndpoint());
 config.setSslConfig(new SSLConfig());
 config.getSslConfig().setEnabled(false);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("path/to/keystore");
 sslFileEntry.setKeyStoreFilePassword("keystore_password");
 config.getSslConfig().setSslFileEntry(sslFileEntry);
 config.getJdbcUrlParameters().put("key", "value");
 return config;
 }
}