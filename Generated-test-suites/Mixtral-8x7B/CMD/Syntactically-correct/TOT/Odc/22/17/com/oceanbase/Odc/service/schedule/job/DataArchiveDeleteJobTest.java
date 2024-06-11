import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.of(dataArchiveTask));
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.SUCCESS));
    }
}