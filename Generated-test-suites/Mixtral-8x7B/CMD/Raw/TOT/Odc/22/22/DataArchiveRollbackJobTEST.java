import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    private ScheduleTaskEntity taskEntity;

    private DataArchiveRollbackParameters rollbackParameters;

    private List<DlmTask> dlmTasks;

    @Before
    public void setup() throws JobExecutionException {
        // Initialize the mocked repository
        initMockedRepository();

        // Initialize the jobExecutionContext
        jobExecutionContext = new JobExecutionContext();

        // Initialize the taskEntity
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        // Initialize the dlmTasks
        dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setJobName("testJob");
        dlmTask1.setJobGroup("testGroup");
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.ARCHIVE);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setJobName("testJob");
        dlmTask2.setJobGroup("testGroup");
        dlmTask2.setSourceDatabaseId(3L);
        dlmTask2.setTargetDatabaseId(4L);
        dlmTask2.setJobType(JobType.ARCHIVE);
        dlmTask2.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);
    }

    @Test
    public void testExecute_ValidParameters_TaskStatusRollback() throws Exception {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.ROLLBACK)).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.ROLLBACK, taskEntity.getStatus());
    }

    @Test
    public void testExecute_InvalidParameters_TaskStatusFailed() throws Exception {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.ofNullable(null));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_NoDataArchiveTask_TaskStatusFailed() throws Exception {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.ofNullable(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);

        DataArchiveRollbackParameters rollbackParametersWithNoDataArchiveTaskId = new DataArchiveRollbackParameters();
        rollbackParametersWithNoDataArchiveTaskId.setDataArchiveTaskId(null);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParametersWithNoDataArchiveTaskId));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    private void initMockedRepository() {
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(null);
        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class))).thenReturn(null);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
    }
}