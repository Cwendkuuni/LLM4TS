import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
connectionConfig = new ConnectionConfig();
obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
}

@Test
public void testGetJdbcUrl() {
// Set the necessary parameters for generating JDBC URL
String host = "example.com";
int port = 3306;
String defaultSchema = "test_schema";
Map<String, String> parameters = new HashMap<>();
parameters.put("maxAllowedPacket", "64000000");

connectionConfig.setHost(host);
connectionConfig.setPort(port);
connectionConfig.setDefaultSchema(defaultSchema);
connectionConfig.setJdbcUrlParameters(parameters);

String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();

// Assert the generated JDBC URL
assertTrue(jdbcUrl.contains(host));
assertTrue(jdbcUrl.contains(Integer.toString(port)));
assertTrue(jdbcUrl.contains(defaultSchema));
assertTrue(jdbcUrl.contains("maxAllowedPacket=64000000"));
}

@Test
public void testGetUsername() {
String dbUser = "test_user";
connectionConfig.setUsername(dbUser);

String username = obConsoleDataSourceFactory.getUsername();

// Assert the username with schema and tenant name
assertEquals("\"" + dbUser + "\"@test_tenant", username);
}

@Test
public void testGetPassword() {
String password = "test_password";
connectionConfig.setPassword(password);

String getPassword = obConsoleDataSourceFactory.getPassword();

assertEquals(password, getPassword);
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);

// Assert the default JDBC parameters
assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
assertTrue(jdbcParams.containsKey("allowMultiQueries"));
assertTrue(jdbcParams.containsKey("connectTimeout"));
assertTrue(jdbcParams.containsKey("zeroDateTimeBehavior"));
assertTrue(jdbcParams.containsKey("noDatetimeStringSync"));
assertTrue(jdbcParams.containsKey("jdbcCompliantTruncation"));
// ... and more
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();

// Assert the DataSource properties
assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), dataSource.getUrl());
assertEquals(obConsoleDataSourceFactory.getUsername(), dataSource.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), dataSource.getPassword());
assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getDriverClassName());
// ... and more
}

@Test
public void testGetDialectType() {
DialectType dialectType = DialectType.OB_MYSQL;
connectionConfig.setDialectType(dialectType);

DialectType dataSourceDialectType = obConsoleDataSourceFactory.getDialectType();

assertEquals(dialectType, dataSourceDialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory deepCopy = obConsoleDataSourceFactory.deepCopy();

// Assert the copied DataSourceFactory properties
assertNotSame(obConsoleDataSourceFactory, deepCopy);
assertEquals(obConsoleDataSourceFactory.getUsername(), deepCopy.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), deepCopy.getPassword());
assertEquals(obConsoleDataSourceFactory.getHost(), deepCopy.getHost());
assertEquals(obConsoleDataSourceFactory.getPort(), deepCopy.getPort());
assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), deepCopy.getDefaultSchema());
assertEquals(obConsoleDataSourceFactory.getParameters(), deepCopy.getParameters());
}

@Test
public void testResetUsername() {
String newUsername = "new_username";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newUsername;

obConsoleDataSourceFactory.resetUsername(mapper);

assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "new_password";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newPassword;

obConsoleDataSourceFactory.resetPassword(mapper);

assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "new_host";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newHost;

obConsoleDataSourceFactory.resetHost(mapper);

assertEquals(newHost, obConsoleDataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 3307;
CloneableDataSourceFactory.ValueMapper<Integer> mapper = (value) -> newPort;

obConsoleDataSourceFactory.resetPort(mapper);

assertEquals(newPort, obConsoleDataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newDefaultSchema = "new_schema";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newDefaultSchema;

obConsoleDataSourceFactory.resetSchema(mapper);

assertEquals(newDefaultSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("new_param1", "value1");
newParameters.put("new_param2", "value2");

CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (value) -> newParameters;

obConsoleDataSourceFactory.resetParameters(mapper);

assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
}

}