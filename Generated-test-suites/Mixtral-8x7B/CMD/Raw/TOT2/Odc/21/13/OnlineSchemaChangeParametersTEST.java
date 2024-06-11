import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testGetSqlContent() {
String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testGetErrorStrategy() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testGetLockTableTimeOutSeconds() {
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
Assert.assertEquals(10, onlineSchemaChangeParameters.getLockTableTimeOutSeconds().intValue());
}

@Test
public void testGetSwapTableNameRetryTimes() {
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
Assert.assertEquals(5, onlineSchemaChangeParameters.getSwapTableNameRetryTimes().intValue());
}

@Test
public void testGetOriginTableCleanStrategy() {
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
Assert.assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testGetDelimiter() {
onlineSchemaChangeParameters.setDelimiter(";");
Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testGetLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testGetSwapTableType() {
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);
Assert.assertEquals(SwapTableType.SWAP_TABLE, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Assert.assertNotNull(subTaskParameters);
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws SQLException, UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
onlineSchemaChangeParameters.setSqlContent(null);
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}

@Test
public void testSerialization() {
OnlineSchemaChangeParameters original = new OnlineSchemaChangeParameters();
OnlineSchemaChangeParameters copy;
try {
copy = SerializationUtils.clone(original);
Assert.assertEquals(original, copy);
} catch (IOException e) {
Assert.fail("Failed to serialize/deserialize: " + e.getMessage());
}
}

}