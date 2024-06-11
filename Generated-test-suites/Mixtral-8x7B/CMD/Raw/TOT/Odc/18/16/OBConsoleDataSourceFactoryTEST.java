import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionExtensionPoint connectionExtensionPoint;

@Before
public void setup() {
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
sslFileEntry.setKeyStoreFilePassword("keystore_password");
sslConfig.setSslFileEntry(sslFileEntry);

OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
tenantEndpoint.setProxyHost("proxy_host");
tenantEndpoint.setProxyPort(8080);

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("test_user");
connectionConfig.setPassword("test_password");
connectionConfig.setOBTenantName("test_tenant");
connectionConfig.setSslConfig(sslConfig);
connectionConfig.setEndpoint(tenantEndpoint);

connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
assertEquals("jdbc:oceanbase:mySQL://localhost:3306/test_tenant?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=round&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&socksProxyHost=proxy_host&socksProxyPort=8080&useSSL=true&trustServerCertificate=true&trustStore=/path/to/keystore&trustStorePassword=keystore_password&keyStore=/path/to/keystore&keyStorePassword=keystore_password&trustServerCertificate=false", jdbcUrl);
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
Properties properties = dataSource.getConnectionProperties();
assertEquals("false", properties.get("allowLoadLocalInfile").toString());
assertEquals("false", properties.get("allowUrlInLocalInfile").toString());
assertEquals("", properties.get("allowLoadLocalInfileInPath").toString());
assertEquals("false", properties.get("autoDeserialize").toString());
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
assertNotNull(deepCopy);
assertEquals(dataSourceFactory.getUsername(), deepCopy.getUsername());
assertEquals(dataSourceFactory.getPassword(), deepCopy.getPassword());
}

@Test
public void testResetUsername() {
ValueMapper<String> mapper = (value) -> "new_username";
dataSourceFactory.resetUsername(mapper);
assertEquals("new_username", dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
ValueMapper<String> mapper = (value) -> "new_password";
dataSourceFactory.resetPassword(mapper);
assertEquals("new_password", dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
ValueMapper<String> mapper = (value) -> "new_host";
dataSourceFactory.resetHost(mapper);
assertEquals("new_host", dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
ValueMapper<Integer> mapper = (value) -> 3307;
dataSourceFactory.resetPort(mapper);
assertEquals(Integer.valueOf(3307), dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
ValueMapper<String> mapper = (value) -> "new_schema";
dataSourceFactory.resetSchema(mapper);
assertEquals("new_schema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("new_param", "new_value");
ValueMapper<Map<String, String>> mapper = (value) -> newParams;
dataSourceFactory.resetParameters(mapper);
assertEquals(newParams, dataSourceFactory.getParameters());
}

}