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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionConfig connectionConfig;
private ConnectionExtensionPoint connectionExtensionPoint;
private EventPublisher eventPublisher;

@Before
public void setup() {
connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setHost("localhost");
connectionConfig.setPort(26000);
connectionConfig.setUsername("testuser");
connectionConfig.setPassword("testpassword");
connectionConfig.setDefaultSchema("testschema");

connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
.thenReturn("jdbc:oceanbase:oracle://localhost:26000/testschema");
when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.odc.driver.OracleDriver");

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

eventPublisher = mock(EventPublisher.class);
dataSourceFactory.setEventPublisher(eventPublisher);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbc:oceanbase:oracle://localhost:26000/testschema", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("\"testuser\"@testtenant#testcluster", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
assertEquals("testpassword", dataSourceFactory.getPassword());
}

@Test
public void testGetDataSource() {
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
assertEquals("com.oceanbase.odc.driver.OracleDriver", dataSource.getDriverClassName());
assertEquals("jdbc:oceanbase:oracle://localhost:26000/testschema", dataSource.getUrl());
assertEquals("testuser", dataSource.getUsername());
assertEquals("testpassword", dataSource.getPassword());
assertEquals(true, dataSource.isAutoCommit());
verify(eventPublisher).publishEvent(any());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotSame(dataSourceFactory, clone);
assertEquals(dataSourceFactory.getUsername(), clone.getUsername());
assertEquals(dataSourceFactory.getPassword(), clone.getPassword());
}

@Test
public void testResetUsername() {
String newUsername = "newuser";
dataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newpassword";
dataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newhost";
dataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, dataSourceFactory.getConnectionConfig().getHost());
}

@Test
public void testResetPort() {
int newPort = 30000;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getConnectionConfig().getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newschema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.getConnectionConfig().getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getConnectionConfig().getJdbcUrlParameters());
}

@Test
public void testGetConnectType() {
assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
}

@Test
public void testGetDefaultSchema() {
assertEquals("testschema", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
}

@Test
public void testGetDbUser() {
assertEquals("testuser", OBConsoleDataSourceFactory.getDbUser(connectionConfig));
}

}