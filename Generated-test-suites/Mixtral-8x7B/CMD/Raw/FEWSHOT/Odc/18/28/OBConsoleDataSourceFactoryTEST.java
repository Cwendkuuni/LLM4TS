import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

private static final String VALID_USERNAME = "testUser";
private static final String VALID_PASSWORD = "testPassword";
private static final String VALID_HOST = "localhost";
private static final int VALID_PORT = 3306;
private static final String VALID_DEFAULT_SCHEMA = "testSchema";
private static final String VALID_JDBC_PARAM = "testParam";
private static final String VALID_JDBC_URL = "jdbc:test:localhost:3306/testSchema";
private static final String VALID_DRIVER_CLASS = "com.test.Driver";
private static final String VALID_DIALECT_TYPE = "TEST";
private static final ConnectType VALID_CONNECT_TYPE = ConnectType.CONSOLE;
private static final boolean VALID_AUTO_COMMIT = true;
private static final boolean VALID_INIT_CONNECTION = true;

private ConnectionConfig validConnectionConfig;
private ConnectionExtensionPoint mockConnectionExtensionPoint;
private EventPublisher mockEventPublisher;
private Map<String, String> validJdbcParams;
private Map<String, String> emptyJdbcParams;
private Properties validProperties;
private List<ConnectionInitializer> validInitializers;
private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
mockConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
mockEventPublisher = mock(EventPublisher.class);
validJdbcParams = new HashMap<>();
validJdbcParams.put(VALID_JDBC_PARAM, "true");
emptyJdbcParams = new HashMap<>();
validProperties = new Properties();
validProperties.setProperty("allowLoadLocalInfile", "false");
validProperties.setProperty("allowUrlInLocalInfile", "false");
validProperties.setProperty("allowLoadLocalInfileInPath", "");
validProperties.setProperty("autoDeserialize", "false");
validInitializers = Collections.singletonList(new SessionCreatedInitializer(null));
validConnectionConfig = new ConnectionConfig();
validConnectionConfig.setUsername(VALID_USERNAME);
validConnectionConfig.setPassword(VALID_PASSWORD);
validConnectionConfig.setHost(VALID_HOST);
validConnectionConfig.setPort(VALID_PORT);
validConnectionConfig.setDefaultSchema(VALID_DEFAULT_SCHEMA);
validConnectionConfig.setJdbcUrlParameters(validJdbcParams);
validConnectionConfig.setDialectType(VALID_DIALECT_TYPE);
validConnectionConfig.setType(VALID_CONNECT_TYPE);
obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(validConnectionConfig, VALID_AUTO_COMMIT,
VALID_INIT_CONNECTION);
}

@Test
public void testGetJdbcUrl() {
when(mockConnectionExtensionPoint.generateJdbcUrl(VALID_HOST, VALID_PORT, VALID_DEFAULT_SCHEMA,
validJdbcParams)).thenReturn(VALID_JDBC_URL);
assertEquals(VALID_JDBC_URL, obConsoleDataSourceFactory.getJdbcUrl());
verify(mockConnectionExtensionPoint).generateJdbcUrl(VALID_HOST, VALID_PORT, VALID_DEFAULT_SCHEMA,
validJdbcParams);
}

@Test
public void testGetUsername() {
assertEquals(VALID_USERNAME, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals(VALID_PASSWORD, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = obConsoleDataSourceFactory.getJdbcParams();
assertEquals(validJdbcParams, jdbcParams);
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();
assertEquals(VALID_JDBC_URL, dataSource.getUrl());
assertEquals(VALID_USERNAME, dataSource.getUsername());
assertEquals(VALID_PASSWORD, dataSource.getPassword());
assertEquals(VALID_DRIVER_CLASS, dataSource.getDriverClassName());
assertEquals(validProperties, dataSource.getConnectionProperties());
assertEquals(VALID_AUTO_COMMIT, dataSource.isAutoCommit());
verify(mockConnectionExtensionPoint).getDriverClassName();
}

@Test
public void testGetDialectType() {
assertEquals(VALID_DIALECT_TYPE, obConsoleDataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
OBConsoleDataSourceFactory copyDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
assertNotSame(obConsoleDataSourceFactory, copyDataSourceFactory);
assertEquals(obConsoleDataSourceFactory.getUsername(), copyDataSourceFactory.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), copyDataSourceFactory.getPassword());
assertEquals(obConsoleDataSourceFactory.getHost(), copyDataSourceFactory.getHost());
assertEquals(obConsoleDataSourceFactory.getPort(), copyDataSourceFactory.getPort());
assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), copyDataSourceFactory.getDefaultSchema());
assertEquals(obConsoleDataSourceFactory.getJdbcParams(), copyDataSourceFactory.getJdbcParams());
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
String newHost = "newTestHost";
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
String newSchema = "newTestSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newJdbcParams = new HashMap<>();
newJdbcParams.put("newParam", "true");
obConsoleDataSourceFactory.resetParameters(value -> newJdbcParams);
assertEquals(newJdbcParams, obConsoleDataSourceFactory.getJdbcParams());
}

@Test
public void testGetConnectType() {
assertEquals(VALID_CONNECT_TYPE, obConsoleDataSourceFactory.getConnectType());
}

@Test
public void testGetDefaultSchema() {
String defaultSchema = obConsoleDataSourceFactory.getDefaultSchema();
assertEquals(VALID_DEFAULT_SCHEMA, defaultSchema);
}

@Test
public void testGetDbUser() {
String dbUser = OBConsoleDataSourceFactory.getDbUser(validConnectionConfig);
assertEquals(VALID_USERNAME, dbUser);
}

@Test
public void testGetPasswordNull() {
ConnectionConfig nullPasswordConnectionConfig = new ConnectionConfig();
nullPasswordConnectionConfig.setUsername(VALID_USERNAME);
nullPasswordConnectionConfig.setPassword(null);
String password = OBConsoleDataSourceFactory.getPassword(nullPasswordConnectionConfig);
assertEquals("", password);
}

@Test
public void testGetJdbcParamsEmpty() {
Map<String, String> emptyJdbcParams = new HashMap<>();
OBConsoleDataSourceFactory obConsoleDataSourceFactoryEmptyJdbcParams = new OBConsoleDataSourceFactory(
validConnectionConfig, VALID_AUTO_COMMIT, VALID_INIT_CONNECTION);
obConsoleDataSourceFactoryEmptyJdbcParams.getJdbcParams();
assertEquals(emptyJdbcParams, obConsoleDataSourceFactoryEmptyJdbcParams.getJdbcParams());
}

@Test
public void testGetDataSourceNullProperties() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory
.getDataSourceWithNullProperties();
assertEquals(VALID_JDBC_URL, dataSource.getUrl());
assertEquals(VALID_USERNAME, dataSource.getUsername());
assertEquals(VALID_PASSWORD, dataSource.getPassword());
assertEquals("", dataSource.getDriverClassName());
assertEquals(emptyProperties, dataSource.getConnectionProperties());
assertEquals(VALID_AUTO_COMMIT, dataSource.isAutoCommit());
}

@Test
public void testGetDataSourceWithInitializers() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory
.getDataSourceWithInitializers();
assertEquals(VALID_JDBC_URL, dataSource.getUrl());
assertEquals(VALID_USERNAME, dataSource.getUsername());
assertEquals(VALID_PASSWORD, dataSource.getPassword());
assertEquals(VALID_DRIVER_CLASS, dataSource.getDriverClassName());
assertEquals(validProperties, dataSource.getConnectionProperties());
assertEquals(VALID_AUTO_COMMIT, dataSource.isAutoCommit());
assertEquals(validInitializers.size(), dataSource.getInitializers().size());
verify(mockConnectionExtensionPoint, times(2)).getDriverClassName();
}

}