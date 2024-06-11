import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private Scheduler scheduler;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() throws SchedulerException {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduler = mock(Scheduler.class);
        jobExecutionContext = mock(JobExecutionContext.class);

        when(jobExecutionContext.getResult()).thenReturn(mock(ScheduleTaskEntity.class));

        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected Scheduler getScheduler() {
                return scheduler;
            }

            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed()
            throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone_thenUpdateTaskStatusToFailed()
            throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }

    @Test
    public void testExecute_whenPrepareTasksForClearSuccess_thenUpdateTaskStatus()
            throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setJobType(JobType.DELETE);
        dlmTask.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = List.of(dlmTask);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
                .thenReturn(taskUnits);

        when(scheduler.getJobKeys(any(GroupMatcher.class)))
                .thenReturn(null);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.PREPARING,
                ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }

    @Test
    public void testExecute_whenExecuteTaskSuccess_thenUpdateTaskStatus()
            throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setJobType(JobType.DELETE);
        dlmTask.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = List.of(dlmTask);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
                .thenReturn(taskUnits);

        when(scheduler.getJobKeys(any(GroupMatcher.class)))
                .thenReturn(null);

        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("historyJobId");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.PREPARING,
                ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }
}