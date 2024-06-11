import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.calendar.AnnualCalendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private AbstractDlmJob abstractDlmJob;
    private Scheduler scheduler;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() throws SchedulerException {
        abstractDlmJob = mock(AbstractDlmJob.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected AbstractDlmJob getAbstractDlmJob() {
                return abstractDlmJob;
            }
        };

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        scheduleTaskEntity = ScheduleTaskEntity.builder().id(1L)
                .jobName("DataArchiveDeleteJob")
                .jobGroup("group1")
                .parametersJson("{}")
                .resultJson("[]")
                .status(TaskStatus.PREPARING)
                .build();

        dataArchiveTask = ScheduleTaskEntity.builder().id(2L)
                .jobName("DataArchiveTask")
                .jobGroup("group1")
                .parametersJson("{}")
                .resultJson("[]")
                .status(TaskStatus.DONE)
                .build();

        taskUnits = Arrays.asList(
                DlmTask.builder().id(1L).build(),
                DlmTask.builder().id(2L).build()
        );

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        when(abstractDlmJob.getScheduleTaskRepository())
                .thenReturn(new ScheduleTaskRepositoryMock());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity dataArchiveClearParameters =
                ScheduleTaskEntity.builder().id(1L)
                        .parametersJson("{\"dataArchiveTaskId\": 3}")
                        .build();

        when(abstractDlmJob.getScheduleTaskRepository()
                .findById(eq(3L)))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(abstractDlmJob.getScheduleTaskRepository(), times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity dataArchiveClearParameters =
                ScheduleTaskEntity.builder().id(1L)
                        .parametersJson("{\"dataArchiveTaskId\": 2}")
                        .build();

        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(abstractDlmJob.getScheduleTaskRepository()
                .findById(eq(2L)))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(abstractDlmJob.getScheduleTaskRepository(), times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_PrepareTasksForClear() throws Exception {
        ScheduleTaskEntity dataArchiveClearParameters =
                ScheduleTaskEntity.builder().id(1L)
                        .parametersJson("{\"dataArchiveTaskId\": 2}")
                        .build();

        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(abstractDlmJob.getScheduleTaskRepository()
                .findById(eq(2L)))
                .thenReturn(Optional.of(dataArchiveTask));

        when(abstractDlmJob.getJsonUtils()
                .fromJson(eq(dataArchiveTask.getResultJson()), any(TypeReference.class)))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(abstractDlmJob.getScheduleTaskRepository(), times(1))
                .updateStatusById(eq(1L), any(TaskStatus.class));

        verify(abstractDlmJob, times(1))
                .executeTask(eq(1L), anyList());
    }

    @Test
    public void testExecuteTask() throws Exception {
        when(abstractDlmJob.getDlmJobIdUtil()
                .generateHistoryJobId(eq("DataArchiveDeleteJob"), eq("group1"), eq(1L), eq(2)))
                .thenReturn("historyJobId1");

        when(abstractDlmJob.getDlmJobIdUtil()
                .generateHistoryJobId(eq("DataArchiveDeleteJob"), eq("group1"), eq(1L), eq(1)))
                .thenReturn("historyJobId2");

        dataArchiveDeleteJob.executeTask(1L, taskUnits);

        DlmTask dlmTask1 = taskUnits.get(0);
        DlmTask dlmTask2 = taskUnits.get(1);

        verify(abstractDlmJob, times(1))
                .addDlmTask(eq("historyJobId1"), eq(dlmTask1));

        verify(abstractDlmJob, times(1))
                .addDlmTask(eq("historyJobId2"), eq(dlmTask2));
    }

    @Test
    public void testGetTaskStatus() {
        when(abstractDlmJob.getDlmJobIdUtil()
                .generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("historyJobId1")
                .thenReturn("historyJobId2");

        taskUnits.get(0).setStatus(TaskStatus.FAILED);
        taskUnits.get(1).setStatus(TaskStatus.DONE);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    private static class ScheduleTaskRepositoryMock {
        public Optional<ScheduleTaskEntity> findById(Long id) {
            return Optional.ofNullable(id == 2 ? dataArchiveTask : null);
        }

        public void updateStatusById(Long id, TaskStatus status) {
            // No-op.
        }
    }
}