import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        taskEntity.setResultJson("[]");
        doReturn(taskEntity).when(dataArchiveRollbackJob).getTaskEntity(context);
        dataArchiveRollbackJob.execute(context);
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecuteInvalidContext() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        doReturn(null).when(dataArchiveRollbackJob).getTaskEntity(context);
        dataArchiveRollbackJob.execute(context);
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskEntity(context).getStatus());
    }

    @Test
    public void testExecuteNonExistentTask() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);
        doReturn(taskEntity).when(dataArchiveRollbackJob).getTaskEntity(context);
        doReturn(Optional.of(new ScheduleTaskEntity())).when(scheduleTaskRepository).findById(1L);
        dataArchiveRollbackJob.execute(context);
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskEntity(context).getStatus());
    }

    private ScheduleTaskEntity getTaskEntity(JobExecutionContext context) {
        return (ScheduleTaskEntity) context.getMergedJobDataMap().get(AbstractDlmJob.TASK_ENTITY);
    }
}