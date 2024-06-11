import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{}");
        scheduleTaskEntity.setResultJson("[]");
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 10, \"targetDatabaseId\": 20}]");

        taskUnits = JsonUtils.fromJson("[{\"id\": 1, \"sourceDatabaseId\": 10, \"targetDatabaseId\": 20}]",
                new TypeReference<List<DlmTask>>() {});

        dataArchiveRollbackJob.setScheduleTaskRepository(mockScheduleTaskRepository());
        dataArchiveRollbackJob.setJobThread(Thread.currentThread());
    }

    private AbstractDlmJob.ScheduleTaskRepository mockScheduleTaskRepository() {
        AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository = mock(AbstractDlmJob.ScheduleTaskRepository.class);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(null);
        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class))).thenReturn(null);

        return scheduleTaskRepository;
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusToFailed() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(3L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 3}");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository).updateStatusById(3L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenPrepareTasksForRollback() throws Exception {
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(3L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        for (DlmTask taskUnit : taskUnits) {
            Long temp = taskUnit.getSourceDatabaseId();
            taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(),
                    scheduleTaskEntity.getJobGroup(), scheduleTaskEntity.getId(), taskUnits.size()));
            taskUnit.setSourceDatabaseId(taskUnit.getTargetDatabaseId());
            taskUnit.setTargetDatabaseId(temp);
            taskUnit.setJobType(JobType.ROLLBACK);
            taskUnit.setStatus(taskUnit.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING);
        }

        Mockito.verify(scheduleTaskRepository).updateStatusById(2L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_whenTasksExecutedSuccessfully_thenUpdateStatusToDone() throws Exception {
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(3L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository).updateStatusById(2L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_whenTasksExecutedWithError_thenUpdateStatusToFailed() throws Exception {
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(3L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        doThrow(new Exception("test exception")).when(dataArchiveRollbackJob).executeTask(1L, taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository).updateStatusById(2L, TaskStatus.FAILED);
    }
}