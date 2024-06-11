import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertiesBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(false);

ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), any(Map.class)))
.thenReturn("jdbc:mock:test");
when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.Driver");

List<ConnectionInitializer> initializers = Arrays.asList(mock(ConnectionInitializer.class));

dataSourceFactory = new OBConsoleDataSourceFactory(createValidConnectionConfig(), true, true, null, sslConfig,
connectionExtensionPoint, initializers);
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("jdbc:mock:test", dataSource.getUrl());
assertEquals("testUsername", dataSource.getUsername());
assertEquals("testPassword", dataSource.getPassword());
assertEquals("com.mock.Driver", dataSource.getDriverClassName());

Properties properties = dataSource.getConnectionProperties();
assertEquals("false", properties.getProperty("allowLoadLocalInfile"));
assertEquals("false", properties.getProperty("allowUrlInLocalInfile"));
assertEquals("", properties.getProperty("allowLoadLocalInfileInPath"));
assertEquals("false", properties.getProperty("autoDeserialize"));
}

@Test
public void testResetUsername() {
dataSourceFactory.resetUsername(value -> "newTestUsername");

SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("newTestUsername", dataSource.getUsername());
}

@Test
public void testResetPassword() {
dataSourceFactory.resetPassword(value -> "newTestPassword");

SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("newTestPassword", dataSource.getPassword());
}

@Test
public void testResetHost() {
dataSourceFactory.resetHost(value -> "newTestHost");

assertEquals("newTestHost", ((OBConsoleDataSourceFactory) dataSourceFactory).getHost());
}

@Test
public void testResetPort() {
dataSourceFactory.resetPort(value -> 9999);

assertEquals(Integer.valueOf(9999), ((OBConsoleDataSourceFactory) dataSourceFactory).getPort());
}

@Test
public void testResetSchema() {
dataSourceFactory.resetSchema(value -> "newTestSchema");

assertEquals("newTestSchema", ((OBConsoleDataSourceFactory) dataSourceFactory).getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("newParam", "newValue");

dataSourceFactory.resetParameters(value -> newParameters);

assertEquals(newParameters, ((OBConsoleDataSourceFactory) dataSourceFactory).getJdbcParams(createValidConnectionConfig()));
}

private ConnectionConfig createValidConnectionConfig() {
return new ConnectionConfig()
.setUsername("testUsername")
.setPassword("testPassword")
.setHost("testHost")
.setPort(1234)
.setDefaultSchema("testSchema")
.setJdbcUrlParameters(new HashMap<>())
.setType(ConnectType.LONG);
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

assertEquals(dataSourceFactory.getDialectType(), copy.getDialectType());
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getHost(), copy.getHost());
assertEquals(dataSourceFactory.getPort(), copy.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
assertEquals(dataSourceFactory.getJdbcParams(createValidConnectionConfig()), copy.getJdbcParams(createValidConnectionConfig()));
}

private Map<String, String> getJdbcParams(@NonNull ConnectionConfig connectionConfig) {
Map<String, String> jdbcUrlParams = new HashMap<>();
jdbcUrlParams.put("maxAllowedPacket", "64000000");
jdbcUrlParams.put("allowMultiQueries", "true");
jdbcUrlParams.put("connectTimeout", "5000");
jdbcUrlParams.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
jdbcUrlParams.put("noDatetimeStringSync", "true");
jdbcUrlParams.put("jdbcCompliantTruncation", "false");
jdbcUrlParams.put("sendConnectionAttributes", "false");
return jdbcUrlParams;
}

}