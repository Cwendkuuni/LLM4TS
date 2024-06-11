import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testSchema");

SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
sslFileEntry.setKeyStoreFilePassword("keystorePassword");
sslConfig.setSslFileEntry(sslFileEntry);
connectionConfig.setSslConfig(sslConfig);

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
}

@Test
public void testGetUsername() {
String username = obConsoleDataSourceFactory.getUsername();
assertEquals("testUser", username);
}

@Test
public void testGetPassword() {
String password = obConsoleDataSourceFactory.getPassword();
assertEquals("testPassword", password);
}

@Test
public void testGetConnectType() {
ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
assertEquals(ConnectType.CONSOLE, connectType);
}

@Test
public void testGetDataSource() {
DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
assertNotNull(dataSource);
}

@Test
public void testGetDialectType() {
DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clonedDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
assertNotNull(clonedDataSourceFactory);
}

@Test
public void testResetUsername() {
String newUsername = "newTestUser";
obConsoleDataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newTestPassword";
obConsoleDataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.connectionConfig.getHost());
}

@Test
public void testResetPort() {
int newPort = 4444;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.connectionConfig.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.connectionConfig.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
obConsoleDataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, obConsoleDataSourceFactory.parameters);
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegative() {
int newPort = -1;
obConsoleDataSourceFactory.resetPort(value -> newPort);
}

@Test
public void testGetDefaultSchema() {
String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
assertEquals("testSchema", defaultSchema);
}

@Test
public void testGetDbUser() {
String dbUser = OBConsoleDataSourceFactory.getDbUser(connectionConfig);
assertEquals("testUser", dbUser);
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertFalse(CollectionUtils.isEmpty(jdbcParams));
}
}