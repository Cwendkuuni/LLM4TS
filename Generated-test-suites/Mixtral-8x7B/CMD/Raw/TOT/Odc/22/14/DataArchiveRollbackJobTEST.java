
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest extends AbstractDlmJobTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setStatus(TaskStatus.PREPARING);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);
        when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(taskEntity));
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(3L);
        taskUnit.setJobType(JobType.ARCHIVE);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findAllByJobNameAndJobGroupAndStatusIn(anyString(), anyString(), any()))
                .thenReturn(taskUnits);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).findById(10L);
        verify(scheduleTaskRepository, times(1)).findAllByJobNameAndJobGroupAndStatusIn(anyString(), anyString(),
                any());
        verify(scheduleTaskRepository, times(1)).saveAll(anyList());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}
