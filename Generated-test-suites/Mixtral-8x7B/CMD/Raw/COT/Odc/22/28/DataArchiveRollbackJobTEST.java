import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.simpl.SimpleJobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private AbstractDlmJob abstractDlmJob;
    private JobExecutionContext jobExecutionContext;
    private Scheduler scheduler;
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        abstractDlmJob = mock(AbstractDlmJob.class);
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(mock(ScheduleTaskEntity.class));

        applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

        JobFactory jobFactory = new SimpleJobFactory() {
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
                return applicationContext.getBean(bundle.getJobDetail().getJobClass());
            }
        };

        try {
            SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("Error initializing scheduler", e);
        }
    }

    @Test
    public void testExecute() throws Exception {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        when(scheduleTaskRepository.findById(eq(1L))).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = Collections.singletonList(mock(DlmTask.class));
        when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
                .thenReturn(dataArchiveTaskOption)
                .thenReturn(Optional.of(mock(ScheduleTaskEntity.class)));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verifyTaskStatus(1L, TaskStatus.DONE);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(jobExecutionContext.getResult()).thenReturn(mock(ScheduleTaskEntity.class));

        when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verifyTaskStatus(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenTaskUnitsIsEmpty() throws Exception {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        when(scheduleTaskRepository.findById(eq(1L))).thenReturn(dataArchiveTaskOption);

        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class))).thenReturn(Collections.emptyList());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verifyTaskStatus(1L, TaskStatus.DONE);
    }

    private void verifyTaskStatus(Long id, TaskStatus expectedStatus) throws SchedulerException {
        Optional<ScheduleTaskEntity> task =
                scheduleTaskRepository.findById(id);

        assertTrue(task.isPresent());
        assertEquals(expectedStatus, task.get().getStatus());
    }
}