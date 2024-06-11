import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private AbstractDlmJob abstractDlmJob;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob.abstractDlmJob = abstractDlmJob;
    }

    @Test
    public void testExecute() {
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(null);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": \"123\"}");
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

        when(JsonUtils.fromJson(any(), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(JsonUtils.fromJson(any(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);

        verify(JsonUtils, times(1)).fromJson(any(), eq(DataArchiveClearParameters.class));
        verify(JsonUtils, times(1))
                .fromJson(any(), eq(new TypeReference<List<DlmTask>>() {}));

        verifyNoMoreInteractions(scheduleTaskRepository, JsonUtils);
    }

    @Test
    public void testExecuteTask() {
        dataArchiveDeleteJob.executeTask(1L, mock(List.class));
        verify(abstractDlmJob, times(1)).executeTask(any(), any());
        verifyNoMoreInteractions(abstractDlmJob);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
        assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
    }
}