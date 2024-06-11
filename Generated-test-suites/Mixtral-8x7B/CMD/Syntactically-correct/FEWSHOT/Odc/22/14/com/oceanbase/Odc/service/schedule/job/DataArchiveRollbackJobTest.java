import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);

        jobExecutionContext = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setResultJson("[]");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        doReturn(rollbackParameters).when(dataArchiveRollbackJob).parseTaskParameters(taskEntity);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_prepareTasksForRollback_shouldConvertAndUpdateTaskList() throws JobExecutionException {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 3, \"targetDatabaseId\": 4}]");

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(DlmJobIdUtil.generateHistoryJobId(dataArchiveRollbackJob.getJobName(),
                dataArchiveRollbackJob.getJobGroup(), 1L, 1));

        doReturn(taskUnits).when(dataArchiveRollbackJob).prepareTasksForRollback(anyLong());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(1)).prepareTasksForRollback(2L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }

    private DataArchiveRollbackParameters parseTaskParameters(ScheduleTaskEntity taskEntity) {
        return JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveRollbackParameters.class);
    }
}