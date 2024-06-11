import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("sqlContent");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
onlineSchemaChangeParameters.setDelimiter(";");
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.DEFAULT);

Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
Assert.assertEquals("sqlContent", onlineSchemaChangeParameters.getSqlContent());
Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
Assert.assertEquals(10, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
Assert.assertEquals(5, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
Assert.assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());
Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
Assert.assertEquals(0, onlineSchemaChangeParameters.getLockUsers().size());
Assert.assertEquals(OnlineSchemaChangeParameters.SwapTableType.DEFAULT, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "schema";

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("sql1;sql2");
parameters.setDelimiter(";");

SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

try {
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(),
parameters.getDelimiter());
for (String sql : sqls) {
OnlineSchemaChangeScheduleTaskParameters parameter = subTaskParameterFactory.generate(sql, parameters.getSqlType());
TableIdentity key = new TableIdentity("databaseName", "originTableName");
taskParameters.putIfAbsent(key, parameter);
if (parameters.getSqlType() == OnlineSchemaChangeSqlType.ALTER) {
String newAlterStmt = DdlUtils.replaceTableName(sql, "newTableName", connectionConfig.getDialectType(),
parameters.getSqlType());
taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);
}
}
} catch (Exception e) {
throw new UnexpectedException("Failed to generate subtasks with parameter: " + parameters, e);
}}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws UnexpectedException {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "schema";

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("sql1;sql2");
parameters.setDelimiter(";");

SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenThrow(new RuntimeException());

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}