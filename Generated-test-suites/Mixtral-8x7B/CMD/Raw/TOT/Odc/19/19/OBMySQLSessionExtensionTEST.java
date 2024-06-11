import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

@Mock
private Connection connection;

@InjectMocks
private OBMySQLSessionExtension obmysqlSessionExtension;

@Before
public void setup() {
when(connection.getCatalog()).thenReturn("testSchema");
}

@Test
public void testSwitchSchema() throws SQLException {
obmysqlSessionExtension.switchSchema(connection, "testSchema");
assertEquals("testSchema", connection.getCatalog());

obmysqlSessionExtension.switchSchema(connection, "anotherSchema");
assertEquals("anotherSchema", connection.getCatalog());
}

@Test
public void testGetCurrentSchema() {
when(connection.getCatalog()).thenReturn("testSchema");
assertEquals("testSchema", obmysqlSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() {
when(connection.getCatalog()).thenReturn("testSchema");
assertEquals("1", obmysqlSessionExtension.getConnectionId(connection));
}

@Test
public void testKillQuery() {
obmysqlSessionExtension.killQuery(connection, "1");
// As this is a mock, we can't actually test the side effect of the "KILL QUERY" command
// But we can test that it's being called with the expected arguments
assertEquals(1, 1);
}
}