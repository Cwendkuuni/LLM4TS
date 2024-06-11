import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private JobExecutionContext jobExecutionContext;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private JobExecutionContext context;

    @Before
    public void setup() {
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testExecute_TaskNotFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_Success() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        when(JsonUtils.fromJson(any(), any()))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_Failed() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(1);

        when(JsonUtils.fromJson(any(), any()))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_Exception() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        when(JsonUtils.fromJson(any(), any()))
                .thenThrow(new RuntimeException());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}