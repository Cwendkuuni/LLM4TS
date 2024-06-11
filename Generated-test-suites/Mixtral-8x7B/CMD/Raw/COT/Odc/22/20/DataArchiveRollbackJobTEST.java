import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.simpl.PropertySettingJobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;

    private JobFactory jobFactory;

    private Scheduler scheduler;

    @Before
    public void setup() throws SchedulerException {
        jobFactory = new PropertySettingJobFactory();
        scheduler = SchedulerFactory.getDefaultScheduler();
        scheduler.start();

        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenFastFailedAndUpdateStatus() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(20L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenPrepareTasksForRollback() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(10L);
        dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 1, \"targetDatabaseId\": 2}]");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        List<DlmTask> taskUnits = JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {
        });
        for (DlmTask taskUnit : taskUnits) {
            Long temp = taskUnit.getSourceDatabaseId();
            assertEquals(DlmJobIdUtil.generateHistoryJobId(jobExecutionContext.getJobDetail().getKey().getName(),
                    jobExecutionContext.getJobDetail().getKey().getGroup(),
                    jobExecutionContext.getMergedJobDataMap().getLong("scheduleTaskId"),
                    taskUnits.size()),
                    taskUnit.getId());
            assertEquals(jobExecutionContext.getMergedJobDataMap().getLong("targetDatabaseId"),
                    taskUnit.getSourceDatabaseId());
            assertEquals(jobExecutionContext.getMergedJobDataMap().getLong("sourceDatabaseId"),
                    taskUnit.getTargetDatabaseId());
            assertEquals(JobType.ROLLBACK, taskUnit.getJobType());
            assertEquals(TaskStatus.DONE, taskUnit.getStatus());
        }
    }

    @Test
    public void testExecute_whenTaskUnitsPrepared_thenExecuteTaskAndUpdateStatus() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(10L);
        dataArchiveTask.setResultJson("[]");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.NO_TASKS, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_whenTaskUnitsExecuted_thenUpdateStatus() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(10L);
        dataArchiveTask.setResultJson("[]");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.NO_TASKS, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testGetTaskStatus_whenAllTasksSucceeded_thenReturnSUCCESS() {
        List<DlmTask> taskUnits = createTaskUnits(true, 5);
        assertEquals(TaskStatus.SUCCESS, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_whenAtLeastOneTaskFailed_thenReturnFAILED() {
        List<DlmTask> taskUnits = createTaskUnits(false, 5);
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_whenNoTasks_thenReturnNO_TASKS() {
        List<DlmTask> taskUnits = createTaskUnits(true, 0);
        assertEquals(TaskStatus.NO_TASKS, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    private List<DlmTask> createTaskUnits(boolean succeeded, int count) {
        for (int i = 0; i < count; i++) {
            DlmTask taskUnit = new DlmTask();
            taskUnit.setStatus(succeeded ? TaskStatus.SUCCESS : TaskStatus.FAILED);
            taskUnits.add(taskUnit);
        }
        return taskUnits;
    }
}