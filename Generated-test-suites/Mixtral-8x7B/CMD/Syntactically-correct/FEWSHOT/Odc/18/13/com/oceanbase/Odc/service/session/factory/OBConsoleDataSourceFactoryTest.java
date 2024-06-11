import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
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
 connectionConfig = mock(ConnectionConfig.class);
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 eventPublisher = mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher,
 connectionExtensionPoint);
 }

 @Test
 public void testGetJdbcUrl() {
 String host = "localhost";
 int port = 3306;
 String defaultSchema = "test";
 Map<String, String> parameters = new HashMap<>();
 when(connectionConfig.getHost()).thenReturn(host);
 when(connectionConfig.getPort()).thenReturn(port);
 when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters))
 .thenReturn("jdbc:test:test");

 assertEquals("jdbc:test:test", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 String username = "testUser";
 String tenantName = "testTenant";
 String clusterName = "testCluster";
 when(connectionConfig.getUsername()).thenReturn(username);
 when(connectionConfig.getOBTenantName()).thenReturn(tenantName);
 when(connectionConfig.getTenantName()).thenReturn(tenantName);
 when(connectionConfig.getClusterName()).thenReturn(clusterName);

 assertEquals(username + "@" + tenantName, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetUsernameWithClusterName() {
 String username = "testUser";
 String tenantName = "testTenant";
 String clusterName = "testCluster";
 when(connectionConfig.getUsername()).thenReturn(username);
 when(connectionConfig.getOBTenantName()).thenReturn(tenantName);
 when(connectionConfig.getTenantName()).thenReturn(tenantName);
 when(connectionConfig.getClusterName()).thenReturn(clusterName);

 assertEquals(username + "@" + tenantName + "#" + clusterName,
 dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 String password = "testPassword";
 when(connectionConfig.getPassword()).thenReturn(password);

 assertEquals(password, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcUrlParams = new HashMap<>();
 jdbcUrlParams.put("maxAllowedPacket", "64000000");
 jdbcUrlParams.put("allowMultiQueries", "true");
 jdbcUrlParams.put("connectTimeout", "5000");
 jdbcUrlParams.put("zeroDateTimeBehavior", "DEFAULT_ZERO_DATE_TIME_BEHAVIOR");
 jdbcUrlParams.put("noDatetimeStringSync", "true");
 jdbcUrlParams.put("jdbcCompliantTruncation", "false");
 jdbcUrlParams.put("sendConnectionAttributes", "false");

 assertEquals(jdbcUrlParams, dataSourceFactory.getJdbcParams(connectionConfig));
 }

 @Test
 public void testGetDataSource() {
 String jdbcUrl = "jdbc:test:test";
 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 when(dataSource.getUrl()).thenReturn(jdbcUrl);
 when(dataSource.getUsername()).thenReturn("testUser");
 when(dataSource.getPassword()).thenReturn("testPassword");
 when(dataSource.getDriverClassName()).thenReturn("com.test.Driver");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.test.Driver");

 dataSourceFactory.getDataSource();

 verify(dataSource).setUrl(jdbcUrl);
 verify(dataSource).setUsername("testUser");
 verify(dataSource).setPassword("testPassword");
 verify(dataSource).setDriverClassName("com.test.Driver");
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldValue) -> newUsername;
 dataSourceFactory.resetUsername(mapper);

 verify(connectionConfig).setUsername(newUsername);
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldValue) -> newPassword;
 dataSourceFactory.resetPassword(mapper);

 verify(connectionConfig).setPassword(newPassword);
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldValue) -> newHost;
 dataSourceFactory.resetHost(mapper);

 verify(connectionConfig).setHost(newHost);
 }

 @Test
 public void testResetPort() {
 int newPort = 1234;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = (oldValue) -> newPort;
 dataSourceFactory.resetPort(mapper);

 verify(connectionConfig).setPort(newPort);
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldValue) -> newSchema;
 dataSourceFactory.resetSchema(mapper);

 verify(connectionConfig).setDefaultSchema(newSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key1", "value1");
 newParameters.put("key2", "value2");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (oldValue) -> newParameters;
 dataSourceFactory.resetParameters(mapper);

 verify(connectionConfig).setJdbcUrlParameters(newParameters);
 }
}