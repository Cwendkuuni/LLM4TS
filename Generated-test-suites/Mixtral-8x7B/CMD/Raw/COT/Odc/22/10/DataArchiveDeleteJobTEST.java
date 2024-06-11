import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

        jobExecutionContext = new JobExecutionContext();
        jobExecutionContext.setResult(new ScheduleTaskEntity());

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", mockScheduleTaskRepository());
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed()
            throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

        when(mockScheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(mockScheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone_shouldUpdateTaskStatusToFailed()
            throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

        when(mockScheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(mockScheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenPrepareTasksForClear_shouldExecuteTaskAndUpdateTaskStatus()
            throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

        when(mockScheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        when(mockJsonUtils.fromJson(anyString(), any(TypeReference.class)))
                .thenReturn(dlmTasks);

        doNothing().when(dataArchiveDeleteJob)
                .executeTask(anyLong(), anyList());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(dataArchiveDeleteJob, times(1)).executeTask(anyLong(), anyList());
        verify(mockScheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), any(TaskStatus.class));
    }

    private ScheduleTaskRepository mockScheduleTaskRepository() {
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        doReturn(mock(ScheduleTaskEntity.class)).when(scheduleTaskRepository).save(any(ScheduleTaskEntity.class));

        return scheduleTaskRepository;
    }

    private JsonUtils mockJsonUtils() {
        JsonUtils jsonUtils = mock(JsonUtils.class);

        return jsonUtils;
    }
}