import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(1);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any()))
                .thenReturn("testSchema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any()))
                .thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection)
                .thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId())
                .thenReturn(123456);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        assertEquals("123456", connectionId);
    }

    @Extension
    static class TestOBOracleSessionExtension extends OBOracleSessionExtension {
    }
}