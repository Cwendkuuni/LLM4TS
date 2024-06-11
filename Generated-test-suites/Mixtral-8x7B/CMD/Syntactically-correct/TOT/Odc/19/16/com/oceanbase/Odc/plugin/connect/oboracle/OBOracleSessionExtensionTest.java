import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        // Create a connection
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        
        // Start a query in the background
        Statement statement = connection.createStatement();
        statement.executeQuery("SELECT sleep(5) FROM dual");
        
        // Get the connection id
        String connectionId = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class);
        
        // Kill the query
        OBOracleSessionExtension.killQuery(connection, connectionId);
        
        // Check if the query is killed
        assertThrows(SQLException.class, () -> statement.getResultSet());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Create a connection
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        
        // Get the current schema
        String currentSchema = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
        
        // Switch to a different schema
        OBOracleSessionExtension.switchSchema(connection, "new_schema");
        
        // Check if the schema is switched
        assertEquals("new_schema", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class));
        
        // Switch back to the original schema
        OBOracleSessionExtension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Create a connection
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        
        // Get the current schema
        String currentSchema = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
        
        // Check if the schema is returned correctly
        assertTrue(currentSchema.contains("USER"));
    }
}