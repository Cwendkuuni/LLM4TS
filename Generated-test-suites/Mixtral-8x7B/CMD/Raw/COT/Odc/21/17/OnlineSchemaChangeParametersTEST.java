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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(PowerMockRunner.class)
public class OnlineSchemaChangeParametersTest {

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_happyPath() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // One subtask should be generated
 assertEquals(1, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = result.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("test_table", subTaskParameter.getOriginTableName());
 assertEquals("test_table", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT",
 subTaskParameter.getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParameters_multiSql() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT; ALTER TABLE test_table ADD COLUMN test_column2 INT;");

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // Two subtasks should be generated
 assertEquals(2, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter1 = result.get(0);
 assertEquals("test_schema", subTaskParameter1.getDatabaseName());
 assertEquals("test_table", subTaskParameter1.getOriginTableName());
 assertEquals("test_table", subTaskParameter1.getNewTableName());
 assertEquals(1, subTaskParameter1.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT",
 subTaskParameter1.getSqlsToBeExecuted().get(0));

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter2 = result.get(1);
 assertEquals("test_schema", subTaskParameter2.getDatabaseName());
 assertEquals("test_table", subTaskParameter2.getOriginTableName());
 assertEquals("test_table", subTaskParameter2.getNewTableName());
 assertEquals(1, subTaskParameter2.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE test_table ADD COLUMN test_column2 INT",
 subTaskParameter2.getSqlsToBeExecuted().get(0));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_invalidSql() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("INVALID SQL;");

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }

 @Test
 public void testGenerateSubTaskParameters_alterTable() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // One subtask should be generated
 assertEquals(1, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = result.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("test_table", subTaskParameter.getOriginTableName());
 assertEquals("test_table", subTaskParameter.getNewTableName());
 assertEquals(2, subTaskParameter.getSqlsToBeExecuted().size());

 assertTrue(subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE test_table ADD COLUMN test_column INT"));

 String newAlterStmt = DdlUtils.replaceTableName(subTaskParameter.getSqlsToBeExecuted().get(1),
 subTaskParameter.getNewTableName(), connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlType());

 assertEquals(newAlterStmt, subTaskParameter.getSqlsToBeExecuted().get(1));
 }

 @Test
 public void testGenerateSubTaskParameters_createTable() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // One subtask should be generated
 assertEquals(1, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = result.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("test_table", subTaskParameter.getOriginTableName());
 assertEquals("test_table", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50))",
 subTaskParameter.getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParameters_dropTable() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("DROP TABLE test_table;");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.DROP);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // One subtask should be generated
 assertEquals(1, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = result.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("test_table", subTaskParameter.getOriginTableName());
 assertEquals("test_table", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("DROP TABLE test_table", subTaskParameter.getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParameters_renameTable() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlContent("RENAME TABLE test_table TO test_table_new;");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.RENAME);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);

 // One subtask should be generated
 assertEquals(1, result.size());

 // Verify the subtask parameters
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = result.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("test_table", subTaskParameter.getOriginTableName());
 assertEquals("test_table_new", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("RENAME TABLE test_table TO test_table_new", subTaskParameter.getSqlsToBeExecuted().get(0));
 }

}