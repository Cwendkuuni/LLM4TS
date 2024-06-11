import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 connectionConfig = Mockito.mock(ConnectionConfig.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetUsername() {
 Mockito.when(connectionConfig.getUsername()).thenReturn("testUser");
 assertEquals("testUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 Mockito.when(connectionConfig.getPassword()).thenReturn("testPassword");
 assertEquals("testPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 Mockito.when(connectionConfig.getType()).thenReturn(ConnectType.CONSOLE);
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetJdbcUrl() {
 Map<String, String> params = new HashMap<>();
 params.put("key", "value");
 Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
 Mockito.anyString(), Mockito.anyMap())).thenReturn("jdbc:test:uri");
 dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 dataSourceFactory.setParameters(params);
 assertEquals("jdbc:test:uri", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetPassword_with_null_connectionConfig() {
 dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
 assertNull(dataSourceFactory.getPassword());
 }

 @Test
 public void testGetUsername_with_null_connectionConfig() {
 dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
 assertNull(dataSourceFactory.getUsername());
 }

 @Test
 public void testGetJdbcUrl_with_null_connectionExtensionPoint() {
 dataSourceFactory.setConnectionExtensionPoint(null);
 assertNull(dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetJdbcUrl_with_empty_params() {
 Map<String, String> params = new HashMap<>();
 dataSourceFactory.setParameters(params);
 assertNull(dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> params = new HashMap<>();
 params.put("key", "value");
 assertEquals(params, dataSourceFactory.getJdbcParams(connectionConfig));
 }

 @Test
 public void testGetJdbcParams_with_null_connectionConfig() {
 assertNull(dataSourceFactory.getJdbcParams(null));
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 Mockito.when(dataSource.getEventPublisher()).thenReturn(Mockito.mock(EventPublisher.class));
 Mockito.when(dataSource.getUrl()).thenReturn("jdbc:test:uri");
 Mockito.when(dataSource.getUsername()).thenReturn("testUser");
 Mockito.when(dataSource.getPassword()).thenReturn("testPassword");
 Mockito.when(dataSource.getDriverClassName()).thenReturn("testDriver");
 Mockito.when(dataSource.isAutoCommit()).thenReturn(true);
 Mockito.when(dataSource.getConnectionProperties()).thenReturn(new Properties());
 Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
 assertEquals(dataSource, dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSource_with_null_connectionConfig() {
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
 assertNotNull(deepCopy);
 assertNotSame(dataSourceFactory, deepCopy);
 }

 @Test
 public void testResetUsername() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newUser";
 dataSourceFactory.resetUsername(mapper);
 assertEquals("newUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newPassword";
 dataSourceFactory.resetPassword(mapper);
 assertEquals("newPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newHost";
 dataSourceFactory.resetHost(mapper);
 assertEquals("newHost", dataSourceFactory.getJdbcUrl().split(":")[1]);
 }

 @Test
 public void testResetPort() {
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> 1234;
 dataSourceFactory.resetPort(mapper);
 assertEquals("jdbc:test:uri/1234", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testResetSchema() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newSchema";
 dataSourceFactory.resetSchema(mapper);
 assertEquals("newSchema", dataSourceFactory.getJdbcUrl().split("\\?")[0].split(":")[3]);
 }

 @Test
 public void testResetParameters() {
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> new HashMap<String, String>() {
 {
 put("key", "value");
 }
 };
 dataSourceFactory.resetParameters(mapper);
 assertEquals("value", dataSourceFactory.getJdbcUrl().split("\\?")[1].split("&")[0].split("=")[1]);
 }

 @Test
 public void testResetParameters_with_null_mapper() {
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = null;
 dataSourceFactory.resetParameters(mapper);
 assertNull(dataSourceFactory.getParameters());
 }

 @Test
 public void testGetDialectType() {
 Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 private void setUpConnectionExtensionPoint(ConnectionExtensionPoint connectionExtensionPoint) {
 dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 }

 private void setUpConnectionConfig(ConnectionConfig connectionConfig) {
 dataSourceFactory.setConnectionConfig(connectionConfig);
 }

 private void setUpParameters(Map<String, String> parameters) {
 dataSourceFactory.setParameters(parameters);
 }
}