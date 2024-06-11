import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

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
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
// Testing getters and setters for all the fields

onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());

onlineSchemaChangeParameters.setSqlContent("test sql content");
Assert.assertEquals("test sql content", onlineSchemaChangeParameters.getSqlContent());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
Assert.assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());

onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
Assert.assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());

onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
Assert.assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());

onlineSchemaChangeParameters.setDelimiter(";");
Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());

List<String> lockUsersList = new ArrayList<>();
lockUsersList.add("testUser1");
lockUsersList.add("testUser2");
onlineSchemaChangeParameters.setLockUsers(lockUsersList);
Assert.assertEquals(lockUsersList, onlineSchemaChangeParameters.getLockUsers());

onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP);
Assert.assertEquals(SwapTableType.SWAP, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";

OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table1 ADD COLUMN test_column1 INT; ALTER TABLE test_table2 ADD COLUMN test_column2 INT");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Assert.assertEquals(2, subTaskParameters.size());

for (OnlineSchemaChangeScheduleTaskParameters param : subTaskParameters) {
TableIdentity key = new TableIdentity(param.getDatabaseName(), param.getOriginTableName());
Assert.assertTrue(taskParameters.containsKey(key));
Assert.assertEquals(param, taskParameters.get(key));
}
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParameters_exception() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";

OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table1 ADD COLUMN test_column1 INT; ALTER TABLE test_table2 ADD COLUMN test_column2 INT");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.INVALID);

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}