import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
connectionExtensionPoint = new ConnectionExtensionPoint() {
@Override
public String generateJdbcUrl(String host, Integer port, String defaultSchema,
Map<String, String> parameters) {
return "jdbc:oceanbase://" + host + ":" + port + "/" + defaultSchema;
}

@Override
public List<ConnectionInitializer> getConnectionInitializers() {
return Arrays.asList(new ConnectionInitializer() {
@Override
public void initializeConnection(SingleConnectionDataSource dataSource) {
// Do nothing
}
});
}

@Override
public String getDriverClassName() {
return "oceanbase.jdbc.Driver";
}
};

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");
connectionConfig.setDefaultSchema("testdb");

SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("path/to/keystore.jks");
sslFileEntry.setKeyStoreFilePassword("password");
sslConfig.setSslFileEntry(sslFileEntry);
connectionConfig.setSslConfig(sslConfig);

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:oceanbase://localhost:3306/testdb";
String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
assertEquals(expectedJdbcUrl, jdbcUrl);
}

@Test
public void testGetUsername() {
String expectedUsername = "root";
String username = obConsoleDataSourceFactory.getUsername();
assertEquals(expectedUsername, username);
}

@Test
public void testGetPassword() {
String expectedPassword = "password";
String password = obConsoleDataSourceFactory.getPassword();
assertEquals(expectedPassword, password);
}

@Test
public void testGetConnectType() {
ConnectType expectedConnectType = ConnectType.CONSOLE;
ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
assertEquals(expectedConnectType, connectType);
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();
assertEquals(connectionConfig.getHost(), dataSource.getHost());
assertEquals(connectionConfig.getPort(), dataSource.getPort());
assertEquals(connectionConfig.getUsername(), dataSource.getUsername());
assertEquals(connectionConfig.getPassword(), dataSource.getPassword());
assertEquals("oceanbase.jdbc.Driver", dataSource.getDriverClassName());
assertEquals("false", dataSource.getConnectionProperties().get("allowLoadLocalInfile"));
assertEquals("false", dataSource.getConnectionProperties().get("allowUrlInLocalInfile"));
assertEquals("", dataSource.getConnectionProperties().get("allowLoadLocalInfileInPath"));
assertEquals("false", dataSource.getConnectionProperties().get("autoDeserialize"));
assertFalse(dataSource.isAutoCommit());
}

@Test
public void testGetDialectType() {
DialectType expectedDialectType = DialectType.OB_MYSQL;
DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
assertEquals(expectedDialectType, dialectType);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copiedObConsoleDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
assertNotSame(obConsoleDataSourceFactory, copiedObConsoleDataSourceFactory);
assertEquals(obConsoleDataSourceFactory.getUsername(), copiedObConsoleDataSourceFactory.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), copiedObConsoleDataSourceFactory.getPassword());
assertEquals(obConsoleDataSourceFactory.getHost(), copiedObConsoleDataSourceFactory.getHost());
assertEquals(obConsoleDataSourceFactory.getPort(), copiedObConsoleDataSourceFactory.getPort());
assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), copiedObConsoleDataSourceFactory.getDefaultSchema());
assertEquals(obConsoleDataSourceFactory.getParameters(), copiedObConsoleDataSourceFactory.getParameters());
}

@Test
public void testResetUsername() {
String newUsername = "newUser";
obConsoleDataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
obConsoleDataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 4444;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("key", "value");
obConsoleDataSourceFactory.resetParameters(value -> newParameters);
assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
}
}