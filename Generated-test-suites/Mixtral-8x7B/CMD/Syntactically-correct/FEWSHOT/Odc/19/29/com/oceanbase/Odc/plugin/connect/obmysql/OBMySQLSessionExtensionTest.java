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
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@org.powermock.modules.junit4.PowerMockRunner
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;

    private Connection connection;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString()))
                .thenReturn(null);

        obMySQLSessionExtension.killQuery(connection, connectionId);

        String expectedQuery = "KILL QUERY " + connectionId;
        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute(expectedQuery);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = "newSchema";
        String currentSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, newSchema);

        PowerMockito.verifyStatic(Connection.class);
        connection.setCatalog(newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "expectedSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection)
                .thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_JdbcOperationsSuccess() throws SQLException {
        String expectedConnectionId = "456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(expectedConnectionId);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_ConnectionNotOceanBaseConnection() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(false);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertNull(actualConnectionId);
    }
}