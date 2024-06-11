import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.generateJdbcUrl("host", 1234, "schema", new HashMap<>()))
.thenReturn("jdbc:oceanbase:oracle:@host:1234:schema");
when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.odc.driver.OracleDriver");

dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true, true);
dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbc:oceanbase:oracle:@host:1234:schema", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("user", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("pass", dataSourceFactory.getPassword());
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("com.oceanbase.odc.driver.OracleDriver", dataSource.getClass().getName());
SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
assertEquals("jdbc:oceanbase:oracle:@host:1234:schema", singleConnectionDataSource.getUrl());
assertEquals("user", singleConnectionDataSource.getUsername());
assertEquals("pass", singleConnectionDataSource.getPassword());
Properties properties = singleConnectionDataSource.getConnectionProperties();
assertEquals("false", properties.getProperty("allowLoadLocalInfile"));
assertEquals("false", properties.getProperty("allowUrlInLocalInfile"));
assertEquals("", properties.getProperty("allowLoadLocalInfileInPath"));
assertEquals("false", properties.getProperty("autoDeserialize"));
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
DataSourceFactory copy = dataSourceFactory.deepCopy();
assertNotNull(copy);
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getHost(), copy.getHost());
assertEquals(dataSourceFactory.getPort(), copy.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(), copy.getParameters());
assertFalse(dataSourceFactory == copy);
}

@Test
public void testResetUsername() {
String newUsername = "newUser";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
assertEquals(newUsername, dataSourceFactory.getDataSource().getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPass";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
assertEquals(newPassword, dataSourceFactory.getDataSource().getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getHost());
assertEquals(newHost, dataSourceFactory.getDataSource().getHost());
}

@Test
public void testResetPort() {
int newPort = 5678;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getPort());
assertEquals(newPort, dataSourceFactory.getDataSource().getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
assertEquals(newSchema, dataSourceFactory.getDataSource().getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
assertEquals(newParams, dataSourceFactory.getDataSource().getParameters());
}

private ConnectionConfig createConnectionConfig() {
ConnectionConfig config = new ConnectionConfig();
config.setUsername("user");
config.setPassword("pass");
config.setHost("host");
config.setPort(1234);
config.setDefaultSchema("schema");
config.setDialectType(DialectType.OB_ORACLE);
config.setEndpoint(new OBTenantEndpoint("proxyHost", 8080));
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
sslFileEntry.setKeyStoreFilePassword("password");
sslConfig.setSslFileEntry(sslFileEntry);
config.setSslConfig(sslConfig);
config.getJdbcUrlParameters().put("paramKey", "paramValue");
return config;
}

}