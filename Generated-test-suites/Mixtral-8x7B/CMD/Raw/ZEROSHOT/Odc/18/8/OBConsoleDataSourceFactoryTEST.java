import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;
 @Mock
 private ConnectionConfig connectionConfig;
 @Mock
 private OBTenantEndpoint endpoint;
 @Mock
 private SSLConfig sslConfig;
 @Mock
 private SSLFileEntry sslFileEntry;

 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
 when(connectionConfig.getHost()).thenReturn("localhost");
 when(connectionConfig.getPort()).thenReturn(3306);
 when(connectionConfig.getUsername()).thenReturn("testUser");
 when(connectionConfig.getPassword()).thenReturn("testPassword");
 when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
 when(connectionConfig.getEndpoint()).thenReturn(endpoint);
 when(endpoint.getProxyHost()).thenReturn("proxyHost");
 when(endpoint.getProxyPort()).thenReturn(1234);
 when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
 when(sslConfig.getEnabled()).thenReturn(true);
 when(sslConfig.getSslFileEntry()).thenReturn(sslFileEntry);
 when(sslFileEntry.getKeyStoreFilePath()).thenReturn("/path/to/keyStoreFile");
 when(sslFileEntry.getKeyStoreFilePassword()).thenReturn("keyStorePassword");
 when(connectionExtensionPoint.generateJdbcUrl(
 "localhost",
 3306,
 "testSchema",
 new HashMap<String, String>() {{
 put("maxAllowedPacket", "64000000");
 put("allowMultiQueries", "true");
 put("connectTimeout", "5000");
 put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 put("noDatetimeStringSync", "true");
 put("jdbcCompliantTruncation", "false");
 put("sendConnectionAttributes", "false");
 put("socksProxyHost", "proxyHost");
 put("socksProxyPort", "1234");
 put("useSSL", "true");
 put("trustServerCertificate", "true");
 put("trustStore", "/path/to/keyStoreFile");
 put("trustStorePassword", "keyStorePassword");
 put("keyStore", "/path/to/keyStoreFile");
 put("keyStorePassword", "keyStorePassword");
 put("trustServerCertificate", "false");
 }})
 ).thenReturn("jdbc:oceanbase:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR"
 + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&socksProxyHost=proxyHost&socksProxyPort=1234&useSSL=true&trustServerCertificate=false"
 + "&trustStore=/path/to/keyStoreFile&trustStorePassword=keyStorePassword&keyStore=/path/to/keyStoreFile&keyStorePassword=keyStorePassword");
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher, connectionExtensionPoint);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
 assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR"
 + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&socksProxyHost=proxyHost&socksProxyPort=1234&useSSL=true&trustServerCertificate=false"
 + "&trustStore=/path/to/keyStoreFile&trustStorePassword=keyStorePassword&keyStore=/path/to/keyStoreFile&keyStorePassword=keyStorePassword", dataSource.getUrl());
 assertEquals("testUser", ((SingleConnectionDataSource) dataSource).getUsername());
 assertEquals("testPassword", ((SingleConnectionDataSource) dataSource).getPassword());
 assertEquals(DriverManagerDataSource.class, ((SingleConnectionDataSource) dataSource).getDriver().getClass());
 assertEquals("com.oceanbase.odc.plugin.connect.mysql.Driver", ((SingleConnectionDataSource) dataSource).getDriver().getClass().getName());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory deepCopy = obConsoleDataSourceFactory.deepCopy();
 assertEquals(OBConsoleDataSourceFactory.class, deepCopy.getClass());
 assertEquals(connectionConfig.getDialectType(), deepCopy.getDialectType());
 assertEquals(connectionConfig.getHost(), deepCopy.getHost());
 assertEquals(connectionConfig.getPort(), deepCopy.getPort());
 assertEquals(connectionConfig.getUsername(), deepCopy.getUsername());
 assertEquals(connectionConfig.getPassword(), deepCopy.getPassword());
 assertEquals(connectionConfig.getDefaultSchema(), deepCopy.getDefaultSchema());
 }
}