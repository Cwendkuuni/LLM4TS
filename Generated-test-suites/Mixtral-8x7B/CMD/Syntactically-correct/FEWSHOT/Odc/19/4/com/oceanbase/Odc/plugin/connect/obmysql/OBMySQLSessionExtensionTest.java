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

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString()))
                .thenReturn(null);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);

        // Verify the KILL QUERY command is executed with the correct connection ID
        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String newSchemaName = "test_schema";
        String originalSchemaName = "original_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(originalSchemaName).thenReturn(newSchemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, newSchemaName);

        // Verify the setCatalog method is called with the new schema name
        PowerMockito.verifyStatic(Connection.class);
        connection.setCatalog(newSchemaName);
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsTheSame() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(schemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);

        // Verify that setCatalog method is not called when the current schema is the same as the target schema
        PowerMockito.verifyStatic(Connection.class, PowerMockito.never());
        connection.setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(),
                PowerMockito.eq(String.class))).thenReturn(schemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getCurrentSchema(connection);

        assertEquals(schemaName, result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdWhenExceptionOccurs() {
        Connection connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenThrow(new RuntimeException());

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);

        assertNull(result);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNotOceanBaseConnection() {
        Connection connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(false);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);

        assertNull(result);
    }
}