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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters createParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT";
 TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
 Integer lockTableTimeOutSeconds = 30;
 Integer swapTableNameRetryTimes = 5;
 OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
 String delimiter = ";";
 List<String> lockUsers = new ArrayList<>();
 SwapTableType swapTableType = SwapTableType.SWAP_IF_EXISTS;

 return new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, lockTableTimeOutSeconds,
 swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);
 }

 @Test
 public void testOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameters = createParameters();
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertEquals(sqlContent, parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(Integer.valueOf(30), parameters.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());
 assertEquals(lockUsers, parameters.getLockUsers());
 assertEquals(SwapTableType.SWAP_IF_EXISTS, parameters.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = createParameters();
 assertEquals(true, parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createParameters();
 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "schema");

 // TODO: Add more assertions based on the expected output
 assertEquals(1, taskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParametersWithMultipleSqls() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table2 ADD COLUMN c2 INT";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(sqlType, sqlContent,
 TaskErrorStrategy.CONTINUE, 30, 5, OriginTableCleanStrategy.KEEP, ";", new ArrayList<>(),
 SwapTableType.SWAP_IF_EXISTS);

 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters
 .generateSubTaskParameters(connectionConfig, "schema");

 // TODO: Add more assertions based on the expected output
 assertEquals(2, taskParameters.size());
 }

}