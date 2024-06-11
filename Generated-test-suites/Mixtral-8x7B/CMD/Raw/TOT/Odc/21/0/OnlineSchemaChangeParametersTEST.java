import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setup() {
 parameters = new OnlineSchemaChangeParameters();
 }

 // Test for setters and getters
 @Test
 public void testSettersAndGetters() {
 
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

 parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
 assertEquals("ALTER TABLE table_name ADD column_name datatype", parameters.getSqlContent());

 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());

 parameters.setLockTableTimeOutSeconds(1800);
 assertEquals(new Integer(1800), parameters.getLockTableTimeOutSeconds());

 parameters.setSwapTableNameRetryTimes(5);
 assertEquals(new Integer(5), parameters.getSwapTableNameRetryTimes());

 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
 parameters.getOriginTableCleanStrategy());

 parameters.setDelimiter(";");
 assertEquals(";", parameters.getDelimiter());

 parameters.setLockUsers(new ArrayList<String>() {{
 add("user1");
 add("user2");
 }});
 assertEquals(2, parameters.getLockUsers().size());

 parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
 assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY, parameters.getSwapTableType());
 }

 // Test for isContinueOnError()
 @Test
 public void testIsContinueOnError() {
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 // Test for generateSubTaskParameters()
 @Test
 public void testGenerateSubTaskParameters() throws UnexpectedException {
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 String schema = "test_schema";
 Mockito.when(connectionConfig.getDialectType()).thenReturn("MYSQL");
 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
 schema);
 assertEquals(0, result.size());

 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 parameters.setSqlType(sqlType);
 String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
 parameters.setSqlContent(sqlContent);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>();
 subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 TableIdentity tableIdentity = new TableIdentity("test_schema", "table_name");
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertTrue(subTaskParameter.getSqlsToBeExecuted().contains(sqlContent));
 assertEquals(tableIdentity.getDatabaseName(), subTaskParameter.getDatabaseName());
 assertEquals(tableIdentity.getTableName(), subTaskParameter.getOriginTableName());
 assertEquals(sqlType, subTaskParameter.getSqlType());
 }

 // Test for TaskParameters interface method
 @Test
 public void testTaskParametersInterface() {
 assertNull(parameters.getTaskId());
 assertNull(parameters.getTaskName());
 }

}