import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanServiceImpl;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleServiceImpl;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

public class PartitionPlanJobTest {

private PartitionPlanTaskService partitionPlanTaskService;

private PartitionPlanService partitionPlanService;

private ScheduleService scheduleService;

private JobExecutionContext context;

@Before
public void setup() {
ApplicationContext context = new AnnotationConfigApplicationContext(
TestConfig.class);

this.partitionPlanTaskService = context.getBean(PartitionPlanTaskServiceImpl.class);
this.partitionPlanService = context.getBean(PartitionPlanServiceImpl.class);
this.scheduleService = context.getBean(ScheduleServiceImpl.class);

context = mock(JobExecutionContext.class);
}

// Test methods from Expert 1, 2, and 3

}