import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:mysql://localhost:3306/root";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
String expectedUsername = "root";
assertEquals(expectedUsername, dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
String expectedPassword = "password";
assertEquals(expectedPassword, dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
ConnectType expectedConnectType = ConnectType.CONSOLE;
assertEquals(expectedConnectType, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("com.oceanbase.odc.plugin.mysql.driver.MySQLDriver", dataSource.getDriverClassName());
assertEquals("localhost", dataSource.getUrl());
assertEquals("root", dataSource.getUsername());
assertEquals("password", dataSource.getPassword());
}

@Test
public void testGetDialectType() {
DialectType expectedDialectType = DialectType.OB_MYSQL;
assertEquals(expectedDialectType, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(dataSourceFactory.getUsername(), ((OBConsoleDataSourceFactory) clone).getUsername());
}

@Test
public void testResetUsername() {
String newUsername = "newUser";
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
String newHost = "127.0.0.1";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.connectionConfig.getHost());
}

@Test
public void testResetPort() {
int newPort = 3307;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.connectionConfig.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.defaultSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("key", "value");
dataSourceFactory.resetParameters(value -> newParameters);
assertEquals(newParameters, dataSourceFactory.parameters);
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegativeValue() {
int newPort = -1;
dataSourceFactory.resetPort(value -> newPort);
}

@Test
public void testGetDbUser() {
String dbUser = OBConsoleDataSourceFactory.getDbUser(new ConnectionConfig());
assertEquals("", dbUser);
}

@Test
public void testGetDefaultSchema() {
String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(new ConnectionConfig());
assertEquals("", defaultSchema);
}
}