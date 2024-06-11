import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import lombok.NonNull;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPass");
connectionConfig.setHost("testHost");
connectionConfig.setPort(3306);
connectionConfig.setDefaultSchema("testSchema");

Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("param1", "value1");
connectionConfig.setJdbcUrlParameters(jdbcParams);

this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:mysql://testHost:3306/testSchema?param1=value1";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("testUser", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("testPass", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("testUser", dataSource.getUsername());
assertEquals("testPass", dataSource.getPassword());
assertEquals("jdbc:mysql://testHost:3306/testSchema?param1=value1", dataSource.getUrl());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
assertNotNull(copy);
assertNotSame(dataSourceFactory, copy);
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
assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
}

@Test
public void testResetPort() {
int newPort = 3307;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getConnectType().getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("newParam1", "newValue1");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getConnectType().getJdbcUrlParameters());
}

@Test
public void testGetDefaultSchema() {
assertEquals("testSchema", OBConsoleDataSourceFactory.getDefaultSchema(new ConnectionConfig()));
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegativeValue() {
dataSourceFactory.resetPort(value -> -1);
}

@Test
public void testGetUsernameWithOracleDialect() {
ConnectionConfig oracleConfig = new ConnectionConfig();
oracleConfig.setDialectType(DialectType.OB_ORACLE);
oracleConfig.setUsername("oracleUser");
dataSourceFactory = new OBConsoleDataSourceFactory(oracleConfig, true);
assertEquals("\"oracleUser\"", dataSourceFactory.getUsername());
}

@Test
public void testGetDefaultSchemaWithOracleDialect() {
ConnectionConfig oracleConfig = new ConnectionConfig();
oracleConfig.setDialectType(DialectType.OB_ORACLE);
oracleConfig.setDefaultSchema("oracleSchema");
dataSourceFactory = new OBConsoleDataSourceFactory(oracleConfig, true);
assertEquals("\"oracleSchema\"", dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetDefaultSchemaWithOracleDialectAndEmptyDefaultSchema() {
ConnectionConfig oracleConfig = new ConnectionConfig();
oracleConfig.setDialectType(DialectType.OB_ORACLE);
dataSourceFactory = new OBConsoleDataSourceFactory(oracleConfig, true);
assertEquals("\"oracleUser\"", dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetDefaultSchemaWithMySQLDialect() {
ConnectionConfig mysqlConfig = new ConnectionConfig();
mysqlConfig.setDialectType(DialectType.OB_MYSQL);
mysqlConfig.setDefaultSchema("mysqlSchema");
dataSourceFactory = new OBConsoleDataSourceFactory(mysqlConfig, true);
assertEquals("mysqlSchema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetDefaultSchemaWithMySQLDialectAndEmptyDefaultSchema() {
ConnectionConfig mysqlConfig = new ConnectionConfig();
mysqlConfig.setDialectType(DialectType.OB_MYSQL);
dataSourceFactory = new OBConsoleDataSourceFactory(mysqlConfig, true);
assertEquals("", dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetJdbcUrlWithSSL() {
ConnectionConfig sslConfig = new ConnectionConfig();
sslConfig.setDialectType(DialectType.OB_MYSQL);
sslConfig.setSslConfig(new SSLConfig());
sslConfig.getSslConfig().setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("/path/to/keyStore");
sslFileEntry.setKeyStoreFilePassword("keyStorePassword");
sslConfig.setSslFileEntry(sslFileEntry);
dataSourceFactory = new OBConsoleDataSourceFactory(sslConfig, true);
String expectedJdbcUrl = "jdbc:mysql://testHost:3306/testSchema?param1=value1&useSSL=true&trustStore=/path/to/keyStore&trustStorePassword=keyStorePassword&keyStore=/path/to/keyStore&keyStorePassword=keyStorePassword&trustServerCertificate=false";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetJdbcUrlWithSSLDisabled() {
ConnectionConfig sslConfig = new ConnectionConfig();
sslConfig.setDialectType(DialectType.OB_MYSQL);
sslConfig.setSslConfig(new SSLConfig());
sslConfig.getSslConfig().setEnabled(false);
dataSourceFactory = new OBConsoleDataSourceFactory(sslConfig, true);
String expectedJdbcUrl = "jdbc:mysql://testHost:3306/testSchema?param1=value1&useSSL=false";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

}