import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = DataArchiveRollbackJob.class)
public class DataArchiveRollbackJobTest {

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenTaskStatusIsFailed()
            throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
    }

    // ... More test cases for other methods and scenarios
}