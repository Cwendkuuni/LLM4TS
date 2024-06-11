import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        scheduleTaskEntity.setResultJson("[]");
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
    }

    @Test
    public void testExecute() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 10, \"targetDatabaseId\": 20}]");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"id\": 1, \"sourceDatabaseId\": 10, \"targetDatabaseId\": 20}]",
                new TypeReference<List<DlmTask>>() {});

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class))).thenReturn(1);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).findById(eq(2L));
        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.DONE));
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }
}