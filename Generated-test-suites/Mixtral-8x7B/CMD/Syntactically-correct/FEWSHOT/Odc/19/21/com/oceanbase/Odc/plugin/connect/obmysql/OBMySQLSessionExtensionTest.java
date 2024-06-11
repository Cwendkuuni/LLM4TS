import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("rawtypes")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString()))
                .thenReturn(null);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(schemaName);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(schemaName);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(67890);

        PowerMockito.whenNew(OceanBaseConnection.class).withAnyArguments().thenReturn(oceanBaseConnection);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("67890", connectionId);
    }

    @Test
    public void testGetNullConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(false);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }
}