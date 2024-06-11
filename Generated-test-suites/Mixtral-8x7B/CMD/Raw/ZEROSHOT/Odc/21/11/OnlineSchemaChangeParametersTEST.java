import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

@Mock
private ConnectionConfig connectionConfig;

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("SQL_CONTENT");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AFTER_SWAP);
onlineSchemaChangeParameters.setDelimiter(";");
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);

Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
Assert.assertEquals("SQL_CONTENT", onlineSchemaChangeParameters.getSqlContent());
Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
Assert.assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
Assert.assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
Assert.assertEquals(OriginTableCleanStrategy.DROP_AFTER_SWAP,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());
Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
Assert.assertNotNull(onlineSchemaChangeParameters.getLockUsers());
Assert.assertEquals(SwapTableType.SWAP_TABLE, onlineSchemaChangeParameters.getSwapTableType());
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
Mockito.when(connectionConfig.getDialectType()).thenReturn("dialectType");

onlineSchemaChangeParameters.setSqlContent("sql1;sql2;sql3");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(new TableIdentity("db1", "tbl1"),
new OnlineSchemaChangeScheduleTaskParameters("db1", "tbl1", "newTbl1", "sql1"));
taskParameters.put(new TableIdentity("db2", "tbl2"),
new OnlineSchemaChangeScheduleTaskParameters("db2", "tbl2", "newTbl2", "sql2"));
taskParameters.put(new TableIdentity("db3", "tbl3"),
new OnlineSchemaChangeScheduleTaskParameters("db3", "tbl3", "newTbl3", "sql3"));

Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenAnswer(invocation -> taskParameters.get(new TableIdentity(invocation.getArgument(0), invocation.getArgument(1))));

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

Assert.assertEquals(3, subTaskParameters.size());
Assert.assertEquals("newTbl1", subTaskParameters.get(0).getNewTableName());
Assert.assertEquals("newTbl2", subTaskParameters.get(1).getNewTableName());
Assert.assertEquals("newTbl3", subTaskParameters.get(2).getNewTableName());
}
}