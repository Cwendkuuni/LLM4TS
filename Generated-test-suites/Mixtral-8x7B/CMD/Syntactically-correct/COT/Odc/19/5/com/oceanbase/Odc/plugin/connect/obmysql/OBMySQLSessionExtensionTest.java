import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(null, "connectionId"));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "schemaName"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithEqualSchemas() throws SQLException {
        String schemaName = "currentSchema";
        PowerMockito.doReturn(schemaName).when(connection, "getCatalog");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        PowerMockito.doReturn(currentSchema).when(connection, "createStatement").callRealMethod();

        String result = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "123456";
        PowerMockito.doReturn(connectionId).when(connection, "createStatement").callRealMethod();

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        long serverThreadId = 123456;
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(serverThreadId);

        String result = obMySQLSessionException.getConnectionId(oceanBaseConnection);

        assertEquals("" + serverThreadId, result);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getConnectionId(null));
    }
}