import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtilsTest;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private static final String VALID_USERNAME = "testUser";
 private static final String VALID_PASSWORD = "testPassword";
 private static final String VALID_HOST = "testHost";
 private static final int VALID_PORT = 1234;
 private static final String VALID_DEFAULT_SCHEMA = "testSchema";
 private static final Map<String, String> VALID_JDBC_PARAMS = new HashMap<String, String>() {
 {
 put("param1", "value1");
 put("param2", "value2");
 }
 };
 private static final ConnectionConfig VALID_CONNECTION_CONFIG = new ConnectionConfig() {
 {
 setUsername(VALID_USERNAME);
 setPassword(VALID_PASSWORD);
 setHost(VALID_HOST);
 setPort(VALID_PORT);
 setDefaultSchema(VALID_DEFAULT_SCHEMA);
 setJdbcUrlParameters(VALID_JDBC_PARAMS);
 }
 };
 private static final ConnectionExtensionPoint VALID_CONNECTION_EXTENSION_POINT = mock(ConnectionExtensionPoint.class);
 private static final EventPublisher VALID_EVENT_PUBLISHER = mock(EventPublisher.class);
 private static final CloneableDataSourceFactory OB_CONSOLE_DATA_SOURCE_FACTORY = new OBConsoleDataSourceFactory(VALID_CONNECTION_CONFIG, true, VALID_CONNECTION_EXTENSION_POINT);

 @Before
 public void setup() {
 when(VALID_CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.oceanbase.odc.driver.Driver");
 when(VALID_CONNECTION_EXTENSION_POINT.generateJdbcUrl(VALID_HOST, VALID_PORT, VALID_DEFAULT_SCHEMA, VALID_JDBC_PARAMS))
 .thenReturn("jdbc:oceanbase://testHost:1234/testSchema?param1=value1&param2=value2");
 when(VALID_CONNECTION_EXTENSION_POINT.getConnectionInitializers()).thenReturn(Arrays.asList(new ConnectionInitializer[0]));
 }

 @Test
 public void testGetUsername() {
 assertEquals(VALID_USERNAME, OB_CONSOLE_DATA_SOURCE_FACTORY.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals(VALID_PASSWORD, OB_CONSOLE_DATA_SOURCE_FACTORY.getPassword());
 }

 @Test
 public void testGetHost() {
 assertEquals(VALID_HOST, OB_CONSOLE_DATA_SOURCE_FACTORY.getHost());
 }

 @Test
 public void testGetPort() {
 assertEquals(VALID_PORT, OB_CONSOLE_DATA_SOURCE_FACTORY.getPort());
 }

 @Test
 public void testGetDefaultSchema() {
 assertEquals(VALID_DEFAULT_SCHEMA, OB_CONSOLE_DATA_SOURCE_FACTORY.getDefaultSchema());
 }

 @Test
 public void testGetJdbcParams() {
 assertEquals(VALID_JDBC_PARAMS, OB_CONSOLE_DATA_SOURCE_FACTORY.getJdbcParams());
 }

 @Test
 public void testGetJdbcUrl() {
 assertEquals("jdbc:oceanbase://testHost:1234/testSchema?param1=value1&param2=value2", OB_CONSOLE_DATA_SOURCE_FACTORY.getJdbcUrl());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) OB_CONSOLE_DATA_SOURCE_FACTORY.getDataSource();
 assertEquals("com.oceanbase.odc.driver.Driver", dataSource.getDriverClassName());
 assertEquals("jdbc:oceanbase://testHost:1234/testSchema?param1=value1&param2=value2", dataSource.getUrl());
 assertEquals(VALID_USERNAME, dataSource.getUsername());
 assertEquals(VALID_PASSWORD, dataSource.getPassword());
 assertEquals(VALID_EVENT_PUBLISHER, dataSource.getEventPublisher());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, OB_CONSOLE_DATA_SOURCE_FACTORY.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 CloneableDataSourceFactory clonedDataSourceFactory = OB_CONSOLE_DATA_SOURCE_FACTORY.deepCopy();
 assertNotSame(OB_CONSOLE_DATA_SOURCE_FACTORY, clonedDataSourceFactory);
 assertEquals(VALID_CONNECTION_CONFIG, ((OBConsoleDataSourceFactory) clonedDataSourceFactory).connectionConfig);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newTestUser";
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetUsername, VALID_USERNAME, newUsername);
 assertEquals(newUsername, OB_CONSOLE_DATA_SOURCE_FACTORY.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newTestPassword";
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetPassword, VALID_PASSWORD, newPassword);
 assertEquals(newPassword, OB_CONSOLE_DATA_SOURCE_FACTORY.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newTestHost";
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetHost, VALID_HOST, newHost);
 assertEquals(newHost, OB_CONSOLE_DATA_SOURCE_FACTORY.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 5678;
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetPort, VALID_PORT, newPort);
 assertEquals(newPort, OB_CONSOLE_DATA_SOURCE_FACTORY.getPort());
 }

 @Test
 public void testResetSchema() {
 String newDefaultSchema = "newTestSchema";
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetSchema, VALID_DEFAULT_SCHEMA, newDefaultSchema);
 assertEquals(newDefaultSchema, OB_CONSOLE_DATA_SOURCE_FACTORY.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newJdbcParams = new HashMap<String, String>() {
 {
 put("param1", "newValue1");
 put("param3", "value3");
 }
 };
 OBConsoleDataSourceFactoryTest.resetValue(OB_CONSOLE_DATA_SOURCE_FACTORY::resetParameters, VALID_JDBC_PARAMS, newJdbcParams);
 assertEquals(newJdbcParams, OB_CONSOLE_DATA_SOURCE_FACTORY.getJdbcParams());
 }

 private static void resetValue(ResettableFactory resettableFactory, Object oldValue, Object newValue) {
 resettableFactory.reset(value -> value.equals(oldValue) ? newValue : value);
 }

 private interface ResettableFactory {
 void reset(ObjectMapper<Object> mapper);
 }

 private interface ObjectMapper<T> {
 T map(T value);
 }

}