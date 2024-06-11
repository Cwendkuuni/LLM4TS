import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for constructor and getters/setters
 @Test
 public void testConstructorAndGettersSetters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(300);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<String>() {{ add("user1"); add("user2"); }});
 params.setSwapTableType(SwapTableType.SNAPSHOT);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table_name ADD column_name datatype", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(300), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<String>() {{ add("user1"); add("user2"); }}, params.getLockUsers());
 assertEquals(SwapTableType.SNAPSHOT, params.getSwapTableType());
 }

 // Test for isContinueOnError method
 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 // Test for generateSubTaskParameters method with typical use case
 @Test
 public void testGenerateSubTaskParametersTypicalUseCase() throws Exception {
 OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter1.setDatabaseName("db1");
 taskParameter1.setOriginTableName("table1");
 taskParameter1.setNewTableName("new_table1");

 OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter2.setDatabaseName("db2");
 taskParameter2.setOriginTableName("table2");
 taskParameter2.setNewTableName("new_table2");

 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();
 expectedTaskParameters.put(new TableIdentity("db1", "table1"), taskParameter1);
 expectedTaskParameters.put(new TableIdentity("db2", "table2"), taskParameter2);

 List<String> sqls = new ArrayList<String>() {{
 add("ALTER TABLE table1 ADD column1 datatype1");
 add("ALTER TABLE table2 ADD column2 datatype2");
 }};

 when(subTaskParameterFactory.generate(eq(sqls.get(0)), eq(OnlineSchemaChangeSqlType.ALTER)))
 .thenReturn(taskParameter1);

 when(subTaskParameterFactory.generate(eq(sqls.get(1)), eq(OnlineSchemaChangeSqlType.ALTER)))
 .thenReturn(taskParameter2);

 when(connectionConfig.getDialectType()).thenReturn(DdlUtils.Dialect.MYSQL);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(expectedTaskParameters, result);
 }

 // Test for generateSubTaskParameters method with exception handling scenario
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersExceptionScenario() throws Exception {
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenThrow(new Exception("Unexpected exception occurred"));

 when(connectionConfig.getDialectType()).thenReturn(DdlUtils.Dialect.MYSQL);

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Edge case: Test for generateSubTaskParameters method with empty sqlContent
 @Test
 public void testGenerateSubTaskParametersEmptySqlContent() {
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertTrue(result.isEmpty());
 }
}