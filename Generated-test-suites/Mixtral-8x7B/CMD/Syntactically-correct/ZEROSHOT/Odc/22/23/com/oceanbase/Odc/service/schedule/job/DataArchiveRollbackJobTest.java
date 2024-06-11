import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = new JobExecutionContext();
        jobExecutionContext.setResult(new ScheduleTaskEntity());

        when(abstractDlmJob.getJobThread()).thenReturn(Thread.currentThread());
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String jsonParameters = JsonUtils.toJson(rollbackParameters);
        ((ScheduleTaskEntity) jobExecutionContext.getResult()).setParametersJson(jsonParameters);

        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]",
                new TypeReference<List<DlmTask>>() {
                });
        ((ScheduleTaskEntity) jobExecutionContext.getResult()).setResultJson(JsonUtils.toJson(dlmTasks));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        TaskStatus taskStatus = TaskStatus.DONE;
        Assert.assertEquals(taskStatus, ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }

    @Test
    public void testExecuteFailed() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String jsonParameters = JsonUtils.toJson(rollbackParameters);
        ((ScheduleTaskEntity) jobExecutionContext.getResult()).setParametersJson(jsonParameters);

        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        TaskStatus taskStatus = TaskStatus.FAILED;
        Assert.assertEquals(taskStatus, ((ScheduleTaskEntity) jobExecutionContext.getResult()).getStatus());
    }

    @Test
    public void testExecuteTask() throws JobExecutionException {
        DataArchiveRollbackJob dataArchiveRollbackJobSpy = spy(dataArchiveRollbackJob);
        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]",
                new TypeReference<List<DlmTask>>() {
                });

        doNothing().when(dataArchiveRollbackJobSpy).executeTask(any(), any());
        dataArchiveRollbackJobSpy.executeTask(1L, dlmTasks);
        verify(dataArchiveRollbackJobSpy).executeTask(1L, dlmTasks);
    }

    @Test
    public void testGetTaskStatus() throws JobExecutionException {
        DataArchiveRollbackJob dataArchiveRollbackJobSpy = spy(dataArchiveRollbackJob);
        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]",
                new TypeReference<List<DlmTask>>() {
                });

        when(dataArchiveRollbackJobSpy.getTaskStatus(any())).thenReturn(TaskStatus.DONE);
        TaskStatus taskStatus = dataArchiveRollbackJobSpy.getTaskStatus(dlmTasks);
        Assert.assertEquals(taskStatus, TaskStatus.DONE);
        verify(dataArchiveRollbackJobSpy).getTaskStatus(dlmTasks);
    }
}