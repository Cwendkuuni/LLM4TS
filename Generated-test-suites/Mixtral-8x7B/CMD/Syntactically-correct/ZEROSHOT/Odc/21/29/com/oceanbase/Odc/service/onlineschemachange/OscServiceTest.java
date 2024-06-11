import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

public class OscServiceTest {

 @Mock
 private DatabaseRepository databaseRepository;
 @Mock
 private ConnectionService connectionService;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ScheduleRepository scheduleRepository;

 @InjectMocks
 private OscService oscService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 Long id = 1L;
 Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
 Mockito.when(databaseRepository.findById(id)).thenReturn(database);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
 Assert.assertNotNull(result);
 Assert.assertEquals(id, result.getDatabaseId());
 }

 @Test
 public void testGetOscDatabaseInfoWithNonExistentDatabase() {
 Long id = 1L;
 Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
 Assert.assertNotNull(result);
 Assert.assertEquals(0L, result.getDatabaseId());
 Assert.assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

 Long scheduleId = 2L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 Mockito.when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 String oscParametersJson = JsonUtils.toJson(oscParameters);
 ReflectionTestUtils.setField(scheduleEntity, "jobParametersJson", oscParametersJson);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 String resultJson = JsonUtils.toJson(result);
 scheduleTask.setResultJson(resultJson);

 OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
 Assert.assertNotNull(oscSwapTable);
 Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidScheduleTask() {
 Long scheduleTaskId = 1L;
 Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidSchedule() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("1");
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

 Long scheduleId = 2L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 Mockito.when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithInvalidSwapTableType() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("1");
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

 Long scheduleId = 2L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 Mockito.when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
 String oscParametersJson = JsonUtils.toJson(oscParameters);
 ReflectionTestUtils.setField(scheduleEntity, "jobParametersJson", oscParametersJson);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 String resultJson = JsonUtils.toJson(result);
 scheduleTask.setResultJson(resultJson);

 oscService.swapTable(scheduleTaskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSwapTableWithManualSwapTableDisabled() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(scheduleTaskId);
 scheduleTask.setJobName("1");
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

 Long scheduleId = 2L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 Mockito.when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 String oscParametersJson = JsonUtils.toJson(oscParameters);
 ReflectionTestUtils.setField(scheduleEntity, "jobParametersJson", oscParametersJson);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 String resultJson = JsonUtils.toJson(result);
 scheduleTask.setResultJson(resultJson);

 oscService.swapTable(scheduleTaskId);
 }
}