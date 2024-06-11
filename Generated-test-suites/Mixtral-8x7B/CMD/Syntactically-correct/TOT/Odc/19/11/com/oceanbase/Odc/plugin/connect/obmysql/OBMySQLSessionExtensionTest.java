import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OBMySQLSessionExtensionTest {

@Mock
private Connection connection;

@InjectMocks
private OBMySQLSessionExtension obMySQLSessionExtension;

@BeforeEach
void setUp() {
// Initialize any mocks or variables here
}

@Test
void testKillQuery() throws SQLException {
when(connection.createStatement()).thenReturn(new java.sql.Statement() {
@Override
public void execute(String sql) throws SQLException {
// Do nothing
}
});

obMySQLSessionExtension.killQuery(connection, "1");
}

@Test
void testSwitchSchema() throws SQLException {
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("test");

obMySQLSessionExtension.switchSchema(connection, "test");
assertEquals("test", obMySQLSessionExtension.getCurrentSchema(connection));

obMySQLSessionExtension.switchSchema(connection, "new_test");
assertEquals("new_test", obMySQLSessionExtension.getCurrentSchema(connection));
}

@Test
void testGetCurrentSchema() throws SQLException {
when(connection.createStatement()).thenReturn(new java.sql.Statement() {
@Override
public String executeQuery(String sql) throws SQLException {
return "test";
}
});

assertEquals("test", obMySQLSessionExtension.getCurrentSchema(connection));
}

@Test
void testGetConnectionId() throws SQLException {
when(connection.createStatement()).thenReturn(new java.sql.Statement() {
@Override
public String executeQuery(String sql) throws SQLException {
return null;
}
});

when(connection instanceof OceanBaseConnection).thenReturn(true);

obMySQLSessionExtension.getConnectionId(connection);
}
}