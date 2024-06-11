import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private static final String VALID_USERNAME = "testUser";
private static final String VALID_PASSWORD = "testPassword";
private static final String VALID_HOST = "127.0.0.1";
private static final int VALID_PORT = 3306;
private static final String VALID_DEFAULT_SCHEMA = "testSchema";
private static final String VALID_JDBC_PARAM_KEY = "paramKey";
private static final String VALID_JDBC_PARAM_VALUE = "paramValue";

private static final String INVALID_USERNAME = "";
private static final String INVALID_PASSWORD = "";
private static final String INVALID_HOST = "";
private static final int INVALID_PORT = -1;
private static final String INVALID_DEFAULT_SCHEMA = "";

private ConnectionConfig connectionConfig;
private OBConsoleDataSourceFactory dataSourceFactory;
private SingleConnectionDataSource dataSource;

@Before
public void setup() {
connectionConfig = new ConnectionConfig();
connectionConfig.setUsername(VALID_USERNAME);
connectionConfig.setPassword(VALID_PASSWORD);
connectionConfig.setHost(VALID_HOST);
connectionConfig.setPort(VALID_PORT);
connectionConfig.setDefaultSchema(VALID_DEFAULT_SCHEMA);

Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put(VALID_JDBC_PARAM_KEY, VALID_JDBC_PARAM_VALUE);
connectionConfig.setJdbcUrlParameters(jdbcParams);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
}

@Test
public void testGetUsername() {
String username = dataSourceFactory.getUsername();
assertEquals(VALID_USERNAME, username);
}

@Test
public void testGetPassword() {
String password = dataSourceFactory.getPassword();
assertEquals(VALID_PASSWORD, password);
}

@Test
public void testGetConnectType() {
ConnectType connectType = dataSourceFactory.getConnectType();
assertNotNull(connectType);
}

@Test
public void testGetDataSource() {
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(VALID_USERNAME, dataSource.getUsername());
assertEquals(VALID_PASSWORD, dataSource.getPassword());
assertEquals(VALID_HOST, dataSource.getUrl().split(":")[0]);
assertEquals(VALID_PORT + "", dataSource.getUrl().split(":")[1].split("/")[0]);
assertEquals(VALID_DEFAULT_SCHEMA, dataSource.getUrl().split("/")[1]);
Properties connectionProperties = dataSource.getConnectionProperties();
assertNotNull(connectionProperties);
assertEquals(VALID_JDBC_PARAM_VALUE, connectionProperties.get(VALID_JDBC_PARAM_KEY));
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertNotNull(dialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(VALID_USERNAME, clone.getUsername());
}

@Test
public void testResetUsername() {
String newUsername = "newTestUser";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newUsername;
dataSourceFactory.resetUsername(mapper);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newTestPassword";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newPassword;
dataSourceFactory.resetPassword(mapper);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "192.168.1.1";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newHost;
dataSourceFactory.resetHost(mapper);
assertEquals(newHost, dataSourceFactory.getJdbcUrl().split(":")[1].split("/")[0]);
}

@Test
public void testResetPort() {
int newPort = 3307;
CloneableDataSourceFactory.ValueMapper<Integer> mapper = (value) -> newPort;
dataSourceFactory.resetPort(mapper);
assertEquals(newPort + "", dataSourceFactory.getJdbcUrl().split(":")[1].split("/")[0]);
}

@Test
public void testResetSchema() {
String newSchema = "newTestSchema";
CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> newSchema;
dataSourceFactory.resetSchema(mapper);
assertEquals(newSchema, dataSourceFactory.getJdbcUrl().split("/")[1]);
}

@Test
public void testResetParameters() {
String newParamKey = "newParamKey";
String newParamValue = "newParamValue";
Map<String, String> newParams = new HashMap<>();
newParams.put(newParamKey, newParamValue);
CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (value) -> newParams;
dataSourceFactory.resetParameters(mapper);
assertEquals(newParamValue, dataSourceFactory.getJdbcUrl().split("&")[1].split("=")[1]);
}

@Test
public void testEdgeCasesAndExceptions() {
// Test empty username
connectionConfig.setUsername(INVALID_USERNAME);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getUsername());

// Test empty password
connectionConfig.setPassword(INVALID_PASSWORD);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getPassword());

// Test empty host
connectionConfig.setHost(INVALID_HOST);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getUrl());

// Test invalid port
connectionConfig.setPort(INVALID_PORT);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getUrl());

// Test empty default schema
connectionConfig.setDefaultSchema(INVALID_DEFAULT_SCHEMA);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getUrl());

// Test null ConnectionConfig
dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNull(dataSource.getUsername());
assertNull(dataSource.getPassword());
assertNull(dataSource.getUrl());

// Test empty jdbc parameters
connectionConfig.setJdbcUrlParameters(Collections.emptyMap());
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
Properties connectionProperties = dataSource.getConnectionProperties();
assertTrue(CollectionUtils.isEmpty(connectionProperties));

// Test null ConnectionInitializer
dataSourceFactory.resetInitializers(null);
}

@Test(expected = IllegalArgumentException.class)
public void testExceptionWhenResetInitializersWithEmptyList() {
List<ConnectionInitializer> initializers = Collections.emptyList();
dataSourceFactory.resetInitializers(initializers);
}

}