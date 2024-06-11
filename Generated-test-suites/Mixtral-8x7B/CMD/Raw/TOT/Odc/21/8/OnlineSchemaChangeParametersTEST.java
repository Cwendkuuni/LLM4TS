import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(120);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_DELETE);
onlineSchemaChangeParameters.setDelimiter(";");

List<String> lockUsers = new ArrayList<>(Arrays.asList("user1", "user2"));
onlineSchemaChangeParameters.setLockUsers(lockUsers);
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.TEMPORARY);

assertNotNull(onlineSchemaChangeParameters.getSqlType());
assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());

assertNotNull(onlineSchemaChangeParameters.getSqlContent());
assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT", onlineSchemaChangeParameters.getSqlContent());

assertNotNull(onlineSchemaChangeParameters.getErrorStrategy());
assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());

assertNotNull(onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
assertEquals(new Integer(120), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());

assertNotNull(onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
assertEquals(new Integer(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());

assertNotNull(onlineSchemaChangeParameters.getOriginTableCleanStrategy());
assertEquals(OriginTableCleanStrategy.BACKUP_AND_DELETE,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());

assertNotNull(onlineSchemaChangeParameters.getDelimiter());
assertEquals(";", onlineSchemaChangeParameters.getDelimiter());

assertNotNull(onlineSchemaChangeParameters.getLockUsers());
assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());

assertNotNull(onlineSchemaChangeParameters.getSwapTableType());
assertEquals(SwapTableType.TEMPORARY, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws Exception {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "test_schema";

onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT");

OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
taskParameter1.setDatabaseName("test_schema");
taskParameter1.setOriginTableName("table1");
taskParameter1.setNewTableName("new_table1");
List<String> sqlsToBeExecuted1 = new ArrayList<>();
sqlsToBeExecuted1.add("ALTER TABLE new_table1 ADD COLUMN col1 INT");
taskParameter1.setSqlsToBeExecuted(sqlsToBeExecuted1);

OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
taskParameter2.setDatabaseName("test_schema");
taskParameter2.setOriginTableName("table2");
taskParameter2.setNewTableName("new_table2");
List<String> sqlsToBeExecuted2 = new ArrayList<>();
sqlsToBeExecuted2.add("ALTER TABLE new_table2 ADD COLUMN col2 INT");
taskParameter2.setSqlsToBeExecuted(sqlsToBeExecuted2);

Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");

Mockito.when(new SubTaskParameterFactory(connectionConfig, schema).generate(Mockito.anyString(),
Mockito.any(OnlineSchemaChangeSqlType.class)))
.thenReturn(taskParameter1, taskParameter2);

Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(new TableIdentity("test_schema", "table1"), taskParameter1);
taskParameters.put(new TableIdentity("test_schema", "table2"), taskParameter2);

List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>(
taskParameters.values());

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);

assertEquals(expectedSubTaskParameters, subTaskParameters);
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParameters_Exception() throws Exception {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "test_schema";

onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT");

Mockito.when(connectionConfig.getDialectType()).thenThrow(new Exception());

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}

}