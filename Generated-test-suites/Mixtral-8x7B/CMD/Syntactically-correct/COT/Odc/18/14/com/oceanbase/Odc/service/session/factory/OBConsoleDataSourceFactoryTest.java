import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;

@Before
public void setup() {
connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);

connectionConfig = new ConnectionConfig();
connectionConfig.setType(ConnectType.CONSOLE);
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("root");
connectionConfig.setDefaultSchema("testdb");

SSLConfig sslConfig = new SSLConfig();
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("keystore.p12");
sslFileEntry.setKeyStoreFilePassword("password");
sslConfig.setEnabled(true);
sslConfig.setSslFileEntry(sslFileEntry);
connectionConfig.setSslConfig(sslConfig);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/testdb?allowMultiQueries=true&allowUrlInLocalInfile=false&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustServerCertificate=false&zeroDateTimeBehavior=convertToNull", jdbcUrl);
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertEquals("root", username);
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertEquals("root", password);
}

@Test
public void testGetConnectType() {
ConnectType connectType = dataSourceFactory.getConnectType();
assertEquals(ConnectType.CONSOLE, connectType);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
}

@Test
public void testResetUsername() {
String newUsername = "newUsername";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getJdbcUrl().split(":")[1]);
}

@Test
public void testResetPort() {
int newPort = 3307;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getJdbcUrl().split(":")[2].split("/")[0].split("\\?")[0]);
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getJdbcUrl().split("\\?")[0].split("/")[2]);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("newParam", "newValue");
dataSourceFactory.resetParameters(value -> newParams);
// TODO: check if parameters are correctly reset
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegative() {
int newPort = -1;
dataSourceFactory.resetPort(value -> newPort);
}

}