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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

import lombok.NonNull;
import lombok.Setter;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 @Mock
 private EventPublisher eventPublisher;

 private ConnectionExtensionPoint connectionExtensionPointMock;

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPointMock = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
 dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = connectionExtensionPointMock.generateJdbcUrl("testHost", 1234, "testSchema",
 Collections.emptyMap());
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 ConnectionConfig connectionConfig = getConnectionConfig();
 String expectedUsername = getDbUser(connectionConfig);
 assertEquals(expectedUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 ConnectionConfig connectionConfig = getConnectionConfig();
 String expectedPassword = connectionConfig.getPassword();
 assertEquals(expectedPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetDialectType() {
 DialectType expectedDialectType = DialectType.OB_MYSQL;
 assertEquals(expectedDialectType, dataSourceFactory.getDialectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 assertEquals(dataSourceFactory.getUsername(), dataSource.getUsername());
 assertEquals(dataSourceFactory.getPassword(), dataSource.getPassword());
 assertEquals(connectionExtensionPointMock.getDriverClassName(), dataSource.getDriverClassName());
 Properties properties = dataSource.getConnectionProperties();
 assertFalse(properties.isEmpty());
 assertTrue(properties.containsKey("allowLoadLocalInfile"));
 assertTrue(properties.containsKey("allowUrlInLocalInfile"));
 assertTrue(properties.containsKey("allowLoadLocalInfileInPath"));
 assertTrue(properties.containsKey("autoDeserialize"));
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 ValueMapper<String> valueMapper = value -> newUsername;
 dataSourceFactory.resetUsername(valueMapper);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 ValueMapper<String> valueMapper = value -> newPassword;
 dataSourceFactory.resetPassword(valueMapper);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 ValueMapper<String> valueMapper = value -> newHost;
 dataSourceFactory.resetHost(valueMapper);
 assertEquals(newHost, dataSourceFactory.host);
 }

 @Test
 public void testResetPort() {
 int newPort = 9876;
 ValueMapper<Integer> valueMapper = value -> newPort;
 dataSourceFactory.resetPort(valueMapper);
 assertEquals(newPort, dataSourceFactory.port);
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 ValueMapper<String> valueMapper = value -> newSchema;
 dataSourceFactory.resetSchema(valueMapper);
 assertEquals(newSchema, dataSourceFactory.defaultSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("newKey", "newValue");
 ValueMapper<Map<String, String>> valueMapper = value -> newParameters;
 dataSourceFactory.resetParameters(valueMapper);
 assertEquals(newParameters, dataSourceFactory.parameters);
 }

 private ConnectionConfig getConnectionConfig() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("testHost");
 connectionConfig.setPort(1234);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setDefaultSchema("testSchema");
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(false);
 connectionConfig.setSslConfig(sslConfig);
 OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
 tenantEndpoint.setProxyHost("proxyHost");
 tenantEndpoint.setProxyPort(8080);
 connectionConfig.setEndpoint(tenantEndpoint);
 return connectionConfig;
 }

 private static String getDbUser(ConnectionConfig connectionConfig) {
 String username = connectionConfig.getUsername();
 if (DialectType.OB_ORACLE.equals(connectionConfig.getDialectType())) {
 return "\"" + username + "\"";
 }
 return username;
 }

}