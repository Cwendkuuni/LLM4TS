import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.Schedule;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanServiceMock;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private ScheduleService scheduleServiceMock;

    private ScheduleTaskUtils scheduleTaskUtilsMock;

    private SpringContextUtil springContextUtilMock;

    private JobExecutionContext jobExecutionContextMock;

    private Schedule scheduleMock;

    private PartitionPlanJobParameters partitionPlanJobParametersMock;

    private DatabasePartitionPlanEntity databasePartitionPlanEntityMock;

    private List<TablePartitionPlanEntity> tablePartitionPlansMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleTaskUtilsMock = mock(ScheduleTaskUtils.class);
        springContextUtilMock = mock(SpringContextUtil.class);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanServiceMock);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskServiceMock);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);

        jobExecutionContextMock = mock(JobExecutionContext.class);

        scheduleMock = new Schedule();
        scheduleMock.setId(1L);
        scheduleMock.setJobParametersJson("{}");

        partitionPlanJobParametersMock = new PartitionPlanJobParameters();

        databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntityMock.setId(1L);
        databasePartitionPlanEntityMock.setFlowInstanceId("123");
        databasePartitionPlanEntityMock.setConfigEnabled(true);

        tablePartitionPlansMock = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlanEntityMock = new TablePartitionPlanEntity();
        tablePartitionPlansMock.add(tablePartitionPlanEntityMock);

        when(springContextUtilMock.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(
                springContextUtilMock.getBean(PartitionPlanTaskService.class))
                        .thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);
        when(scheduleTaskUtilsMock.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
        when(JsonUtils.fromJson(any(), any())).thenReturn(partitionPlanJobParametersMock);
        when(partitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlanEntityMock);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlansMock);
        when(jobExecutionContextMock.getMergedJobDataMap()).thenReturn(scheduleMock.getJobDataMap());
    }

    @Test
    public void testExecute() throws SchedulerException {
        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan("123", tablePartitionPlansMock);
        verify(scheduleService, times(1)).terminate(scheduleMock);
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob = new PartitionPlanJob();

        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    @Test
    public void testGetPartitionPlanJobParameters() {
        partitionPlanJob = new PartitionPlanJob();

        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        PartitionPlanJobParameters result =
                (PartitionPlanJobParameters) ReflectionTestUtils.invokeMethod(partitionPlanJob,
                        "getPartitionPlanJobParameters", scheduleEntity);

        assertNotNull(result);
        assertEquals(1L, (long) result.getDatabasePartitionPlanId());
    }

    @Test
    public void testGetPartitionPlanJobParametersNullScheduleEntity() {
        partitionPlanJob = new PartitionPlanJob();

        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);

        PartitionPlanJobParameters result =
                (PartitionPlanJobParameters) ReflectionTestUtils.invokeMethod(partitionPlanJob,
                        "getPartitionPlanJobParameters", null);

        assertNull(result);
    }

    @Test
    public void testGetPartitionPlanJobParametersInvalidJson() {
        partitionPlanJob = new PartitionPlanJob();

        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("invalid json");

        PartitionPlanJobParameters result =
                (PartitionPlanJobParameters) ReflectionTestUtils.invokeMethod(partitionPlanJob,
                        "getPartitionPlanJobParameters", scheduleEntity);

        assertNull(result);
    }

    @Test
    public void testIsPartitionPlanJob() {
        partitionPlanJob = new PartitionPlanJob();

        Schedule schedule = new Schedule();
        schedule.setJobClass("com.oceanbase.odc.service.schedule.job.PartitionPlanJob");

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(partitionPlanJob, "isPartitionPlanJob", schedule);

        assertTrue(result);
    }

    @Test
    public void testIsPartitionPlanJobNullSchedule() {
        partitionPlanJob = new PartitionPlanJob();

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(partitionPlanJob, "isPartitionPlanJob", null);

        assertFalse(result);
    }

    @Test
    public void testIsPartitionPlanJobInvalidJobClass() {
        partitionPlanJob = new PartitionPlanJob();

        Schedule schedule = new Schedule();
        schedule.setJobClass("com.oceanbase.odc.service.schedule.job.InvalidJob");

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(partitionPlanJob, "isPartitionPlanJob", schedule);

        assertFalse(result);
    }

    @Test
    public void testHandleSchedulerException() throws SchedulerException {
        partitionPlanJob = new PartitionPlanJob();

        doThrow(new SchedulerException("test")).when(scheduleServiceMock).terminate(scheduleMock);

        partitionPlanJob.handleSchedulerException(scheduleMock, new SchedulerException("test"));

        verify(scheduleServiceMock, times(1)).terminate(scheduleMock);
    }
}