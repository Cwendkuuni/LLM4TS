import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobInstance;
import org.quartz.spi.OperableTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters dataArchiveRollbackParameters;
    private List<DlmTask> dlmTasks;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = ScheduleTaskEntity.builder()
                .id(1L)
                .jobName("testJob")
                .jobGroup("testGroup")
                .parametersJson("{\"dataArchiveTaskId\": 2}")
                .resultJson("[]")
                .status(TaskStatus.PREPARING)
                .build();

        dataArchiveRollbackParameters = DataArchiveRollbackParameters.builder()
                .dataArchiveTaskId(2L)
                .build();

        dlmTasks = new ArrayList<>();

        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduler", schedulerFactoryBean.getScheduler());
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_shouldPrepareTasksForRollback() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTasks.add(dlmTask);
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        for (DlmTask task : dlmTasks) {
            assertEquals(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(),
                            scheduleTaskEntity.getJobGroup(), scheduleTaskEntity.getId(), 1), task.getId());
            assertEquals(task.getTargetDatabaseId(), task.getSourceDatabaseId());
            assertEquals(task.getSourceDatabaseId(), task.getTargetDatabaseId());
            assertEquals(JobType.ROLLBACK, task.getJobType());
        }
    }

    @Test
    public void testExecute_whenTasksPrepared_shouldExecuteTasksAndUpdateTaskStatus() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTasks.add(dlmTask);
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        JobInstance jobInstance = mock(JobInstance.class);
        OperableTrigger operableTrigger = mock(OperableTrigger.class);
        when(operableTrigger.getKey()).thenReturn(new org.quartz.Trigger.TriggerKey("testJob", "testGroup"));
        when(scheduler.getJobDetail(jobInstance.getKey())).thenReturn(jobInstance);
        when(scheduler.getTrigger(jobInstance.getKey(), GroupMatcher.jobGroupEquals("testGroup")))
                .thenReturn(operableTrigger);
        when(scheduler.rescheduleJob(operableTrigger.getKey(), TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build())).thenReturn(operableTrigger);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertTrue(dataArchiveRollbackJob.getTaskStatus(dlmTasks).isAfter(TaskStatus.PREPARING));
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreDone_shouldReturnDONE() {
        dlmTasks.forEach(task -> task.setStatus(TaskStatus.DONE));

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreFailed_shouldReturnFAILED() {
        dlmTasks.forEach(task -> task.setStatus(TaskStatus.FAILED));

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTasksHaveMixedStatuses_shouldReturnRUNNING() {
        dlmTasks.get(0).setStatus(TaskStatus.DONE);
        dlmTasks.get(1).setStatus(TaskStatus.FAILED);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.RUNNING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTasksHaveSameStatus_shouldReturnThatStatus() {
        dlmTasks.forEach(task -> task.setStatus(TaskStatus.PREPARING));

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTasksAreEmpty_shouldReturnPREPARING() {
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(new ArrayList<>());
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}