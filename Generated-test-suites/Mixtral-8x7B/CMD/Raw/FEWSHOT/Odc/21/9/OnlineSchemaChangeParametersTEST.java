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

 private OnlineSchemaChangeParameters createParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_SWAP);
 parameters.setDelimiter(";");

 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 lockUsers.add("user2");
 parameters.setLockUsers(lockUsers);

 parameters.setSwapTableType(SwapTableType.INTERNAL_TABLE);
 return parameters;
 }

 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters parameters = createParameters();

 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.BACKUP_AND_SWAP, parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());
 assertEquals(lockUsers, parameters.getLockUsers());
 assertEquals(SwapTableType.INTERNAL_TABLE, parameters.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = createParameters();
 assertEquals(true, parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
 assertEquals(false, parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 OnlineSchemaChangeParameters parameters = createParameters();

 // Mock ConnectionConfig and generate subtask parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals("c1", subTaskParameter.getNewColumnName());
 assertEquals("INT", subTaskParameter.getNewColumnType());
 assertEquals(true, subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN c1 INT"));
 }

 @Test
 public void testGenerateSubTaskParametersWithAlterTableSql() {
 OnlineSchemaChangeParameters parameters = createParameters();
 parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT, ALTER TABLE table1 ADD COLUMN c2 VARCHAR(255)");

 // Mock ConnectionConfig and generate subtask parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals("c1", subTaskParameter.getNewColumnName());
 assertEquals("INT", subTaskParameter.getNewColumnType());
 assertEquals(true,
 subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN c1 INT"));
 assertEquals(true,
 subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN c2 VARCHAR(255)"));
 }

 @Test
 public void testGenerateSubTaskParametersWithMultipleTables() {
 OnlineSchemaChangeParameters parameters = createParameters();
 parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table2 ADD COLUMN c1 INT");

 // Mock ConnectionConfig and generate subtask parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(2, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter1 = subTaskParameters.get(0);
 assertEquals("test_schema", subTaskParameter1.getDatabaseName());
 assertEquals("table1", subTaskParameter1.getOriginTableName());
 assertEquals("c1", subTaskParameter1.getNewColumnName());
 assertEquals("INT", subTaskParameter1.getNewColumnType());
 assertEquals(true,
 subTaskParameter1.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN c1 INT"));

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter2 = subTaskParameters.get(1);
 assertEquals("test_schema", subTaskParameter2.getDatabaseName());
 assertEquals("table2", subTaskParameter2.getOriginTableName());
 assertEquals("c1", subTaskParameter2.getNewColumnName());
 assertEquals("INT", subTaskParameter2.getNewColumnType());
 assertEquals(true,
 subTaskParameter2.getSqlsToBeExecuted().contains("ALTER TABLE table2 ADD COLUMN c1 INT"));
 }

 @Test
 public void testGenerateSubTaskParametersWithNullSqlContent() {
 OnlineSchemaChangeParameters parameters = createParameters();
 parameters.setSqlContent(null);

 // Mock ConnectionConfig and generate subtask parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParametersWithEmptySqlContent() {
 OnlineSchemaChangeParameters parameters = createParameters();
 parameters.setSqlContent("");

 // Mock ConnectionConfig and generate subtask parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(0, subTaskParameters.size());
 }

}