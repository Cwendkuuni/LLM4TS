import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setJobName("testJob");
        taskEntity.setJobGroup("testGroup");

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");

        taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.add(new DlmTask());

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", mockScheduleTaskRepository());
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "jobThread", Thread.currentThread());
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        when(mockScheduleTaskRepository().findById(anyString())).thenReturn(Optional.ofNullable(null));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(taskEntity.getId()));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFinished_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(mockScheduleTaskRepository().findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(taskEntity.getId()));
    }

    @Test
    public void testExecute_whenTaskUnitsIsNull_shouldDoNothing() throws JobExecutionException {
        dataArchiveTask.setResultJson(null);
        when(mockScheduleTaskRepository().findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.PREPARING, scheduleTaskRepository.getTaskStatus(taskUnits.get(0).getId()));
    }

    @Test
    public void testExecute_whenTaskUnitsIsEmpty_shouldDoNothing() throws JobExecutionException {
        dataArchiveTask.setResultJson("[]");
        when(mockScheduleTaskRepository().findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        Mockito.verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_whenEverythingIsFine_shouldExecuteTasks() throws JobExecutionException {
        when(mockScheduleTaskRepository().findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        for (DlmTask task : taskUnits) {
            assertEquals(TaskStatus.PREPARING, scheduleTaskRepository.getTaskStatus(task.getId()));
        }
    }

    private ScheduleTaskRepository mockScheduleTaskRepository() {
        ScheduleTaskRepository repository = mock(ScheduleTaskRepository.class);
        when(repository.findById(anyString()))
                .thenReturn(Optional.of(dataArchiveTask))
                .thenReturn(Optional.of(taskEntity));
        when(repository.getTaskStatus(anyString()))
                .thenReturn(TaskStatus.PREPARING);
        when(repository.updateStatusById(anyString(), any()))
                .thenAnswer(invocation -> {
                    taskEntity.setStatus((TaskStatus) invocation.getArguments()[1]);
                    return taskEntity;
                });
        when(repository.executeTask(anyString(), anyList()))
                .thenAnswer(invocation -> {
                    List<DlmTask> tasks = (List<DlmTask>) invocation.getArguments()[1];
                    tasks.forEach(task -> {
                        task.setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(),
                                taskEntity.getJobGroup(),
                                taskEntity.getId(),
                                tasks.size()));
                        task.setJobType(JobType.DELETE);
                        task.setStatus(TaskStatus.PREPARING);
                    });
                    return tasks;
                });
        return repository;
    }
}

class ScheduleTaskRepository {

    public Optional<ScheduleTaskEntity> findById(String id) {
        return Optional.empty();
    }

    public TaskStatus getTaskStatus(String id) {
        return null;
    }

    public ScheduleTaskEntity updateStatusById(String id, TaskStatus status) {
        return null;
    }

    public List<DlmTask> executeTask(String id, List<DlmTask> taskUnits) {
        return new ArrayList<>();
    }
}

class AbstractDlmJob extends QuartzJobBean {

    public Thread jobThread;

    public void executeTask(String id, List<DlmTask> taskUnits) {
    }

    public TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return null;
    }
}

class DataArchiveClearParameters {

    private String dataArchiveTaskId;

    public String getDataArchiveTaskId() {
        return dataArchiveTaskId;
    }

    public void setDataArchiveTaskId(String dataArchiveTaskId) {
        this.dataArchiveTaskId = dataArchiveTaskId;
    }
}