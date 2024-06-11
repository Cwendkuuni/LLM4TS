import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob abstractDlmJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");

        jobExecutionContext.setResult(taskEntity);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTaskEntity = mock(ScheduleTaskEntity.class);
        when(dataArchiveTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");

        List<DlmTask> taskUnits = mock(List.class);

        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getResultJson()).thenReturn("[]");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));
        when(JsonUtils.fromJson(dataArchiveTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        when(abstractDlmJob.executeTask(any(), any())).thenReturn(null);

        jobExecutionContext.setResult(taskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // add assertions here
    }
}