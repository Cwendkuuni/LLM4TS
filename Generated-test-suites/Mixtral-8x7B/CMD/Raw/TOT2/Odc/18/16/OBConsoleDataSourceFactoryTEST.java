import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
 .thenReturn("jdbcUrl");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("driverClassName");

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true, true);
 obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 }

 @Test
 public void testGetJdbcUrl() {
 assertEquals("jdbcUrl", obConsoleDataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("username", obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = obConsoleDataSourceFactory.getDataSource();
 assertEquals("jdbcUrl", dataSource.getUrl());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals("driverClassName", dataSource.getDriverClassName());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
 assertEquals(obConsoleDataSourceFactory.getUsername(), copy.getUsername());
 assertEquals(obConsoleDataSourceFactory.getPassword(), copy.getPassword());

 // Ensure the clone is separate object
 obConsoleDataSourceFactory.setUsername("updatedUsername");
 assertEquals("updatedUsername", obConsoleDataSourceFactory.getUsername());
 assertEquals("username", copy.getUsername());
 }

 @Test
 public void testResetUsername() {
 obConsoleDataSourceFactory.resetUsername(value -> "updatedUsername");
 assertEquals("updatedUsername", obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 obConsoleDataSourceFactory.resetPassword(value -> "updatedPassword");
 assertEquals("updatedPassword", obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 obConsoleDataSourceFactory.resetHost(value -> "updatedHost");
 assertEquals("updatedHost", obConsoleDataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 obConsoleDataSourceFactory.resetPort(value -> 1234);
 assertEquals(Integer.valueOf(1234), obConsoleDataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 obConsoleDataSourceFactory.resetSchema(value -> "updatedSchema");
 assertEquals("updatedSchema", obConsoleDataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("key", "value");
 obConsoleDataSourceFactory.resetParameters(value -> newParams);
 assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
 }

 private ConnectionConfig getConnectionConfig() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("username");
 connectionConfig.setPassword("password");
 connectionConfig.setHost("host");
 connectionConfig.setPort(5000);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setProxyHost("proxyHost");
 endpoint.setProxyPort(5001);
 connectionConfig.setEndpoint(endpoint);

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keyStoreFilePath");
 sslFileEntry.setKeyStoreFilePassword("keyStoreFilePassword");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 Map<String, Object> jdbcUrlParameters = new HashMap<>();
 jdbcUrlParameters.put("key", "value");
 connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);

 return connectionConfig;
 }
}