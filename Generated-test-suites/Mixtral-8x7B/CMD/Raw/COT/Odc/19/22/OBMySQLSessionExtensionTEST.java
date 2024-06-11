import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    // Test case for killQuery()
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    // Test case for switchSchema()
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    // Edge case: switchSchema() with the same schema name
    @Test
    public void testSwitchSchemaSameSchemaName() throws SQLException {
        String schemaName = "testSchema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    // Test case for getCurrentSchema()
    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("testSchema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assert "testSchema".equals(currentSchema);
    }

    // Test case for getConnectionId()
    @Test
    public void testGetConnectionId() {
        when(connection.createStatement().executeQuery("select connection_id()")).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assert connectionId != null;
    }
}