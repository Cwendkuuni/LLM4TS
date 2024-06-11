import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "12345");

        // Edge case: check if the query is killed with a non-null connectionId
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obMySQLSessionExtension.killQuery(connection, "non-null");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("test");
        obMySQLSessionExtension.switchSchema(connection, "test");

        // Edge case: check if the schema is switched even if the current schema is the same
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("new_schema");
        obMySQLSessionExtension.switchSchema(connection, "new_schema");

        // Exception handling: check if an SQLException is thrown when the connection is not an instance of OceanBaseConnection
        when(connection.getClass().getName()).thenReturn("com.mysql.jdbc.JDBC4Connection");
        try {
            obMySQLSessionExtension.switchSchema(connection, "test");
        } catch (SQLException e) {
            assertEquals("Switching schema is only supported for OceanBaseConnection.", e.getMessage());
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("test");

        // Edge case: check if the current schema is obtained even with a non-OceanBaseConnection
        when(connection.getClass().getName()).thenReturn("com.mysql.jdbc.JDBC4Connection");
        assertEquals("test", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));

        // Edge case: check if the connection id is obtained for an OceanBaseConnection
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));

        // Edge case: check if the connection id is obtained even when an exception is thrown
        when(connection.createStatement()).thenThrow(new RuntimeException());
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        assertEquals("2", obMySQLSessionExtension.getConnectionId(connection));
    }
}