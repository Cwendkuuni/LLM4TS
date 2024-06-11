import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");

        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("paramKey1", "paramValue1");
        jdbcParams.put("paramKey2", "paramValue2");
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        when(connectionExtensionPoint.generateJdbcUrl(
                "localhost",
                3306,
                "testSchema",
                jdbcParams
        )).thenReturn("jdbc:oceanbase:mySQL://localhost:3306/testSchema?paramKey1=paramValue1&paramKey2=paramValue2");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obConsoleDataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oceanbase:mySQL://localhost:3306/testSchema?paramKey1=paramValue1&paramKey2=paramValue2";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser";
        assertEquals(expectedUsername, obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        assertEquals(expectedPassword, obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        ConnectType expectedConnectType = ConnectType.LONG;
        assertEquals(expectedConnectType, obConsoleDataSourceFactory.getConnectType());
    }
}