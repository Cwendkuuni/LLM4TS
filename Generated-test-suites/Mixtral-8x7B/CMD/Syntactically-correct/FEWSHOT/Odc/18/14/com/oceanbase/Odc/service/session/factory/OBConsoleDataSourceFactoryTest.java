import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {
 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private EventPublisher eventPublisher;
 private ConnectionInitializer connectionInitializer;
 private ConnectionMapper connectionMapper;
 private SessionCreatedInitializer sessionCreatedInitializer;

 @Before
 public void setup() {
 connectionConfig = mock(ConnectionConfig.class);
 eventPublisher = mock(EventPublisher.class);
 connectionInitializer = mock(ConnectionInitializer.class);
 connectionMapper = mock(ConnectionMapper.class);
 sessionCreatedInitializer = mock(SessionCreatedInitializer.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher, connectionInitializer,
 connectionMapper, sessionCreatedInitializer);
 }

 @Test
 public void testGetJdbcUrl() {
 String host = "testHost";
 Integer port = 1234;
 String defaultSchema = "testSchema";
 Map<String, String> parameters = new HashMap<>();
 when(connectionConfig.getHost()).thenReturn(host);
 when(connectionConfig.getPort()).thenReturn(port);
 when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);
 String result = dataSourceFactory.getJdbcUrl();
 assertEquals("jdbc:oceanbase://" + host + ":" + port + "/" + defaultSchema + "?" + parameters, result);
 }

 @Test
 public void testGetUsername() {
 String username = "testUser";
 String tenantName = "testTenant";
 String clusterName = "testCluster";
 when(connectionConfig.getUsername()).thenReturn(username);
 when(connectionConfig.getTenantName()).thenReturn(tenantName);
 when(connectionConfig.getClusterName()).thenReturn(clusterName);
 String result = OBConsoleDataSourceFactory.getUsername(connectionConfig);
 assertEquals(username + "@" + tenantName + "#" + clusterName, result);
 }

 @Test
 public void testGetPassword() {
 String password = "testPassword";
 when(connectionConfig.getPassword()).thenReturn(password);
 String result = OBConsoleDataSourceFactory.getPassword(connectionConfig);
 assertEquals(password, result);
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
 assertEquals(10, result.size());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals(true, dataSource.isAutoCommit());
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = DialectType.OB_MYSQL;
 when(connectionConfig.getDialectType()).thenReturn(dialectType);
 assertEquals(dialectType, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copy);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newUsername;
 dataSourceFactory.resetUsername(mapper);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newPassword;
 dataSourceFactory.resetPassword(mapper);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newHost;
 dataSourceFactory.resetHost(mapper);
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 Integer newPort = 9876;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> newPort;
 dataSourceFactory.resetPort(mapper);
 assertEquals(newPort, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newSchema;
 dataSourceFactory.resetSchema(mapper);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key", "value");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newParameters;
 dataSourceFactory.resetParameters(mapper);
 assertEquals(newParameters, dataSourceFactory.getParameters());
 }
}