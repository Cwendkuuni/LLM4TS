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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);
    private JobExecutionContext jobExecutionContext;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // arrange
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        // act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // assert
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskFound() {
        // arrange
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(getDlmTasks()));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // assert
        assertEquals(any(), scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteWithException() {
        // arrange
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenThrow(new RuntimeException());

        // act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // assert
        assertEquals(TaskStatus.UNKNOWN, scheduleTaskEntity.getStatus());
    }

    private List<DlmTask> getDlmTasks() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.MIGRATE);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);
        return dlmTasks;
    }
}