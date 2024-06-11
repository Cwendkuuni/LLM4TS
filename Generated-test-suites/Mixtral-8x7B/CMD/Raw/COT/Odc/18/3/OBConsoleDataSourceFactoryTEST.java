import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.connectionExtensionPoint = connectionExtensionPoint;
    }

    @Test
    public void testGetJdbcUrl() {
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(
                dataSourceFactory.host, dataSourceFactory.port,
                dataSourceFactory.defaultSchema, dataSourceFactory.parameters))
            .thenReturn("jdbc:mock:test");

        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        Assert.assertEquals("jdbc:mock:test", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        Mockito.when(getDbUser(connectionConfig)).thenReturn("testUser");

        String username = dataSourceFactory.getUsername();

        Assert.assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        Mockito.when(getPassword(connectionConfig)).thenReturn("testPassword");

        String password = dataSourceFactory.getPassword();

        Assert.assertEquals("testPassword", password);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);

        Assert.assertEquals(13, jdbcParams.size());
    }

    @Test
    public void testGetDataSource() {
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn("mockDriver");

        DataSource dataSourceSpy = Mockito.spy(dataSource);
        Mockito.when(dataSourceSpy.getUrl()).thenReturn("mockURL");
        Mockito.when(dataSourceSpy.getUsername()).thenReturn("mockUsername");
        Mockito.when(dataSourceSpy.getPassword()).thenReturn("mockPassword");

        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
            .thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));

        DataSource dataSourceResult = dataSourceFactory.getDataSource();

        Assert.assertEquals("mockURL", dataSourceResult.getUrl());
        Assert.assertEquals("mockUsername", dataSourceResult.getUsername());
        Assert.assertEquals("mockPassword", dataSourceResult.getPassword());
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = DialectType.OB_MYSQL;
        Mockito.when(connectionConfig.getDialectType()).thenReturn(dialectType);

        DialectType result = dataSourceFactory.getDialectType();

        Assert.assertEquals(dialectType, result);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();

        Assert.assertNotSame(dataSourceFactory, deepCopy);
        Assert.assertEquals(dataSourceFactory.autoCommit, deepCopy.autoCommit);
        Assert.assertEquals(dataSourceFactory.connectionConfig, mapper.clone(deepCopy.connectionConfig));
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetUsername(mapper);

        Assert.assertEquals("newTest", dataSourceFactory.username);
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetPassword(mapper);

        Assert.assertEquals("newTest", dataSourceFactory.password);
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetHost(mapper);

        Assert.assertEquals("newTest", dataSourceFactory.host);
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(123)).thenReturn(456);

        dataSourceFactory.resetPort(mapper);

        Assert.assertEquals(Integer.valueOf(456), dataSourceFactory.port);
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetSchema(mapper);

        Assert.assertEquals("newTest", dataSourceFactory.defaultSchema);
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Map<String, String> newParams = new HashMap<>();
        newParams.put("newKey", "newValue");
        Mockito.when(mapper.map(dataSourceFactory.parameters)).thenReturn(newParams);

        dataSourceFactory.resetParameters(mapper);

        Assert.assertEquals(newParams, dataSourceFactory.parameters);
    }

    private static String getDbUser(@NonNull ConnectionConfig connectionConfig) {
        // Implementation omitted for brevity
        return null;
    }

    private static String getPassword(@NonNull ConnectionConfig connectionConfig) {
        // Implementation omitted for brevity
        return null;
    }
}