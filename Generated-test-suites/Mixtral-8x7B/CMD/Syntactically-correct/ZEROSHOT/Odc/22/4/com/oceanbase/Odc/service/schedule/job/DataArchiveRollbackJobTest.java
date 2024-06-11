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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithMissingDataArchiveTask() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById(1L).get().getStatus());
    }

    @Test
    public void testExecuteWithPresentDataArchiveTask() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());
        String tasksJson = JsonUtils.toJson(dlmTasks);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(tasksJson);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.DONE, scheduleTaskRepository.findById(1L).get().getStatus());
    }

    @Test
    public void testExecuteWithNullDlmTasks() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("null");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById(1L).get().getStatus());
    }
}