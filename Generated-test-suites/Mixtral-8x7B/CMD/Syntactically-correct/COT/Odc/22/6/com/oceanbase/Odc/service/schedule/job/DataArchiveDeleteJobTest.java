import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.simpl.ContextProviderClass;
import org.quartz.simpl.SimpleThreadPool;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        SimpleThreadPool threadPool = new SimpleThreadPool(1, SimpleThreadPool.THREAD_PRIORITY_Below_Normal, 1);
        threadPool.start();
        ContextProviderClass.currentContext = threadPool.getContextProvider();

        JobExecutionContext jobExecutionContext = new JobExecutionContext(context);
        jobExecutionContext.setResult(new ScheduleTaskEntity());
        dataArchiveDeleteJob.execute(jobExecutionContext);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        JobExecutionContext jobExecutionContext = new JobExecutionContext(context);
        jobExecutionContext.setResult(taskEntity);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("123");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        JobExecutionContext jobExecutionContext = new JobExecutionContext(context);
        jobExecutionContext.setResult(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_ClearTasksSuccess() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("123");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_ClearTasksFailure() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("123");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        when(dataArchiveDeleteJob.executeTask(taskEntity.getId(), taskUnits))
                .thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTask_ReturnFailed() {
        List<DlmTask> taskUnits = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class)))
                .thenReturn(true);

        when(dataArchiveDeleteJob.prepareTasksForClear(taskUnits))
                .thenReturn(taskUnits);

        when(dataArchiveDeleteJob.executeTasks(taskUnits))
                .thenReturn(TaskStatus.FAILED);

        TaskStatus taskStatus = dataArchiveDeleteJob.executeTask("123", taskUnits);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    @Test
    public void testExecuteTask_ReturnDone() {
        List<DlmTask> taskUnits = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class)))
                .thenReturn(true);

        when(dataArchiveDeleteJob.prepareTasksForClear(taskUnits))
                .thenReturn(taskUnits);

        when(dataArchiveDeleteJob.executeTasks(taskUnits))
                .thenReturn(TaskStatus.DONE);

        TaskStatus taskStatus = dataArchiveDeleteJob.executeTask("123", taskUnits);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testExecuteTask_ReturnPreparing() {
        List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class)))
                .thenReturn(true);

        when(dataArchiveDeleteJob.prepareTasksForClear(taskUnits))
                .thenReturn(taskUnits);

        when(dataArchiveDeleteJob.executeTasks(taskUnits))
                .thenReturn(TaskStatus.PREPARING);

        TaskStatus taskStatus = dataArchiveDeleteJob.executeTask("123", taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}