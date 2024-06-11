import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private EventPublisher eventPublisher;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");

eventPublisher = Mockito.mock(EventPublisher.class);

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
obConsoleDataSourceFactory.setEventPublisher(eventPublisher);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:myob://localhost:3306/testUser";
Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
Mockito.anyString(), Mockito.anyMap())).thenReturn(expectedJdbcUrl);

assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("testUser", obConsoleDataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("testPassword", obConsoleDataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.CONSOLE, obConsoleDataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();
assertEquals("testUser", dataSource.getUsername());
assertEquals("testPassword", dataSource.getPassword());
assertEquals("jdbc:myob://localhost:3306/testUser", dataSource.getUrl());
assertEquals(true, dataSource.isAutoCommit());
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
String newHost = "127.0.0.1";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.getConnectType().getHost());
}

@Test
public void testResetPort() {
int newPort = 3307;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.getConnectType().getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
obConsoleDataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
assertNotSame(obConsoleDataSourceFactory, copy);
assertEquals(obConsoleDataSourceFactory.getUsername(), copy.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), copy.getPassword());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
}
}