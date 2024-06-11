import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    @Mock
    private Scheduler scheduler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() throws SchedulerException {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob(scheduler, scheduleTaskRepository);

        when(scheduler.getJobKeys(GroupMatcher.groupContains("DATA_ARCHIVE_ROLLBACK"))).thenReturn(
                mock(List.class));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createJobExecutionContext(1L, "{\"dataArchiveTaskId\": 10L}");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(jobExecutionContext.getMergedJobDataMap().getLong("taskId"), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTaskUnitsIsNull_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createJobExecutionContext(1L, "{\"dataArchiveTaskId\": 10L}",
                null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(jobExecutionContext.getMergedJobDataMap().getLong("taskId"), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTaskUnitsIsEmpty_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createJobExecutionContext(1L, "{\"dataArchiveTaskId\": 10L}",
                mock(List.class));

        when(jobExecutionContext.getMergedJobDataMap().get("taskUnits")).thenReturn(mock(List.class));
        when(jobExecutionContext.getMergedJobDataMap().get("taskUnits")).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(jobExecutionContext.getMergedJobDataMap().getLong("taskId"), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTaskUnitsArePrepared_shouldUpdateTaskStatusToPrepared() throws JobExecutionException {
        Long taskId = 1L;
        List<DlmTask> taskUnits = createTaskUnits(5, TaskStatus.PREPARING);

        JobExecutionContext jobExecutionContext = createJobExecutionContext(taskId, "{\"dataArchiveTaskId\": 10L}",
                taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskId, TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_whenTaskUnitsAreDone_shouldUpdateTaskStatusToDone() throws
            JobExecutionException {
        Long taskId = 1L;
        List<DlmTask> taskUnits = createTaskUnits(5, TaskStatus.DONE);

        JobExecutionContext jobExecutionContext = createJobExecutionContext(taskId, "{\"dataArchiveTaskId\": 10L}",
                taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskId, TaskStatus.DONE);
    }

    private List<DlmTask> createTaskUnits(int count, TaskStatus status) {
        return JsonUtils.toJsonList(
                List.of(
                        DlmTask.builder()
                                .sourceDatabaseId(1L)
                                .targetDatabaseId(2L)
                                .status(status)
                                .jobType(JobType.DATA_ARCHIVE)
                                .build()),
                new TypeReference<List<DlmTask>>() {});
    }

    private JobExecutionContext createJobExecutionContext(Long taskId, String parametersJson,
                                                          List<DlmTask> taskUnits) throws SchedulerException {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

        when(jobExecutionContext.getMergedJobDataMap().getLong("taskId")).thenReturn(taskId);
        when(jobExecutionContext.getMergedJobDataMap().get("parametersJson")).thenReturn(parametersJson);

        if (taskUnits != null) {
            ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);

            when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

            when(jobExecutionContext.getMergedJobDataMap().get("taskEntity")).thenReturn(taskEntity);
        }

        return jobExecutionContext;
    }
}