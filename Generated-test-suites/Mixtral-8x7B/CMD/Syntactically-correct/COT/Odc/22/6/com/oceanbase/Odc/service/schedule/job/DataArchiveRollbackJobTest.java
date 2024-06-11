import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob abstractDlmJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JsonUtils jsonUtils;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveRollbackJob.setJsonUtils(jsonUtils);
    }

    @Test
    public void testExecuteWithValidParameters() {
        // Arrange
        Long taskId = 1L;
        Long dataArchiveTaskId = 2L;
        String parametersJson = "{\"dataArchiveTaskId\": " + dataArchiveTaskId + "}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> taskUnits = generateDlmTasks(3);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);
        when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        // TODO: Add assertions based on the expected behavior of the method
    }

    @Test
    public void testExecuteWithInvalidDataArchiveTask() {
        // Arrange
        Long taskId = 1L;
        Long dataArchiveTaskId = 2L;
        String parametersJson = "{\"dataArchiveTaskId\": " + dataArchiveTaskId + "}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        // TODO: Add assertions based on the expected behavior of the method
    }

    @Test
    public void testExecuteWithEmptyTaskUnits() {
        // Arrange
        Long taskId = 1L;
        Long dataArchiveTaskId = 2L;
        String parametersJson = "{\"dataArchiveTaskId\": " + dataArchiveTaskId + "}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> taskUnits = generateDlmTasks(0);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);
        when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        // TODO: Add assertions based on the expected behavior of the method
    }

    private List<DlmTask> generateDlmTasks(int size) {
        // TODO: Implement the generation of the dlmTasks list based on the given size
        return null;
    }
}