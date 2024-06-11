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

 private OnlineSchemaChangeParameters buildOnlineSchemaChangeParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 int";
 TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
 Integer lockTableTimeOutSeconds = 300;
 Integer swapTableNameRetryTimes = 5;
 OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
 String delimiter = ";";
 List<String> lockUsers = new ArrayList<>();
 SwapTableType swapTableType = SwapTableType.ALTER_SWAP;

 return new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, lockTableTimeOutSeconds,
 swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);
 }

 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters params = buildOnlineSchemaChangeParameters();

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals(sqlContent, params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(300), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(lockUsers, params.getLockUsers());
 assertEquals(SwapTableType.ALTER_SWAP, params.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = buildOnlineSchemaChangeParameters();
 assertTrue(params.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_whenSqlTypeIsAlter() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = buildOnlineSchemaChangeParameters();

 // Invoke method
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, schema);

 // Assertions
 assertEquals(1, subTaskParameters.size());
 assertEquals(schema, subTaskParameters.get(0).getDatabaseName());
 assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
 assertEquals("ALTER TABLE table1 ADD COLUMN c1 int",
 subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
 String newAlterStmt = "ALTER TABLE table1 ADD COLUMN c1 int";
 assertEquals(newAlterStmt,
 subTaskParameters.get(0).getSqlsToBeExecuted().get(1));
 }

 @Test
 public void testGenerateSubTaskParameters_whenSqlTypeIsOtherThanAlter() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.DROP);
 params.setSqlContent("DROP TABLE table1");

 // Invoke method
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params
 .generateSubTaskParameters(connectionConfig, schema);

 // Assertions
 assertEquals(1, subTaskParameters.size());
 assertEquals(schema, subTaskParameters.get(0).getDatabaseName());
 assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
 assertEquals("DROP TABLE table1",
 subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParameters_whenSqlTypeIsNull() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

 Exception exception = null;
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 } catch (Exception e) {
 exception = e;
 }

 // Assertions
 assertTrue(exception instanceof UnexpectedException);
 }

 @Test
 public void testGenerateSubTaskParameters_whenConnectionConfigIsNull() {
 // Build test data
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = buildOnlineSchemaChangeParameters();

 Exception exception = null;
 try {
 params.generateSubTaskParameters(null, schema);
 } catch (Exception e) {
 exception = e;
 }

 // Assertions
 assertTrue(exception instanceof NullPointerException);
 }

 @Test
 public void testGenerateSubTaskParameters_whenSchemaIsNull() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters params = buildOnlineSchemaChangeParameters();

 Exception exception = null;
 try {
 params.generateSubTaskParameters(connectionConfig, null);
 } catch (Exception e) {
 exception = e;
 }

 // Assertions
 assertTrue(exception instanceof NullPointerException);
 }

 @Test
 public void testGenerateSubTaskParameters_whenSqlIsNull() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlContent(null);
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

 Exception exception = null;
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 } catch (Exception e) {
 exception = e;
 }

 // Assertions
 assertTrue(exception instanceof UnexpectedException);
 }

 @Test
 public void testGenerateSubTaskParameters_whenSqlTypeIsNullOrEmpty() {
 // Build test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int");
 params.setSqlType(null);

 Exception exception = null;
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 } catch (Exception e) {
 exception = e;
 }

 assertTrue(exception instanceof UnexpectedException);

 params.setSqlType("");
 exception = null;
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 } catch (Exception e) {
 exception = e;
 }

 assertTrue(exception instanceof UnexpectedException);
 }

}