package com.oceanbase.odc.plugin.connect.obmysql;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBMySQLSessionExtensionTest {

    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "testuser";
        String password = "testpassword";
        connection = DriverManager.getConnection(url, user, password);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        String connectionId = null;
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() throws SQLException {
        String connectionId = "";
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String schemaName = null;
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidConnection() throws SQLException {
        Connection invalidConnection = null;
        String schemaName = "testSchema";
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.switchSchema(invalidConnection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        String schemaName = "";
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        obj.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        String currentSchema = obj.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithValidConnection() throws SQLException {
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        String currentSchema = obj.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNoSchema() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/";
        Connection connectionWithoutSchema = DriverManager.getConnection(url, "testuser", "testpassword");
        OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
        String currentSchema = obj.getCurrentSchema(connectionWithoutSchema);
        assertEquals("", currentSchema);
    }

}