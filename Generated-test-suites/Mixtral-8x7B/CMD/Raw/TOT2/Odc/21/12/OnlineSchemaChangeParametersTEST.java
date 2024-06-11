import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

@Data
private static class TestConnectionConfig implements Serializable, ConnectionConfig {
String url = "jdbc:mysql://localhost:3306/schema";
String username = "root";
String password = "password";
String driverClassName = "com.mysql.cj.jdbc.Driver";
String dialectType = "MYSQL";
}

@Test
public void testOnlineSchemaChangeParameters() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN);
onlineSchemaChangeParameters.setDelimiter(";");
List<String> lockUsers = new ArrayList<>();
lockUsers.add("testUser");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.BLACKHOLE);
Map<String, String> properties = new HashMap<>();
properties.put("key", "value");
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
TestConnectionConfig connectionConfig = new TestConnectionChangeParameters.TestConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 ADD COLUMN c2 INT");
onlineSchemaChangeParameters
.setErrorStrategy(TaskErrorStrategy.CONTINUE);
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParametersMap = new HashMap<>();
try (SubTaskParameterFactory factory = subTaskParameterFactory) {
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
TableIdentity key = new TableIdentity(subTaskParameter.getDatabaseName(),
subTaskParameter.getOriginTableName());
taskParametersMap.putIfAbsent(key, subTaskParameter);
}
}
Assert.assertEquals(1, taskParametersMap.size());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParameters_exception() throws SQLException, UnexpectedException {
TestConnectionConfig connectionConfig = new TestConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
}
}