import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private ConnectionExtensionPoint connectionExtensionPoint;

public OBConsoleDataSourceFactoryTest() {
connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
}

@Test
public void testGetJdbcUrl() {
String host = "127.0.0.1";
Integer port = 3306;
String defaultSchema = "testdb";
Map<String, String> parameters = new HashMap<>();
parameters.put("param1", "value1");
parameters.put("param2", "value2");
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost(host);
connectionConfig.setPort(port);
connectionConfig.setDefaultSchema(defaultSchema);
connectionConfig.setJdbcUrlParameters(parameters);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = dataSourceFactory.getJdbcUrl();

// Verify the generated JDBC URL
assertEquals("jdbc:mysql://127.0.0.1:3306/testdb?param1=value1&param2=value2", jdbcUrl);
}

@Test
public void testGetUsername() {
String username = "testuser";
String tenantName = "testtenant";
String clusterName = "testcluster";
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername(username);
connectionConfig.setTenantName(tenantName);
connectionConfig.setClusterName(clusterName);
connectionConfig.setDialectType(DialectType.OB_ORACLE);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String result = dataSourceFactory.getUsername();

// Verify the generated username
assertEquals("\"testuser\"@testtenant#testcluster", result);
}

@Test
public void testGetPassword() {
String password = "testpassword";
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword(password);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
String result = dataSourceFactory.getPassword();

// Verify the generated password
assertEquals("testpassword", result);
}

@Test
public void testGetDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
DialectType result = dataSourceFactory.getDialectType();

// Verify the dialect type
assertEquals(DialectType.OB_MYSQL, result);
}

@Test
public void testGetDataSource() {
String host = "127.0.0.1";
Integer port = 3306;
String defaultSchema = "testdb";
String username = "testuser";
String password = "testpassword";
Map<String, String> parameters = new HashMap<>();
parameters.put("param1", "value1");
parameters.put("param2", "value2");
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost(host);
connectionConfig.setPort(port);
connectionConfig.setDefaultSchema(defaultSchema);
connectionConfig.setUsername(username);
connectionConfig.setPassword(password);
connectionConfig.setJdbcUrlParameters(parameters);
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
DataSource dataSource = dataSourceFactory.getDataSource();

// Verify the DataSource properties
assertEquals(host, dataSource.getConnection().getMetaData().getURL());
assertEquals(username, dataSource.getUser());
assertEquals(password, dataSource.getPassword());
assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getConnection().getMetaData().getDriverName());

// Verify the connection properties
JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
Map<String, Object> connectionProperties = jdbcTemplate.queryForMap("SELECT * FROM DUAL");
assertTrue(connectionProperties.containsKey("param1"));
assertTrue(connectionProperties.containsKey("param2"));
assertEquals("value1", connectionProperties.get("param1"));
assertEquals("value2", connectionProperties.get("param2"));
}

@Test
public void testDeepCopy() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory1 = new OBConsoleDataSourceFactory(connectionConfig, true);
CloneableDataSourceFactory dataSourceFactory2 = dataSourceFactory1.deepCopy();

// Verify the deep copy of the DataSourceFactory
assertEquals(dataSourceFactory1.getUsername(), dataSourceFactory2.getUsername());
assertEquals(dataSourceFactory1.getPassword(), dataSourceFactory2.getPassword());
assertEquals(dataSourceFactory1.getHost(), dataSourceFactory2.getHost());
assertEquals(dataSourceFactory1.getPort(), dataSourceFactory2.getPort());
assertEquals(dataSourceFactory1.getDefaultSchema(), dataSourceFactory2.getDefaultSchema());
}

@Test
public void testResetUsername() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

String newUsername = "newtestuser";
dataSourceFactory.resetUsername(value -> newUsername);

// Verify the updated username
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

String newPassword = "newtestpassword";
dataSourceFactory.resetPassword(value -> newPassword);

// Verify the updated password
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

String newHost = "192.168.1.1";
dataSourceFactory.resetHost(value -> newHost);

// Verify the updated host
assertEquals(newHost, dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

Integer newPort = 1234;
dataSourceFactory.resetPort(value -> newPort);

// Verify the updated port
assertEquals(newPort, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

String newSchema = "newtestdb";
dataSourceFactory.resetSchema(value -> newSchema);

// Verify the updated schema
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testdb");
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDialectType(DialectType.OB_MYSQL);

CloneableDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

Map<String, String> newParameters = new HashMap<>();
newParameters.put("param1", "value3");
newParameters.put("param2", "value4");
dataSourceFactory.resetParameters(value -> newParameters);

// Verify the updated parameters
assertEquals(newParameters, dataSourceFactory.getParameters());
}

@Test(expected = NotImplementedException.class)
public void testConnectTypeNotImplemented() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setType(ConnectType.JDBC);

new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidDialectType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.H2);

new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidConnectionConfig() {
new OBConsoleDataSourceFactory(null, true);
}

@Test
public void testSSLConfig() {
String keyStoreFilePath = "path/to/keystore.p12";
String keyStoreFilePassword = "keystorepassword";
String trustStoreFilePath = "path/to/truststore.p12";
String trustStoreFilePassword = "truststorepassword";

SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
sslConfig.setKeyStoreFileEntry(new SSLFileEntry(keyStoreFilePath, keyStoreFilePassword));
sslConfig.setTrustStoreFileEntry(new SSLFileEntry(trustStoreFilePath, trustStoreFilePassword));

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setSslConfig(sslConfig);

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
Properties properties = dataSourceFactory.getDataSource().getConnection().getProperties();

assertTrue(properties.containsKey("useSSL"));
assertTrue((boolean) properties.get("useSSL"));
assertTrue(properties.containsKey("trustServerCertificate"));
assertTrue((boolean) properties.get("trustServerCertificate"));
assertTrue(properties.containsKey("trustStore"));
assertEquals(trustStoreFilePath, properties.get("trustStore"));
assertTrue(properties.containsKey("trustStorePassword"));
assertEquals(trustStoreFilePassword, properties.get("trustStorePassword"));
assertTrue(properties.containsKey("keyStore"));
assertEquals(keyStoreFilePath, properties.get("keyStore"));
assertTrue(properties.containsKey("keyStorePassword"));
assertEquals(keyStoreFilePassword, properties.get("keyStorePassword"));
}

@Test
public void testSSLConfigDisabled() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setSslConfig(new SSLConfig());

OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
Properties properties = dataSourceFactory.getDataSource().getConnection().getProperties();

assertTrue(properties.containsKey("useSSL"));
assertTrue((boolean) properties.get("useSSL") == false);
}

@Test
public void testLoadSpringContext() {
ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
DataSourceFactory dataSourceFactory = (DataSourceFactory) context.getBean("dataSourceFactory");

// Verify the DataSourceFactory bean is loaded from Spring context
assertNotNull(dataSourceFactory);
}

@Test
public void testSpringContextWithProperties() {
String configFilePath = "classpath:odc.properties";
ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml", false);
context.getEnvironment().getPropertySources().addLast(new org.springframework.core.env.PropertySource<Object>("odcProperties", () -> {
Map<String, Object> source = new HashMap<>();
source.put("odc.host", "127.0.0.1");
source.put("odc.port", 3306);
source.put("odc.defaultSchema", "testdb");
source.put("odc.username", "testuser");
source.put("odc.password", "testpassword");
return source;
}));

DataSourceFactory dataSourceFactory = (DataSourceFactory) context.getBean("dataSourceFactory");

// Verify the DataSourceFactory properties are loaded from Spring context
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("jdbc:mysql://127.0.0.1:3306/testdb", dataSource.getUrl());
assertEquals("testuser", dataSource.getUsername());
assertEquals("testpassword", dataSource.getPassword());
}
}