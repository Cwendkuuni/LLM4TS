import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oceanbase.jdbc.OceanBaseDriver;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.config.OBMySQLPluginConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    private static final String VALID_CONNECTION_ID = "1";
    private static final String INVALID_CONNECTION_ID = "-1";
    private static final String EXISTING_SCHEMA_NAME = "test_schema";
    private static final String NON_EXISTING_SCHEMA_NAME = "non_existing_schema";
    private static final String VALID_QUERY_NAME = "KILL QUERY " + VALID_CONNECTION_ID;
    private static final String INVALID_QUERY_NAME = "KILL QUERYX " + VALID_CONNECTION_ID;

    private SessionExtensionPoint obj;
    private Connection connection;

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(OBMySQLPluginConfiguration.class);
        context.setId("test-context");
        context.getBeanFactory().registerSingleton("pluginWrapper", new PluginWrapper() {{
            setPluginId("obmysql");
            setPluginVersion("4.2.0");
            setPluginState(PluginWrapper.PluginState.STARTED);
            setRuntimeMode(RuntimeMode.CLASSPATH);
        }});
        OceanBaseDriver oceanBaseDriver = new OceanBaseDriver();
        try {
            connection = oceanBaseDriver.connect("jdbc:oceanbase://localhost:port", "user", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        obj = context.getBean(OBMySQLSessionExtension.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        // Test with a valid query
        obj.killQuery(connection, VALID_CONNECTION_ID);
        // Add assertions here to check if the query was killed
    }

    @Test
    public void testKillQueryWithInvalidQuery() {
        // Test with an invalid query
        try {
            obj.killQuery(connection, INVALID_CONNECTION_ID);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testKillQueryWithNonExistingMethodName() {
        // Test with a non-existing method name in the query
        try {
            obj.killQuery(connection, INVALID_QUERY_NAME);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test with a valid schema name
        obj.switchSchema(connection, EXISTING_SCHEMA_NAME);
        // Add assertions here to check if the schema was switched
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        // Test with a null connection
        Connection nullConnection = null;
        try {
            obj.switchSchema(nullConnection, EXISTING_SCHEMA_NAME);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        // Test with a null schema name
        try {
            obj.switchSchema(connection, null);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() {
        // Test with an empty schema name
        try {
            obj.switchSchema(connection, "");
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchemaWithSchemaNameWithSpecialCharacters() {
        // Test with a schema name that contains special characters
        String schemaNameWithSpecialCharacters = "test_schema!@#";
        try {
            obj.switchSchema(connection, schemaNameWithSpecialCharacters);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchemaWithReadOnlyUser() throws SQLException {
        // Test with a read-only user account
        Connection readOnlyConnection = mock(Connection.class);
        when(readOnlyConnection.setCatalog(EXISTING_SCHEMA_NAME)).thenThrow(new SQLException("Read-only user"));
        try {
            obj.switchSchema(readOnlyConnection, EXISTING_SCHEMA_NAME);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testSwitchSchemaWithNonExistentSchema() throws SQLException {
        // Test with a non-existent schema
        try {
            obj.switchSchema(connection, NON_EXISTING_SCHEMA_NAME);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Test with a valid connection
        String currentSchema = obj.getCurrentSchema(connection);
        assertEquals(EXISTING_SCHEMA_NAME, currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        // Test with a null connection
        Connection nullConnection = null;
        String currentSchema = obj.getCurrentSchema(nullConnection);
        assertNull(currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithReadOnlyUser() throws SQLException {
        // Test with a read-only user account
        Connection readOnlyConnection = mock(Connection.class);
        when(readOnlyConnection.createStatement().executeQuery("SELECT DATABASE()")).thenThrow(new SQLException("Read-only user"));
        try {
            obj.getCurrentSchema(readOnlyConnection);
        } catch (SQLException e) {
            // Add assertions here to check if the expected SQLException is thrown
        }
    }
}