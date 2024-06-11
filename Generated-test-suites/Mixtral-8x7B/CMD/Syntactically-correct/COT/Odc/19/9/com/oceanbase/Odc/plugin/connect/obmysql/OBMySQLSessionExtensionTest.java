import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obMySQLSessionExtension;
    private PluginWrapper pluginWrapper;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        pluginWrapper = mock(PluginWrapper.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        PowerMockito.when(pluginWrapper.getPluginId()).thenReturn("test-plugin");
        obMySQLSessionExtension.setPluginWrapper(pluginWrapper);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.killQuery(null, "123"));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.switchSchema(null, "test_schema"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("test_schema");
        String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", schemaName);
    }

    @Test
    public void testGetCurrentSchemaWithException() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenThrow(new RuntimeException());
        String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("test-plugin", schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("123");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new RuntimeException());
        if (connection instanceof OceanBaseConnection) {
            assertEquals(((OceanBaseConnection) connection).getServerThreadId() + "",
                    obMySQLSessionExtension.getConnectionId(connection));
        } else {
            assertEquals(null, obMySQLSessionExtension.getConnectionId(connection));
        }
    }

}