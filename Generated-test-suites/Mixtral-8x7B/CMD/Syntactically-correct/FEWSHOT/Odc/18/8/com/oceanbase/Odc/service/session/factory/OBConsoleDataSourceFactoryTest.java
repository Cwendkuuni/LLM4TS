import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtilsTest;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionConfig connectionConfig;
private ConnectionExtensionPoint connectionExtensionPoint;
private EventPublisher eventPublisher;

@Before
public void setUp() {
connectionConfig = new ConnectionConfig();
connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
eventPublisher = mock(EventPublisher.class);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher) {
@Override
protected ConnectionExtensionPoint getConnectionExtensionPoint() {
return connectionExtensionPoint;
}
};
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:oceanbase://localhost:20000/my_schema";
when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap())).thenReturn(expectedJdbcUrl);

assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
String expectedUsername = "my_user";
when(connectionConfig.getUsername()).thenReturn(expectedUsername);

assertEquals(expectedUsername, dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
String expectedPassword = "my_password";
when(connectionConfig.getPassword()).thenReturn(expectedPassword);

assertEquals(expectedPassword, dataSourceFactory.getPassword());
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);

assertFalse(jdbcParams.isEmpty());
assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
assertEquals("true", jdbcParams.get("allowMultiQueries"));
assertEquals("5000", jdbcParams.get("connectTimeout"));
assertEquals(OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR, jdbcParams.get("zeroDateTimeBehavior"));
assertEquals("true", jdbcParams.get("noDatetimeStringSync"));
assertEquals("false", jdbcParams.get("jdbcCompliantTruncation"));
assertEquals("false", jdbcParams.get("sendConnectionAttributes"));
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

assertEquals(dataSourceFactory.getJdbcUrl(), dataSource.getUrl());
assertEquals(dataSourceFactory.getUsername(), dataSource.getUsername());
assertEquals(dataSourceFactory.getPassword(), dataSource.getPassword());
assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getDriverClassName());

Properties properties = dataSource.getConnectionProperties();
assertEquals("false", properties.getProperty("allowLoadLocalInfile"));
assertEquals("false", properties.getProperty("allowUrlInLocalInfile"));
assertEquals("", properties.getProperty("allowLoadLocalInfileInPath"));
assertEquals("false", properties.getProperty("autoDeserialize"));
}

@Test
public void testGetDialectType() {
DialectType dialectType = DialectType.OB_MYSQL;
when(connectionConfig.getDialectType()).thenReturn(dialectType);

assertEquals(dialectType, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

assertNotSame(dataSourceFactory, copy);
assertNotSame(dataSourceFactory.connectionConfig, copy.connectionConfig);
}

@Test
public void testResetUsername() {
String newUsername = "new_username";
CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newUsername;
dataSourceFactory.resetUsername(valueMapper);

assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "new_password";
CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newPassword;
dataSourceFactory.resetPassword(valueMapper);

assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "new_host";
CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newHost;
dataSourceFactory.resetHost(valueMapper);

verify(connectionConfig).setHost(newHost);
}

@Test
public void testResetPort() {
int newPort = 30000;
CloneableDataSourceFactory.ValueMapper<Integer> valueMapper = value -> newPort;
dataSourceFactory.resetPort(valueMapper);

verify(connectionConfig).setPort(newPort);
}

@Test
public void testResetSchema() {
String newSchema = "new_schema";
CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newSchema;
dataSourceFactory.resetSchema(valueMapper);

verify(connectionConfig).setDefaultSchema(newSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
CloneableDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = value -> newParams;
dataSourceFactory.resetParameters(valueMapper);

verify(connectionConfig).setJdbcUrlParameters(newParams);
}
}

class StringUtilsTest {
public static String isNotEmpty(String value) {
return StringUtils.isNotEmpty(value) ? value : "";
}
}