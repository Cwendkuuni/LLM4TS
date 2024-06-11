import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionConfig connectionConfig;
private ConnectionExtensionPoint connectionExtensionPoint;
private EventPublisher eventPublisher;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDefaultSchema("testdb");

connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
.thenReturn("jdbc:oceanbase://localhost:3306/testdb");
when(connectionExtensionPoint.getDriverClassName()).thenReturn("oceanbase.jdbc.Driver");

eventPublisher = mock(EventPublisher.class);

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher,
connectionExtensionPoint);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbc:oceanbase://localhost:3306/testdb", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("testuser", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("testpassword", dataSourceFactory.getPassword());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("jdbc:oceanbase://localhost:3306/testdb", dataSource.getUrl());
assertEquals("testuser", dataSource.getUsername());
assertEquals("testpassword", dataSource.getPassword());
assertEquals("oceanbase.jdbc.Driver", dataSource.getDriverClassName());

Properties properties = dataSource.getConnectionProperties();
assertEquals("false", properties.get("allowLoadLocalInfile"));
assertEquals("false", properties.get("allowUrlInLocalInfile"));
assertEquals("", properties.get("allowLoadLocalInfileInPath"));
assertEquals("false", properties.get("autoDeserialize"));

assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
}

@Test
public void testResetUsername() {
ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);

dataSourceFactory.resetUsername(value -> "newusername");

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testdb"),
anyMap());
verify(dataSourceFactory, times(1)).getJdbcUrl();

SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("newusername", dataSource.getUsername());
}

@Test
public void testResetPassword() {
ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

dataSourceFactory.resetPassword(value -> "newpassword");

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testdb"),
anyMap());
verify(dataSourceFactory, times(1)).getJdbcUrl();

SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("newpassword", dataSource.getPassword());
}

@Test
public void testResetHost() {
ArgumentCaptor<String> hostCaptor = ArgumentCaptor.forClass(String.class);

dataSourceFactory.resetHost(value -> "newhost");

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(hostCaptor.capture(), eq(3306), eq("testdb"),
anyMap());
verify(dataSourceFactory, times(1)).getJdbcUrl();

assertEquals("newhost", hostCaptor.getValue());
}

@Test
public void testResetPort() {
ArgumentCaptor<Integer> portCaptor = ArgumentCaptor.forClass(Integer.class);

dataSourceFactory.resetPort(value -> 4406);

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), portCaptor.capture(), eq("testdb"),
anyMap());
verify(dataSourceFactory, times(1)).getJdbcUrl();

assertEquals(4406, (int) portCaptor.getValue());
}

@Test
public void testResetSchema() {
ArgumentCaptor<String> schemaCaptor = ArgumentCaptor.forClass(String.class);

dataSourceFactory.resetSchema(value -> "newschema");

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), schemaCaptor.capture(),
anyMap());
verify(dataSourceFactory, times(1)).getJdbcUrl();

assertEquals("newschema", schemaCaptor.getValue());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");

ArgumentCaptor<Map<String, String>> paramsCaptor = ArgumentCaptor.forClass(Map.class);

dataSourceFactory.resetParameters(value -> newParams);

verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testdb"),
paramsCaptor.capture());
verify(dataSourceFactory, times(1)).getJdbcUrl();

Map<String, String> capturedParams = paramsCaptor.getValue();
assertEquals(2, capturedParams.size());
assertEquals("value1", capturedParams.get("param1"));
assertEquals("value2", capturedParams.get("param2"));
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

assertNotSame(dataSourceFactory, copy);
assertEquals(dataSourceFactory.getDialectType(), copy.getDialectType());
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
assertEquals(dataSourceFactory.getHost(), copy.getHost());
assertEquals(dataSourceFactory.getPort(), copy.getPort());
assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
assertEquals(dataSourceFactory.getParameters(), copy.getParameters());
}
}