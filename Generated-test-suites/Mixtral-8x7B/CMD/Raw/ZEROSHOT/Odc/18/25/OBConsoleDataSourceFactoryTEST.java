import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private EventPublisher eventPublisher;

@Before
public void setUp() {
connectionConfig = Mockito.mock(ConnectionConfig.class);
connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
eventPublisher = Mockito.mock(EventPublisher.class);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint);
}

@Test
public void testGetJdbcUrl() {
Mockito.when(connectionConfig.getHost()).thenReturn("localhost");
Mockito.when(connectionConfig.getPort()).thenReturn(3306);
Mockito.when(connectionConfig.getDefaultSchema()).thenReturn("testdb");
Mockito.when(connectionConfig.getJdbcUrlParameters())
.thenReturn(Collections.singletonMap("key", "value"));

Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
Mockito.anyString(), Mockito.anyMap()))
.thenReturn("jdbc:test:testdb");

assertEquals("jdbc:test:testdb", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
Mockito.when(connectionConfig.getUsername()).thenReturn("testuser");
Mockito.when(connectionConfig.getTenantName()).thenReturn("tenant1");
Mockito.when(connectionConfig.getClusterName()).thenReturn("cluster1");
Mockito.when(connectionConfig.getOBTenantName()).thenReturn("obtenant1");

assertEquals("\"testuser\"@obtenant1#cluster1", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
Mockito.when(connectionConfig.getPassword()).thenReturn("testpassword");

assertEquals("testpassword", dataSourceFactory.getPassword());
}

@Test
public void testGetJdbcParams() {
Mockito.when(connectionConfig.getJdbcUrlParameters())
.thenReturn(Collections.singletonMap("key", "value"));

Map<String, String> expectedMap = new HashMap<>();
expectedMap.put("maxAllowedPacket", "64000000");
expectedMap.put("allowMultiQueries", "true");
expectedMap.put("connectTimeout", "5000");
expectedMap.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
expectedMap.put("noDatetimeStringSync", "true");
expectedMap.put("jdbcCompliantTruncation", "false");
expectedMap.put("sendConnectionAttributes", "false");
expectedMap.put("key", "value");

assertEquals(expectedMap, dataSourceFactory.getJdbcParams(connectionConfig));
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);

Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn("testdriver");

dataSourceFactory.getDataSource();

Mockito.verify(dataSource).setEventPublisher(eventPublisher);
Mockito.verify(dataSource).setUrl(dataSourceFactory.getJdbcUrl());
Mockito.verify(dataSource).setUsername(dataSourceFactory.getUsername());
Mockito.verify(dataSource).setPassword(dataSourceFactory.getPassword());
Mockito.verify(dataSource).setDriverClassName("testdriver");
Mockito.verify(dataSource).setConnectionProperties(Mockito.anyMap());
Mockito.verify(dataSource).addInitializer(Mockito.any(ConnectionInitializer.class));
}

@Test
public void testGetDialectType() {
Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);

assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
ConnectionMapper mapper = ConnectionMapper.INSTANCE;
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getConnectType(), copy.getConnectType());
assertEquals(dataSourceFactory.getJdbcUrl(), copy.getJdbcUrl());

// Check that modifying the original object does not affect the copy
dataSourceFactory.resetUsername(value -> "newusername");
assertNotEquals(copy.getUsername(), "newusername");
}

@Test
public void testResetUsername() {
String originalUsername = dataSourceFactory.getUsername();
dataSourceFactory.resetUsername(value -> "newusername");

assertEquals("newusername", dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String originalPassword = dataSourceFactory.getPassword();
dataSourceFactory.resetPassword(value -> "newpassword");

assertEquals("newpassword", dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String originalHost = dataSourceFactory.getHost();
dataSourceFactory.resetHost(value -> "newhost");

assertEquals("newhost", dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
int originalPort = dataSourceFactory.getPort();
dataSourceFactory.resetPort(value -> 9999);

assertEquals(9999, dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String originalSchema = dataSourceFactory.getDefaultSchema();
dataSourceFactory.resetSchema(value -> "newschema");

assertEquals("newschema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> originalParams = dataSourceFactory.getJdbcParams(connectionConfig);
Map<String, String> newParams = new HashMap<>();
newParams.put("newkey", "newvalue");

dataSourceFactory.resetParameters(value -> newParams);

assertEquals(newParams, dataSourceFactory.getJdbcParams(connectionConfig));
}

}