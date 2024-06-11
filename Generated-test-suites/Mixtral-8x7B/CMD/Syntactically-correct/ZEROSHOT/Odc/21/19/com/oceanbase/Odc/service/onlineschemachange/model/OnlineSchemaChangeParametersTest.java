import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
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
public void testGetSetSqlType() {
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
onlineSchemaChangeParameters.setSqlType(sqlType);
Assert.assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testGetSetSqlContent() {
String sqlContent = "test sql content";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testGetSetErrorStrategy() {
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
Assert.assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testGetSetLockTableTimeOutSeconds() {
Integer lockTableTimeOutSeconds = 10;
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
Assert.assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testGetSetSwapTableNameRetryTimes() {
Integer swapTableNameRetryTimes = 5;
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
Assert.assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testGetSetOriginTableCleanStrategy() {
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
Assert.assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testGetSetDelimiter() {
String delimiter = ",";
onlineSchemaChangeParameters.setDelimiter(delimiter);
Assert.assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testGetSetLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("testUser1");
lockUsers.add("testUser2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testGetSetSwapTableType() {
SwapTableType swapTableType = SwapTableType.TEMPORARY;
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
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
Assert.assertNotNull(subTaskParameters);
}

}