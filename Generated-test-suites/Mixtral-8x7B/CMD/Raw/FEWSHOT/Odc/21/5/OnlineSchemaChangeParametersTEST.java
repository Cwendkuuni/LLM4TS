import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");

OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table2 ADD COLUMN c2 INT;");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

OnlineSchemaChangeScheduleTaskParameters taskParameters1 = new OnlineSchemaChangeScheduleTaskParameters();
taskParameters1.setDatabaseName("db1");
taskParameters1.setOriginTableName("table1");
taskParameters1.setNewTableName("new_table1");
List<String> sqlsToBeExecuted1 = new ArrayList<>();
sqlsToBeExecuted1.add("ALTER TABLE table1 ADD COLUMN c1 INT");
taskParameters1.setSqlsToBeExecuted(sqlsToBeExecuted1);

OnlineSchemaChangeScheduleTaskParameters taskParameters2 = new OnlineSchemaChangeScheduleTaskParameters();
taskParameters2.setDatabaseName("db2");
taskParameters2.setOriginTableName("table2");
taskParameters2.setNewTableName("new_table2");
List<String> sqlsToBeExecuted2 = new ArrayList<>();
sqlsToBeExecuted2.add("ALTER TABLE table2 ADD COLUMN c2 INT");
taskParameters2.setSqlsToBeExecuted(sqlsToBeExecuted2);

Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();
expectedTaskParameters.put(new TableIdentity("db1", "table1"), taskParameters1);
expectedTaskParameters.put(new TableIdentity("db2", "table2"), taskParameters2);

List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
"schema");

assertEquals(expectedTaskParameters.values(), result);
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(null);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

}