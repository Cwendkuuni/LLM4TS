import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("currentSchema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("currentSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(67890);
        when(connection.getClass().equals(oceanBaseConnection.getClass())).thenReturn(true);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("67890", connectionId);
    }

    @Test
    public void testGetConnectionId_WithNormalCondition() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("connectionId");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("connectionId", connectionId);
    }

    @Test
    public void testGetConnectionId_WithNullConnection() {
        String connectionId = obOracleSessionExtension.getConnectionId(null);
        assertNull(connectionId);
    }
}