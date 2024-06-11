import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OnlineSchemaChangeParametersTest {

@Data
@EqualsAndHashCode(callSuper = false)
private static class TestOnlineSchemaChangeParameters extends OnlineSchemaChangeParameters {
@Override
public List<OnlineSchemaChangeScheduleTaskParameters> generateSubTaskParameters(ConnectionConfig connectionConfig,
String schema) {
throw new UnsupportedOperationException("This is a test class and should not be used for generating subtasks.");
}
}

@Test
public void testGettersAndSetters() {
TestOnlineSchemaChangeParameters params = new TestOnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
params.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255))");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(300);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
params.setDelimiter(";");
params.setLockUsers(List.of("user1", "user2"));
params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLE);

assertEquals(OnlineSchemaChangeSqlType.CREATE, params.getSqlType());
assertEquals("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255))", params.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
assertEquals(Integer.valueOf(300), params.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
assertEquals(";", params.getDelimiter());
assertEquals(List.of("user1", "user2"), params.getLockUsers());
assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLE, params.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
TestOnlineSchemaChangeParameters params = new TestOnlineSchemaChangeParameters();
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(params.isContinueOnError());

params.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
assertFalse(params.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters_happyPath() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

TestOnlineSchemaChangeParameters params = new TestOnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE test ADD COLUMN description VARCHAR(255)");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(300);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
params.setDelimiter(";");
params.setLockUsers(List.of("user1", "user2"));
params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLE);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
schema);

assertEquals(1, subTaskParameters.size());
 assertEquals(schema, subTaskParameters.get(0).getDatabaseName());
 assertEquals("test", subTaskParameters.get(0).getOriginTableName());
 assertEquals("description", subTaskParameters.get(0).getNewColumnName());
 assertEquals("VARCHAR(255)", subTaskParameters.get(0).getNewColumnType());
}

@Test
public void testGenerateSubTaskParameters_exception() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

TestOnlineSchemaChangeParameters params = new TestOnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE test ADD COLUMN description VARCHAR(255) WHERE id = 1");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(300);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
params.setDelimiter(";");
params.setLockUsers(List.of("user1", "user2"));
params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLE);

assertThrows(UnexpectedException.class, () -> params.generateSubTaskParameters(connectionConfig, schema));
}
}