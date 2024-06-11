import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setUp() {
 parameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testIsContinueOnError() {
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT";
 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, schema);

 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals("table1", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT",
 subTaskParameter.getSqlsToBeExecuted().get(0));

 sqlContent = "ALTER TABLE table1 ADD COLUMN c2 INT; ALTER TABLE table2 ADD COLUMN c3 INT";
 parameters.setSqlContent(sqlContent);

 subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

 assertNotNull(subTaskParameters);
 assertEquals(2, subTaskParameters.size());

 subTaskParameter = subTaskParameters.get(0);
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals("table1", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE table1 ADD COLUMN c2 INT",
 subTaskParameter.getSqlsToBeExecuted().get(0));

 subTaskParameter = subTaskParameters.get(1);
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals("table2", subTaskParameter.getOriginTableName());
 assertEquals("table2", subTaskParameter.getNewTableName());
 assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE table2 ADD COLUMN c3 INT",
 subTaskParameter.getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParametersWithReplaceTableName()
 throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT";
 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);

 String newTableName = "new_table1";
 parameters.setNewTableName(newTableName);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
 .generateSubTaskParameters(connectionConfig, schema);

 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertEquals(schema, subTaskParameter.getDatabaseName());
 assertEquals("table1", subTaskParameter.getOriginTableName());
 assertEquals(newTableName, subTaskParameter.getNewTableName());
 assertEquals(2, subTaskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT",
 subTaskParameter.getSqlsToBeExecuted().get(0));
 assertEquals("ALTER TABLE " + newTableName + " ADD COLUMN c1 INT",
 subTaskParameter.getSqlsToBeExecuted().get(1));
 }
}