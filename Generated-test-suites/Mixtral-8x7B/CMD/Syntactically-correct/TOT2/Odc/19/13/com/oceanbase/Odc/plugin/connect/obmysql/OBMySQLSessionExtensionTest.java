import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    private Connection connection;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        jdbcOperationsUtil.execute(anyString());
        assertEquals(connectionId, "KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        jdbcOperationsUtil.execute("USE " + schemaName);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("12345");
        assertEquals("12345", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        if (connection instanceof OceanBaseConnection) {
            assertEquals("Thread-" + ((OceanBaseConnection) connection).getServerThreadId(), obMySQLSessionExtension.getConnectionId(connection));
        }
    }

    // More test cases to be added
}