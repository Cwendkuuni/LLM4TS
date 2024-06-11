import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

@Data
private static class TestConnectionConfig implements Serializable, ConnectionConfig {
String url = "jdbc:mysql://localhost:3306/testdb";
String username = "testuser";
String password = "testpassword";
String driverClassName = "com.mysql.cj.jdbc.Driver";
Integer connectionTimeoutSeconds = 30;
Integer queryTimeoutSeconds = 30;
}

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN new_col INT;");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SYSTEMPERSISTENT);
}

@Test
public void testIsContinueOnError() {
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
TestConnectionConfig connectionConfig = new TestConnectionConfig();
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testschema");
// Adding assertions based on the expected output
Assert.assertNotNull(subTaskParameters);
Assert.assertTrue(!subTaskParameters.isEmpty());
}

// Add more test cases for other methods and edge cases

// Add test cases for exception handling scenarios

}