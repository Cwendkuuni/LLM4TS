import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 connectionConfig = Mockito.mock(ConnectionConfig.class);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 // Mock the inputs for getJdbcUrl method
 String host = "localhost";
 Integer port = 3306;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");

 Mockito.when(connectionConfig.getHost()).thenReturn(host);
 Mockito.when(connectionConfig.getPort()).thenReturn(port);
 Mockito.when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
 Mockito.when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);

 String result = obConsoleDataSourceFactory.getJdbcUrl();

 String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters);

 assertEquals(expectedJdbcUrl, result);
 }

 @Test
 public void testGetUsername() {
 String dbUser = "test_user";
 String tenantName = "test_tenant";
 String clusterName = "test_cluster";

 Mockito.when(connectionConfig.getUsername()).thenReturn(dbUser);
 Mockito.when(connectionConfig.getOBTenantName()).thenReturn(tenantName);
 Mockito.when(connectionConfig.getTenantName()).thenReturn(tenantName);
 Mockito.when(connectionConfig.getClusterName()).thenReturn(clusterName);

 String result = OBConsoleDataSourceFactory.getUsername(connectionConfig);

 String expectedUsername = "\"" + dbUser + "\"@" + tenantName;
 if (StringUtils.isNotBlank(clusterName)) {
 expectedUsername += "#" + clusterName;
 }

 assertEquals(expectedUsername, result);
 }

 @Test
 public void testGetPassword() {
 String password = "test_password";

 Mockito.when(connectionConfig.getPassword()).thenReturn(password);

 String result = OBConsoleDataSourceFactory.getPassword(connectionConfig);

 assertEquals(password, result);
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcUrlParams = new HashMap<>();
 jdbcUrlParams.put("param1", "value1");
 jdbcUrlParams.put("param2", "value2");

 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 Mockito.when(connectionConfig.getEndpoint()).thenReturn(null);
 Mockito.when(connectionConfig.getSslConfig()).thenReturn(null);
 Mockito.when(connectionConfig.getJdbcUrlParameters()).thenReturn(jdbcUrlParams);

 Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 assertEquals(jdbcUrlParams, result);
 }

 @Test
 public void testGetDataSource() {
 String jdbcUrl = "jdbc:test:url";
 String username = "test_user";
 String password = "test_password";

 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
 dataSource.setUrl(jdbcUrl);
 dataSource.setUsername(username);
 dataSource.setPassword(password);

 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 obConsoleDataSourceFactory.setDataSource(dataSource);

 DataSource result = obConsoleDataSourceFactory.getDataSource();

 assertEquals(dataSource, result);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = DialectType.OB_MYSQL;

 Mockito.when(connectionConfig.getDialectType()).thenReturn(dialectType);

 DialectType result = obConsoleDataSourceFactory.getDialectType();

 assertEquals(dialectType, result);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory2 = obConsoleDataSourceFactory.deepCopy();

 assertNotSame(obConsoleDataSourceFactory, obConsoleDataSourceFactory2);

 // Verify that the properties of the original and copied data sources are the same
 assertEquals(obConsoleDataSourceFactory.getUsername(), obConsoleDataSourceFactory2.getUsername());
 assertEquals(obConsoleDataSourceFactory.getPassword(), obConsoleDataSourceFactory2.getPassword());
 assertEquals(obConsoleDataSourceFactory.getHost(), obConsoleDataSourceFactory2.getHost());
 assertEquals(obConsoleDataSourceFactory.getPort(), obConsoleDataSourceFactory2.getPort());
 assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), obConsoleDataSourceFactory2.getDefaultSchema());
 assertEquals(obConsoleDataSourceFactory.getParameters(), obConsoleDataSourceFactory2.getParameters());
 assertEquals(obConsoleDataSourceFactory.getConnectionConfig(), obConsoleDataSourceFactory2.getConnectionConfig());
 assertEquals(obConsoleDataSourceFactory.isAutoCommit(), obConsoleDataSourceFactory2.isAutoCommit());
 assertEquals(obConsoleDataSourceFactory.isInitConnection(), obConsoleDataSourceFactory2.isInitConnection());
 }

 @Test
 public void testResetUsername() {
 String originalUsername = "originalUsername";
 String newUsername = "newUsername";

 OBConsoleDataSourceFactory.ValueMapper<String> valueMapper = input -> {
 if (input.equals(originalUsername)) {
 return newUsername;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetUsername(valueMapper);

 assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String originalPassword = "originalPassword";
 String newPassword = "newPassword";

 OBConsoleDataSourceFactory.ValueMapper<String> valueMapper = input -> {
 if (input.equals(originalPassword)) {
 return newPassword;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetPassword(valueMapper);

 assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String originalHost = "originalHost";
 String newHost = "newHost";

 OBConsoleDataSourceFactory.ValueMapper<String> valueMapper = input -> {
 if (input.equals(originalHost)) {
 return newHost;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetHost(valueMapper);

 assertEquals(newHost, obConsoleDataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 Integer originalPort = 1234;
 Integer newPort = 4321;

 OBConsoleDataSourceFactory.ValueMapper<Integer> valueMapper = input -> {
 if (input.equals(originalPort)) {
 return newPort;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetPort(valueMapper);

 assertEquals(newPort, obConsoleDataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 String originalSchema = "originalSchema";
 String newSchema = "newSchema";

 OBConsoleDataSourceFactory.ValueMapper<String> valueMapper = input -> {
 if (input.equals(originalSchema)) {
 return newSchema;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetSchema(valueMapper);

 assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> originalParameters = new HashMap<>();
 originalParameters.put("param1", "value1");
 originalParameters.put("param2", "value2");

 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "newValue1");

 OBConsoleDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = input -> {
 if (input.equals(originalParameters)) {
 return newParameters;
 }
 return input;
 };

 obConsoleDataSourceFactory.resetParameters(valueMapper);

 assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
 }
}