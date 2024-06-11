import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private EventPublisher eventPublisher;

 @InjectMocks
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetJdbcUrl() {
 // Arrange
 String host = "localhost";
 Integer port = 3306;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 String result = obConsoleDataSourceFactory.getJdbcUrl();

 // Assert
 assertEquals(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters), result);
 }

 @Test
 public void testGetUsername() {
 // Arrange
 String dbUser = "test_user";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(dbUser);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 String result = obConsoleDataSourceFactory.getUsername();

 // Assert
 assertEquals(dbUser, result);
 }

 @Test
 public void testGetPassword() {
 // Arrange
 String password = "test_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(password);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 String result = obConsoleDataSourceFactory.getPassword();

 // Assert
 assertEquals(password, result);
 }

 @Test
 public void testGetJdbcParams() {
 // Arrange
 Map<String, String> expectedParams = new HashMap<>();
 expectedParams.put("maxAllowedPacket", "64000000");
 expectedParams.put("allowMultiQueries", "true");
 expectedParams.put("connectTimeout", "5000");
 expectedParams.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 expectedParams.put("noDatetimeStringSync", "true");
 expectedParams.put("jdbcCompliantTruncation", "false");

 ConnectionConfig connectionConfig = new ConnectionConfig();

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 Map<String, String> result = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 // Assert
 assertEquals(expectedParams, result);
 }

 @Test
 public void testGetDataSource() {
 // Arrange
 String jdbcUrl = "jdbc:mysql://localhost:3306/test_schema";
 String username = "test_user";
 String password = "test_password";
 ConnectionExtensionPoint mockConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 when(mockConnectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn(jdbcUrl);
 when(mockConnectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
 obConsoleDataSourceFactory.connectionExtensionPoint = mockConnectionExtensionPoint;

 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 dataSource.setEventPublisher(eventPublisher);
 dataSource.setUrl(jdbcUrl);
 dataSource.setUsername(username);
 dataSource.setPassword(password);
 dataSource.setDriverClassName("com.mysql.jdbc.Driver");
 dataSource.setConnectionProperties(new Properties());

 // Act
 DataSource result = obConsoleDataSourceFactory.getDataSource();

 // Assert
 assertEquals(dataSource, result);
 }

 @Test
 public void testGetDialectType() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 DialectType result = obConsoleDataSourceFactory.getDialectType();

 // Assert
 assertEquals(DialectType.OB_MYSQL, result);
 }

 @Test
 public void testDeepCopy() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 CloneableDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();

 // Assert
 assertNotSame(obConsoleDataSourceFactory, copy);
 }

 @Test
 public void testResetUsername() {
 // Arrange
 String originalUsername = "original_username";
 String newUsername = "new_username";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(originalUsername);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetUsername(value -> newUsername);

 // Assert
 assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 // Arrange
 String originalPassword = "original_password";
 String newPassword = "new_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(originalPassword);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetPassword(value -> newPassword);

 // Assert
 assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 // Arrange
 String originalHost = "original_host";
 String newHost = "new_host";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost(originalHost);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetHost(value -> newHost);

 // Assert
 assertEquals(newHost, obConsoleDataSourceFactory.getConnectType().getHost());
 }

 @Test
 public void testResetPort() {
 // Arrange
 Integer originalPort = 1234;
 Integer newPort = 5678;
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPort(originalPort);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetPort(value -> newPort);

 // Assert
 assertEquals(newPort, obConsoleDataSourceFactory.getConnectType().getPort());
 }

 @Test
 public void testResetSchema() {
 // Arrange
 String originalSchema = "original_schema";
 String newSchema = "new_schema";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDefaultSchema(originalSchema);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetSchema(value -> newSchema);

 // Assert
 assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 // Arrange
 Map<String, String> originalParameters = new HashMap<>();
 originalParameters.put("param1", "value1");
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "new_value1");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setJdbcUrlParameters(originalParameters);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 obConsoleDataSourceFactory.resetParameters(value -> newParameters);

 // Assert
 assertEquals(newParameters, obConsoleDataSourceFactory.getJdbcParams(connectionConfig));
 }

}