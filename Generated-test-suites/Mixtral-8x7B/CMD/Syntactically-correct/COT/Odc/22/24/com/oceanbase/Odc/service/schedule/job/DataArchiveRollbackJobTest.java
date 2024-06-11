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
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.Result;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.JobMatcher;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobFactory;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.jobstore.JobStore;
import org.quartz.utils.Key;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JsonUtils jsonUtils;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobFactory jobFactory;

    private JobStore jobStore;

    private SimpleThreadPool threadPool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        threadPool = new SimpleThreadPool(10, "TestThreadPool");
        threadPool.init();

        jobStore = new RAMJobStore();
        jobStore.setThreadPool(threadPool);
        jobStore.setContext(mock(org.quartz.SchedulerContext.class));

        jobFactory = spy(dataArchiveRollbackJob);

        SchedulerFactory schedulerFactory = mock(SchedulerFactory.class);
        when(schedulerFactory.getScheduler()).thenReturn(mock(Scheduler.class));
        when(schedulerFactory.getScheduler().getJobStore()).thenReturn(jobStore);
        when(schedulerFactory.getScheduler().getJobFactory()).thenReturn(jobFactory);
        when(schedulerFactory.getScheduler().getTriggerFiredBundle(any(), eq(true))).thenAnswer(invocationOnMock -> {
            TriggerFiredBundle triggerFiredBundle = mock(TriggerFiredBundle.class);
            JobDetail jobDetail = mock(JobDetail.class);
            when(jobDetail.getKey()).thenReturn(Key.getKey("TestJob", "TestGroup"));
            when(jobDetail.getJobClass()).thenReturn(DataArchiveRollbackJob.class);
            when(triggerFiredBundle.getJobDetail()).thenReturn(jobDetail);
            return triggerFiredBundle;
        });

        dataArchiveRollbackJob.setSchedulerFactory(schedulerFactory);
    }

    /**
     * Test execute() method with valid parameters
     */
    @Test
    public void testExecuteWithValidParameters() throws Exception {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 3, \"targetDatabaseId\": 4}]");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"sourceDatabaseId\": 4, \"targetDatabaseId\": 3}]",
                new TypeReference<List<DlmTask>>() {});

        when(context.getResult()).thenReturn(taskEntity);
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.of(dataArchiveTask));
        when(jsonUtils.fromJson(anyString(),
                eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }

    /**
     * Test execute() method with non-existing data archive task id
     */
    @Test
    public void testExecuteWithNonExistingDataArchiveTaskId() throws Exception {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);

        when(context.getResult()).thenReturn(taskEntity);
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(new DataArchiveRollbackParameters());
        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    /**
     * Test execute() method with exception in findById()
     */
    @Test
    public void testExecuteWithExceptionInFindById() throws Exception {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);

        when(context.getResult()).thenReturn(taskEntity);
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(new DataArchiveRollbackParameters());
        when(scheduleTaskRepository.findById(eq(2L)))
                .thenThrow(new RuntimeException());

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }
}

/**
 * 
 */