
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testschema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=testschema");
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() throws SQLException {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchemaWithEmptySchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), any())).thenReturn("");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("", currentSchema);
    }

    @Test
    public void testGetConnectionIdWithQueryResult() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), any())).thenReturn("123456");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithQueryException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), any())).thenThrow(new SQLException());
        obOracleSessionExtension.getConnectionId(connection);
    }
}
