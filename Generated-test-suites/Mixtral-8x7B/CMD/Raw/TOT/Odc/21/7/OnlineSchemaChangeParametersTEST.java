import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setup() {
 parameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testGettersAndSetters() {
 // Test typical use cases
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT)");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(300);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
 parameters.setDelimiter(";");

 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE test (id INT)", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(Integer.valueOf(300), parameters.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
 parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());

 // Test edge cases
 parameters.setSqlType(null);
 parameters.setSqlContent(null);
 parameters.setErrorStrategy(null);
 parameters.setLockTableTimeOutSeconds(null);
 parameters.setSwapTableNameRetryTimes(null);
 parameters.setOriginTableCleanStrategy(null);
 parameters.setDelimiter(null);

 assertEquals(null, parameters.getSqlType());
 assertEquals(null, parameters.getSqlContent());
 assertEquals(null, parameters.getErrorStrategy());
 assertEquals(null, parameters.getLockTableTimeOutSeconds());
 assertEquals(null, parameters.getSwapTableNameRetryTimes());
 assertEquals(null, parameters.getOriginTableCleanStrategy());
 assertEquals(null, parameters.getDelimiter());

 // Test error scenarios
 parameters.setSqlType("INVALID");
 parameters.setErrorStrategy(TaskErrorStrategy.INVALID);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.INVALID);

 try {
 parameters.getSqlType();
 fail("Expected an UnexpectedException.");
 } catch (UnexpectedException e) {
 // Expected behavior
 }

 try {
 parameters.getErrorStrategy();
 fail("Expected an UnexpectedException.");
 } catch (UnexpectedException e) {
 // Expected behavior
 }

 try {
 parameters.getOriginTableCleanStrategy();
 fail("Expected an UnexpectedException.");
 } catch (UnexpectedException e) {
 // Expected behavior
 }
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 // Test

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("MySQL");
String schema = "test";
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
assertTrue(subTaskParameters.isEmpty());

parameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255)");
parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(2, subTaskParameters.size());

parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(2, subTaskParameters.size());

// Test exception handling
parameters.setSqlContent(null);
try {
    parameters.generateSubTaskParameters(connectionConfig, schema);
    fail("Expected an UnexpectedException.");
} catch (UnexpectedException e) {
    // Expected behavior
}

}
}