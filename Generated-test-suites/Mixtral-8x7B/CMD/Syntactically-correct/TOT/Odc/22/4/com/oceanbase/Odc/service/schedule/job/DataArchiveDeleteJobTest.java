import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
        jobExecutionContext = mock(JobExecutionContext.class);

        doReturn(Optional.of(mock(ScheduleTaskEntity.class))).when(scheduleTaskRepository).findById(any());
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);

        doReturn(taskEntity).when(scheduleTaskRepository).findById(taskEntity.getId());

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(mock(DlmTask.class));

        doReturn(taskUnits).when(dataArchiveDeleteJob).executeTask(taskEntity.getId(), taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}