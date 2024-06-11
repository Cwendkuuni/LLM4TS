import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobProfileMap;
import org.quartz.Trigger;
import org.springframework.data.repository.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;

    private ScheduleTaskEntity scheduleTaskEntity;

    private DataArchiveRollbackParameters dataArchiveRollbackParameters;

    private List<DlmTask> dlmTasks;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = mock(ScheduleTaskEntity.class);

        dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(3L);
        dlmTask.setId(4L);
        dlmTask.setJobType(JobType.MIGRATION);
        dlmTasks.add(dlmTask);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveRollbackParameters));
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveRollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
        verify(jobExecutionContext, times(1)).setResult(scheduleTaskEntity);
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveRollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(0)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
        verify(jobExecutionContext, times(1)).setResult(scheduleTaskEntity);
    }

    @Test
    public void testExecuteWithNullResultJson() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveRollbackParameters));
        when(scheduleTaskEntity.getResultJson()).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveRollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(0)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
        verify(jobExecutionContext, times(1)).setResult(scheduleTaskEntity);
    }

    @Test
    public void testExecuteTask() {
        dataArchiveRollbackJob.executeTask(1L, dlmTasks);

        assertEquals(1, dlmTasks.get(0).getSourceDatabaseId().longValue());
        assertEquals(2, dlmTasks.get(0).getTargetDatabaseId().longValue());
        assertEquals(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), eq(dlmTasks.size())),
                dlmTasks.get(0).getId());
        assertEquals(JobType.ROLLBACK, dlmTasks.get(0).getJobType());
        assertEquals(TaskStatus.DONE, dlmTasks.get(0).getStatus());
    }

    @Test
    public void testGetTaskStatus() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        JobProfileMap jobProfileMap = new JobProfileMap();
        jobProfileMap.put("taskUnits", dlmTasks);
        scheduleTaskEntity.setJobProfileMap(jobProfileMap);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatusWithNullDlmTasks() {
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(null);

        assertNull(taskStatus);
    }

    @Test
    public void testGetTaskStatusWithNoTaskUnits() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        JobProfileMap jobProfileMap = new JobProfileMap();
        scheduleTaskEntity.setJobProfileMap(jobProfileMap);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatusWithNotAllTasksDone() {
        dlmTasks.get(0).setStatus(TaskStatus.PREPARING);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}