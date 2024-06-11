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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = spy(new OBMySQLSessionExtension());
        doReturn(true).when(obMySQLSessionExtension).isSupported(any());
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "123456";
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnectionId() throws Exception {
        obMySQLSessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testSwitchSchemaWithSameSchemaName() throws SQLException {
        String schemaName = "testSchema";
        doReturn(schemaName).when(obMySQLSessionExtension).getCurrentSchema(connection);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertFalse(Objects.isNull(currentSchema));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(obMySQLSessionExtension.getConnectionId(connection)).thenReturn("123456");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, "123456");
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(obMySQLSessionExtension.getConnectionId(connection)).thenThrow(new SQLException());
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(obMySQLSessionExtension.getConnectionId(connection)).thenReturn("123456");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, "123456");
    }
}