import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoint;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private JdbcOperations jdbcOperations;
    private Connection connection;
    private AbstractDataSource dataSource;

    @Before
    public void setUp() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        dataSource = mock(AbstractDataSource.class);

        obMySQLSessionExtension = new OBMySQLSessionExtension();

        when(dataSource.getConnection()).thenReturn(connection);
        when(jdbcOperations.getJdbcTemplate()).thenReturn(dataSource);
        when(jdbcOperations.queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/test_schema");
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "1234");
        verify(jdbcOperations, times(1)).execute("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "new_schema");
        verify(connection, times(1)).setCatalog("new_schema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/test_schema?connectionId=1234");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertEquals("1234", connectionId);

        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/test_schema");
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertNotNull(connectionId);
    }
}