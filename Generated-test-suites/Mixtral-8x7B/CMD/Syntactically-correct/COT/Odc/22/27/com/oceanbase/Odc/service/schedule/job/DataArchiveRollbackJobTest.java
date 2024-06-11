import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;

@SpringBootTest
public class DataArchiveRollbackJobTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWithDataArchiveTaskFound() throws JobExecutionException {
        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(1L);
        dataArchiveTaskEntity.setResultJson("[{\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setId(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 1));
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE)).thenReturn(null);
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}