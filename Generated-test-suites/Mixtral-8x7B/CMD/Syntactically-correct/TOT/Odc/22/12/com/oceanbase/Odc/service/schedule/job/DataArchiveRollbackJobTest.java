import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() throws Exception {
        jobExecutionContext = new JobExecutionContext(new MockJobExecution());
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        taskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        when(scheduleTaskRepository.findTaskUnitsByTaskIdAndJobType(taskEntity.getId(), JobType.DATA_ARCHIVE))
                .thenReturn(taskUnits);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testFindDataArchiveTaskByIdPresent() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        taskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        Optional<ScheduleTaskEntity> task =
                dataArchiveRollbackJob.findDataArchiveTaskById(rollbackParameters.getDataArchiveTaskId());
        assertEquals(Optional.of(taskEntity), task);
    }

    @Test
    public void testFindDataArchiveTaskByIdNotPresent() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());
        Optional<ScheduleTaskEntity> task =
                dataArchiveRollbackJob.findDataArchiveTaskById(rollbackParameters.getDataArchiveTaskId());
        assertEquals(Optional.empty(), task);
    }

    @Test
    public void testExecuteTaskSuccess() throws Exception {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        dataArchiveRollbackJob.executeTask(1L, taskUnits);
        assertEquals(1, taskUnits.size());
    }

    @Test(expected = Exception.class)
    public void testExecuteTaskFailure() throws Exception {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(null);
        dataArchiveRollbackJob.executeTask(1L, taskUnits);
    }

    @Test
    public void testGetTaskStatusAllSuccess() {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        TaskStatus status = dataArchiveRollbackJob.getTaskStatus(taskUnits);
        assertEquals(TaskStatus.DONE, status);
    }

    @Test
    public void testGetTaskStatusSomeFailure() {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.FAILED);
        TaskStatus status = dataArchiveRollbackJob.getTaskStatus(taskUnits);
        assertEquals(TaskStatus.FAILED, status);
    }

    class MockJobExecution extends JobExecutionContext {

        @Override
        public <T> T getResult() throws JobExecutionException {
            return (T) new Object();
        }
    }
}