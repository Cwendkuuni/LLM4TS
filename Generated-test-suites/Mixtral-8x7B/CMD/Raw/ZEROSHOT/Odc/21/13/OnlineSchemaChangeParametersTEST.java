import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;


@Before
public void setUp() {
    onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
    OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
    onlineSchemaChangeParameters.setSqlType(sqlType);
    Assert.assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testGetSqlContent() {
    String sqlContent = "SQL CONTENT";
    onlineSchemaChangeParameters.setSqlContent(sqlContent);
    Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testGetErrorStrategy() {
    TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
    onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
    Assert.assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testIsContinueOnError() {
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    Assert.assertTrue(parameters.isContinueOnError());

    parameters.setErrorStrategy(TaskErrorStrategy.STOP);
    Assert.assertFalse(parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws Exception {
    ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
    Mockito.when(connectionConfig.getDialectType()).thenReturn("dialect");

    String schema = "schema";
    String sqlContent = "SQL CONTENT";
    onlineSchemaChangeParameters.setSqlContent(sqlContent);

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
            .generateSubTaskParameters(connectionConfig, schema);

    Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
    Mockito.verifyNoMoreInteractions(connectionConfig);

    Assert.assertNotNull(subTaskParameters);
    Assert.assertTrue(!subTaskParameters.isEmpty());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws Exception {
    ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
    Mockito.when(connectionConfig.getDialectType()).thenThrow(new RuntimeException("Exception"));

    String schema = "schema";
    onlineSchemaChangeParameters.setSqlContent("SQL CONTENT");

    onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

    Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
    Mockito.verifyNoMoreInteractions(connectionConfig);
}

@Test
public void testGenerateSubTaskParametersWithAlter() throws Exception {
    ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
    Mockito.when(connectionConfig.getDialectType()).thenReturn("dialect");

    String schema = "schema";
    String sqlContent = "ALTER TABLE table ALTER COLUMN column type";
    onlineSchemaChangeParameters.setSqlContent(sqlContent);
    onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
            .generateSubTaskParameters(connectionConfig, schema);

    Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
    Mockito.verifyNoMoreInteractions(connectionConfig);

    Assert.assertNotNull(subTaskParameters);
    Assert.assertTrue(!subTaskParameters.isEmpty());

    OnlineSchemaChangeScheduleTaskParameters parameters = subTaskParameters.get(0);
    Assert.assertEquals("table", parameters.getOriginTableName());
    Assert.assertEquals("schema.table", parameters.getDatabaseName());

    List<String> sqlsToBeExecuted = parameters.getSqlsToBeExecuted();
    Assert.assertNotNull(sqlsToBeExecuted);
    Assert.assertTrue(!sqlsToBeExecuted.isEmpty());

    String newAlterStmt = sqlsToBeExecuted.get(1);
    Assert.assertTrue(newAlterStmt.contains("newTableName"));
}

@Test
public void testSerializeDeserialize() throws Exception {
    OnlineSchemaChangeParameters original = new OnlineSchemaChangeParameters();
    OnlineSchemaChangeParameters copy = OnlineSchemaChangeParameters.deepCopy(original);

    Assert.assertEquals(original, copy);
    Assert.assertEquals(original.hashCode(), copy.hashCode());

    Serializable serialize = OnlineSchemaChangeParameters.serializer().apply(original);
    OnlineSchemaChangeParameters deserialize = OnlineSchemaChangeParameters.deserializer().apply((Serializable) serialize);

    Assert.assertEquals(original, deserialize);
    Assert.assertEquals(original.hashCode(), deserialize.hashCode());
}

@Test
public void testSerializeDeserializeWithNull() throws Exception {
    OnlineSchemaChangeParameters original = null;
    OnlineSchemaChangeParameters copy = OnlineSchemaChangeParameters.deepCopy(original);

    Assert.assertNull(copy);

    Serializable serialize = OnlineSchemaChangeParameters.serializer().apply(original);
    OnlineSchemaChangeParameters deserialize = OnlineSchemaChangeParameters.deserializer().apply((Serializable) serialize);

    Assert.assertNull(deserialize);
}

@Test
public void testDeepCopy() throws Exception {
    OnlineSchemaChangeParameters original = new OnlineSchemaChangeParameters();
    OnlineSchemaChangeParameters copy = OnlineSchemaChangeParameters.deepCopy(original);

    Assert.assertNotSame(original, copy);
    Assert.assertEquals(original, copy);
    Assert.assertEquals(original.hashCode(), copy.hashCode());
}

@Test
public void testLockTableTimeOutSeconds() {
    Integer lockTableTimeOutSeconds = 10;
    onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
    Assert.assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testSwapTableNameRetryTimes() {
    Integer swapTableNameRetryTimes = 5;
    onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
    Assert.assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testOriginTableCleanStrategy() {
    OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy = OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP;
    onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
    Assert.assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testDelimiter() {
    String delimiter = ",";
    onlineSchemaChangeParameters.setDelimiter(delimiter);
    Assert.assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testLockUsers() {
    List<String> lockUsers = new ArrayList<>();
    lockUsers.add("user1");
    lockUsers.add("user2");

    onlineSchemaChangeParameters.setLockUsers(lockUsers);
    Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testSwapTableType() {
    OnlineSchemaChangeParameters.SwapTableType swapTableType = OnlineSchemaChangeParameters.SwapTableType.AUTO;
    onlineSchemaChangeParameters.setSwapTableType(swapTableType);
    Assert.assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
}

}