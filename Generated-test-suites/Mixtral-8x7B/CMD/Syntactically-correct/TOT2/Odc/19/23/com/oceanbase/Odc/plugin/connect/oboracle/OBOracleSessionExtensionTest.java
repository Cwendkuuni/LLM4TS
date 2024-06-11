import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.ConnectionFactoryUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.pf4j.PluginWrapper.PluginState.NOT_INSTALLED;
import static org.pf4j.PluginWrapper.PluginState.INSTALLED;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private PluginWrapper pluginWrapper;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    private DriverManagerDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        connection = ConnectionFactoryUtils.getConnection(dataSource);

        pluginWrapper.setPluginState(NOT_INSTALLED);
        pluginWrapper.setPluginState(INSTALLED);

        when(jdbcOperations.execute(anyString())).thenReturn(0);
        when(jdbcOperations.update(anyString())).thenReturn(0);
        when(jdbcOperations.queryForObject(anyString(), String.class)).thenReturn("test_schema");
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "123456");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidConnection() throws SQLException {
        when(jdbcOperations.update(anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.switchSchema(connection, "test_schema2");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "test_schema");
    }

    @Test
    public void testSwitchSchemaValidConnection() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "test_schema2");
    }

    @Test
    public void testGetCurrentSchemaValidConnection() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionIdValidConnection() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assert(connectionId != null);
    }
}