import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartitionPlanJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionPlanJobTest.class);

    private PartitionPlanJob partitionPlanJob;

    private ScheduleService scheduleService;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        scheduleService = mock(ScheduleService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        SpringContextUtil.putBean(ScheduleService.class, scheduleService);
        SpringContextUtil.putBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.putBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        partitionPlanJob = new PartitionPlanJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception());
        partitionPlanJob.execute(jobExecutionContext);
        verify(LOGGER).warn("Schedule not found,scheduleId={}", 1L);
    }
}