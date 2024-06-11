import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JsonUtils jsonUtils;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveDeleteJob.setJsonUtils(jsonUtils);
    }

    @Test
    public void testExecute_WhenTaskEntityIsNull_ThenTaskStatusIsFailed() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(null);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskIsNull_ThenTaskStatusIsFailed() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskIsNotDone_ThenTaskStatusIsFailed() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskIsDone_ThenExecuteTaskAndUpdateStatus() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        List<DlmTask> taskUnits = createTaskUnits();
        String resultJson = JsonUtils.toJson(taskUnits);
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(jsonUtils.fromJson(parametersJson, DataArchiveClearParameters.class))
                .thenReturn(dataArchiveClearParameters);
        when(jsonUtils.fromJson(resultJson, new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        verify(taskUnit -> taskUnit.setId(anyString()), times(taskUnits.size())).accept(any(DlmTask.class));
        verify(taskUnit -> taskUnit.setJobType(eq(JobType.DELETE)), times(taskUnits.size()))
                .accept(any(DlmTask.class));
        verify(taskUnit -> taskUnit.setStatus(TaskStatus.PREPARING), times(taskUnits.size()))
                .accept(any(DlmTask.class));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
    }

    private List<DlmTask> createTaskUnits() {
        return List.of(
                new DlmTask(),
                new DlmTask()
        );
    }
}