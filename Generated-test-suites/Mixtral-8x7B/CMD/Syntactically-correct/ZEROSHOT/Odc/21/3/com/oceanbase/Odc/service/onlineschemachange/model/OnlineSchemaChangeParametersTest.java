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
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("SQL CONTENT");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
onlineSchemaChangeParameters.setDelimiter(";");
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);

Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
Assert.assertEquals("SQL CONTENT", onlineSchemaChangeParameters.getSqlContent());
Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
Assert.assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
Assert.assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
Assert.assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
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
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "schema";

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT; ALTER TABLE t1 DROP COLUMN c1;");
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
parameters.generateSubTaskParameters(connectionConfig, schema);

Assert.assertEquals(2, subTaskParameters.size());

OnlineSchemaChangeScheduleTaskParameters firstParameter = subTaskParameters.get(0);
Assert.assertEquals("schema", firstParameter.getDatabaseName());
Assert.assertEquals("t1", firstParameter.getOriginTableName());
Assert.assertEquals("c1", firstParameter.getNewColumnName());
Assert.assertEquals(1, firstParameter.getSqlsToBeExecuted().size());
Assert.assertEquals("ALTER TABLE t1 ADD COLUMN c1 INT",
firstParameter.getSqlsToBeExecuted().get(0));

OnlineSchemaChangeScheduleTaskParameters secondParameter = subTaskParameters.get(1);
Assert.assertEquals("schema", secondParameter.getDatabaseName());
Assert.assertEquals("t1", secondParameter.getOriginTableName());
Assert.assertEquals("c1", secondParameter.getNewColumnName());
Assert.assertEquals(1, secondParameter.getSqlsToBeExecuted().size());
Assert.assertEquals("ALTER TABLE t1 DROP COLUMN c1",
secondParameter.getSqlsToBeExecuted().get(0));
}
}