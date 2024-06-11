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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType.ALTER;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType.CREATE;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createParameters(OnlineSchemaChangeSqlType sqlType, String sqlContent,
TaskErrorStrategy errorStrategy, Integer lockTableTimeOutSeconds, Integer swapTableNameRetryTimes,
OriginTableCleanStrategy originTableCleanStrategy, List<String> lockUsers, SwapTableType swapTableType) {
List<String> lockUsersList = lockUsers != null ? lockUsers : new ArrayList<>();
return new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, lockTableTimeOutSeconds,
swapTableNameRetryTimes, originTableCleanStrategy, "delimiter", lockUsersList, swapTableType);
}

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters parameters = this.createParameters(CREATE, "sqlContent", TaskErrorStrategy.CONTINUE,
10, 10, OriginTableCleanStrategy.DROP, null, SwapTableType.ALTER_WITH_SAME_TABLE);
assertEquals(CREATE, parameters.getSqlType());
assertEquals("sqlContent", parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(10), parameters.getSwapTableNameRetryTimes());
assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
assertEquals("delimiter", parameters.getDelimiter());
assertEquals(0, parameters.getLockUsers().size());
assertEquals(SwapTableType.ALTER_WITH_SAME_TABLE, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = this.createParameters(ALTER, "sqlContent", TaskErrorStrategy.CONTINUE,
10, 10, OriginTableCleanStrategy.DROP, null, SwapTableType.ALTER_WITH_NEW_TABLE);
assertEquals(true, parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("dialect");
String schema = "schema";
List<String> sqls = new ArrayList<>();
sqls.add("sql1");
sqls.add("sql2");
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
OnlineSchemaChangeParameters parameters = createParameters(sqlType, String.join("delimiter", sqls),
TaskErrorStrategy.CONTINUE, 10, 10, OriginTableCleanStrategy.DROP, null, SwapTableType.ALTER_WITH_NEW_TABLE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(2, result.size());
TableIdentity tableIdentity1 = new TableIdentity("databaseName1", "tableName1");
TableIdentity tableIdentity2 = new TableIdentity("databaseName2", "tableName2");
OnlineSchemaChangeScheduleTaskParameters expected1 = new OnlineSchemaChangeScheduleTaskParameters();
expected1.setDatabaseName("databaseName1");
expected1.setOriginTableName("tableName1");
expected1.setNewTableName("newTableName1");
expected1.getSqlsToBeExecuted().add("sql1");
expected1.getSqlsToBeExecuted().add(
DdlUtils.replaceTableName("sql2", "newTableName1", "dialect", sqlType));
OnlineSchemaChangeScheduleTaskParameters expected2 = new OnlineSchemaChangeScheduleTaskParameters();
expected2.setDatabaseName("databaseName2");
expected2.setOriginTableName("tableName2");
expected2.setNewTableName("newTableName2");
expected2.getSqlsToBeExecuted().add("sql2");
expected2.getSqlsToBeExecuted().add(
DdlUtils.replaceTableName("sql1", "newTableName2", "dialect", sqlType));
assertEquals(expected1, result.get(0));
assertEquals(expected2, result.get(1));
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("dialect");
String schema = "schema";
List<String> sqls = new ArrayList<>();
sqls.add("sql1");
sqls.add("sql2");
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
OnlineSchemaChangeParameters parameters = createParameters(sqlType, String.join("delimiter", sqls),
TaskErrorStrategy.CONTINUE, 10, 10, OriginTableCleanStrategy.DROP, null, SwapTableType.ALTER_WITHOUT_NEW_TABLE);
parameters.generateSubTaskParameters(connectionConfig, schema);
}

}