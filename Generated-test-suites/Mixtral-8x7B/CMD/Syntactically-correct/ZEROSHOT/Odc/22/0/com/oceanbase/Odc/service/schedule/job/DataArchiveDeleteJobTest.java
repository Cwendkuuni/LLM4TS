import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.RamJobStore;
import org.quartz.simpl.RamJobStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);
private Scheduler scheduler;
private JobDetail jobDetail;
private JobBuilder jobBuilder;
private JobDataMap jobDataMap;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity scheduleTaskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private TypeReference<List<DlmTask>> typeReference;

@Before
public void setup() {
scheduler = StdSchedulerFactory.getDefaultScheduler();
scheduler.start();

jobBuilder = JobBuilder.newJob(DataArchiveDeleteJob.class);
jobDataMap = new JobDataMap();
dataArchiveDeleteJob = new DataArchiveDeleteJob();

try {
RamJobStoreFactory factory = new RamJobStoreFactory();
factory.setUseProperties(false);
scheduler.setJobStore(factory.createJobStore());
} catch (SchedulerException e) {
log.error("Error initializing scheduler", e);
}
}

@Test
public void testExecute() throws SchedulerException, InterruptedException {
// Initialize mock objects
scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveTask = new ScheduleTaskEntity();
taskUnits = new ArrayList<>();
typeReference = new TypeReference<List<DlmTask>>() {};

// Set up jobDetail with mock objects
jobDetail = jobBuilder.withIdentity("testJob", "testJobGroup").setJobData(jobDataMap).build();
jobDataMap.put("scheduleTaskRepository", mock(ScheduleTaskRepository.class));
jobDataMap.put("scheduleTaskEntity", scheduleTaskEntity);
jobDataMap.put("dataArchiveClearParameters", dataArchiveClearParameters);
jobDataMap.put("dataArchiveTask", dataArchiveTask);
jobDataMap.put("taskUnits", taskUnits);
jobDataMap.put("typeReference", typeReference);

// Define behavior for mock objects
scheduleTaskRepository mockScheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(mockScheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(mockScheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class))).thenReturn(null);

jobDataMap.put("scheduleTaskRepository", mockScheduleTaskRepository);

// Call the method under test
dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

// Assertions
verify(mockScheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(mockScheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}
}