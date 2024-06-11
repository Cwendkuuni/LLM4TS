import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.validate.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
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
 public void setup() {
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 connectionConfig = new ConnectionConfig();
 eventPublisher = mock(EventPublisher.class);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 dataSourceFactory.connectionExtensionPoint = connectionExtensionPoint;
 dataSourceFactory.eventPublisher = eventPublisher;
 }

 @Test
 public void testGetJdbcUrl() {
 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn("jdbc:mock://mockHost:mockPort/mockSchema");

 assertEquals("jdbc:mock://mockHost:mockPort/mockSchema", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("username@tenant", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

 assertEquals("jdbc:mock://mockHost:mockPort/mockSchema", dataSource.getUrl());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getDriverClassName());
 assertEquals(true, dataSource.isAutoCommit());
 }

 @Test
 public void testResetUsername() {
 dataSourceFactory.resetUsername(value -> "new_username");

 assertEquals("new_username", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 dataSourceFactory.resetPassword(value -> "new_password");

 assertEquals("new_password", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 dataSourceFactory.resetHost(value -> "new_host");

 verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("new_host"), anyInt(), anyString(), anyMap());
 }

 @Test
 public void testResetPort() {
 dataSourceFactory.resetPort(value -> 1234);

 verify(connectionExtensionPoint, times(1)).generateJdbcUrl(anyString(), eq(1234), anyString(), anyMap());
 }

 @Test
 public void testResetSchema() {
 dataSourceFactory.resetSchema(value -> "new_schema");

 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), eq("new_schema"), anyMap()))
 .thenReturn("jdbc:mock://mockHost:mockPort/new_schema");

 assertEquals("jdbc:mock://mockHost:mockPort/new_schema", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("new_key", "new_value");

 dataSourceFactory.resetParameters(value -> newParameters);

 verify(connectionExtensionPoint, times(1))
 .generateJdbcUrl(anyString(), anyInt(), anyString(), eq(newParameters));
 }
}