import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

@Mock
private ConnectionExtensionPoint connectionExtensionPoint;

@Mock
private EventPublisher eventPublisher;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testGetJdbcUrl() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

// When
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String result = factory.getJdbcUrl();

// Then
assertEquals("jdbc:oceanbase:mysql://", result.substring(0, 21));
}

@Test
public void testGetUsername() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setUsername("user");
connectionConfig.setTenantName("tenant");
connectionConfig.setClusterName("cluster");

// When
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String result = factory.getUsername();

// Then
assertEquals("\"user@tenant#cluster\"", result);
}

@Test
public void testGetPassword() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("password");

// When
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String result = factory.getPassword();

// Then
assertEquals("password", result);
}

@Test
public void testGetJdbcParams() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

// When
Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

// Then
assertEquals("64000000", result.get("maxAllowedPacket"));
assertEquals("true", result.get("allowMultiQueries"));
assertEquals("5000", result.get("connectTimeout"));
assertEquals(DEFAULT_ZERO_DATE_TIME_BEHAVIOR, result.get("zeroDateTimeBehavior"));
assertEquals("true", result.get("noDatetimeStringSync"));
assertEquals("false", result.get("jdbcCompliantTruncation"));
assertEquals("false", result.get("sendConnectionAttributes"));
}

@Test
public void testGetDataSource() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
.thenReturn("jdbc:oceanbase:mysql://host:port/schema");

when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");

ArgumentCaptor<ConnectionInitializer> initializerCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);

// When
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint,
eventPublisher);
DataSource result = factory.getDataSource();

// Then
assertEquals(dataSource.getClass(), result.getClass());
verify(dataSource).setEventPublisher(eventPublisher);
verify(dataSource).setUrl("jdbc:oceanbase:mysql://host:port/schema");
verify(dataSource).setUsername("username");
verify(dataSource).setPassword("password");
verify(dataSource).setDriverClassName("com.oceanbase.jdbc.Driver");
verify(dataSource).addInitializer(initializerCaptor.capture());
List<ConnectionInitializer> initializers = initializerCaptor.getAllValues();
assertEquals(1, initializers.size());
assertTrue(initializers.get(0) instanceof SessionCreatedInitializer);
}

@Test
public void testDeepCopy() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

// When
OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();

// Then
assertNotSame(originalFactory, clonedFactory);
assertEquals(originalFactory.getUsername(), clonedFactory.getUsername());
assertEquals(originalFactory.getPassword(), clonedFactory.getPassword());
assertEquals(originalFactory.getConnectType(), clonedFactory.getConnectType());
}

@Test
public void testResetUsername() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String newUsername = "newUsername";

// When
factory.resetUsername(value -> newUsername);

// Then
assertEquals(newUsername, factory.getUsername());
}

@Test
public void testResetPassword() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String newPassword = "newPassword";

// When
factory.resetPassword(value -> newPassword);

// Then
assertEquals(newPassword, factory.getPassword());
}

@Test
public void testResetHost() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String newHost = "newHost";

// When
factory.resetHost(value -> newHost);

// Then
// Need to mock getJdbcUrl method to check the change of host
}

@Test
public void testResetPort() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
int newPort = 1234;

// When
factory.resetPort(value -> newPort);

// Then
// Need to mock getJdbcUrl method to check the change of port
}

@Test
public void testResetSchema() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String newSchema = "newSchema";

// When
factory.resetSchema(value -> newSchema);

// Then
// Need to mock getDefaultSchema method to check the change of schema
}

@Test
public void testResetParameters() {
// Given
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");

// When
factory.resetParameters(value -> newParams);

// Then
// Need to mock getJdbcParams method to check the change of parameters
}
}