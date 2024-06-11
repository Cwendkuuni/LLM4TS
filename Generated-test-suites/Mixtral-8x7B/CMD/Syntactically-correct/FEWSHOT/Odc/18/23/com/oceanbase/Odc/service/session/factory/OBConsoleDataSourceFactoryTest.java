import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 connectionConfig = mock(ConnectionConfig.class);
 eventPublisher = mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "testSchema";
 Map<String, String> parameters = new HashMap<>();
 when(connectionConfig.getHost()).thenReturn(host);
 when(connectionConfig.getPort()).thenReturn(port);
 when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters))
 .thenReturn("jdbc:test:testSchema");

 assertEquals("jdbc:test:testSchema", dataSourceFactory.getJdbcUrl());
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

 assertEquals("\"" + username + "\"@" + tenantName, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 String password = "testPassword";
 when(connectionConfig.getPassword()).thenReturn(password);

 assertEquals(password, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("maxAllowedPacket", "64000000");
 jdbcParams.put("allowMultiQueries", "true");
 jdbcParams.put("connectTimeout", "5000");
 jdbcParams.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 jdbcParams.put("noDatetimeStringSync", "true");
 jdbcParams.put("jdbcCompliantTruncation", "false");

 assertEquals(jdbcParams, dataSourceFactory.getJdbcParams(connectionConfig));
 }

 @Test
 public void testGetDataSource() {
 String jdbcUrl = "jdbc:test:testSchema";
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
 dataSource.setEventPublisher(eventPublisher);
 dataSource.setUrl(jdbcUrl);
 dataSource.setUsername("testUser");
 dataSource.setPassword("testPassword");
 dataSource.setDriverClassName("testDriver");
 Properties properties = new Properties();
 properties.setProperty("allowLoadLocalInfile", "false");
 properties.setProperty("allowUrlInLocalInfile", "false");
 properties.setProperty("allowLoadLocalInfileInPath", "");
 properties.setProperty("autoDeserialize", "false");
 dataSource.setConnectionProperties(properties);

 when(connectionExtensionPoint.getDriverClassName()).thenReturn("testDriver");
 when(connectionConfig.getEndpoint()).thenReturn(null);
 when(connectionConfig.getSslConfig()).thenReturn(null);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());

 assertEquals(dataSource, dataSourceFactory.getDataSource());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newTestUser";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newUsername;
 dataSourceFactory.resetUsername(mapper);
 verify(connectionConfig).setUsername(newUsername);
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newTestPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newPassword;
 dataSourceFactory.resetPassword(mapper);
 verify(connectionConfig).setPassword(newPassword);
 }

 @Test
 public void testResetHost() {
 String newHost = "newTestHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newHost;
 dataSourceFactory.resetHost(mapper);
 verify(connectionConfig).setHost(newHost);
 }

 @Test
 public void testResetPort() {
 Integer newPort = 5678;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> newPort;
 dataSourceFactory.resetPort(mapper);
 verify(connectionConfig).setPort(newPort);
 }

 @Test
 public void testResetSchema() {
 String newDefaultSchema = "newTestSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newDefaultSchema;
 dataSourceFactory.resetSchema(mapper);
 verify(connectionConfig).setDefaultSchema(newDefaultSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("testKey", "testValue");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newParameters;
 dataSourceFactory.resetParameters(mapper);
 verify(connectionConfig).getJdbcUrlParameters().clear();
 verify(connectionConfig).getJdbcUrlParameters().putAll(newParameters);
 }
}