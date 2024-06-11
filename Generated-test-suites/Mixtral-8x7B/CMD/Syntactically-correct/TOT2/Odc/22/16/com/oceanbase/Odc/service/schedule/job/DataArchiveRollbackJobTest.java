import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.quartz.CronExpression.validateExpression;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
    }

    @Test
    public void testExecute() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createMockJobExecutionContext();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createMockJobExecutionContext();

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.ofNullable(null));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithValidTaskUnits() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createMockJobExecutionContext();

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = getValidTaskUnits();
        Mockito.when(JsonUtils.fromJson(Mockito.anyString(), Mockito.eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyLong(), Mockito.ne(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithInvalidTaskUnits() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = createMockJobExecutionContext();

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = getInvalidTaskUnits();
        Mockito.when(JsonUtils.fromJson(Mockito.anyString(), Mockito.eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
    }

    private JobExecutionContext createMockJobExecutionContext() {
        return newTrigger()
                .withIdentity("testJob", "testGroup")
                .startNow()
                .build()
                .getFireInstanceId()
                .let(jobKey -> newJob(DataArchiveRollbackJob.class).withIdentity(jobKey).build())
                .let(jobDetail -> newJobExecutionContext(jobDetail, null));
    }

    private List<DlmTask> getValidTaskUnits() {
        List<DlmTask> taskUnits = new ArrayList<>();

        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnits.add(taskUnit);

        return taskUnits;
    }

    private List<DlmTask> getInvalidTaskUnits() {
        List<DlmTask> taskUnits = new ArrayList<>();

        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(null);
        taskUnits.add(taskUnit);

        return taskUnits;
    }

    private JobExecutionContext newJobExecutionContext(JobDetail jobDetail, Trigger trigger) {
        JobExecutionContext context = new JobExecutionContext(jobDetail, trigger);
        context.setScheduler(Mockito.mock(org.quartz.Scheduler.class));
        context.addJobDataMap(new JobDataMap());
        return context;
    }
}