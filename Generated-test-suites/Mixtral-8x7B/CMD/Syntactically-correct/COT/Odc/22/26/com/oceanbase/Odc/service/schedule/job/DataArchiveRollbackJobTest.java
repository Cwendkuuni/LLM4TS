import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    @Mock
    private Logger log;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity taskEntity;

    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws Exception {
        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(eq(rollbackParameters.getDataArchiveTaskId()));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_dataArchiveTaskFound() throws Exception {
        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});
        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.of(taskEntity));

        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.of(taskEntity));

        when(
                scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.of(taskEntity));

        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.of(taskEntity));

        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(eq(rollbackParameters.getDataArchiveTaskId()));
        verify(scheduleTaskRepository, never()).updateStatusById(eq(taskEntity.getId()), any());
        verify(log, times(1)).warn(anyString());
    }

    @Test
    public void testExecute_taskUnitsNotEmpty() throws Exception {
        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});
        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(Optional.of(taskEntity));

        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(taskEntity))
                .thenReturn(Optional.of(taskEntity))
                .thenReturn(Optional.of(taskEntity));

        doNothing().when(dataArchiveRollbackJob).executeTask(eq(taskEntity.getId()), any());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(eq(rollbackParameters.getDataArchiveTaskId()));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any());
        verify(JsonUtils, times(1)).fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}));
        verify(dataArchiveRollbackJob, times(1)).executeTask(eq(taskEntity.getId()), any());
        verify(log, never()).warn(anyString());
    }
}