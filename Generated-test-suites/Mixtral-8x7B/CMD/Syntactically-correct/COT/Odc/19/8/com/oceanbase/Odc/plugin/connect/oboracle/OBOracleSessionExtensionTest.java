import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "queryId");
        // Add assertions if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");

        obOracleSessionExtension.switchSchema(connection, "schema2");

        // Add assertions if needed
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema1");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
        when(connection.getClass().getName().contains("OceanBaseConnection")).thenReturn(true);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNull(connectionId);
    }
}