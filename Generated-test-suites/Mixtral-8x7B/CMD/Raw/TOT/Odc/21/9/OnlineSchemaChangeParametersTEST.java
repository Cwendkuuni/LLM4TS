import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
 public void testGetSqlType() {
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
 }

 @Test
 public void testGetSqlContent() {
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
 assertEquals("ALTER TABLE table1 ADD column1 int", onlineSchemaChangeParameters.getSqlContent());
 }

 @Test
 public void testGetErrorStrategy() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
 }

 @Test
 public void testGetLockTableTimeOutSeconds() {
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
 assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
 }

 @Test
 public void testGetSwapTableNameRetryTimes() {
 onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
 assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
 }

 @Test
 public void testGetOriginTableCleanStrategy() {
 onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP,
 onlineSchemaChangeParameters.getOriginTableCleanStrategy());
 }

 @Test
 public void testGetDelimiter() {
 onlineSchemaChangeParameters.setDelimiter(";");
 assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
 }

 @Test
 public void testGetLockUsers() {
 List<String> lockUsers = Arrays.asList("user1", "user2");
 onlineSchemaChangeParameters.setLockUsers(lockUsers);
 assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
 }

 @Test
 public void testGetSwapTableType() {
 onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLESPACE);
 assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_WITH_SAME_TABLESPACE,
 onlineSchemaChangeParameters.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws UnexpectedException {
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();

 // Create a mock subTaskParameterFactory and set its return value
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 // Generate sub tasks with the given sqlContent
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
 "schema");

 // Check the generated sub tasks
 assertEquals(expected.size(), result.size());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_exception() throws UnexpectedException {
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

 // Set the subTaskParameterFactory to throw an exception
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenThrow(new UnexpectedException("Failed to generate subtask with parameter: " + onlineSchemaChangeParameters));

 // Generate sub tasks with the given sqlContent
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 }

 @Test
 public void testGenerateSubTaskParameters_noSql() throws UnexpectedException {
 onlineSchemaChangeParameters.setSqlContent("");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

 // Generate sub tasks with the given sqlContent
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
 "schema");

 // Check the generated sub tasks
 assertTrue(result.isEmpty());
 }
}