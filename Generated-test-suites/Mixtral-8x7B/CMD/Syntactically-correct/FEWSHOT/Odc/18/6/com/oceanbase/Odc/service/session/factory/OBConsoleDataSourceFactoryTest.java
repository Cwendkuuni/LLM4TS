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
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private EventPublisher eventPublisher;
 private SingleConnectionDataSource dataSource;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDefaultSchema("testSchema");

 eventPublisher = mock(EventPublisher.class);

 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.odc.driver.Driver");
 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn("jdbc:oceanbase:mysql://localhost:3306/testSchema");

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
 dataSourceFactory.setEventPublisher(eventPublisher);
 }

 @Test
 public void testGetJdbcUrl() {
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/testSchema", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("testUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("testPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testGetDataSource() {
 dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

 assertEquals("localhost", dataSource.getHost());
 assertEquals(3306, dataSource.getPort());
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 assertEquals("com.oceanbase.odc.driver.Driver", dataSource.getDriverClassName());
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/testSchema", dataSource.getUrl());
 assertTrue(dataSource.isAutoCommit());

 Map<String, String> properties = new HashMap<>();
 properties.put("allowLoadLocalInfile", "false");
 properties.put("allowUrlInLocalInfile", "false");
 properties.put("allowLoadLocalInfileInPath", "");
 properties.put("autoDeserialize", "false");
 assertEquals(properties, dataSource.getConnectionProperties());

 ArgumentCaptor<ConnectionInitializer> initializerCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
 verify(connectionExtensionPoint).getConnectionInitializers();
 verify(dataSource).addInitializers(initializerCaptor.capture());

 List<ConnectionInitializer> initializers = initializerCaptor.getAllValues();
 assertEquals(1, initializers.size());
 assertTrue(initializers.get(0) instanceof SessionCreatedInitializer);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();

 assertNotSame(dataSourceFactory, deepCopy);
 assertNotSame(dataSourceFactory.connectionConfig, deepCopy.connectionConfig);

 assertEquals(dataSourceFactory.getUsername(), deepCopy.getUsername());
 assertEquals(dataSourceFactory.getPassword(), deepCopy.getPassword());
 assertEquals(dataSourceFactory.getConnectType(), deepCopy.getConnectType());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newUsername;
 dataSourceFactory.resetUsername(mapper);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newPassword;
 dataSourceFactory.resetPassword(mapper);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newHost;
 dataSourceFactory.resetHost(mapper);
 assertEquals(newHost, dataSourceFactory.connectionConfig.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = (value) -> newPort;
 dataSourceFactory.resetPort(mapper);
 assertEquals(newPort, dataSourceFactory.connectionConfig.getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newSchema;
 dataSourceFactory.resetSchema(mapper);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("key", "value");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (value) -> newParams;
 dataSourceFactory.resetParameters(mapper);
 assertEquals(newParams, dataSourceFactory.getJdbcParams(connectionConfig));
 }

 private void assertProperties(Map<String, String> expected, Map<String, String> actual) {
 assertEquals(expected.size(), actual.size());
 for (Map.Entry<String, String> entry : expected.entrySet()) {
 String value = actual.get(entry.getKey());
 if (value == null) {
 fail("Expected property '" + entry.getKey() + "' not found.");
 }
 assertEquals(entry.getValue(), value);
 }
 }
}