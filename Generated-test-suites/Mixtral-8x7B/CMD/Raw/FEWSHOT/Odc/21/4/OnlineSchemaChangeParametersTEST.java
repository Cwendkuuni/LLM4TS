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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");

OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 DROP COLUMN c2;");
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(3600);
params.setSwapTableNameRetryTimes(3);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE);
params.setDelimiter(";");
params.setLockUsers(new ArrayList<>());
params.setSwapTableType(SwapTableType.TABLE_CLONE);

OnlineSchemaChangeScheduleTaskParameters taskParam1 = new OnlineSchemaChangeScheduleTaskParameters();
taskParam1.setDatabaseName("testdb");
taskParam1.setOriginTableName("table1");
taskParam1.setNewTableName("newtable1");
taskParam1.setSqlsToBeExecuted(new ArrayList<>());
taskParam1.getSqlsToBeExecuted().add("ALTER TABLE newtable1 ADD COLUMN c1 INT");

OnlineSchemaChangeScheduleTaskParameters taskParam2 = new OnlineSchemaChangeScheduleTaskParameters();
taskParam2.setDatabaseName("testdb");
taskParam2.setOriginTableName("table1");
taskParam2.setNewTableName("newtable1");
taskParam2.setSqlsToBeExecuted(new ArrayList<>());
taskParam2.getSqlsToBeExecuted().add("DROP COLUMN c2 FROM newtable1");

Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(new TableIdentity("testdb", "table1"), taskParam1);
taskParameters.put(new TableIdentity("testdb", "table1"), taskParam2);

List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>(
taskParameters.values());

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
"testdb");

assertEquals(expectedSubTaskParameters, subTaskParameters);
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(params.isContinueOnError());

params.setErrorStrategy(TaskErrorStrategy.TERMINATE);
assertFalse(params.isContinueOnError());

params.setErrorStrategy(null);
assertFalse(params.isContinueOnError());
}

@Test
public void testGetOriginTableCleanStrategy() {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE);
assertEquals(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE, params.getOriginTableCleanStrategy());

params.setOriginTableCleanStrategy(null);
assertNull(params.getOriginTableCleanStrategy());
}

@Test
public void testGetSwapTableType() {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSwapTableType(SwapTableType.TABLE_CLONE);
assertEquals(SwapTableType.TABLE_CLONE, params.getSwapTableType());

params.setSwapTableType(null);
assertNull(params.getSwapTableType());
}
}