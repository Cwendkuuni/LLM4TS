import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob job;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        job = new DataArchiveRollbackJob() {
            @Override
            ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_validInputs() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = List.of(new DlmTask());
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        // When
        job.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_missingDataArchiveTask() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        job.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testDeserializeTaskUnits() {
        // Given
        String json = "{\"data\":[{\"id\":1,\"name\":\"task1\"}]}";

        // When
        List<DlmTask> taskUnits = JsonUtils.fromJson(json, new TypeReference<List<DlmTask>>() {});

        // Then
        assertFalse(taskUnits.isEmpty());
        assertEquals(1, taskUnits.size());
        assertEquals(1L, taskUnits.get(0).getId().longValue());
    }
}