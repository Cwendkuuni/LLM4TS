import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
connectionExtensionPoint = new ConnectionExtensionPoint() {
@Override
public String generateJdbcUrl(String host, Integer port, String defaultSchema,
Map<String, String> jdbcUrlParams) {
return "jdbc:obmysql://" + host + ":" + port + "/" + defaultSchema;
}

@Override
public List<ConnectionInitializer> getConnectionInitializers() {
return Arrays.asList();
}

@Override
public String getDriverClassName() {
return "com.mysql.jdbc.Driver";
}
};

connectionConfig = new ConnectionConfig();
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPass");
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setType(ConnectType.CONSOLE);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:obmysql://localhost:3306/testUser", jdbcUrl);
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertEquals("testUser", username);
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertEquals("testPass", password);
}

@Test
public void testGetConnectType() {
ConnectType connectType = dataSourceFactory.getConnectType();
assertEquals(ConnectType.CONSOLE, connectType);
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("com.mysql.jdbc.JDBC4Connection", dataSource.getConnection().getClass().getName());
}

@Test
public void testResetUsername() {
String newUsername = "newTestUser";
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetUsername(mapper.clone(newUsername));
String updatedUsername = dataSourceFactory.getUsername();
assertEquals(newUsername, updatedUsername);
}

@Test
public void testResetPassword() {
String newPassword = "newTestPass";
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetPassword(mapper.clone(newPassword));
String updatedPassword = dataSourceFactory.getPassword();
assertEquals(newPassword, updatedPassword);
}

@Test
public void testResetHost() {
String newHost = "newHost";
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetHost(mapper.clone(newHost));
String updatedHost = dataSourceFactory.getJdbcUrl().split(":")[1].split("/")[0];
assertEquals(newHost, updatedHost);
}

@Test
public void testResetPort() {
int newPort = 3307;
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetPort(mapper.clone(newPort));
int updatedPort = Integer.parseInt(dataSourceFactory.getJdbcUrl().split(":")[2].split("/")[0]);
assertEquals(newPort, updatedPort);
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetSchema(mapper.clone(newSchema));
String updatedSchema = dataSourceFactory.getJdbcUrl().split("/")[2];
assertEquals(newSchema, updatedSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("testKey", "testValue");
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
dataSourceFactory.resetParameters(mapper.clone(newParams));
Map<String, String> updatedParams = dataSourceFactory.getParameters();
assertEquals(newParams, updatedParams);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotSame(dataSourceFactory, clone);
}
}