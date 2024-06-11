import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{}");
        scheduleTaskEntity.setResultJson("[]");
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void testExecute_withValidData_shouldExecuteSuccessfully() {
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, never()).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}