import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
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
public void setup() {
String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));";
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent(sqlContent);
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
taskParameter.setDatabaseName(schema);
taskParameter.setOriginTableName("test_table");
taskParameter.setNewTableName("new_test_table");
List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = new ArrayList<>();
taskParameters.add(taskParameter);
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();
expectedTaskParameters.put(new TableIdentity(schema, "test_table"), taskParameter);
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
onlineSchemaChangeParameters.setSqlType(sqlType);
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SHARED_LOCK);
onlineSchemaChangeParameters.setSqlContent(DdlUtils.replaceTableName(sqlContent, taskParameter.getNewTableName(),
subTaskParameterFactory.getDialectType(), sqlType));
List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Assert.assertEquals(expectedTaskParameters.values(), result);
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SHARED_LOCK);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE non_existent_table ADD COLUMN new_column INT;");
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}