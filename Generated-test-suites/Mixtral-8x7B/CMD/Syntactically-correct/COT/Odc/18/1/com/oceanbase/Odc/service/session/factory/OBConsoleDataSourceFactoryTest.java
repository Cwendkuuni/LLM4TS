import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.ValueMapper;

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

this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertFalse(StringUtils.isEmpty(jdbcUrl));
}

@Test
public void testGetUsername() {
assertEquals(dataSourceFactory.getUsername(), "root");
}

@Test
public void testGetPassword() {
assertEquals(dataSourceFactory.getPassword(), "password");
}

@Test
public void testGetConnectType() {
assertEquals(dataSourceFactory.getConnectType(), ConnectType.CONSOLE);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(dataSource.getClass(), SingleConnectionDataSource.class);
}

@Test
public void testGetDialectType() {
assertEquals(dataSourceFactory.getDialectType(), DialectType.OB_MYSQL);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
assertNotNull(copy);
assertNotSame(copy, dataSourceFactory);
}

@Test
public void testResetUsername() {
ValueMapper<String> mapper = (value) -> "newUsername";
dataSourceFactory.resetUsername(mapper);
assertEquals(dataSourceFactory.getUsername(), "newUsername");
}

@Test
public void testResetPassword() {
ValueMapper<String> mapper = (value) -> "newPassword";
dataSourceFactory.resetPassword(mapper);
assertEquals(dataSourceFactory.getPassword(), "newPassword");
}

@Test
public void testResetHost() {
ValueMapper<String> mapper = (value) -> "192.168.1.1";
dataSourceFactory.resetHost(mapper);
assertEquals(dataSourceFactory.getHost(), "192.168.1.1");
}

@Test
public void testResetPort() {
ValueMapper<Integer> mapper = (value) -> 3307;
dataSourceFactory.resetPort(mapper);
assertEquals(dataSourceFactory.getPort(), 3307);
}

@Test
public void testResetSchema() {
ValueMapper<String> mapper = (value) -> "newSchema";
dataSourceFactory.resetSchema(mapper);
assertEquals(dataSourceFactory.getDefaultSchema(), "newSchema");
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
ValueMapper<Map<String, String>> mapper = (value) -> newParams;
dataSourceFactory.resetParameters(mapper);
assertEquals(dataSourceFactory.getParameters(), newParams);
}

@Test(expected = IllegalArgumentException.class)
public void testResetPortNegative() {
ValueMapper<Integer> mapper = (value) -> -1;
dataSourceFactory.resetPort(mapper);
}

@Test
public void testGetDbUser() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setUsername("userName");
connectionConfig.setDialectType(DialectType.OB_ORACLE);
String dbUser = OBConsoleDataSourceFactory.getDbUser(connectionConfig);
assertEquals(dbUser, "\"userName\"");
}

@Test
public void testGetDefaultSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
assertEquals(defaultSchema, "odc");
}

@Test
public void testGetPasswordWithSSL() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.getSslConfig().setEnabled(true);
OBConsoleDataSourceFactory dataSourceFactoryWithSSL = new OBConsoleDataSourceFactory(connectionConfig, true);
assertNotNull(dataSourceFactoryWithSSL.getPassword());
}

@Test
public void testGetJdbcParamsWithSSL() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.getSslConfig().setEnabled(true);
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertTrue(jdbcParams.containsKey("useSSL"));
assertTrue(jdbcParams.get("useSSL").equalsIgnoreCase("true"));
}

@Test
public void testGetJdbcParamsWithSSLFileEntry() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.getSslConfig().setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
sslFileEntry.setKeyStoreFilePassword("keystorePassword");
connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);
Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
assertTrue(jdbcParams.containsKey("trustStore"));
assertTrue(jdbcParams.containsKey("trustStorePassword"));
assertTrue(jdbcParams.containsKey("keyStore"));
assertTrue(jdbcParams.containsKey("keyStorePassword"));
}
}