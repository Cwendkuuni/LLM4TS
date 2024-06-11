import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

@Mock
private ConnectionConfig connectionConfig;

@Mock
private SubTaskParameterFactory subTaskParameterFactory;

@InjectMocks
private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testIsContinueOnError_Continue() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testIsContinueOnError_NotContinue() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters_Normal() throws Exception {
String schema = "test_schema";
List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, result.size());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParameters_Exception() throws Exception {
when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
.thenThrow(new Exception("Expected exception"));
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
}

@Test
public void testGenerateSubTaskParameters_MultipleSubTasks() throws Exception {
String sqlContent = "ALTER TABLE t1 ADD COLUMN c1 INT; ALTER TABLE t1 ADD COLUMN c2 DATE;";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
.thenReturn(getOnlineSchemaChangeScheduleTaskParameters());

List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
assertEquals(2, result.size());
}

private List<OnlineSchemaChangeScheduleTaskParameters> getOnlineSchemaChangeScheduleTaskParameters() {
OnlineSchemaChangeScheduleTaskParameters parameter1 = new OnlineSchemaChangeScheduleTaskParameters();
parameter1.setDatabaseName("test_schema");
parameter1.setOriginTableName("t1");
parameter1.setNewTableName("t1_new");

List<String> sqlsToBeExecuted1 = new ArrayList<>();
sqlsToBeExecuted1.add("ALTER TABLE t1 ADD COLUMN c1 INT");
parameter1.setSqlsToBeExecuted(sqlsToBeExecuted1);

OnlineSchemaChangeScheduleTaskParameters parameter2 = new OnlineSchemaChangeScheduleTaskParameters();
parameter2.setDatabaseName("test_schema");
parameter2.setOriginTableName("t1");
parameter2.setNewTableName("t1_new");

List<String> sqlsToBeExecuted2 = new ArrayList<>();
sqlsToBeExecuted2.add("ALTER TABLE t1 ADD COLUMN c2 DATE");
parameter2.setSqlsToBeExecuted(sqlsToBeExecuted2);

List<OnlineSchemaChangeScheduleTaskParameters> parameters = new ArrayList<>();
parameters.add(parameter1);
parameters.add(parameter2);

return parameters;
}
}