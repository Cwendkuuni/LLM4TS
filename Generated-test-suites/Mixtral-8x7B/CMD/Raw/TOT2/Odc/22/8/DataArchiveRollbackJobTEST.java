
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest extends AbstractDlmJobTest {

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);

        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteTypicalUseCase() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(2L);
        dataArchiveTaskEntity.setResultJson("[]");

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTaskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        String jsonTaskUnits = JsonUtils.toJson(taskUnits);

        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteTaskUnitsEmpty() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(2L);
        dataArchiveTaskEntity.setResultJson("[]");

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTaskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }
}
