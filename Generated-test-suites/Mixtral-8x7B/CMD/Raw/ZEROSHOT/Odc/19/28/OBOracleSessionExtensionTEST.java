import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123456";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify that the KILL QUERY statement was executed with the correct connection ID
        // This test is incomplete without an implementation of the JdbcOperationsUtil class
        // or by using a mocking framework to mock its behavior.
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).update(
                "ALTER SESSION SET CURRENT_SCHEMA=test_schema")).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the ALTER SESSION statement was executed with the correct schema name
        // This test is incomplete without an implementation of the JdbcOperationsUtil class
        // or by using a mocking framework to mock its behavior.
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(
                "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("current_schema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("current_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(
                "select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(789012);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("789012", connectionId);
    }

    @Extension
    static class TestOBOracleSessionExtension extends OBOracleSessionExtension {
    }
}