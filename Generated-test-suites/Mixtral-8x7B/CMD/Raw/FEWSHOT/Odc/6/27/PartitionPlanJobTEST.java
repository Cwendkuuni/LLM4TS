import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceTest.TestPartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanServiceTest.TestPartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanServiceTest.TestTablePartitionPlanEntity;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleServiceTest.TestScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleServiceTest.TestScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest implements ApplicationContextAware {

    private PartitionPlanJob partitionPlanJob;

    private TestPartitionPlanService testPartitionPlanService;

    private TestPartitionPlanTaskService testPartitionPlanTaskService;

    private TestScheduleService testScheduleService;

    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        testPartitionPlanService = new TestPartitionPlanService();
        testPartitionPlanTaskService = new TestPartitionPlanTaskService();
        testScheduleService = new TestScheduleService();

        ReflectionTestUtils.setField(testPartitionPlanService, "tablePartitionPlanService",
                testPartitionPlanTaskService);

        ReflectionTestUtils.setField(testPartitionPlanTaskService, "partitionPlanService",
                testPartitionPlanService);

        ReflectionTestUtils.setField(testPartitionPlanTaskService, "scheduleService", testScheduleService);

        partitionPlanJob = new PartitionPlanJob();

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                testPartitionPlanTaskService);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", testPartitionPlanService);

        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", testScheduleService);

        ReflectionTestUtils.setField(testScheduleService, "applicationContext", applicationContext);
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new TestScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new TestDatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TestTablePartitionPlanEntity());

        when(testScheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        when(testPartitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(testPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(testScheduleService, times(1)).terminate(scheduleEntity);

        verify(testPartitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecuteWhenScheduleNotFound() throws Exception {
        Long scheduleId = 1L;

        when(testScheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(testPartitionPlanService, never()).getDatabasePartitionPlanById(anyLong());

        verify(testPartitionPlanService, never()).getValidTablePlanByDatabasePartitionPlanId(anyLong());

        verify(testPartitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());

        verify(testScheduleService, times(1)).nullSafeGetById(scheduleId);
    }

    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisable() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new TestScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new TestDatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(testScheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        when(testPartitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(testPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(testPartitionPlanService, times(1)).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());

        verify(testPartitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId());

        verify(testPartitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());

        verify(testScheduleService, times(1)).nullSafeGetById(scheduleId);
    }

    @Test
    public void testExecuteWhenNoTableToProcess() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new TestScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new TestDatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(testScheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        when(testPartitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(testPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(testPartitionPlanService, times(1)).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());

        verify(testPartitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId());

        verify(testPartitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());

        verify(testScheduleService, times(1)).nullSafeGetById(scheduleId);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

class TestPartitionPlanTaskService extends PartitionPlanTaskServiceImpl {

    public void executePartitionPlan(String flowInstanceId, List<TablePartitionPlanEntity> tablePartitionPlans)
            throws Exception {
        super.executePartitionPlan(flowInstanceId, tablePartitionPlans);
    }
}

class TestPartitionPlanService {

    public DatabasePartitionPlanEntity getDatabasePartitionPlanById(Long id) {
        // Implement the logic to get a DatabasePartitionPlanEntity by its ID
    }

    public List<TablePartitionPlanEntity> getValidTablePlanByDatabasePartitionPlanId(Long id) {
        // Implement the logic to get a list of TablePartitionPlanEntity by a DatabasePartitionPlanEntity's ID
    }
}

class TestScheduleService {

    public ScheduleEntity nullSafeGetById(Long id) throws Exception {
        // Implement the logic to get a ScheduleEntity by its ID
    }

    public void terminate(ScheduleEntity schedule) throws SchedulerException {
        // Implement the logic to terminate a ScheduleEntity
    }

    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        // Implement the logic to delete a Job by its name and group
    }

    public String getNextValidTime(ScheduleEntity schedule) {
        // Implement the logic to get the next valid time for a ScheduleEntity
    }

    public void scheduleJob(ScheduleEntity schedule, String time) throws SchedulerException {
        // Implement the logic to schedule a Job for a ScheduleEntity
    }

    public List<ScheduleEntity> listSchedules() {
        // Implement the logic to list all ScheduleEntities
    }

    public List<ScheduleEntity> listSchedules(String jobGroup) {
        // Implement the logic to list all ScheduleEntities for a given Job group
    }

    public List<ScheduleEntity> listSchedules(GroupMatcher<String> matcher) {
        // Implement the logic to list ScheduleEntities based on a GroupMatcher
    }
}

class TestDatabasePartitionPlanEntity extends DatabasePartitionPlanEntity {

    // Add any necessary fields, getters, and setters
}

class TestTablePartitionPlanEntity extends TablePartitionPlanEntity {

    // Add any necessary fields, getters, and setters
}

class TestScheduleEntity extends ScheduleEntity {

    public TestScheduleEntity() {
        super();
    }

    public TestScheduleEntity(Long id, String jobName, String jobGroup, String jobClassName, String cronExpression,
            String description, String jobParametersJson) {
        super(id, jobName, jobGroup, jobClassName, cronExpression, description, jobParametersJson);
    }

    // Add any necessary getters and setters
}