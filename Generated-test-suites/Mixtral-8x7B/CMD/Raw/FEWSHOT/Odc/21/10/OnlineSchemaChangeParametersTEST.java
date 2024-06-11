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

private OnlineSchemaChangeParameters createParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(100);
parameters.setSwapTableNameRetryTimes(5);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<String>() {{
add("user1");
add("user2");
}});
parameters.setSwapTableType(SwapTableType.TEMPORARY);
return parameters;
}

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters parameters = createParameters();
assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
assertEquals("ALTER TABLE table_name ADD column_name datatype", parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(100, parameters.getLockTableTimeOutSeconds().intValue());
assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());
assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
assertEquals(";", parameters.getDelimiter());
assertEquals(2, parameters.getLockUsers().size());
assertEquals("user1", parameters.getLockUsers().get(0));
assertEquals("user2", parameters.getLockUsers().get(1));
assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = createParameters();
assertEquals(true, parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = createParameters();
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
.generateSubTaskParameters(connectionConfig, "test_schema");
assertEquals(1, result.size());
OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(0);
assertEquals("test_schema", taskParameters.getDatabaseName());
assertEquals("table_name", taskParameters.getOriginTableName());
assertEquals("drop", taskParameters.getOriginTableCleanStrategy().toString().toLowerCase());
assertEquals(1, taskParameters.getSqlsToBeExecuted().size());
assertEquals("ALTER TABLE table_name ADD column_name datatype",
taskParameters.getSqlsToBeExecuted().get(0));
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithException() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = createParameters();
parameters.setSqlContent(null);
parameters.generateSubTaskParameters(connectionConfig, "test_schema");
}

}