import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.CollectionUtils;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

private ConnectionConfig connectionConfig;
private ConnectionExtensionPoint connectionExtensionPoint;
private EventPublisher eventPublisher;
private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
connectionConfig = mock(ConnectionConfig.class);
connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
eventPublisher = mock(EventPublisher.class);
dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
}

@Test
public void testGetJdbcUrl() {
when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
.thenReturn("jdbc:test:test");
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertEquals("jdbc:test:test", jdbcUrl);
}

@Test
public void testGetUsername() {
when(getDbUser(connectionConfig)).thenReturn("testUser");
String username = dataSourceFactory.getUsername();
assertEquals("testUser", username);
}

@Test
public void testGetPassword() {
when(getPassword(connectionConfig)).thenReturn("testPassword");
String password = dataSourceFactory.getPassword();
assertEquals("testPassword", password);
}

@Test
public void testGetJdbcParams() {
Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);
assertNotNull(jdbcParams);
assertEquals(11, jdbcParams.size());
}

@Test
public void testGetDataSource() {
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
}

@Test
public void testGetDialectType() {
DialectType dialectType = dataSourceFactory.getDialectType();
assertEquals(DialectType.OB_MYSQL, dialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(OBConsoleDataSourceFactory.class, clone.getClass());
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
assertEquals(newHost, dataSourceFactory.host);
}

@Test
public void testResetPort() {
int newPort = 1234;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.port);
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
dataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, dataSourceFactory.defaultSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.parameters);
}

private static String getDbUser(ConnectionConfig connectionConfig) {
return "testUser";
}

private static String getPassword(ConnectionConfig connectionConfig) {
return "testPassword";
}

}