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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = DataArchiveRollbackJob.class)
public class DataArchiveRollbackJobTest {

    @MockBean
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        scheduleTaskEntity.setResultJson("[]");

        context.setResult(scheduleTaskEntity);

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteFailed() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": \"invalid\"}");
        scheduleTaskEntity.setResultJson("[]");

        context.setResult(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
}