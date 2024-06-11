import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
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

 private ConnectionExtensionPoint connectionExtensionPoint;
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 eventPublisher = new EventPublisher();
 connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
 }

 @Test
 public void testGetJdbcUrl() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
 assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String username = obConsoleDataSourceFactory.getUsername();
 assertNull(username);
 }

 @Test
 public void testGetPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String password = obConsoleDataSourceFactory.getPassword();
 assertNull(password);
 }

 @Test
 public void testGetConnectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
 assertNull(connectType);
 }

 @Test
 public void testGetJdbcParams() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 Map<String, String> jdbcParams = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);
 assertNotNull(jdbcParams);
 assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
 }

 @Test
 public void testGetDataSource() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher);
 DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 }

 @Test
 public void testGetDialectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
 assertNull(dialectType);
 }

 @Test
 public void testDeepCopy() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
 OBConsoleDataSourceFactory copy = original.deepCopy();
 assertNotSame(original, copy);
 }

 @Test
 public void testResetUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String originalUsername = obConsoleDataSourceFactory.getUsername();
 String newUsername = "newUsername";
 obConsoleDataSourceFactory.resetUsername(value -> newUsername);
 String updatedUsername = obConsoleDataSourceFactory.getUsername();
 assertEquals(newUsername, updatedUsername);
 assertEquals(originalUsername, updatedUsername);
 }

 @Test
 public void testResetPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String originalPassword = obConsoleDataSourceFactory.getPassword();
 String newPassword = "newPassword";
 obConsoleDataSourceFactory.resetPassword(value -> newPassword);
 String updatedPassword = obConsoleDataSourceFactory.getPassword();
 assertEquals(newPassword, updatedPassword);
 assertEquals(originalPassword, updatedPassword);
 }

 @Test
 public void testResetHost() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String originalHost = obConsoleDataSourceFactory.getHost();
 String newHost = "newHost";
 obConsoleDataSourceFactory.resetHost(value -> newHost);
 String updatedHost = obConsoleDataSourceFactory.getHost();
 assertEquals(newHost, updatedHost);
 assertEquals(originalHost, updatedHost);
 }

 @Test
 public void testResetPort() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 Integer originalPort = obConsoleDataSourceFactory.getPort();
 Integer newPort = 1234;
 obConsoleDataSourceFactory.resetPort(value -> newPort);
 Integer updatedPort = obConsoleDataSourceFactory.getPort();
 assertEquals(newPort, updatedPort);
 assertEquals(originalPort, updatedPort);
 }

 @Test
 public void testResetSchema() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 String originalSchema = obConsoleDataSourceFactory.getDefaultSchema();
 String newSchema = "newSchema";
 obConsoleDataSourceFactory.resetSchema(value -> newSchema);
 String updatedSchema = obConsoleDataSourceFactory.getDefaultSchema();
 assertEquals(newSchema, updatedSchema);
 assertEquals(originalSchema, updatedSchema);
 }

 @Test
 public void testResetParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 Map<String, String> originalParameters = obConsoleDataSourceFactory.getParameters();
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key", "value");
 obConsoleDataSourceFactory.resetParameters(value -> newParameters);
 Map<String, String> updatedParameters = obConsoleDataSourceFactory.getParameters();
 assertEquals(newParameters, updatedParameters);
 assertEquals(originalParameters, updatedParameters);
 }
}