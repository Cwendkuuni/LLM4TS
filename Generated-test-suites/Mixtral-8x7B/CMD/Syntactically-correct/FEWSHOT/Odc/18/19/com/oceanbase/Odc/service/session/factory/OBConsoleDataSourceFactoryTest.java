import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

@Test
public void testGetUsername() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertEquals("username", factory.getUsername());
}

@Test
public void testGetPassword() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertEquals("password", factory.getPassword());
}

@Test
public void testGetConnectType() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertEquals(ConnectType.CONSOLE, factory.getConnectType());
}

@Test
public void testGetJdbcUrl() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertEquals("jdbcUrl", factory.getJdbcUrl());
}

@Test
public void testGetDbUser() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("dbUser");
connectionConfig.setDialectType(DialectType.OB_ORACLE);
assertEquals("\"dbUser\"", OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

@Test
public void testGetPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setPassword("password");
assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
}

@Test
public void testGetJdbcParams() {
ConnectionConfig connectionConfig = new ConnectionConfig();
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertEquals(8, jdbcParams.size());
}

@Test
public void testGetDataSource() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
assertEquals("jdbcUrl", dataSource.getUrl());
assertEquals("username", dataSource.getUsername());
assertEquals("password", dataSource.getPassword());
assertEquals(ConnectionInitializer.class, dataSource.getInitializer().getClass());
}

@Test
public void testGetDialectType() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
CloneableDataSourceFactory clone = factory.deepCopy();
assertEquals(factory.getUsername(), clone.getUsername());
}

@Test
public void testResetUsername() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetUsername(string -> "newUsername");
assertEquals("newUsername", factory.getUsername());
}

@Test
public void testResetPassword() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetPassword(string -> "newPassword");
assertEquals("newPassword", factory.getPassword());
}

@Test
public void testResetHost() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetHost(string -> "newHost");
assertEquals("newHost", factory.getHost());
}

@Test
public void testResetPort() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetPort(integer -> 1234);
assertEquals(Integer.valueOf(1234), factory.getPort());
}

@Test
public void testResetSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
factory.resetSchema(string -> "newSchema");
assertEquals("newSchema", factory.getDefaultSchema());
}

@Test
public void testResetParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
Map<String, String> newParams = new HashMap<>();
factory.resetParameters(map -> newParams);
assertEquals(0, factory.getParameters().size());
}

@Test
public void testGetDefaultSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDefaultSchema("defaultSchema");
connectionConfig.setDialectType(DialectType.OB_ORACLE);
assertEquals("\"defaultSchema\"", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
}
}