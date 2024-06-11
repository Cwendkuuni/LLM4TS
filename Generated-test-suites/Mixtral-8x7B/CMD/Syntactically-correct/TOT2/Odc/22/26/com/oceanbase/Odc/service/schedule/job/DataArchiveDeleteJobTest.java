import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private Scheduler scheduler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() throws SchedulerException {
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduler", scheduler);

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(new ScheduleTaskEntity());
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_setsFailedStatus() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("some-id");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone_setsFailedStatus() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("some-id");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("some-data-archive-id");
        dataArchiveTask.setStatus(TaskStatus.CREATED);

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenPreparingTasks_executesTasksAndUpdatesStatus() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("some-id");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("some-task-id");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("some-data-archive-id");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
                .thenReturn(taskEntity);

        when(scheduleTaskRepository.save(taskEntity))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).save(taskEntity);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FINISHED);
    }

    @Test
    public void testExecute_whenPreparingTasks_generatesCorrectJobIds() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("some-id");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("some-task-id");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("some-data-archive-id");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
                .thenReturn(taskEntity);

        when(scheduleTaskRepository.save(taskEntity))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(dlmTasks);

        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("some-job-id");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(DlmJobIdUtil, times(dlmTasks.size()))
                .generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(), taskEntity.getId(), eq(dlmTasks.size()));
    }
}