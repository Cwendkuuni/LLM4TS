import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Mock
private EventPublisher eventPublisher;

@Mock
private ConnectionExtensionPoint connectionExtensionPoint;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
connectionConfig.setDefaultSchema("testSchema");
SSLConfig sslConfig = new SSLConfig();
sslConfig.setEnabled(true);
connectionConfig.setSslConfig(sslConfig);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher,
connectionExtensionPoint);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbc:oceanbase:mysql://localhost:3306/testSchema?allowMultiQueries=true&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&trustServerCertificate=false&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR",
dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("testUser", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("testPassword", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("localhost", dataSource.getUrl());
assertEquals("testUser", dataSource.getUsername());
assertEquals("testPassword", dataSource.getPassword());
assertEquals("jdbc:oceanbase:mysql", dataSource.getDriverClassName());
assertEquals(true, dataSource.isAutoCommit());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
assertNotSame(dataSourceFactory, clonedFactory);
assertEquals(dataSourceFactory.getUsername(), clonedFactory.getUsername());
assertEquals(dataSourceFactory.getPassword(), clonedFactory.getPassword());
assertEquals(dataSourceFactory.getHost(), clonedFactory.getHost());
assertEquals(dataSourceFactory.getPort(), clonedFactory.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(),
clonedFactory.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(),
clonedFactory.getParameters());
}

@Test
public void testResetUsername() {
String newUsername = "newUsername";
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
String newHost = "newHost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int newPort = 4444;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
}

}