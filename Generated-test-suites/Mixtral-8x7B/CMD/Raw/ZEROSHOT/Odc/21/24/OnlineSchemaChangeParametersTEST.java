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
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
 onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
 onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.MULTI_TABLE);
 }

 @Test
 public void testIsContinueOnError() {
 Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws UnexpectedException {
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 String schema = "test_schema";

 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
 taskParameters.put(new TableIdentity("test_schema", "table1"),
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "table1", "new_table1",
 new ArrayList<>(), OnlineSchemaChangeSqlType.ALTER));

 SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
 Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test_schema", "table1", "new_table1",
 new ArrayList<>(), OnlineSchemaChangeSqlType.ALTER));

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

 Mockito.verify(subTaskParameterFactory, Mockito.times(1)).generate(onlineSchemaChangeParameters.getSqlContent(),
 onlineSchemaChangeParameters.getSqlType());
 Assert.assertEquals(taskParameters.values(), result);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_exception() throws UnexpectedException {
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 String schema = "test_schema";

 SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
 Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
 .thenThrow(new UnexpectedException("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters));

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }
}