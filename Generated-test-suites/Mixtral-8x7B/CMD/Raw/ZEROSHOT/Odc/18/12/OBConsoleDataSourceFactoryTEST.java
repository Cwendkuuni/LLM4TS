import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils.ValueMapper;
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

import lombok.NonNull;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("testHost");
 connectionConfig.setPort(1234);
 connectionConfig.setDefaultSchema("testSchema");

 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 parameters.put("param2", "value2");
 connectionConfig.setParameters(parameters);

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 connectionConfig.setSslConfig(sslConfig);

 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 sslConfig.setSslFileEntry(sslFileEntry);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:oracle:@testHost:1234:testSchema?param1=value1&param2=value2&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=keystore.p12&trustStorePassword=password&keyStore=keystore.p12&keyStorePassword=password&trustServerCertificate=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
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
 assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 assertEquals("jdbc:oceanbase:oracle:@testHost:1234:testSchema?param1=value1&param2=value2&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=keystore.p12&trustStorePassword=password&keyStore=keystore.p12&keyStorePassword=password&trustServerCertificate=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false",
 dataSource.getUrl());
 }

 @Test
 public void testResetUsername() {
 ValueMapper<String> mapper = new ValueMapper<String>() {
 @Override
 public String map(String value) {
 return "newUser";
 }
 };
 dataSourceFactory.resetUsername(mapper);
 assertEquals("newUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 ValueMapper<String> mapper = new ValueMapper<String>() {
 @Override
 public String map(String value) {
 return "newPassword";
 }
 };
 dataSourceFactory.resetPassword(mapper);
 assertEquals("newPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 ValueMapper<String> mapper = new ValueMapper<String>() {
 @Override
 public String map(String value) {
 return "newHost";
 }
 };
 dataSourceFactory.resetHost(mapper);
 assertEquals("newHost", dataSourceFactory.connectionConfig.getHost());
 }

 @Test
 public void testResetPort() {
 ValueMapper<Integer> mapper = new ValueMapper<Integer>() {
 @Override
 public Integer map(Integer value) {
 return 5678;
 }
 };
 dataSourceFactory.resetPort(mapper);
 assertEquals(Integer.valueOf(5678), dataSourceFactory.connectionConfig.getPort());
 }

 @Test
 public void testResetSchema() {
 ValueMapper<String> mapper = new ValueMapper<String>() {
 @Override
 public String map(String value) {
 return "newSchema";
 }
 };
 dataSourceFactory.resetSchema(mapper);
 assertEquals("newSchema", dataSourceFactory.connectionConfig.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "newValue1");
 newParameters.put("param3", "newValue3");

 ValueMapper<Map<String, String>> mapper = new ValueMapper<Map<String, String>>() {
 @Override
 public Map<String, String> map(Map<String, String> value) {
 return newParameters;
 }
 };

 dataSourceFactory.resetParameters(mapper);

 Map<String, String> parameters = dataSourceFactory.connectionConfig.getParameters();
 assertEquals(2, parameters.size());
 assertTrue(parameters.containsKey("param1"));
 assertEquals("newValue1", parameters.get("param1"));
 assertTrue(parameters.containsKey("param3"));
 assertEquals("newValue3", parameters.get("param3"));
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

 assertEquals(dataSourceFactory.connectionConfig.getDialectType(), copy.connectionConfig.getDialectType());
 assertEquals(dataSourceFactory.connectionConfig.getUsername(), copy.connectionConfig.getUsername());
 assertEquals(dataSourceFactory.connectionConfig.getPassword(), copy.connectionConfig.getPassword());
 assertEquals(dataSourceFactory.connectionConfig.getHost(), copy.connectionConfig.getHost());
 assertEquals(dataSourceFactory.connectionConfig.getPort(), copy.connectionConfig.getPort());
 assertEquals(dataSourceFactory.connectionConfig.getDefaultSchema(), copy.connectionConfig.getDefaultSchema());
 assertEquals(dataSourceFactory.connectionConfig.getParameters(), copy.connectionConfig.getParameters());
 assertEquals(dataSourceFactory.connectionConfig.getSslConfig(), copy.connectionConfig.getSslConfig());
 assertTrue(dataSourceFactory.autoCommit == copy.autoCommit);
 }

}