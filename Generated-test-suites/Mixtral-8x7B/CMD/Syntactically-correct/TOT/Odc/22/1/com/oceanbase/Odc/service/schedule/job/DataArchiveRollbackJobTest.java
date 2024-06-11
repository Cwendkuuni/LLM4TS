import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(null);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}