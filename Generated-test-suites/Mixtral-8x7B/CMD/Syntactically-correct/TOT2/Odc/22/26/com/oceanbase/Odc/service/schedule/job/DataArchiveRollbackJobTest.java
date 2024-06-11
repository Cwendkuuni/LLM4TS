import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.quartz.JobKey.jobKey;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private Scheduler scheduler;

    @Before
    public void setUp() throws SchedulerException {
        scheduler = mock(Scheduler.class);
        dataArchiveRollbackJob.setScheduler(scheduler);
    }

    @After
    public void tearDown() throws SchedulerException {
        List<JobKey> jobKeys =
                scheduler.getJobKeys(GroupMatcher.groupContains(dataArchiveRollbackJob.getJobGroup()));
        for (JobKey jobKey : jobKeys) {
            scheduler.deleteJob(jobKey);
        }
    }

    private JobExecutionContext createJobExecutionContext() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":12345}");
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(dataArchiveRollbackJob.createJobDetail());
        when(context.getResult()).thenReturn(taskEntity);
        return context;
    }

    // Test cases proposed by Expert 1, 2, and 3

}