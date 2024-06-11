import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private static final Logger logger = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jobExecutionContext = createMockJobExecutionContext();
        doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed()
            throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_shouldExecuteTaskAndUpdateTaskStatus()
            throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(
                Optional.of(scheduleTaskEntity));
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(dlmTasks);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    private JobExecutionContext createMockJobExecutionContext() {
        JobExecutionContext jobExecutionContext = new JobExecutionContext(null);
        jobExecutionContext.setResult(new ScheduleTaskEntity());
        return jobExecutionContext;
    }
}