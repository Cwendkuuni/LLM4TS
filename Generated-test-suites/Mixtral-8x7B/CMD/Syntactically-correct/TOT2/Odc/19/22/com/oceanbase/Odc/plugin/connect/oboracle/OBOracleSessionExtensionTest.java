import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.pf4j.PluginWrapper.PLUGIN_WRAPPER;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    private static final String CONNECTION_ID = "12345";
    private static final String SCHEMA_NAME = "test_schema";
    private static final String CURRENT_SCHEMA = "current_schema";

    @Mock
    private PluginWrapper pluginWrapper;

    @Mock
    private ConnectionHolder connectionHolder;

    @Mock
    private JdbcOperations jdbcOperations;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        when(pluginWrapper.getName()).thenReturn("test");
        when(connectionHolder.getConnection()).thenReturn(createConnection());
        when(JdbcOperationsUtil.getJdbcOperations(connectionHolder.getConnection()))
                .thenReturn(jdbcOperations);

        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setPluginWrapper(pluginWrapper);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connectionHolder.getConnection(), CONNECTION_ID);
        // You should add assertions here based on the expected behavior of the method
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(jdbcOperations.update(anyString())).thenReturn(1);

        obOracleSessionExtension.switchSchema(connectionHolder.getConnection(), SCHEMA_NAME);
        // You should add assertions here based on the expected behavior of the method
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn(CURRENT_SCHEMA);

        String schemaName = obOracleSessionExtension.getCurrentSchema(connectionHolder.getConnection());
        assertEquals(CURRENT_SCHEMA, schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn(CONNECTION_ID);

        String connectionId = obOracleSessionExtension.getConnectionId(connectionHolder.getConnection());
        assertEquals(CONNECTION_ID, connectionId);
    }

    private Connection createConnection() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mockito.Mockito");
        dataSource.setUrl("jdbc:mock://test");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}