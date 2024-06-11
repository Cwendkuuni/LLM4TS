import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanJobParameters partitionPlanJobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlan;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        partitionPlanJob = spy(partitionPlanJob);
    }

    @Test
    public void testExecute_ScheduleNotFound() throws Exception {
        doReturn(null).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);
        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(anyLong());
        doReturn(JsonUtils.toJson(partitionPlanJobParameters)).when(scheduleEntity).getJobParametersJson();

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn(anyString(), any(Exception.class));
        verify(partitionPlanService, times(0)).getDatabasePartitionPlanById(anyLong());
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotFound() throws Exception {
        Long scheduleId = 1L;
        doReturn(scheduleId).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);
        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
        doReturn(JsonUtils.toJson(partitionPlanJobParameters)).when(scheduleEntity).getJobParametersJson();
        doReturn(null).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).info(anyString(), anyLong());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_PartitionPlanTaskServiceException() throws Exception {
        Long scheduleId = 1L;
        doReturn(scheduleId).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);
        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
        doReturn(JsonUtils.toJson(partitionPlanJobParameters)).when(scheduleEntity).getJobParametersJson();
        doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        doReturn(Arrays.asList(tablePartitionPlan)).when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(anyString(),
                any(List.class));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn(anyString(), any(Exception.class));
    }

    @Test
    public void testInterrupt() throws JsonProcessingException {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        partitionPlanJob.interrupt();

        verify(log, times(1)).warn(anyString(), any(UnExpectedException.class));
    }

    @Test
    public void testPartitionPlanJob() throws Exception {
        Long scheduleId = 1L;
        doReturn(scheduleId).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);
        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
        doReturn(JsonUtils.toJson(partitionPlanJobParameters)).when(scheduleEntity).getJobParametersJson();
        doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        doReturn(Arrays.asList(tablePartitionPlan)).when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), any(List.class));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).info(anyString(), anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), any(List.class));
    }

    @Test
    public void testGetDatabasePartitionPlanById_NoException() throws Exception {
        doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getDatabasePartitionPlanById(1L);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
    }

    @Test
    public void testGetDatabasePartitionPlanById_Exception() throws Exception {
        doThrow(new Exception()).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getDatabasePartitionPlanById(1L);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
    }

    @Test
    public void testGetValidTablePlanByDatabasePartitionPlanId_NoException() throws Exception {
        doReturn(Arrays.asList(tablePartitionPlan)).when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getValidTablePlanByDatabasePartitionPlanId(1L);

        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
    }

    @Test
    public void testGetValidTablePlanByDatabasePartitionPlanId_Exception() throws Exception {
        doThrow(new Exception()).when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getValidTablePlanByDatabasePartitionPlanId(1L);

        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
    }

    @Test
    public void testExecutePartitionPlan_NoException() throws Exception {
        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), any(List.class));

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.executePartitionPlan("flowInstanceId", Collections.singletonList(tablePartitionPlan));

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), any(List.class));
    }

    @Test
    public void testExecutePartitionPlan_Exception() throws Exception {
        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(anyString(),
                any(List.class));

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.executePartitionPlan("flowInstanceId", Collections.singletonList(tablePartitionPlan));

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), any(List.class));
    }

    @Test
    public void testTerminate_NoException() throws SchedulerException {
        doNothing().when(scheduleService).terminate(any(ScheduleEntity.class));

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.terminate(scheduleEntity);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testTerminate_Exception() throws SchedulerException {
        doThrow(new SchedulerException("")).when(scheduleService).terminate(any(ScheduleEntity.class));

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.terminate(scheduleEntity);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testGetScheduleId_NoException() {
        doReturn(1L).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getScheduleId(jobExecutionContext);

        verify(scheduleTaskUtils, times(1)).getScheduleId(jobExecutionContext);
    }

    @Test
    public void testGetScheduleId_Exception() {
        doThrow(new Exception("")).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getScheduleId(jobExecutionContext);

        verify(scheduleTaskUtils, times(1)).getScheduleId(jobExecutionContext);
    }

    @Test
    public void testGetSpringContextUtil_NoException() {
        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getSpringContextUtil();

        verify(springContextUtil, times(1)).getBean(PartitionPlanTaskService.class);
        verify(springContextUtil, times(1)).getBean(PartitionPlanService.class);
        verify(springContextUtil, times(1)).getBean(ScheduleService.class);
    }

    @Test
    public void testGetSpringContextUtil_Exception() {
        doThrow(new Exception("")).when(springContextUtil).getBean(PartitionPlanTaskService.class);
        doThrow(new Exception("")).when(springContextUtil).getBean(PartitionPlanService.class);
        doThrow(new Exception("")).when(springContextUtil).getBean(ScheduleService.class);

        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.getSpringContextUtil();

        verify(springContextUtil, times(1)).getBean(PartitionPlanTaskService.class);
        verify(springContextUtil, times(1)).getBean(PartitionPlanService.class);
        verify(springContextUtil, times(1)).getBean(ScheduleService.class);
    }

}