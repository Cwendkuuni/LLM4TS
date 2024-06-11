import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
onlineSchemaChangeParameters.setSqlType(sqlType);
Assert.assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testGetSqlContent() {
String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testGetErrorStrategy() {
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
Assert.assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testGetLockTableTimeOutSeconds() {
Integer lockTableTimeOutSeconds = 60;
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
Assert.assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testGetSwapTableNameRetryTimes() {
Integer swapTableNameRetryTimes = 3;
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
Assert.assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testGetOriginTableCleanStrategy() {
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
Assert.assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testGetDelimiter() {
String delimiter = ",";
onlineSchemaChangeParameters.setDelimiter(delimiter);
Assert.assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testGetLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testGetSwapTableType() {
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
public void testGenerateSubTaskParameters() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 varchar(255);";
onlineSchemaChangeParameters.setSqlContent(sqlContent);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);

OnlineSchemaChangeScheduleTaskParameters taskParameter1 = subTaskParameterFactory.generate("ALTER TABLE table1 ADD column1 int", OnlineSchemaChangeSqlType.ALTER);
OnlineSchemaChangeScheduleTaskParameters taskParameter2 = subTaskParameterFactory.generate("ALTER TABLE table2 ADD column2 varchar(255)", OnlineSchemaChangeSqlType.ALTER);

Assert.assertEquals(subTaskParameters.get(0).getDatabaseName(), taskParameter1.getDatabaseName());
Assert.assertEquals(subTaskParameters.get(0).getOriginTableName(), taskParameter1.getOriginTableName());
Assert.assertEquals(subTaskParameters.get(1).getDatabaseName(), taskParameter2.getDatabaseName());
Assert.assertEquals(subTaskParameters.get(1).getOriginTableName(), taskParameter2.getOriginTableName());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithException() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
String sqlContent = "ALTER TABLE table1 ADD column1 int; INVALID SQL; ALTER TABLE table2 ADD column2 varchar(255);";
onlineSchemaChangeParameters.setSqlContent(sqlContent);

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}

class SubTaskParameterFactory {

public static SubTaskParameterFactory getInstance() {
return new SubTaskParameterFactory();
}

public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
// Implement the logic to generate SubTaskParameterFactory
}
}

enum SwapTableType {
TEMPORARY,
PERMANENT
}

enum OriginTableCleanStrategy {
KEEP,
DROP
}

class OnlineSchemaChangeScheduleTaskParameters implements Serializable {

private String databaseName;
private String originTableName;
private String newTableName;
private List<String> sqlsToBeExecuted;

// Getters and Setters
}