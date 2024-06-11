import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;
private ConnectionExtensionPoint connectionExtensionPoint;
private ConnectionConfig connectionConfig;
private SingleConnectionDataSource singleConnectionDataSource;
private EventPublisher eventPublisher;

@Before
public void setup() {
connectionConfig = mock(ConnectionConfig.class);
connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
singleConnectionDataSource = mock(SingleConnectionDataSource.class);
eventPublisher = mock(EventPublisher.class);

when(connectionExtensionPoint.generateJdbcUrl(
"host", 1234, "schema", new HashMap<>())).thenReturn("jdbcUrl");
when(connectionConfig.getHost()).thenReturn("host");
when(connectionConfig.getPort()).thenReturn(1234);
when(connectionConfig.getDefaultSchema()).thenReturn("schema");
when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
when(connectionConfig.getSslConfig()).thenReturn(new SSLConfig());
when(connectionConfig.getEndpoint()).thenReturn(new OBTenantEndpoint());
when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());

dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint);
dataSourceFactory.setEventPublisher(eventPublisher);
}

@Test
public void testGetJdbcUrl() {
assertEquals("jdbcUrl", dataSourceFactory.getJdbcUrl());
}

@Test
public void testGetUsername() {
assertEquals("host@", OBConsoleDataSourceFactory.getUsername(connectionConfig));
}

@Test
public void testGetPassword() {
assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
}

@Test
public void testGetDataSource() {
DataSources dataSource = new DataSources();
dataSource.addDataSource(dataSourceFactory.getDataSource());
assertEquals(singleConnectionDataSource, dataSource.getOnlyElement());
assertEquals("jdbcUrl", ((SingleConnectionDataSource) dataSource.getOnlyElement()).getUrl());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory copy = dataSourceFactory.deepCopy();
assertEquals(dataSourceFactory.getDialectType(), copy.getDialectType());
assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
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
assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
}

@Test
public void testResetPort() {
int newPort = 5678;
dataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
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
newParams.put("key", "value");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getJdbcParams(connectionConfig));
}
}

class DataSources extends HashMap<String, SingleConnectionDataSource> {

@Override
public SingleConnectionDataSource getOnlyElement() {
return super.values().stream().findFirst().orElseThrow(IllegalStateException::new);
}
}