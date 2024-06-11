import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import lombok.NonNull;
import lombok.Setter;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private EventPublisher eventPublisher;

 @InjectMocks
 @Setter
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:myql://localhost:3306/mydb?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setHost("localhost");
 endpoint.setPort(3306);
 connectionConfig.setEndpoint(endpoint);
 connectionConfig.setDatabase("mydb");

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

 String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();

 assertEquals(expectedJdbcUrl, jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "myusername";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(expectedUsername);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 String username = obConsoleDataSourceFactory.getUsername();

 assertEquals(expectedUsername, username);
 }

 @Test
 public void testGetPassword() {
 String expectedPassword = "mypassword";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(expectedPassword);

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 String password = obConsoleDataSourceFactory.getPassword();

 assertEquals(expectedPassword, password);
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> expectedJdbcParams = new HashMap<>();
 expectedJdbcParams.put("maxAllowedPacket", "64000000");
 expectedJdbcParams.put("allowMultiQueries", "true");
 expectedJdbcParams.put("connectTimeout", "5000");
 expectedJdbcParams.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 expectedJdbcParams.put("noDatetimeStringSync", "true");
 expectedJdbcParams.put("jdbcCompliantTruncation", "false");
 expectedJdbcParams.put("sendConnectionAttributes", "false");
 expectedJdbcParams.put("allowLoadLocalInfile", "false");
 expectedJdbcParams.put("allowUrlInLocalInfile", "false");
 expectedJdbcParams.put("allowLoadLocalInfileInPath", "");
 expectedJdbcParams.put("autoDeserialize", "false");

 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> jdbcParams = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 assertEquals(expectedJdbcParams, jdbcParams);
 }

 @Test
 public void testGetDataSource() {
 String expectedJdbcUrl = "jdbc:oceanbase:myql://localhost:3306/mydb?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setHost("localhost");
 endpoint.setPort(3306);
 connectionConfig.setEndpoint(endpoint);
 connectionConfig.setDatabase("mydb");

 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

 DataSource dataSource = obConsoleDataSourceFactory.getDataSource();

 assertEquals(expectedJdbcUrl, ((SingleConnectionDataSource) dataSource).getUrl());
 assertEquals(connectionConfig.getUsername(), ((SingleConnectionDataSource) dataSource).getUsername());
 assertEquals(connectionConfig.getPassword(), ((SingleConnectionDataSource) dataSource).getPassword());
 assertEquals(connectionExtensionPoint.getDriverClassName(), ((SingleConnectionDataSource) dataSource).getDriverClassName());

 Properties properties = ((SingleConnectionDataSource) dataSource).getConnectionProperties();
 assertEquals("false", properties.get("allowLoadLocalInfile"));
 assertEquals("false", properties.get("allowUrlInLocalInfile"));
 assertEquals("", properties.get("allowLoadLocalInfileInPath"));
 assertEquals("false", properties.get("autoDeserialize"));
 }

 @Test
 public void testResetUsername() {
 String originalUsername = "originalUsername";
 String newUsername = "newUsername";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(originalUsername);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetUsername(value -> newUsername);

 assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String originalPassword = "originalPassword";
 String newPassword = "newPassword";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(originalPassword);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetPassword(value -> newPassword);

 assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String originalHost = "originalHost";
 String newHost = "newHost";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setHost(originalHost);
 connectionConfig.setEndpoint(endpoint);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetHost(value -> newHost);

 assertEquals(newHost, obConsoleDataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 int originalPort = 3306;
 int newPort = 3307;

 ConnectionConfig connectionConfig = new ConnectionConfig();
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setPort(originalPort);
 connectionConfig.setEndpoint(endpoint);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetPort(value -> newPort);

 assertEquals(newPort, obConsoleDataSourceFactory.getPort().intValue());
 }

 @Test
 public void testResetSchema() {
 String originalSchema = "originalSchema";
 String newSchema = "newSchema";

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDefaultSchema(originalSchema);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetSchema(value -> newSchema);

 assertEquals(newSchema, obConsoleDataSourceFactory.getSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> originalParameters = new HashMap<>();
 originalParameters.put("key1", "value1");
 originalParameters.put("key2", "value2");

 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key1", "newValue1");
 newParameters.put("key3", "value3");

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setJdbcUrlParameters(originalParameters);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 obConsoleDataSourceFactory.resetParameters(value -> newParameters);

 Map<String, String> parameters = obConsoleDataSourceFactory.getParameters();

 assertEquals(newParameters.size(), parameters.size());
 assertTrue(parameters.containsKey("key1"));
 assertTrue(parameters.containsKey("key3"));
 assertFalse(parameters.containsKey("key2"));
 assertEquals("newValue1", parameters.get("key1"));
 assertEquals("value3", parameters.get("key3"));
 }

 @Test
 public void testDeepCopy() {
 ConnectionConfig originalConnectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(originalConnectionConfig, true);

 OBConsoleDataSourceFactory copiedOBConsoleDataSourceFactory = (OBConsoleDataSourceFactory) obConsoleDataSourceFactory.deepCopy();

 assertNotSame(obConsoleDataSourceFactory, copiedOBConsoleDataSourceFactory);
 assertNotSame(obConsoleDataSourceFactory.getConnectionConfig(), copiedOBConsoleDataSourceFactory.getConnectionConfig());
 }
}