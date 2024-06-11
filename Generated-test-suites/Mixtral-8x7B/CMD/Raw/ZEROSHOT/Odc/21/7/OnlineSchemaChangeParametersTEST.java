import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
Assert.assertEquals(OnlineSchemaChangeSqlType.CREATE, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testSetAndGetSqlContent() {
String sqlContent = "CREATE TABLE test (id INT)";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testSetAndGetErrorStrategy() {
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
Assert.assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testSetAndGetLockTableTimeOutSeconds() {
Integer lockTableTimeOutSeconds = 10;
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
Assert.assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testSetAndGetSwapTableNameRetryTimes() {
Integer swapTableNameRetryTimes = 5;
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
Assert.assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testSetAndGetOriginTableCleanStrategy() {
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
Assert.assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testSetAndGetDelimiter() {
String delimiter = ",";
onlineSchemaChangeParameters.setDelimiter(delimiter);
Assert.assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testSetAndGetLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("testUser");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testSetAndGetSwapTableType() {
SwapTableType swapTableType = SwapTableType.ALTER_SWAP;
onlineSchemaChangeParameters.setSwapTableType(swapTableType);
Assert.assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "testSchema";
onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT)");
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
Assert.assertNotNull(subTaskParameters);
}

}