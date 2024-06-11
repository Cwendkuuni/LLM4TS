import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.getDriverClassName()).thenReturn("driverClassName");
when(connectionExtensionPoint.generateJdbcUrl( "host", 1234, "schema", new HashMap<String, String>()))
.thenReturn("jdbcUrl");
when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());

dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true, true, connectionExtensionPoint);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbcUrl", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("username", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("password", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("jdbcUrl", dataSource.getUrl());
assertEquals("username", dataSource.getUsername());
assertEquals("password", dataSource.getPassword());
assertEquals("driverClassName", dataSource.getDriverClassName());
Properties properties = dataSource.getConnectionProperties();
assertEquals("false", properties.get("allowLoadLocalInfile"));
assertEquals("false", properties.get("allowUrlInLocalInfile"));
assertEquals("", properties.get("allowLoadLocalInfileInPath"));
assertEquals("false", properties.get("autoDeserialize"));
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
assertNotNull(copy);
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getHost(), copy.getHost());
assertEquals(dataSourceFactory.getPort(), copy.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(), copy.getParameters());
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
int newPort = 5678;
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
Map<String, String> newParameters = new HashMap<>();
newParameters.put("key", "value");
dataSourceFactory.resetParameters(value -> newParameters);
assertEquals(newParameters, dataSourceFactory.getParameters());
}

private ConnectionConfig getConnectionConfig() {
return ConnectionConfig.builder()
.username("username")
.password("password")
.host("host")
.port(1234)
.dialectType(DialectType.OB_MYSQL)
.build();
}

private ConnectionConfig getConnectionConfigWithSsl() {
ConnectionConfig config = getConnectionConfig();
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("keystoreFilePath");
sslFileEntry.setKeyStoreFilePassword("keystoreFilePassword");
sslConfig.setSslFileEntry(sslFileEntry);
config.setSslConfig(sslConfig);
return config;
}
}