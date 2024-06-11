import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private Connection connection;

 @Mock
 private ExtensionPoint extensionPoint;

 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtension = spy(new OBOracleSessionExtension());

 private String originalSchema;

 @Before
 public void setup() {
 originalSchema = "original_schema";
 doReturn(originalSchema).when(obOracleSessionExtension).getCurrentSchema(any(Connection.class));
 }

 // Test cases for each method will be added here

}

public class TestKillQuery {

 @Test
 public void testKillQuery() {
 String connectionId = "12345";
 obOracleSessionExtension.killQuery(connection, connectionId);
 verify(obOracleSessionExtension, times(1))
 .getJdbcOperations(any(Connection.class))
 .execute(anyString());
 }

 @Test
 public void testKillQueryWithNullConnection() {
 String connectionId = "12345";
 obOracleSessionExtension.killQuery(null, connectionId);
 verify(obOracleSessionExtension, times(0))
 .getJdbcOperations(any(Connection.class))
 .execute(anyString());
 }

 @Test
 public void testKillQueryWithNullConnectionId() {
 obOracleSessionExtension.killQuery(connection, null);
 verify(obOracleSessionExtension, times(0))
 .getJdbcOperations(any(Connection.class))
 .execute(anyString());
 }
}

public class TestSwitchSchema {

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "new_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 verify(obOracleSessionExtension, times(1))
 .getJdbcOperations(any(Connection.class))
 .update(anyString());
 }

 @Test
 public void testSwitchSchemaWithSameSchema() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, originalSchema);
 verify(obOracleSessionExtension, times(0))
 .getJdbcOperations(any(Connection.class))
 .update(anyString());
 }

 @Test
 public void testSwitchSchemaWithNullSchema() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, null);
 verify(obOracleSessionExtension, times(0))
 .getJdbcOperations(any(Connection.class))
 .update(anyString());
 }

 @Test
 public void testSwitchSchemaWithNullConnection() throws SQLException {
 obOracleSessionExtension.switchSchema(null, originalSchema);
 verify(obOracleSessionExtension, times(0))
 .getJdbcOperations(any(Connection.class))
 .update(anyString());
 }
}

public class TestGetCurrentSchema {

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = "current_schema";
 doReturn(currentSchema).when(obOracleSessionExtension).queryForObject(anyString(), any());
 assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetCurrentSchemaWithException() {
 doReturn(null).when(obOracleSessionExtension).queryForObject(anyString(), any());
 assertEquals(originalSchema, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetCurrentSchemaWithNullConnection() {
 assertEquals(originalSchema, obOracleSessionExtension.getCurrentSchema(null));
 }
}

public class TestGetConnectionId {

 @Test
 public void testGetConnectionId() {
 String connectionId = "connection_id";
 doReturn(connectionId).when(obOracleSessionExtension).queryForObject(anyString(), any());
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithException() {
 doReturn(null).when(obOracleSessionExtension).queryForObject(anyString(), any());
 assertEquals(null, obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() {
 long threadId = 12345;
 doReturn(null).when(obOracleSessionExtension).queryForObject(anyString(), any());
 doReturn(threadId).when(connection).getServerThreadId();
 assertEquals(String.valueOf(threadId), obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithNullConnection() {
 assertEquals(null, obOracleSessionExtension.getConnectionId(null));
 }
}