import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob(scheduleTaskRepository);
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithoutExceptions() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        taskEntity.setParametersJson(parametersJson);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(eq("123"))).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        String resultJson = JsonUtils.toJson(taskUnits);
        when(scheduleTaskRepository.updateStatusById(eq(taskEntity.getId()), any())).thenReturn(true);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq("123"));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any());
    }
}