/**
 * 
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = DataArchiveRollbackParameters.builder()
                .dataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity taskEntity = ScheduleTaskEntity.builder()
                .id(1L)
                .jobName("DataArchiveRollbackJob")
                .jobGroup("group1")
                .parametersJson(JsonUtils.toJson(rollbackParameters))
                .resultJson("[]")
                .status(TaskStatus.RUNNING)
                .build();

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = DlmTask.getDummyTasks(1);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn(2L);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = DataArchiveRollbackParameters.builder()
                .dataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity taskEntity = ScheduleTaskEntity.builder()
                .id(1L)
                .jobName("DataArchiveRollbackJob")
                .jobGroup("group1")
                .parametersJson(JsonUtils.toJson(rollbackParameters))
                .resultJson("[]")
                .status(TaskStatus.RUNNING)
                .build();

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_taskUnitsConvertFailed() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = DataArchiveRollbackParameters.builder()
                .dataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity taskEntity = ScheduleTaskEntity.builder()
                .id(1L)
                .jobName("DataArchiveRollbackJob")
                .jobGroup("group1")
                .parametersJson(JsonUtils.toJson(rollbackParameters))
                .resultJson("[]")
                .status(TaskStatus.RUNNING)
                .build();

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenThrow(new RuntimeException("Convert failed."));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    /**
     * 
     */
}