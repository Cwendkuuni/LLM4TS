import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT";
TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
Integer lockTableTimeOutSeconds = 10;
Integer swapTableNameRetryTimes = 5;
OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
String delimiter = ";";
List<String> lockUsers = new ArrayList<>();
SwapTableType swapTableType = SwapTableType.TEMPORARY;

return new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, lockTableTimeOutSeconds,
swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);
}

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
assertEquals(";", parameters.getDelimiter());
assertEquals(lockUsers, parameters.getLockUsers());
assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
assertTrue(parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();

// Create a mock ConnectionConfig object
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
.generateSubTaskParameters(connectionConfig, schema);

// We only test if generateSubTaskParameters does not throw an exception
assertNotNull(subTaskParameters);
}

@Test
public void testSerialization() {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();

// Serialize the object
Serializable serializedParameters = (Serializable) parameters;

// Deserialize the object
OnlineSchemaChangeParameters deserializedParameters = (OnlineSchemaChangeParameters) serializedParameters;

// Check if the deserialized object is equal to the original object
assertEquals(parameters, deserializedParameters);
}

@Test
public void testGenerateSubTaskParametersWithEmptySqlContent() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

// Create a mock ConnectionConfig object
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

assertThrows(UnexpectedException.class,
() -> parameters.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullSqlType() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");

// Create a mock ConnectionConfig object
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

assertThrows(UnexpectedException.class,
() -> parameters.generateSubTaskParameters(connectionConfig, schema));
}

}