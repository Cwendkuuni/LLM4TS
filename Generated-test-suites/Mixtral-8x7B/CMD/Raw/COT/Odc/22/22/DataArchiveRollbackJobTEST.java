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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
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

    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobGroup("testGroup");
        taskEntity.setJobName("testJob");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 100}");
        taskEntity.setResultJson("[]");

        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(100L);

        taskUnits = JsonUtils.fromJson("[{\"id\": 1, \"sourceDatabaseId\": 10, \"targetDatabaseId\": 20}]",
                new TypeReference<List<DlmTask>>() {});

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(eq(100L))).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class))).thenReturn(null);
    }

    @Test
    public void testExecute_Success() {
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(100L));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(eq(100L))).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(100L));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_PrepareTasksForRollback() {
        when(scheduleTaskRepository.findById(eq(100L))).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        DlmTask taskUnit = taskUnits.get(0);
        Long temp = taskUnit.getSourceDatabaseId();
        taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                taskEntity.getId(),
                taskUnits.size()));
        taskUnit.setSourceDatabaseId(taskUnit.getTargetDatabaseId());
        taskUnit.setTargetDatabaseId(temp);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnit.setStatus(taskUnit.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING);

        verify(scheduleTaskRepository, times(1)).findById(eq(100L));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }

    @Test
    public void testDlJobIdUtil_GenerateHistoryJobId() {
        String jobName = "testJob";
        String jobGroup = "testGroup";
        Long taskId = 1L;
        int size = 10;

        String result = DlmJobIdUtil.generateHistoryJobId(jobName, jobGroup, taskId, size);

        assertEquals(String.format("%s-%s-%s-%d", jobName, jobGroup, taskId, size), result);
    }

    @Test
    public void testGetTaskStatus_AllTasksSucceeded() {
        taskUnits.get(0).setStatus(TaskStatus.DONE);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_TasksPartiallySucceeded() {
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        taskUnits.get(1).setStatus(TaskStatus.PREPARING);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_AllTasksFailed() {
        taskUnits.get(0).setStatus(TaskStatus.FAILED);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }
}