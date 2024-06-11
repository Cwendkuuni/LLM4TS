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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private PluginWrapper pluginWrapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.oceanbase.jdbc.OceanBaseDriver");
        dataSource.setUrl("jdbc:oceanbase:thin://@127.0.0.1:2881/orcl");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setAutoCommit(false);

        connection = dataSource.getConnection();
        JdbcOperationsUtil.setJdbcOperations(connection, jdbcTemplate);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add assertions here based on the expected behavior of the method
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add assertions here based on the expected behavior of the method
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        // Add assertions here based on the expected behavior of the method
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        // Add assertions here based on the expected behavior of the method
    }

    // Add edge cases and exception handling scenarios tests here

}