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

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify the query has been executed
        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Mock getCurrentSchema method to return a different schema name
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify the connection catalog has been set to new schema name
        PowerMockito.verifyStatic();
        connection.setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Mock getCurrentSchema method to return the same schema name
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify the method does not change the connection catalog
        PowerMockito.verifyStatic(false);
        connection.setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "test_schema";

        // Mock queryForObject method to return the schema name
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        String result = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        // Mock queryForObject method to return the connection id
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(connectionId);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
        when(connection.getClass().getDeclaredConstructor().newInstance()).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(-1);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertNull(result);
    }
}