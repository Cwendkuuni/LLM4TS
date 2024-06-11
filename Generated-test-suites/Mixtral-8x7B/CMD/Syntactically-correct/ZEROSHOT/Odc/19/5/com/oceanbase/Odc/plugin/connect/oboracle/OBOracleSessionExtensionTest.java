import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Assert;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;
import org.pf4j.test.AbstractExtensionTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import static org.junit.Assert.assertEquals;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest<OBOracleSessionExtension> {

@Override
protected Class<? extends Extension> getExtensionClass() {
return OBOracleSessionExtension.class;
}

@Override
protected PluginWrapper createPluginWrapper() {
return new PluginWrapper() {
@Override
public String getPluginId() {
return "test-oboracle-plugin";
}

@Override
public String getPluginVersion() {
return "1.0.0";
}

@Override
public void init(PluginWrapper pluginWrapper, boolean develop) {
if (develop) {
RuntimeMode.setupDevMode();
}
}
};
}

@Test
public void testKillQuery() throws SQLException {
Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "test");
String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
new OBOracleSessionExtension().killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "test");
String schemaName = "test";
new OBOracleSessionExtension().switchSchema(connection, schemaName);
String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
assertEquals(schemaName, currentSchema);
}

@Test
public void testGetCurrentSchema() throws SQLException {
Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "test");
String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
Assert.assertTrue(Objects.nonNull(currentSchema) && !currentSchema.isEmpty());
}

@Test
public void testGetConnectionId() throws SQLException {
Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "test");
String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
Assert.assertTrue(Objects.nonNull(connectionId) && !connectionId.isEmpty());
}
}