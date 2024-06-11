import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType;

public class OnlineSchemaChangeParametersTest {

@Test
public void testOnlineSchemaChangeSqlType() {
assertEquals(OnlineSchemaChangeSqlType.ALTER, OnlineSchemaChangeSqlType.valueOf("ALTER"));
assertEquals(OnlineSchemaChangeSqlType.CREATE, OnlineSchemaChangeSqlType.valueOf("CREATE"));
assertEquals(OnlineSchemaChangeSqlType.DROP, OnlineSchemaChangeSqlType.valueOf("DROP"));
}

@Test
public void testTaskErrorStrategy() {
assertEquals(TaskErrorStrategy.CONTINUE, TaskErrorStrategy.valueOf("CONTINUE"));
assertEquals(TaskErrorStrategy.ROLLBACK, TaskErrorStrategy.valueOf("ROLLBACK"));
assertEquals(TaskErrorStrategy.STOP, TaskErrorStrategy.valueOf("STOP"));
}

@Test
public void testOriginTableCleanStrategy() {
assertEquals(OriginTableCleanStrategy.DELETE, OriginTableCleanStrategy.valueOf("DELETE"));
assertEquals(OriginTableCleanStrategy.KEEP, OriginTableCleanStrategy.valueOf("KEEP"));
}

@Test
public void testSwapTableType() {
assertEquals(SwapTableType.NORMAL, SwapTableType.valueOf("NORMAL"));
assertEquals(SwapTableType.PARTITION, SwapTableType.valueOf("PARTITION"));
}

@Test
public void testOnlineSchemaChangeParameters() {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(300);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
params.setDelimiter(";");
params.setLockUsers(new ArrayList<>());
params.setSwapTableType(SwapTableType.NORMAL);

assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", params.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
assertEquals(Integer.valueOf(300), params.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
assertEquals(OriginTableCleanStrategy.DELETE, params.getOriginTableCleanStrategy());
assertEquals(";", params.getDelimiter());
assertNull(params.getLockUsers());
assertEquals(SwapTableType.NORMAL, params.getSwapTableType());
assertEquals(false, params.isContinueOnError());

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
.generateSubTaskParameters(new ConnectionConfig(), "schema");
// add assertions for subTaskParameters based on the generateSubTaskParameters method implementation
}

@Test
public void testGenerateSubTaskParameters() {
// create a mock ConnectionConfig and TableIdentity
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("dialect");
TableIdentity tableIdentity = new TableIdentity("schema", "table1");

OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 ADD COLUMN c2 INT");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(300);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
params.setDelimiter(";");
params.setLockUsers(new ArrayList<>());
params.setSwapTableType(SwapTableType.NORMAL);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
.generateSubTaskParameters(connectionConfig, "schema");

// add assertions for subTaskParameters
assertEquals(1, subTaskParameters.size());
OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
assertEquals("schema", subTaskParameter.getDatabaseName());
assertEquals("table1", subTaskParameter.getOriginTableName());
assertEquals(2, subTaskParameter.getSqlsToBeExecuted().size());
assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT",
subTaskParameter.getSqlsToBeExecuted().get(0));
assertEquals("ALTER TABLE table1 ADD COLUMN c2 INT",
subTaskParameter.getSqlsToBeExecuted().get(1));
}
}