import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.demo.DemoPlugin;
import org.pf4j.demo.DemoPluginLoader;
import org.pf4j.inject.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class OBMySQLSessionExtensionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBMySQLSessionExtensionTest.class);

    @Injected
    private OBMySQLSessionExtension obmysqlSessionExtension;

    @Test
    public void testKillQuery() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/test?user=root&password=password";
        Connection connection = DriverManager.getConnection(connectionUrl);
        obmysqlSessionExtension.killQuery(connection, "1");
        // Add assertions or verifications here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/test?user=root&password=password";
        Connection connection = DriverManager.getConnection(connectionUrl);
        obmysqlSessionExtension.switchSchema(connection, "test");
        assertEquals("test", obmysqlSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/test?user=root&password=password";
        Connection connection = DriverManager.getConnection(connectionUrl);
        assertEquals("test", obmysqlSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/test?user=root&password=password";
        Connection connection = DriverManager.getConnection(connectionUrl);
        if (connection instanceof OceanBaseConnection) {
            String connectionId = obmysqlSessionExtension.getConnectionId(connection);
            assert connectionId != null;
        } else {
            LOGGER.info("Connection is not an OceanBaseConnection instance");
        }
    }

    public static class TestPlugin extends DemoPlugin {

        @Override
        public void start() {
            LOGGER.info("Test plugin started");
            pluginWrapper = new PluginWrapper(TestPlugin.class);
            pluginLoader = new DemoPluginLoader(pluginWrapper);
            pluginLoader.initialize();
            pluginLoader.startPlugin();
        }

        @Override
        public void stop() {
            LOGGER.info("Test plugin stopped");
            if (pluginLoader != null) {
                pluginLoader.stopPlugin();
                pluginLoader = null;
            }
            if (pluginWrapper != null) {
                pluginWrapper = null;
            }
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                TestPlugin.getInstance().stop();
            }
        });
    }

}