import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Test;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

@Test
public void testOnlineSchemaChangeParameters() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

// Test default values
Assert.assertEquals(onlineSchemaChangeParameters.getSqlType(), OnlineSchemaChangeSqlType.ALTER);
Assert.assertEquals(onlineSchemaChangeParameters.getSqlContent(), "");
Assert.assertEquals(onlineSchemaChangeParameters.getErrorStrategy(), TaskErrorStrategy.TERMINATE);
Assert.assertEquals(onlineSchemaChangeParameters.getLockTableTimeOutSeconds(), 0);
Assert.assertEquals(onlineSchemaChangeParameters.getSwapTableNameRetryTimes(), 0);
Assert.assertEquals(onlineSchemaChangeParameters.getOriginTableCleanStrategy(), OriginTableCleanStrategy.KEEP);
Assert.assertEquals(onlineSchemaChangeParameters.getDelimiter(), ";");
Assert.assertEquals(onlineSchemaChangeParameters.getLockUsers(), null);
Assert.assertEquals(onlineSchemaChangeParameters.getSwapTableType(), SwapTableType.ALTER);
Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

Assert.assertEquals(subTaskParameters.size(), 2);
Assert.assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().size(), 1);
Assert.assertEquals(subTaskParameters.get(1).getSqlsToBeExecuted().size(), 1);
}

@Test
public void testGenerateSubTaskParametersWithCustomDelimiter() throws SQLException, UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT);--delimiter-- ALTER TABLE test ADD COLUMN name VARCHAR(255);");
onlineSchemaChangeParameters.setDelimiter("--delimiter--");
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

Assert.assertEquals(subTaskParameters.size(), 2);
Assert.assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().size(), 1);
Assert.assertEquals(subTaskParameters.get(1).getSqlsToBeExecuted().size(), 1);
}

@Test
public void testGenerateSubTaskParametersWithLockUsers() throws SQLException, UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new

OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.getLockUsers().add("user1");
onlineSchemaChangeParameters.getLockUsers().add("user2");
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

Assert.assertEquals(subTaskParameters.size(), 2);
Assert.assertEquals(subTaskParameters.get(0).getLockUsers(), new ArrayList<>());
Assert.assertEquals(subTaskParameters.get(1).getLockUsers(), new ArrayList<>());
Assert.assertEquals(subTaskParameters.get(0).getLockUsers().size(), 2);
Assert.assertEquals(subTaskParameters.get(1).getLockUsers().size(), 2);
Assert.assertEquals(subTaskParameters.get(0).getLockUsers().get(0), "user1");
Assert.assertEquals(subTaskParameters.get(0).getLockUsers().get(1), "user2");
Assert.assertEquals(subTaskParameters.get(1).getLockUsers().get(0), "user1");
Assert.assertEquals(subTaskParameters.get(1).getLockUsers().get(1), "user2");
}
}