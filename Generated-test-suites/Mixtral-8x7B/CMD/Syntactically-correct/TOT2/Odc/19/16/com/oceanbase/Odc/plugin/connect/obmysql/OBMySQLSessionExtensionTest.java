import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        assertEquals("KILL QUERY " + connectionId, JdbcOperationsUtil.getLastExecutedQuery(connection));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getCatalog()).thenReturn("testSchema");
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        assertEquals("testSchema", JdbcOperationsUtil.getLastExecutedQuery(connection));
    }

    @Test
    public void testSwitchSchemaToSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getCatalog()).thenReturn("testSchema");
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        assertEquals(0, JdbcOperationsUtil.getLastExecutedQuery(connection).length());
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertEquals("12345", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("java.sql.Connection");
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.getConnectionId(connection));
    }
}