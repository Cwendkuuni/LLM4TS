import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setup() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testSetAndGetSqlContent() {
String sqlContent = "ALTER TABLE table1 ADD column1 INT";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testSetAndGetErrorStrategy() {
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testSetAndGetLockTableTimeOutSeconds() {
Integer lockTableTimeOutSeconds = 60;
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testSetAndGetSwapTableNameRetryTimes() {
Integer swapTableNameRetryTimes = 3;
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testSetAndGetOriginTableCleanStrategy() {
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testSetAndGetDelimiter() {
String delimiter = ",";
onlineSchemaChangeParameters.setDelimiter(delimiter);
assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testSetAndGetLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testSetAndGetSwapTableType() {
SwapTableType swapTableType = SwapTableType.ALGORITHM;
onlineSchemaChangeParameters.setSwapTableType(swapTableType);
assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
}

@Test
public void testGenerateSubTaskParameters_withException() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
onlineSchemaChangeParameters.setSqlContent("INVALID SQL");
assertThrows(UnexpectedException.class,
() -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testSerialization() throws Exception {
Serializable serializedOnlineSchemaChangeParameters = new SerializationUtils().serialize(onlineSchemaChangeParameters);
OnlineSchemaChangeParameters deserializedOnlineSchemaChangeParameters = (OnlineSchemaChangeParameters) new SerializationUtils()
.deserialize(serializedOnlineSchemaChangeParameters);
assertEquals(onlineSchemaChangeParameters, deserializedOnlineSchemaChangeParameters);
}
}