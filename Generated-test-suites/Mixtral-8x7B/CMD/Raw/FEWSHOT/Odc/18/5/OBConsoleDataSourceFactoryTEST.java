import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class OBConsoleDataSourceFactoryTest {

@Before
public void setUp() {
ConnectionPluginUtil.init();
}

@Test
public void testGetJdbcUrl_withDefaultParams() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), true);
String jdbcUrl = factory.getJdbcUrl();
assertEquals("jdbc:oceanbase:oracle:12345@my-schema?allowMultiQueries=true&allowLoadLocalInfile=false&allowUrlInLocalInfile=false"
+"&allowLoadLocalInfileInPath=&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false"
+"&keyStore=path&keyStorePassword=password&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=path"
+"&trustStorePassword=password&trustServerCertificate=false&zeroDateTimeBehavior=false", jdbcUrl);
}

@Test
public void testGetJdbcUrl_withAdditionalParams() {
Map<String, Object> additionalParams = new HashMap<>();
additionalParams.put("param1", "value1");
additionalParams.put("param2", "value2");
ConnectionConfig connectionConfig = createConnectionConfig();
connectionConfig.getJdbcUrlParameters().putAll(additionalParams);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
String jdbcUrl = factory.getJdbcUrl();
assertEquals("jdbc:oceanbase:oracle:12345@my-schema?allowMultiQueries=true&allowLoadLocalInfile=false&allowUrlInLocalInfile=false"
+"&allowLoadLocalInfileInPath=&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false"
+"&keyStore=path&keyStorePassword=password&maxAllowedPacket=64000000&noDatetimeStringSync=true&param1=value1&param2=value2&sendConnectionAttributes=false&trustStore=path"
+"&trustStorePassword=password&trustServerCertificate=false&zeroDateTimeBehavior=false", jdbcUrl);
}

@Test
public void testGetDataSource_withAutoCommitTrue() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), true, true);
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
assertTrue(dataSource.isAutoCommit());
}

@Test
public void testGetDataSource_withAutoCommitFalse() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), false, true);
SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
assertFalse(dataSource.isAutoCommit());
}

@Test
public void testGetDataSource_withInitializers() {
ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(List.of(new SessionCreatedInitializer(createConnectionConfig())));
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), true, true, connectionExtensionPoint);
assertFalse(CollectionUtils.isEmpty(factory.getDataSource().getInitializers()));
}

@Test
public void testGetDataSource_withSslDisabled() {
ConnectionConfig connectionConfig = createConnectionConfig();
connectionConfig.getSslConfig().setEnabled(false);
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
Properties properties = factory.getDataSource().getConnectionProperties();
assertFalse("false".equals(properties.getProperty("useSSL")));
}

@Test
public void testGetDataSource_withSslEnabled() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), true, true);
Properties properties = factory.getDataSource().getConnectionProperties();
assertTrue("true".equals(properties.getProperty("useSSL")));
}

@Test
public void testDeepCopy() {
OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(createConnectionConfig(), true);
OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
assertFalse(factory == clonedFactory);
assertEquals(factory.getUsername(), clonedFactory.getUsername());
assertEquals(factory.getPassword(), clonedFactory.getPassword());
assertEquals(factory.getDefaultSchema(), clonedFactory.getDefaultSchema());
assertEquals(factory.getParameters(), clonedFactory.getParameters());
}

private ConnectionConfig createConnectionConfig() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setUsername("user");
connectionConfig.setPassword("password");
connectionConfig.setHost("123.45.67.89");
connectionConfig.setPort(12345);
connectionConfig.setDefaultSchema("my-schema");
connectionConfig.setSslConfig(new SSLConfig());
SSLFileEntry sslFileEntry = new SSLFileEntry();
sslFileEntry.setKeyStoreFilePath("path");
sslFileEntry.setKeyStoreFilePassword("password");
connectionConfig.getSslConfig().setEnabled(true);
connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);
connectionConfig.getJdbcUrlParameters().put("param1", "value1");
connectionConfig.getJdbcUrlParameters().put("param2", "value2");
return connectionConfig;
}

}