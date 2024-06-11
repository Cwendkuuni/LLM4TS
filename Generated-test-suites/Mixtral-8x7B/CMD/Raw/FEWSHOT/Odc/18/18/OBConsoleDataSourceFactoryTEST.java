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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

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

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetJdbcUrl() {
 // Arrange
 String host = "localhost";
 Integer port = 3306;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Assert
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/test_schema?param1=value1",
 factory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 // Arrange
 String username = "test_user";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(username);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Assert
 assertEquals(username, factory.getUsername());
 }

 @Test
 public void testGetPassword() {
 // Arrange
 String password = "test_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(password);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Assert
 assertEquals(password, factory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 // Arrange
 Map<String, String> jdbcUrlParams = new HashMap<>();
 jdbcUrlParams.put("param1", "value1");
 jdbcUrlParams.put("param2", "value2");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setJdbcUrlParameters(jdbcUrlParams);

 // Act
 Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 // Assert
 assertEquals(result, jdbcUrlParams);
 }

 @Test
 public void testGetDataSource() {
 // Arrange
 String username = "test_user";
 String password = "test_password";
 String host = "localhost";
 Integer port = 3306;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Assert
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
 assertEquals(username, dataSource.getUsername());
 assertEquals(password, dataSource.getPassword());
 assertEquals(host + ":" + port + "/" + defaultSchema, dataSource.getUrl());
 assertEquals(connectionExtensionPoint, dataSource.getConnectionInitializers().get(0).getClass()
 .getAnnotation(ConnectionInitializer.class).value());
 }

 @Test
 public void testGetDialectType() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Assert
 assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 OBConsoleDataSourceFactory copy = factory.deepCopy();

 // Assert
 assertNotSame(factory, copy);
 }

 @Test
 public void testResetUsername() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper> captor = ArgumentCaptor.forClass(ValueMapper.class);
 factory.resetUsername(captor.capture());

 // Assert
 ValueMapper valueMapper = captor.getValue();
 String newUsername = "new_username";
 valueMapper.map(newUsername);
 assertEquals(newUsername, factory.getUsername());
 }

 @Test
 public void testResetPassword() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper> captor = ArgumentCaptor.forClass(ValueMapper.class);
 factory.resetPassword(captor.capture());

 // Assert
 ValueMapper valueMapper = captor.getValue();
 String newPassword = "new_password";
 valueMapper.map(newPassword);
 assertEquals(newPassword, factory.getPassword());
 }

 @Test
 public void testResetHost() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper> captor = ArgumentCaptor.forClass(ValueMapper.class);
 factory.resetHost(captor.capture());

 // Assert
 ValueMapper valueMapper = captor.getValue();
 String newHost = "new_host";
 valueMapper.map(newHost);
 assertEquals(newHost, factory.getHost());
 }

 @Test
 public void testResetPort() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper<Integer>> captor = ArgumentCaptor.forClass(ValueMapper.class);
 factory.resetPort(captor.capture());

 // Assert
 ValueMapper<Integer> valueMapper = captor.getValue();
 Integer newPort = 1234;
 valueMapper.map(newPort);
 assertEquals(newPort, factory.getPort());
 }

 @Test
 public void testResetSchema() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper<String>> captor = ArgumentCaptor.forClass(ValueMapper.class);
 factory.resetSchema(captor.capture());

 // Assert
 ValueMapper<String> valueMapper = captor.getValue();
 String newSchema = "new_schema";
 valueMapper.map(newSchema);
 assertEquals(newSchema, factory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 // Arrange
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);

 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

 // Act
 ArgumentCaptor<ValueMapper<Map<String, String>>> captor = ArgumentCaptor
 .forClass(ValueMapper.class);
 factory.resetParameters(captor.capture());

 // Assert
 ValueMapper<Map<String, String>> valueMapper = captor.getValue();
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 valueMapper.map(newParameters);
 assertEquals(newParameters, factory.getParameters());
 }

}

class ValueMapper<T> {

 public T map(T value) {
 return value;
 }
}