import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @BeforeEach
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob(abstractDlmJob, scheduleTaskRepository);
    }

    @Test
    public void testExecuteWithValidInput() throws Exception {
        // Given
        JobExecutionContext jobExecutionContext = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(UUID.randomUUID().toString());
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("12345");
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
        when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(dataArchiveTaskOption);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(taskEntity.getId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    // Add test cases for null and empty input parameters here

}