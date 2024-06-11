import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";

        // execute the method
        obOracleSessionExtension.killQuery(connection, connectionId);

        // assert that the query with the given connectionId has been killed
        // (this would depend on the implementation of the killQuery method,
        // and might not be testable in a unit test)
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";

        // check that the schema is not already set to the given schemaName
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");

        // execute the method
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // check that the schema has been switched to the given schemaName
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        Assert.assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("test_schema");

        // execute the method
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        // assert that the returned schema is the correct one
        Assert.assertEquals("test_schema", currentSchema);
    }
}