import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPoint() {
@Override
public String generateJdbcUrl(String host, Integer port, String defaultSchema,
Map<String, String> parameters) {
throw new NotImplementedException();
}

@Override
public String getDriverClassName() {
throw new NotImplementedException();
}

@Override
public List<ConnectionInitializer> getConnectionInitializers() {
throw new NotImplementedException();
}
};

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setUsername("username");
connectionConfig.setPassword("password");
connectionConfig.setHost("localhost");
connectionConfig.setPort(1234);
connectionConfig.setDefaultSchema("myschema");
connectionConfig.setSslConfig(new SSLConfig());
connectionConfig.setJdbcUrlParameters(new HashMap<>());

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:oceanbase:oracle:@localhost:1234:myschema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
+ OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";

assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("username", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("password", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals(dataSource.getUrl(), dataSourceFactory.getJdbcUrl());
assertEquals(dataSource.getUsername(), dataSourceFactory.getUsername());
assertEquals(dataSource.getPassword(), dataSourceFactory.getPassword());
assertEquals(dataSource.getDriverClassName(), connectionExtensionPoint.getDriverClassName());
assertTrue(dataSource.isAutoCommit());
assertNotNull(dataSource.getConnectionProperties());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
OBConsoleDataSourceFactory copyDataSourceFactory = dataSourceFactory.deepCopy();
assertEquals(copyDataSourceFactory.getUsername(), dataSourceFactory.getUsername());
assertEquals(copyDataSourceFactory.getPassword(), dataSourceFactory.getPassword());
assertEquals(copyDataSourceFactory.getDialectType(), dataSourceFactory.getDialectType());
assertEquals(copyDataSourceFactory.getConnectType(), dataSourceFactory.getConnectType());
assertEquals(copyDataSourceFactory.getJdbcUrl(), dataSourceFactory.getJdbcUrl());
}

@Test
public void testResetUsername() {
dataSourceFactory.resetUsername(value -> "newUsername");
assertEquals("newUsername", dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
dataSourceFactory.resetPassword(value -> "newPassword");
assertEquals("newPassword", dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
dataSourceFactory.resetHost(value -> "newHost");
assertEquals("newHost", dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
dataSourceFactory.resetPort(value -> 5678);
assertEquals(Integer.valueOf(5678), dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
dataSourceFactory.resetSchema(value -> "newSchema");
assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParameters = new HashMap<>();
newParameters.put("newParam", "newValue");
dataSourceFactory.resetParameters(value -> newParameters);
assertEquals(newParameters, dataSourceFactory.getParameters());
}

}