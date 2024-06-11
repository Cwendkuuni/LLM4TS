import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery_validInput() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testKillQuery_nullInput() {
        obMySQLSessionExtension.killQuery(null, null);
        verify(connection, never()).createStatement();
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_invalidInput() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException());
        obMySQLSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema_validInput() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "schemaName");
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema_nullInput() {
        obMySQLSessionExtension.switchSchema(null, null);
        verify(connection, never()).createStatement();
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_invalidInput() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException());
        obMySQLSessionExtension.switchSchema(connection, "schemaName");
    }

    @Test
    public void testGetCurrentSchema_validInput() throws SQLException {
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).createStatement();
        assertEquals("expectedSchemaName", result);
    }

    @Test
    public void testGetCurrentSchema_nullInput() {
        String result = obMySQLSessionExtension.getCurrentSchema(null);
        assertNull(result);
    }

    @Test
    public void testGetConnectionId_validInput() throws SQLException {
        String result = obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
        assertEquals("expectedConnectionId", result);
    }

    @Test
    public void testGetConnectionId_nullInput() {
        String result = obMySQLSessionExtension.getConnectionId(null);
        assertNull(result);
    }
}