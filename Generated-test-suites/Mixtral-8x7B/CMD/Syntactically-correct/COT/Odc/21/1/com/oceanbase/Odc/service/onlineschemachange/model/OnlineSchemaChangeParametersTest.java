import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
String sqlContent = "CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(255));";
ConnectionConfig connectionConfig = new ConnectionConfig();
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.CREATE, sqlContent,
TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, ";", new ArrayList<>(),
OnlineSchemaChangeParameters.SwapTableType.LOCAL);
}

@Test
public void testIsContinueOnError() {
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(new TableIdentity("test_db", "table1"),
new OnlineSchemaChangeScheduleTaskParameters("test_db", "table1", "table1_new", "CREATE"));
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, "test_db");
Assert.assertEquals(subTaskParameters.size(), 1);
Assert.assertEquals(subTaskParameters.get(0).getDatabaseName(), "test_db");
Assert.assertEquals(subTaskParameters.get(0).getOriginTableName(), "table1");
Assert.assertEquals(subTaskParameters.get(0).getNewTableName(), "table1_new");
Assert.assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().size(), 1);
Assert.assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().get(0),
"CREATE TABLE table1_new (id INT PRIMARY KEY, name VARCHAR(255));");
}

// Additional test cases for different edge cases and exception handling scenarios

@Test(expected = AssertionError.class)
public void testGenerateSubTaskParameters_nullConnectionConfig() throws SQLException, UnexpectedException {
onlineSchemaChangeParameters.generateSubTaskParameters(null, "test_db");
}

@Test(expected = AssertionError.class)
public void testGenerateSubTaskParameters_emptySchema() throws SQLException, UnexpectedException {
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
}

@Test(expected = AssertionError.class)
public void testGenerateSubTaskParameters_nullSqlContent() throws SQLException, UnexpectedException {
onlineSchemaChangeParameters.setSqlContent(null);
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_db");
}

@Test
public void testGenerateSubTaskParameters_handleUnexpectedException() throws SQLException, UnexpectedException {
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "test_db") {
@Override
protected OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType)
throws SQLException {
throw new SQLException("Forced error for testing");
}
};

try {
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_db");
} catch (UnexpectedException e) {
Assert.assertEquals(e.getMessage(),
"Failed to generate subtasks with parameter: OnlineSchemaChangeParameters{sqlType=CREATE, sqlContent=CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(255));, errorStrategy=CONTINUE, lockTableTimeOutSeconds=60, swapTableNameRetryTimes=3, originTableCleanStrategy=KEEP, delimiter=;, lockUsers=[], swapTableType=LOCAL}");
}
}

// Add more test cases for other methods and edge cases in the OnlineSchemaChangeParameters class

}