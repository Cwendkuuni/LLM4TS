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
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private Scheduler scheduler;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setup() throws SchedulerException {
        scheduler = mock(Scheduler.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        rollbackParameters = mock(DataArchiveRollbackParameters.class);

        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            @Override
            protected Scheduler getScheduler() {
                return scheduler;
            }
        };

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);

        GroupedScheduler groupedScheduler = mock(GroupedScheduler.class);
        when(scheduler.getJobDetail(anyString(), anyString())).thenReturn(groupedScheduler);
        when(groupedScheduler.getJobDataMap()).thenReturn(new org.quartz.JobDataMap());
    }

    @Test
    public void testExecuteTypicalUseCase() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // prepare tasks for rollback
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        taskUnits.add(dlmTask);
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.DONE);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskUnitsEmpty() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getResultJson()).thenReturn("[]");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskUnitsNull() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getResultJson()).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskUnitsInvalidJson() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getResultJson()).thenReturn("Invalid Json");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskUnitsInvalidType() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getResultJson()).thenReturn("{\"taskUnits\": \"Invalid Type\"}");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskUnitsWrongType() throws SchedulerException, JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getResultJson()).thenReturn(
                JsonUtils.toJson(Collections.singletonList("Wrong Type")));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(rollbackParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }
}