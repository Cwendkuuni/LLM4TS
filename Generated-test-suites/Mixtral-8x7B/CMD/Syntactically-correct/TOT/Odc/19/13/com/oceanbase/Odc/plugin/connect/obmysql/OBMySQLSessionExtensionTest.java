import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoint;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

private OBMySQLSessionExtension obMySQLSessionExtension;

@Before
public void setup() {
obMySQLSessionExtension = new OBMySQLSessionExtension();
}

@Test
public void testKillQuery() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");
Connection connection = dataSource.getConnection();
obMySQLSessionExtension.killQuery(connection, "123");
assertEquals("KILL QUERY 123", JdbcTemplate.call(connection, "SELECT @@sql_constraints"));
connection.close();
}

@Test
public void testSwitchSchema() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");
Connection connection = dataSource.getConnection();
obMySQLSessionExtension.switchSchema(connection, "testdb");
assertEquals("testdb", obMySQLSessionExtension.getCurrentSchema(connection));
connection.close();
}

@Test
public void testGetCurrentSchema() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");
Connection connection = dataSource.getConnection();
assertEquals("testdb", obMySQLSessionExtension.getCurrentSchema(connection));
connection.close();
}

@Test
public void testGetConnectionId() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");
Connection connection = dataSource.getConnection();
assertNull(obMySQLSessionExtension.getConnectionId(connection));
connection.close();

SingleConnectionDataSource obDataSource = new SingleConnectionDataSource();
obDataSource.setDriverClass(com.oceanbase.jdbc.Driver.class);
obDataSource.setUrl("jdbc:oceanbase://localhost:3306/testdb");
obDataSource.setUsername("testuser");
obDataSource.setPassword("testpassword");
Connection obConnection = obDataSource.getConnection();
assertEquals("12345", obMySQLSessionExtension.getConnectionId(obConnection));
obConnection.close();
}

private static class MockOBMySQLSessionExtension implements OBMySQLSessionExtension, ExtensionPoint {

@Override
public void killQuery(Connection connection, String connectionId) {
// Do nothing
}

@Override
public void switchSchema(Connection connection, String schemaName) throws SQLException {
// Do nothing
}

@Override
public String getCurrentSchema(Connection connection) {
return "testdb";
}

@Override
public String getConnectionId(Connection connection) {
return "12345";
}
}
}