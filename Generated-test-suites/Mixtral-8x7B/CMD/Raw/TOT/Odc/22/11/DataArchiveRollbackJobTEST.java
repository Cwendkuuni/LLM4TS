import com.fasterxml.jackson.core.type.TypeReference;
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
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private DataArchiveRollbackParameters dataArchiveRollbackParameters;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        jobExecutionContext = mock(JobExecutionContext.class);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

        dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(10L);

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(10L);
        dataArchiveTask.setResultJson("{\"tasks\":[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"MIGRATE\"," +
                "\"status\":\"PREPARING\"}]}");

        taskUnits = new ArrayList<>();
        DlmTask task = new DlmTask();
        task.setId(1L);
        task.setSourceDatabaseId(1L);
        task.setTargetDatabaseId(2L);
        task.setJobType(JobType.MIGRATE);
        task.setStatus(TaskStatus.PREPARING);
        taskUnits.add(task);
    }

    @Test
    public void testExecute_shouldExecuteDataArchiveRollbackJob() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_shouldExecuteDataArchiveRollbackJobWithValidData() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        String expectedResultJson = "{\"tasks\":[{\"id\":1,\"sourceDatabaseId\":2,\"targetDatabaseId\":1,\"jobType\":\"ROLLBACK\"," +
                "\"status\":\"DONE\"}]}";
        JSONAssert.assertEquals(expectedResultJson, dataArchiveTask.getResultJson(), JSONCompareMode.LENIENT);
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_shouldExecuteDataArchiveRollbackJobWithEmptyTaskUnits() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackParameters.setDataArchiveTaskId(11L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 11}");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        String expectedResultJson = "{\"tasks\":[]}";
        JSONAssert.assertEquals(expectedResultJson, dataArchiveTask.getResultJson(), JSONCompareMode.LENIENT);
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

}