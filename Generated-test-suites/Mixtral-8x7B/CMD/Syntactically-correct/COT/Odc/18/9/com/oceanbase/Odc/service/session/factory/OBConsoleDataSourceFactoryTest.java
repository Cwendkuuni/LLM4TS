import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

	@Test
	public void testGetJdbcUrl() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
		String jdbcUrl = factory.getJdbcUrl();
		assertEquals("jdbc:oceanbase:mysql://localhost:3306/testdb?param1=value1", jdbcUrl);
	}

	@Test
	public void testGetUsername() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_ORACLE);
		connectionConfig.setUsername("testuser");
		connectionConfig.setTenantName("testtenant");
		connectionConfig.setClusterName("testcluster");

		String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
		assertEquals("\"testuser\"@testtenant#testcluster", username);
	}

	@Test
	public void testGetPassword() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setPassword("testpassword");

		String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
		assertEquals("testpassword", password);
	}

	@Test
	public void testGetJdbcParams() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setEndpoint(new OBTenantEndpoint("proxyhost", 8080));
		SSLConfig sslConfig = new SSLConfig();
		sslConfig.setEnabled(true);
		SSLFileEntry sslFileEntry = new SSLFileEntry();
		sslFileEntry.setKeyStoreFilePath("keystorefile");
		sslFileEntry.setKeyStoreFilePassword("keystorepassword");
		sslConfig.setSslFileEntry(sslFileEntry);
		connectionConfig.setSslConfig(sslConfig);

		Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
		assertEquals("true", jdbcParams.get("useSSL"));
		assertEquals("true", jdbcParams.get("trustServerCertificate"));
		assertEquals("keystorefile", jdbcParams.get("trustStore"));
		assertEquals("keystorepassword", jdbcParams.get("trustStorePassword"));
		assertEquals("keystorefile", jdbcParams.get("keyStore"));
		assertEquals("keystorepassword", jdbcParams.get("keyStorePassword"));
		assertEquals("8080", jdbcParams.get("socksProxyPort"));
	}

	@Test
	public void testGetDataSource() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
		DataSource dataSource = factory.getDataSource();

		assertEquals("localhost", ((SingleConnectionDataSource) dataSource).getHost());
		assertEquals(3306, ((SingleConnectionDataSource) dataSource).getPort());
		assertEquals("testdb", ((SingleConnectionDataSource) dataSource).getDatabaseName());
		assertEquals("jdbc:oceanbase:mysql://localhost:3306/testdb?param1=value1",
				((SingleConnectionDataSource) dataSource).getUrl());
	}

	@Test
	public void testGetDialectType() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
		assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
	}

	@Test
	public void testDeepCopy() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
		CloneableDataSourceFactory clonedFactory = factory.deepCopy();

		assertEquals(factory.getUsername(), clonedFactory.getUsername());
		assertEquals(factory.getPassword(), clonedFactory.getPassword());
		assertEquals(factory.getHost(), clonedFactory.getHost());
		assertEquals(factory.getPort(), clonedFactory.getPort());
		assertEquals(factory.getDefaultSchema(), clonedFactory.getDefaultSchema());
		assertEquals(factory.getJdbcUrl(), clonedFactory.getJdbcUrl());
	}

	@Test
	public void testResetUsername() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("oldusername");
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		factory.resetUsername(value -> "newusername");

		assertEquals("newusername", factory.getUsername());
	}

	@Test
	public void testResetPassword() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("testuser");
		connectionConfig.setPassword("oldpassword");
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		factory.resetPassword(value -> "newpassword");

		assertEquals("newpassword", factory.getPassword());
	}

	@Test
	public void testResetHost() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("testuser");
		connectionConfig.setPassword("testpassword");
		connectionConfig.setHost("oldhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		factory.resetHost(value -> "newhost");

		assertEquals("newhost", factory.getHost());
	}

	@Test
	public void testResetPort() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("testuser");
		connectionConfig.setPassword("testpassword");
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		factory.resetPort(value -> 4444);

		assertEquals(4444, factory.getPort());
	}

	@Test
	public void testResetSchema() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("testuser");
		connectionConfig.setPassword("testpassword");
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("oldschema");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(parameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		factory.resetSchema(value -> "newschema");

		assertEquals("newschema", factory.getDefaultSchema());
	}

	@Test
	public void testResetParameters() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setUsername("testuser");
		connectionConfig.setPassword("testpassword");
		connectionConfig.setHost("localhost");
		connectionConfig.setPort(3306);
		connectionConfig.setDefaultSchema("testdb");
		Map<String, String> oldParameters = new HashMap<>();
		oldParameters.put("param1", "value1");
		connectionConfig.setJdbcUrlParameters(oldParameters);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

		Map<String, String> newParameters = new HashMap<>();
		newParameters.put("param2", "value2");

		factory.resetParameters(value -> newParameters);

		assertEquals("value2", factory.getJdbcUrlParameters().get("param2"));
		assertNull(factory.getJdbcUrlParameters().get("param1"));
	}

	@Test
	public void testGetConnectType() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setType(ConnectType.DIRECT);

		OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
		assertEquals(ConnectType.DIRECT, factory.getConnectType());
	}

	@Test
	public void testGetDefaultSchema() {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		connectionConfig.setDialectType(DialectType.OB_MYSQL);
		connectionConfig.setDefaultSchema("myschema");

		assertEquals("myschema", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
	}

}