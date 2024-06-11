import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;
 private SingleConnectionDataSource singleConnectionDataSource;

 @Before
 public void setup() {
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 connectionConfig = new ConnectionConfig();
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 // Set some parameters
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("maxAllowedPacket", "64000000");
 jdbcParams.put("allowMultiQueries", "true");
 connectionConfig.setJdbcUrlParameters(jdbcParams);

 String jdbcUrl = dataSourceFactory.getJdbcUrl();

 // Validate the generated JDBC URL
 assertTrue(jdbcUrl.contains("maxAllowedPacket=64000000"));
 assertTrue(jdbcUrl.contains("allowMultiQueries=true"));
 }

 @Test
 public void testGetUsername() {
 // Set the username
 connectionConfig.setUsername("testUser");

 String username = dataSourceFactory.getUsername();

 assertEquals("testUser@", username);
 }

 @Test
 public void testGetPassword() {
 // Set the password
 connectionConfig.setPassword("testPassword");

 String password = dataSourceFactory.getPassword();

 assertEquals("testPassword", password);
 }

 @Test
 public void testGetParameters() {
 // Set some parameters
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("maxAllowedPacket", "64000000");
 jdbcParams.put("allowMultiQueries", "true");
 connectionConfig.setJdbcUrlParameters(jdbcParams);

 Map<String, String> parameters = dataSourceFactory.getParameters();

 assertEquals(2, parameters.size());
 assertTrue(parameters.containsKey("maxAllowedPacket"));
 assertTrue(parameters.containsKey("allowMultiQueries"));
 }

 @Test
 public void testGetDataSource() throws SQLException {
 // Mock connection properties and initializers
 Properties connectionProperties = new Properties();
 List<ConnectionInitializer> initializers = CollectionUtils.emptyList();

 // Set the data source
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 Mockito.when(connectionExtensionPoint.createDataSource(Mockito.anyString(), Mockito.anyString(),
 Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap(), Mockito.eq(connectionProperties),
 Mockito.eq(initializers), Mockito.eq(true))).thenReturn(dataSource);

 dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 dataSourceFactory.setConnectionProperties(connectionProperties);
 dataSourceFactory.setInitializers(initializers);

 // Create the data source
 DataSource dataSourceObj = dataSourceFactory.getDataSource();

 // Validate the returned data source
 assertNotNull(dataSourceObj);
 assertTrue(dataSourceObj instanceof SingleConnectionDataSource);

 // Validate that the mocked data source is returned
 Mockito.verify(connectionExtensionPoint, Mockito.times(1)).createDataSource(Mockito.anyString(),
 Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap(),
 Mockito.eq(connectionProperties), Mockito.eq(initializers), Mockito.eq(true));
 }

 @Test
 public void testGetDialectType() {
 // Set the dialect type
 connectionConfig.setDialectType(DialectType.OB_ORACLE);

 DialectType dialectType = dataSourceFactory.getDialectType();

 assertEquals(DialectType.OB_ORACLE, dialectType);
 }

 @Test
 public void testDeepCopy() throws SQLException {
 // Create a new data source factory and copy the old one
 OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();

 // Mock connection properties and initializers
 Properties connectionProperties = new Properties();
 List<ConnectionInitializer> initializers = CollectionUtils.emptyList();

 // Set the data source
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 Mockito.when(connectionExtensionPoint.createDataSource(Mockito.anyString(), Mockito.anyString(),
 Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap(), Mockito.eq(connectionProperties),
 Mockito.eq(initializers), Mockito.eq(true))).thenReturn(dataSource);

 deepCopy.setConnectionExtensionPoint(connectionExtensionPoint);
 deepCopy.setConnectionProperties(connectionProperties);
 deepCopy.setInitializers(initializers);

 // Create the data source
 DataSource dataSourceObj = deepCopy.getDataSource();

 // Validate the returned data source
 assertNotNull(dataSourceObj);
 assertTrue(dataSourceObj instanceof SingleConnectionDataSource);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetUsername(value -> newUsername);

 // Validate that the data source username has been updated
 assertEquals(newUsername, ((DriverManagerDataSource) dataSource).getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetPassword(value -> newPassword);

 // Validate that the data source password has been updated
 assertEquals(newPassword, ((DriverManagerDataSource) dataSource).getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetHost(value -> newHost);

 // Validate that the data source host has been updated
 assertEquals(newHost, ((DriverManagerDataSource) dataSource).getUrl());
 }

 @Test
 public void testResetPort() {
 int newPort = 1234;
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetPort(value -> newPort);

 // Validate that the data source port has been updated
 String url = ((DriverManagerDataSource) dataSource).getUrl();
 assertTrue(url.contains(":" + newPort));
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetSchema(value -> newSchema);

 // Validate that the data source schema has been updated
 String url = ((DriverManagerDataSource) dataSource).getUrl();
 assertTrue(url.contains(newSchema));
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 newParameters.put("param2", "value2");
 DataSource dataSource = new SimpleDriverDataSource();
 dataSourceFactory.setDataSource(dataSource);
 dataSourceFactory.resetParameters(value -> newParameters);

 // Validate that the data source parameters have been updated
 String url = ((DriverManagerDataSource) dataSource).getUrl();
 assertTrue(url.contains("param1=value1"));
 assertTrue(url.contains("param2=value2"));
 }

 @Test
 public void testSslConfig() {
 // Enable SSL and set SSL config
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 // Validate that the generated JDBC URL contains SSL parameters
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("useSSL=true"));
 assertTrue(jdbcUrl.contains("trustStore=keystore.p12"));
 assertTrue(jdbcUrl.contains("trustStorePassword=password"));
 }

 @Test
 public void testSslConfigDisabled() {
 // Disable SSL
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(false);
 connectionConfig.setSslConfig(sslConfig);

 // Validate that the generated JDBC URL does not contain SSL parameters
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("useSSL=false"));
 }

 @Test
 public void testTenantEndpoint() {
 // Set tenant endpoint
 OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
 tenantEndpoint.setProxyHost("proxyHost");
 tenantEndpoint.setProxyPort(8080);
 connectionConfig.setEndpoint(tenantEndpoint);

 // Validate that the generated JDBC URL contains proxy parameters
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("socksProxyHost=proxyHost"));
 assertTrue(jdbcUrl.contains("socksProxyPort=8080"));
 }

 @Test
 public void testCreateDataSourceWithDefaults() throws SQLException {
 // Set the data source
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 Mockito.when(connectionExtensionPoint.createDataSource(Mockito.anyString(), Mockito.anyString(),
 Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap(), Mockito.any(), Mockito.any(),
 Mockito.eq(true))).thenReturn(dataSource);

 dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

 // Create the data source
 DataSource dataSourceObj = dataSourceFactory.getDataSource();

 // Validate the returned data source
 assertNotNull(dataSourceObj);
 assertTrue(dataSourceObj instanceof SingleConnectionDataSource);
 }

 @Test
 public void testCreateDataSourceWithNullExtensionPoint() throws SQLException {
 // Set the data source
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));

 dataSourceFactory.setDataSource(dataSource);

 // Create the data source
 DataSource dataSourceObj = dataSourceFactory.getDataSource();

 // Validate the returned data source
 assertNotNull(dataSourceObj);
 assertTrue(dataSourceObj instanceof SingleConnectionDataSource);
 }

 @Test
 public void testCreateDataSourceWithNullDataSource() throws SQLException {
 dataSourceFactory.setDataSource(null);

 // Create the data source
 DataSource dataSourceObj = dataSourceFactory.getDataSource();

 // Validate that a default data source is created
 assertTrue(dataSourceObj instanceof SingleConnectionDataSource);
 }

 @Test
 public void testGetDialectTypeWithNullExtensionPoint() {
 dataSourceFactory.setConnectionExtensionPoint(null);

 DialectType dialectType = dataSourceFactory.getDialectType();

 assertNull(dialectType);
 }

 @Test
 public void testCreateDataSourceWithNullExtensionPointAndNullDataSource() throws SQLException {
 // Create the data source
 DataSource dataSourceObj = dataSourceFactory.getDataSource();

 // Validate that a default data source is created
 assertTrue(dataSourceObj instanceof SimpleDriverDataSource);
 }
}