
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob job;

    @BeforeEach
    public void setup() {
        job = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);
        when(context.getResult()).thenReturn(taskEntity);
        job.execute(context);
        verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTask() {
        Long taskId = 1L;
        List<DlmTask> taskUnits = new ArrayList<>();
        when(scheduleTaskRepository.updateStatusById(taskId, any())).thenReturn(null);
        job.executeTask(taskId, taskUnits);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, any());
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        TaskStatus taskStatus = job.getTaskStatus(taskUnits);
        assertEquals(TaskStatus.FAILED, taskStatus);
    }
}
