import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
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

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() throws Exception {
 connectionExtensionPoint = new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String database,
 Map<String, String> jdbcParams) {
 return "jdbc:mock:" + host + ":" + port + "/" + database + "?" + jdbcParams.toString();
 }

 @Override
 public String getDriverClassName() {
 return "com.oceanbase.odc.service.session.factory.MockDriver";
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return Collections.singletonList(new ConnectionInitializer() {
 @Override
 public void initConnection(SingleConnectionDataSource dataSource) {
 // do nothing
 }
 });
 }
 };

 connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.CONSOLE);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertEquals("jdbc:mock:localhost:3306/testUser@?autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&trustServerCertificate=false&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=",
 jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 assertEquals("testUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("testPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("com.oceanbase.odc.service.session.factory.MockDriver",
 dataSource.getDriverClassName());
 assertEquals("jdbc:mock:localhost:3306/testUser@?autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false&keyStore=&keyStorePassword=&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustStore=&trustStorePassword=&trustServerCertificate=false&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=",
 dataSource.getUrl());
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 assertNull(dataSource.getSchema());
 Map<String, String> expectedJdbcParams = new HashMap<>();
 expectedJdbcParams.put("maxAllowedPacket", "64000000");
 expectedJdbcParams.put("allowMultiQueries", "true");
 expectedJdbcParams.put("connectTimeout", "5000");
 expectedJdbcParams.put("zeroDateTimeBehavior", "DEFAULT_ZERO_DATE_TIME_BEHAVIOR");
 expectedJdbcParams.put("noDatetimeStringSync", "true");
 expectedJdbcParams.put("jdbcCompliantTruncation", "false");
 expectedJdbcParams.put("sendConnectionAttributes", "false");
 expectedJdbcParams.put("allowLoadLocalInfile", "false");
 expectedJdbcParams.put("allowUrlInLocalInfile", "false");
 expectedJdbcParams.put("allowLoadLocalInfileInPath", "");
 expectedJdbcParams.put("autoDeserialize", "false");
 assertEquals(expectedJdbcParams, dataSource.getConnectionProperties());
 }

 @Test
 public void testResetUsername() {
 dataSourceFactory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return value.toUpperCase();
 }
 });
 assertEquals("TESTUSER", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 dataSourceFactory.resetPassword(new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return value.toUpperCase();
 }
 });
 assertEquals("TESTPASSWORD", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 dataSourceFactory.resetHost(new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return value.toUpperCase();
 }
 });
 assertEquals("LOCALHOST", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPort() {
 dataSourceFactory.resetPort(new CloneableDataSourceFactory.ValueMapper<Integer>() {
 @Override
 public Integer map(Integer value) {
 return value + 100;
 }
 });
 assertEquals(new Integer(3316), dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 dataSourceFactory.resetSchema(new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return value.toUpperCase();
 }
 });
 assertNull(dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("key1", "value1");
 newParams.put("key2", "value2");
 dataSourceFactory.resetParameters(new CloneableDataSourceFactory.ValueMapper<Map<String, String>>() {
 @Override
 public Map<String, String> map(Map<String, String> value) {
 newParams.putAll(value);
 return newParams;
 }
 });
 Map<String, String> expectedParams = new HashMap<>();
 expectedParams.putAll(newParams);
 expectedParams.putAll(dataSourceFactory.getJdbcParams(connectionConfig));
 assertEquals(expectedParams, dataSourceFactory.getParameters());
 }

}