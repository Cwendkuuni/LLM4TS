import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleSchedulerFactory;
import org.quartz.simpl.RMISchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.simpl.PropertySetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);
 private Scheduler scheduler;
 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveRollbackParameters rollbackParameters;
 private List<DlmTask> taskUnits;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() throws SchedulerException {
 Properties properties = new Properties();
 properties.setProperty("org.quartz.threadPool.threadCount", "5");
 properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
 SchedulerFactory schedulerFactory = new SimpleSchedulerFactory(properties);
 scheduler = schedulerFactory.getScheduler();
 scheduler.start();

 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("DataArchiveRollbackJob");
 scheduleTaskEntity.setJobGroup("DEFAULT");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 taskUnits = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());

 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute() throws Exception {
 JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

 // Test when data archive task not found
 Optional<ScheduleTaskEntity> emptyDataArchiveTask = Optional.empty();
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(emptyDataArchiveTask);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());

 // Test when data archive task found
 Optional<ScheduleTaskEntity> dataArchiveTask =
 Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(dataArchiveTask);

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setSourceDatabaseId(1L);
 dlmTask1.setTargetDatabaseId(2L);
 taskUnits.set(0, dlmTask1);

 when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED))
 .thenReturn(true);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());

 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setSourceDatabaseId(2L);
 dlmTask2.setTargetDatabaseId(1L);
 taskUnits.set(0, dlmTask2);

 when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING))
 .thenReturn(true);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());

 }
}