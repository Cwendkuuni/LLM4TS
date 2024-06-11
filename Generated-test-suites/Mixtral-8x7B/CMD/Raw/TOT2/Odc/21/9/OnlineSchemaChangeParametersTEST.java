import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(PowerMockRunner.class)
public class OnlineSchemaChangeParametersTest {

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for constructor and getter/setter methods
 @Test
 public void testOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 assertNotNull(params);

 // Test for all setter methods
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table_name ADD column_name data_type");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.RENAME);

 // Test for all getter methods
 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table_name ADD column_name data_type", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(30), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<>(), params.getLockUsers());
 assertEquals(SwapTableType.RENAME, params.getSwapTableType());
 }

 // Test for isContinueOnError() method
 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 // Test for generateSubTaskParameters() method
 @Test
 public void testGenerateSubTaskParameters() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT");

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(
 connectionConfig, schema);

 // Test for size of the subTaskParameters list
 assertEquals(2, subTaskParameters.size());

 // Test for each subTaskParameter
 for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
 if (subTaskParameter.getOriginTableName().equals("table1")) {
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals("col1", subTaskParameter.getNewColumnName());
 assertEquals("INT", subTaskParameter.getNewColumnType());
 } else if (subTaskParameter.getOriginTableName().equals("table2")) {
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
 assertEquals("table2", subTaskParameter.getOriginTableName());
 assertEquals("col2", subTaskParameter.getNewColumnName());
 assertEquals("INT", subTaskParameter.getNewColumnType());
 }
 }
 }

 // Test for edge cases and exception handling scenarios
 @Test
 public void testGenerateSubTaskParametersEdgeCasesAndExceptions() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 // Test with empty sqlContent
 onlineSchemaChangeParameters.setSqlContent("");
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(
 connectionConfig, schema);
 assertTrue(subTaskParameters.isEmpty());

 // Test with null sqlContent
 onlineSchemaChangeParameters.setSqlContent(null);
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 assertTrue(subTaskParameters.isEmpty());

 // Test with null connectionConfig
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(null, schema);
 assertTrue(subTaskParameters.isEmpty());

 // Test with null schema
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, null);
 assertTrue(subTaskParameters.isEmpty());

 // Test with empty schema
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
 assertTrue(subTaskParameters.isEmpty());

 // Test with invalid sqlContent
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD col1 INT; INVALID SQL");
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(1, subTaskParameters.size());

 // Test with single sql statement
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD col1 INT");
 subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(1, subTaskParameters.size());
 }

 // Test for serialize and deserialize methods
 @Test
 public void testSerialization() throws Exception {
 Serializable serializedOnlineSchemaChangeParameters = (Serializable) OnlineSchemaChangeParametersTest.serialize(onlineSchemaChangeParameters);
 OnlineSchemaChangeParameters deserializedOnlineSchemaChangeParameters = (OnlineSchemaChangeParameters) OnlineSchemaChangeParametersTest.deserialize(serializedOnlineSchemaChangeParameters);
 assertEquals(onlineSchemaChangeParameters, deserializedOnlineSchemaChangeParameters);
 }

 public static Serializable serialize(Object obj) throws Exception {
 byte[] bytes = SerializationUtils.serialize(obj);
 return (Serializable) bytes;
 }

 public static Object deserialize(Serializable ser) throws Exception {
 byte[] bytes = (byte[]) ser;
 Object deserializedObj = SerializationUtils.deserialize(bytes);
 return deserializedObj;
 }
}