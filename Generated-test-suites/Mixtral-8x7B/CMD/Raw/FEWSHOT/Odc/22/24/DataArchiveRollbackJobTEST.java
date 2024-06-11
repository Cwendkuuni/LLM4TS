import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleScheduleBuilder;
import org.quartz.simpl.SimpleTrigger;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private SchedulerFactory schedulerFactory;

    private GroupedScheduler scheduler;

    private JobFactory jobFactory;

    private JobExecutionContext jobExecutionContext;

    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() throws SchedulerException {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        schedulerFactory = new SchedulerFactory();
        schedulerFactory.initialize(createProperties());
        scheduler = (GroupedScheduler) schedulerFactory.getScheduler();
        scheduler.getJobStore().setDriverDelegateClass(RAMJobStore.class.getName());

        jobFactory = mock(JobFactory.class);
        scheduler.setJobFactory(jobFactory);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJob");
        scheduleTaskEntity.setJobGroup("testGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":10}");
        scheduleTaskEntity.setResultJson("[]");

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        scheduler.start();
    }

    private org.quartz.spi.Properties createProperties() {
        org.quartz.spi.Properties properties = new org.quartz.spi.Properties();
        properties.put("org.quartz.scheduler.instanceName", "TestScheduler");
        properties.put("org.quartz.scheduler.instanceId", "AUTO");
        properties.put("org.quartz.threadPool.threadCount", "3");
        properties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        return properties;
    }

    @Test
    public void testExecute() throws Exception {
        when(jobFactory.createJobInstance(any(Class.class), any(TriggerFiredBundle.class))).thenReturn(dataArchiveRollbackJob);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);

        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        JobKey jobKey = JobKey.jobKey("testJob", "testGroup");
        when(jobExecutionContext.getJobDetail().getKey()).thenReturn(jobKey);

        when(scheduler.checkExists(jobKey)).thenReturn(true);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.getTaskUnits(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify the result
        List<DlmTask> updatedTaskUnits = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(),
                new TypeReference<List<DlmTask>>() {});

        for (int i = 0; i < taskUnits.size(); i++) {
            DlmTask originalTaskUnit = taskUnits.get(i);
            DlmTask updatedTaskUnit = updatedTaskUnits.get(i);

            assertEquals(DlmJobIdUtil.generateHistoryJobId(
                            "testJob", "testGroup", scheduleTaskEntity.getId(), taskUnits.size()),
                    updatedTaskUnit.getId());
            assertEquals(originalTaskUnit.getTargetDatabaseId(), updatedTaskUnit.getSourceDatabaseId());
            assertEquals(originalTaskUnit.getSourceDatabaseId(), updatedTaskUnit.getTargetDatabaseId());
            assertEquals(JobType.ROLLBACK, updatedTaskUnit.getJobType());
            assertEquals(TaskStatus.DONE, updatedTaskUnit.getStatus());
        }
    }
}