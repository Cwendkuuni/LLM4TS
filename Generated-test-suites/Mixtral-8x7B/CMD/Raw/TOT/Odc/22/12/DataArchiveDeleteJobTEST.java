import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private JobExecutionContext jobExecutionContext;

    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = new ScheduleTaskEntity();
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);
    }

    @Test
    public void testExecuteValidParameters() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFinished() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.PREPARING);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

}