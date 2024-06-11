import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("testdb");

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oceanbase:myql://localhost:3306/testdb?allowMultiQueries=true&allowUrlInLocalInfile=false&allowLoadLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false&connectTimeout=5000&disableSslHostnameVerification=true&jdbcCompliantTruncation=false&maxAllowedPacket=64000000&noDatetimeStringSync=true&sendConnectionAttributes=false&trustServerCertificate=false&trustStore=keystore.p12&trustStorePassword=password&useSSL=true&zeroDateTimeBehavior=DEFAULT";
        when(connectionExtensionPoint.generateJdbcUrl("localhost", 3306, "testdb", new HashMap<>())).thenReturn(expectedJdbcUrl);

        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();

        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        // Test data source creation with default settings
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();

        // Add your assertions here based on the expected properties of the DataSource object
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();

        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = obConsoleDataSourceFactory.deepCopy();

        // Add your assertions here based on the expected properties of the clonedDataSourceFactory object
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newUsername;

        obConsoleDataSourceFactory.resetUsername(valueMapper);

        // Add your assertions here based on the expected updated username
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newPassword;

        obConsoleDataSourceFactory.resetPassword(valueMapper);

        // Add your assertions here based on the expected updated password
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newHost;

        obConsoleDataSourceFactory.resetHost(valueMapper);

        // Add your assertions here based on the expected updated host
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        CloneableDataSourceFactory.ValueMapper<Integer> valueMapper = value -> newPort;

        obConsoleDataSourceFactory.resetPort(valueMapper);

        // Add your assertions here based on the expected updated port
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = value -> newSchema;

        obConsoleDataSourceFactory.resetSchema(valueMapper);

        // Add your assertions here based on the expected updated schema
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key", "value");
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = value -> newParameters;

        obConsoleDataSourceFactory.resetParameters(valueMapper);

        // Add your assertions here based on the expected updated parameters
    }
}