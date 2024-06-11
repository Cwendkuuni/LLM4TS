import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Test
public void testGetJdbcUrl() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:oceanbase:oracle:@127.0.0.1:20000:testdb", jdbcUrl);
}

@Test
public void testGetUsername() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
assertEquals("\"testuser\"@testtenant#cluster1", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
assertEquals("testpassword", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("jdbc:oceanbase:oracle:@127.0.0.1:20000:testdb", dataSource.getUrl());
assertEquals("testuser", dataSource.getUsername());
assertEquals("testpassword", dataSource.getPassword());
assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getDriverClassName());
assertEquals(true, dataSource.isAutoCommit());
}

@Test
public void testGetDialectType() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
OBConsoleDataSourceFactory copiedDataSourceFactory = dataSourceFactory.deepCopy();
assertNotSame(dataSourceFactory, copiedDataSourceFactory);
assertEquals(dataSourceFactory.getUsername(), copiedDataSourceFactory.getUsername());
assertEquals(dataSourceFactory.getPassword(), copiedDataSourceFactory.getPassword());
assertEquals(dataSourceFactory.getHost(), copiedDataSourceFactory.getHost());
assertEquals(dataSourceFactory.getPort(), copiedDataSourceFactory.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copiedDataSourceFactory.getDefaultSchema());
assertEquals(dataSourceFactory.getJdbcParams(), copiedDataSourceFactory.getJdbcParams());
}

@Test
public void testResetUsername() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
String newUsername = "newUsername";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
String newPassword = "newPassword";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
String newHost = "newHost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
int newPort = 30000;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
ConnectionExtensionPoint connectionExtensionPoint = createConnectionExtensionPoint();
OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true,
connectionExtensionPoint);
Map<String, String> newParams = new HashMap<>();
newParams.put("key1", "value1");
newParams.put("key2", "value2");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getJdbcParams());
}

private ConnectionConfig createConnectionConfig() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setHost("127.0.0.1");
connectionConfig.setPort(20000);
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setType(ConnectType.CONSOLE);
connectionConfig.setEndpoint(new OBTenantEndpoint("testtenant", "cluster1"));
connectionConfig.setJdbcUrlParameters(Collections.singletonMap("zeroDateTimeBehavior", "convertToNull"));
return connectionConfig;
}

private ConnectionExtensionPoint createConnectionExtensionPoint() {
return new ConnectionExtensionPoint() {

@Override
public String generateJdbcUrl(String host, Integer port, String defaultSchema,
Map<String, String> parameters) {
return String.format("jdbc:oceanbase:oracle:@%s:%d:%s", host, port, defaultSchema);
}

@Override
public List<ConnectionInitializer> getConnectionInitializers() {
return Collections.emptyList();
}

@Override
public String getDriverClassName() {
return "oracle.jdbc.driver.OracleDriver";
}

@Override
public void initialize(SingleConnectionDataSource dataSource) {
// No-op
}
};
}
}