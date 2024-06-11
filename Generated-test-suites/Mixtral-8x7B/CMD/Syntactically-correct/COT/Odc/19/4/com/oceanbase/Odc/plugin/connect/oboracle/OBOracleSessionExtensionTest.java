import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    // Test for killQuery method

    @Test
    public void testKillQueryConnectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(null, "connectionId"));
    }

    @Test
    public void testKillQueryConnectionIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    // Test for switchSchema method

    @Test
    public void testSwitchSchemaConnectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(null, "schemaName"));
    }

    @Test
    public void testSwitchSchemaSchemaNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName"))
                .thenReturn(1);
        obOracleSessionExtension.switchSchema(connection, "schemaName");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schemaName");
        obOracleSessionExtension.switchSchema(connection, "schemaName");
    }

    // Test for getCurrentSchema method

    @Test
    public void testGetCurrentSchemaConnectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("schemaName");
        assertEquals("schemaName", obOracleSessionExtension.getCurrentSchema(connection));
    }

    // Test for getConnectionId method

    @Test
    public void testGetConnectionIdConnectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("connectionId");
        assertEquals("connectionId", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdException() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals("0", obOracleSessionExtension.getConnectionId(connection));
    }
}