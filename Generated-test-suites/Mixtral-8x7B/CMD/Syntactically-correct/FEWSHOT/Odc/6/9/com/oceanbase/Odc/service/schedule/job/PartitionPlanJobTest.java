import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.simpl.RAMJobStore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

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

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private JobExecutionContext context;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        when(springContextUtil.getBean(eq(PartitionPlanTaskService.class))).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(eq(PartitionPlanService.class))).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(eq(ScheduleService.class))).thenReturn(scheduleService);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(jsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class))).thenReturn(new PartitionPlanJobParameters());
    }

    @Test
    public void testExecute() throws SchedulerException {
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());
        partitionPlanJob.execute(context);
        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        verify(partitionPlanService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Configuration
    static class TestConfig {

        @Bean
        public RAMJobStore ramJobStore() {
            RAMJobStore ramJobStore = new RAMJobStore();
            ramJobStore.setDriverDelegateClass(null);
            return ramJobStore;
        }

        @Bean
        public SpringContextUtil springContextUtil() {
            SpringContextUtil springContextUtil = new SpringContextUtil();
            springContextUtil.setApplicationContext(applicationContext);
            return springContextUtil;
        }

        @Bean
        public JsonUtils jsonUtils() {
            return new JsonUtils();
        }
    }
}