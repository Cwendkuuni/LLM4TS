import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("rawtypes")
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;

    private Connection connection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString())).thenReturn(0);

        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString())).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
                .thenReturn("testSchema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any()))
                .thenReturn("sessionid");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNull(connectionId);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123456L);

        connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("123456", connectionId);
    }
}