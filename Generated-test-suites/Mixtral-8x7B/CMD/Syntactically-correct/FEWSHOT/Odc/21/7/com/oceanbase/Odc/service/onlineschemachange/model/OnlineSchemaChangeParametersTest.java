import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
// set connectionConfig properties
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT";
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
Integer lockTableTimeOutSeconds = 30;
Integer swapTableNameRetryTimes = 5;
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.BACKUP_AND_DELETE;
String delimiter = ";";
List<String> lockUsers = new ArrayList<>();
SwapTableType swapTableType = SwapTableType.SWAP_WITH_SAME_NAME;

return new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, lockTableTimeOutSeconds,
swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);
}

@Test
public void testGenerateSubTaskParameters() {
OnlineSchemaChangeParameters parameters = createParameters();
ConnectionConfig connectionConfig = new ConnectionConfig();
// set connectionConfig properties
String schema = "test_schema";
List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTasks.size());
OnlineSchemaChangeScheduleTaskParameters subTask = subTasks.get(0);
assertEquals("test_schema", subTask.getDatabaseName());
assertEquals("table1", subTask.getOriginTableName());
assertEquals("c1", subTask.getNewColumnName());
assertEquals(1, subTask.getSqlsToBeExecuted().size());
assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", subTask.getSqlsToBeExecuted().get(0));
}

@Test
public void testGetters() {
OnlineSchemaChangeParameters parameters = createParameters();
assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
assertEquals(sqlContent, parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(30, parameters.getLockTableTimeOutSeconds().intValue());
assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());
assertEquals(OriginTableCleanStrategy.BACKUP_AND_DELETE, parameters.getOriginTableCleanStrategy());
assertEquals(";", parameters.getDelimiter());
assertEquals(lockUsers, parameters.getLockUsers());
assertEquals(SwapTableType.SWAP_WITH_SAME_NAME, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = createParameters();
assertTrue(parameters.isContinueOnError());
}

private void assertTrue(boolean b) {
if (!b) {
throw new RuntimeException("Assertion failed");
}
}

private void assertEquals(Integer expected, Integer actual) {
if (!expected.equals(actual)) {
throw new RuntimeException(String.format("Expected %s, but was %s", expected, actual));
}
}

private void assertEquals(String expected, Object actual) {
if (!expected.equals(actual)) {
throw new RuntimeException(String.format("Expected %s, but was %s", expected, actual));
}
}

private void assertEquals(List<String> expected, List<String> actual) {
if (!expected.equals(actual)) {
throw new RuntimeException(String.format("Expected %s, but was %s", expected, actual));
}
}
}