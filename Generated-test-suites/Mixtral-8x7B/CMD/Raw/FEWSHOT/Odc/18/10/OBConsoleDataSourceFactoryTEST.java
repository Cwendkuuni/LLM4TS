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
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setUsername("username");
 connectionConfig.setPassword("password");
 connectionConfig.setHost("host");
 connectionConfig.setPort(1234);
 connectionConfig.setDefaultSchema("schema");

 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("param1", "value1");
 jdbcParams.put("param2", "value2");
 connectionConfig.setJdbcUrlParameters(jdbcParams);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertEquals("jdbc:oceanbase:oracle://host:1234/schema?param1=value1&param2=value2", jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 assertEquals("\"username\"@tenant#cluster", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
 assertEquals(11, jdbcParams.size());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("jdbc:oceanbase:oracle://host:1234/schema?param1=value1&param2=value2", dataSource.getUrl());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals("oceanbase", dataSource.getDriverClassName());
 Properties properties = dataSource.getConnectionProperties();
 assertEquals("false", properties.get("allowLoadLocalInfile"));
 assertEquals("false", properties.get("allowUrlInLocalInfile"));
 assertEquals("", properties.get("allowLoadLocalInfileInPath"));
 assertEquals("false", properties.get("autoDeserialize"));
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
 assertNotEquals(dataSourceFactory, copy);
 assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
 assertEquals(dataSourceFactory.getHost(), copy.getHost());
 assertEquals(dataSourceFactory.getPort(), copy.getPort());
 assertEquals(dataSourceFactory.getDefaultSchema(), copy.getDefaultSchema());
 assertEquals(dataSourceFactory.getJdbcParams(), copy.getJdbcParams());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newUsername;
 dataSourceFactory.resetUsername(mapper);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newPassword;
 dataSourceFactory.resetPassword(mapper);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newHost;
 dataSourceFactory.resetHost(mapper);
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 5678;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> newPort;
 Validate.isTrue(newPort > 0, "Port can not be negative");
 dataSourceFactory.resetPort(mapper);
 assertEquals(newPort, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newSchema;
 dataSourceFactory.resetSchema(mapper);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newJdbcParams = new HashMap<>();
 newJdbcParams.put("param3", "value3");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newJdbcParams;
 dataSourceFactory.resetParameters(mapper);
 assertEquals(1, dataSourceFactory.getJdbcParams().size());
 assertEquals("value3", dataSourceFactory.getJdbcParams().get("param3"));
 }
}