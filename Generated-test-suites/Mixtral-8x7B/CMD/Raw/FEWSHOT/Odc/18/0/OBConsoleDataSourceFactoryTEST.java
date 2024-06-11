import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

private static final String VALID_USERNAME = "testUser";
private static final String VALID_PASSWORD = "testPassword";
private static final String VALID_HOST = "testHost";
private static final int VALID_PORT = 1234;
private static final String VALID_DEFAULT_SCHEMA = "testSchema";
private static final String VALID_JDBC_PARAM_KEY = "validJdbcParamKey";
private static final String VALID_JDBC_PARAM_VALUE = "validJdbcParamValue";
private static final String VALID_JDBC_URL = "jdbc:validJdbcUrl:test";
private static final String VALID_DRIVER_CLASS_NAME = "validDriverClassName";
private static final DialectType VALID_DIALECT_TYPE = DialectType.OB_MYSQL;
private static final ConnectType VALID_CONNECT_TYPE = ConnectType.CONSOLE;
private static final String VALID_TENANT_NAME = "testTenant";
private static final String VALID_CLUSTER_NAME = "testCluster";
private static final String VALID_OB_TENANT_NAME = "testObTenant";
private static final SSLConfig VALID_SSL_CONFIG = new SSLConfig();
private static final SSLFileEntry VALID_SSL_FILE_ENTRY = new SSLFileEntry();

private ConnectionConfig validConnectionConfig;
private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionMapper connectionMapper;
private EventPublisher eventPublisher;
private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
validConnectionConfig = new ConnectionConfig();
validConnectionConfig.setUsername(VALID_USERNAME);
validConnectionConfig.setPassword(VALID_PASSWORD);
validConnectionConfig.setHost(VALID_HOST);
validConnectionConfig.setPort(VALID_PORT);
validConnectionConfig.setDefaultSchema(VALID_DEFAULT_SCHEMA);
validConnectionConfig.setDialectType(VALID_DIALECT_TYPE);
validConnectionConfig.setType(VALID_CONNECT_TYPE);

mapJdbcParams();

validConnectionConfig.setEndpoint(new OBTenantEndpoint());
validConnectionConfig.getEndpoint().setProxyHost("testProxyHost");
validConnectionConfig.getEndpoint().setProxyPort(5678);

validConnectionConfig.setSslConfig(VALID_SSL_CONFIG);
VALID_SSL_CONFIG.setEnabled(true);
VALID_SSL_CONFIG.setSslFileEntry(VALID_SSL_FILE_ENTRY);

connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
connectionMapper = mock(ConnectionMapper.class);
eventPublisher = mock(EventPublisher.class);

dataSourceFactory = new OBConsoleDataSourceFactory(validConnectionConfig, true) {
@Override
protected ConnectionExtensionPoint getConnectionExtensionPoint() {
return connectionExtensionPoint;
}

@Override
protected ConnectionMapper getConnectionMapper() {
return connectionMapper;
}

@Override
protected EventPublisher getEventPublisher() {
return eventPublisher;
}
};
}

@Test
public void testGetUsername() {
assertEquals(VALID_USERNAME, dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals(VALID_PASSWORD, dataSourceFactory.getPassword());
}

@Test
public void testGetHost() {
assertEquals(VALID_HOST, dataSourceFactory.getHost());
}

@Test
public void testGetPort() {
assertEquals(VALID_PORT, dataSourceFactory.getPort());
}

@Test
public void testGetDefaultSchema() {
assertEquals(VALID_DEFAULT_SCHEMA, dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
assertFalse(jdbcParams.isEmpty());
assertTrue(jdbcParams.containsKey(VALID_JDBC_PARAM_KEY));
assertEquals(VALID_JDBC_PARAM_VALUE, jdbcParams.get(VALID_JDBC_PARAM_KEY));
}

@Test
public void testGetJdbcUrl() {
when(connectionExtensionPoint.generateJdbcUrl(VALID_HOST, VALID_PORT, VALID_DEFAULT_SCHEMA, anyMap()))
.thenReturn(VALID_JDBC_URL);

assertEquals(VALID_JDBC_URL, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetDriverClassName() {
when(connectionExtensionPoint.getDriverClassName()).thenReturn(VALID_DRIVER_CLASS_NAME);

assertEquals(VALID_DRIVER_CLASS_NAME, dataSourceFactory.getDriverClassName());
}

@Test
public void testGetDialectType() {
assertEquals(VALID_DIALECT_TYPE, dataSourceFactory.getDialectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

assertEquals(VALID_JDBC_URL, dataSource.getUrl());
assertEquals(VALID_USERNAME, dataSource.getUsername());
assertEquals(VALID_PASSWORD, dataSource.getPassword());
assertEquals(VALID_DRIVER_CLASS_NAME, dataSource.getDriverClassName());

Properties properties = dataSource.getConnectionProperties();
assertFalse(ObjectUtils.isEmpty(properties));
assertEquals("false", properties.get("allowLoadLocalInfile"));

assertFalse(dataSource.isAutoCommit());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory clone = dataSourceFactory.deepCopy();

assertNotNull(clone);
assertEquals(validConnectionConfig.getUsername(), clone.getUsername());
assertEquals(validConnectionConfig.getPassword(), clone.getPassword());
assertEquals(validConnectionConfig.getHost(), clone.getHost());
assertEquals(validConnectionConfig.getPort(), clone.getPort());
assertEquals(validConnectionConfig.getDefaultSchema(), clone.getDefaultSchema());
assertEquals(validConnectionConfig.getJdbcUrlParameters(), clone.getJdbcParams());
assertFalse(clone.getDataSource() == dataSourceFactory.getDataSource());
}

@Test
public void testResetUsername() {
String newUsername = "newTestUser";
dataSourceFactory.resetUsername(value -> newUsername);

assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newTestPassword";
dataSourceFactory.resetPassword(value -> newPassword);

assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newTestHost";
dataSourceFactory.resetHost(value -> newHost);

assertEquals(newHost, dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 8765;
dataSourceFactory.resetPort(value -> newPort);

assertEquals(newPort, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newTestSchema";
dataSourceFactory.resetSchema(value -> newSchema);

assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newJdbcParams = new HashMap<>();
newJdbcParams.put("newKey", "newValue");

dataSourceFactory.resetParameters(value -> newJdbcParams);

Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
assertFalse(jdbcParams.isEmpty());
assertTrue(jdbcParams.containsKey("newKey"));
assertEquals("newValue", jdbcParams.get("newKey"));
}

private void mapJdbcParams() {
Map<String, Object> jdbcUrlParameters = new HashMap<>();
jdbcUrlParameters.put(VALID_JDBC_PARAM_KEY, VALID_JDBC_PARAM_VALUE);
validConnectionConfig.setJdbcUrlParameters(jdbcUrlParameters);
}
}