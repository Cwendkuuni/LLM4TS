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

 private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(10);
 params.setSwapTableNameRetryTimes(3);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_SWAP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.INTERNAL);

 return params;
 }

 @Test
 public void testConstructor() {
 OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(10), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(3), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.BACKUP_AND_SWAP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<>(), params.getLockUsers());
 assertEquals(SwapTableType.INTERNAL, params.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();

 assertEquals(true, params.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();

 OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter1.setDatabaseName("db1");
 taskParameter1.setOriginTableName("table1");
 taskParameter1.setNewTableName("table1_new");

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(1, subTaskParameters.size());
 assertEquals(taskParameter1.getDatabaseName(), subTaskParameters.get(0).getDatabaseName());
 assertEquals(taskParameter1.getOriginTableName(),
 subTaskParameters.get(0).getOriginTableName());
 assertEquals(taskParameter1.getNewTableName(),
 subTaskParameters.get(0).getNewTableName());
 }

 @Test
 public void testGenerateSubTaskParameters_NoSqls() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();
 params.setSqlContent("");

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParameters_DifferentDialectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("postgresql");

 OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParameters_InvalidSql() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("INVALID SQL");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(10);
 params.setSwapTableNameRetryTimes(3);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_SWAP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.INTERNAL);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, subTaskParameters.size());
 }
}