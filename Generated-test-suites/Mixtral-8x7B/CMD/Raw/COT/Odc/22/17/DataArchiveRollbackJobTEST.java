import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        AbstractDlmJob abstractDlmJob = spy(DataArchiveRollbackJob.class);
        ReflectionTestUtils.setField(abstractDlmJob, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(abstractDlmJob, "jobThread", Thread.currentThread());

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        ReflectionTestUtils.setField(abstractDlmJob, "context", jobExecutionContext);

        this.dataArchiveRollbackJob = abstractDlmJob;
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertTrue(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenExecuteTaskException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        doThrow(new SchedulerException("test")).when(dataArchiveRollbackJob).executeTask(anyLong(), any());

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenRollbackParametersIsNull() {
        scheduleTaskEntity.setParametersJson("null");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenRollbackParametersIsEmpty() {
        scheduleTaskEntity.setParametersJson("");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenRollbackParametersJsonIsInvalid() {
        scheduleTaskEntity.setParametersJson("invalid_json");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenDataArchiveTaskResultJsonIsInvalid() {
        scheduleTaskEntity.setResultJson("invalid_json");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenDataArchiveTaskResultJsonIsNull() {
        scheduleTaskEntity.setResultJson(null);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenDataArchiveTaskResultJsonIsEmpty() {
        scheduleTaskEntity.setResultJson("");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenTaskUnitsIsEmpty() {
        scheduleTaskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenTaskUnitsIsNull() {
        scheduleTaskEntity.setResultJson(null);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenTaskUnitsIsEmptyJson() {
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertFalse(dataArchiveRollbackJob.isJobSuccess());
    }

    @Test
    public void testExecute_whenPrepareTasksSuccess() {
        DataArchiveRollbackParameters dataArchiveRollbackParameters =
                new DataArchiveRollbackParameters(1L, 10L, 1L, 20L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveRollbackParameters));
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(createDlmTask());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(dlmTasks));

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertTrue(dataArchiveRollbackJob.isJobSuccess());
    }

    private ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private DlmTask createDlmTask() {
        return new DlmTask(1L, "test", JobType.DATA_ARCHIVE, 1L, 2L, 1L,
                TaskStatus.PREPARING, "2023-01-01T12:00:00", "2023-01-01T12:00:00",
                DlmJobIdUtil.generateHistoryJobId("test", "test", 1L, 1), "{}");
    }

    @SuppressWarnings("unchecked")
    interface ScheduleTaskRepository {
        Optional<ScheduleTaskEntity> findById(Long aLong);

        ScheduleTaskEntity save(ScheduleTaskEntity scheduleTaskEntity);

        Example<ScheduleTaskEntity> example(ScheduleTaskEntity scheduleTaskEntity);

        void updateStatusById(Long aLong, TaskStatus taskStatus);
    }

}