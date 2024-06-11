import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() {
        obMySQLSessionExtension.killQuery(null, "connectionId");
        // No operation should be performed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        obMySQLSessionExtension.switchSchema(null, "schemaName");
        // No operation should be performed
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verifyNoInteractions(connection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.createStatement().executeQuery("SELECT DATABASE()")).thenReturn(
                Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().getResultSet().next()).thenReturn(true);
        Mockito.when(connection.createStatement().getResultSet().getString(1)).thenReturn(schemaName);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "12345";
        Mockito.when(connection.createStatement().executeQuery("select connection_id()"))
                .thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().getResultSet().next()).thenReturn(true);
        Mockito.when(connection.createStatement().getResultSet().getString(1)).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        Mockito.when(connection.createStatement().executeQuery("select connection_id()"))
                .thenThrow(Mockito.mock(SQLException.class));
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);
        assertEquals("12345", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
}