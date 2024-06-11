import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 connectionConfig = mock(ConnectionConfig.class);
 when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
 when(connectionConfig.getUsername()).thenReturn("test_user");
 when(connectionConfig.getPassword()).thenReturn("test_password");
 when(connectionConfig.getHost()).thenReturn("test_host");
 when(connectionConfig.getPort()).thenReturn(1234);
 when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
 when(connectionConfig.getType()).thenReturn(ConnectType.CONSOLE);
 when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());

 SSLConfig sslConfig = mock(SSLConfig.class);
 SSLFileEntry sslFileEntry = mock(SSLFileEntry.class);
 when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
 when(sslConfig.getEnabled()).thenReturn(false);
 when(sslConfig.getSslFileEntry()).thenReturn(sslFileEntry);

 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 when(connectionExtensionPoint.generateJdbcUrl(connectionConfig.getHost(), connectionConfig.getPort(),
 connectionConfig.getDefaultSchema(), connectionConfig.getJdbcUrlParameters())).thenReturn("test_jdbc_url");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("test_driver_classname");

 eventPublisher = mock(EventPublisher.class);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 obConsoleDataSourceFactory.setEventPublisher(eventPublisher);
 }

 @Test
 public void testGetJdbcUrl() {
 assertEquals("test_jdbc_url", obConsoleDataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("\"test_user\"@test_tenant#test_cluster", obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("test_password", obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();

 assertEquals("test_jdbc_url", dataSource.getUrl());
 assertEquals("test_user", dataSource.getUsername());
 assertEquals("test_password", dataSource.getPassword());
 assertEquals("test_driver_classname", dataSource.getDriverClassName());

 Map<String, String> properties = dataSource.getConnectionProperties();
 assertEquals("false", properties.get("allowLoadLocalInfile"));
 assertEquals("false", properties.get("allowUrlInLocalInfile"));
 assertEquals("", properties.get("allowLoadLocalInfileInPath"));
 assertEquals("false", properties.get("autoDeserialize"));

 assertEquals(true, dataSource.isAutoCommit());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();

 assertEquals(obConsoleDataSourceFactory.getUsername(), copy.getUsername());
 assertEquals(obConsoleDataSourceFactory.getPassword(), copy.getPassword());
 assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), copy.getJdbcUrl());
 assertEquals(obConsoleDataSourceFactory.getDialectType(), copy.getDialectType());
 }

 @Test
 public void testResetUsername() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 String newUsername = "new_username";

 spy.resetUsername(value -> newUsername);

 assertEquals(newUsername, spy.getUsername());
 }

 @Test
 public void testResetPassword() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 String newPassword = "new_password";

 spy.resetPassword(value -> newPassword);

 assertEquals(newPassword, spy.getPassword());
 }

 @Test
 public void testResetHost() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 String newHost = "new_host";

 spy.resetHost(value -> newHost);

 assertEquals(newHost, spy.getJdbcUrl());
 }

 @Test
 public void testResetPort() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 int newPort = 5678;

 spy.resetPort(value -> newPort);

 assertEquals(newPort, obConsoleDataSourceFactory.getJdbcUrl().split(":")[1].equals("5678") ? newPort : 1234);
 }

 @Test
 public void testResetSchema() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 String newSchema = "new_schema";

 spy.resetSchema(value -> newSchema);

 assertEquals(newSchema, obConsoleDataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testResetParameters() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 newParameters.put("param2", "value2");

 spy.resetParameters(value -> newParameters);

 List<String> parameters = Arrays.asList(obConsoleDataSourceFactory.getJdbcUrl().split("\\?")[1].split("&"));

 for (String parameter : parameters) {
 String[] keyValue = parameter.split("=");
 assertEquals(newParameters.get(keyValue[0]), keyValue[1]);
 }
 }

 @Test
 public void testResetParameters_EmptyMap() {
 OBConsoleDataSourceFactory spy = spy(obConsoleDataSourceFactory);

 Map<String, String> newParameters = new HashMap<>();

 spy.resetParameters(value -> newParameters);

 assertEquals(0, obConsoleDataSourceFactory.getJdbcUrl().split("\\?")[1].split("&").length);
 }
}