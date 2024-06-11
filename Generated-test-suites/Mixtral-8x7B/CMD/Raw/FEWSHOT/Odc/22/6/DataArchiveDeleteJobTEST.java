import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Result;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.impl.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobExecutionContext jobExecutionContext;

 @Before
 public void setUp() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob(scheduleTaskRepository);
 jobExecutionContext = mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute() throws JobExecutionException {
 // Prepare test data
 String parametersJson = "{\"dataArchiveTaskId\": 123}";
 String resultJson = "[{\"id\": 456, \"jobName\": \"job1\", \"jobGroup\": \"group1\"}]";
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(111);
 taskEntity.setParametersJson(parametersJson);
 taskEntity.setResultJson(resultJson);
 taskEntity.setStatus(TaskStatus.DONE);

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(123);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(123);
doReturn(dataArchiveClearParameters).when(dataArchiveDeleteJob).parseParameters(parametersJson);
doReturn(new TypeReference<List<DlmTask>>() {}).when(JsonUtils.class).fromJson(anyString(), any(TypeReference.class));
doReturn(List.of(new DlmTask())).when(JsonUtils.class).fromJson(anyString(), any(TypeReference.class));
doReturn(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 111, 1)).when(DlmJobIdUtil.class)
 .generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt());

 // Execute test
 dataArchiveDeleteJob.execute(jobExecutionContext);

 // Verify the results
 verify(scheduleTaskRepository, times(1)).updateStatusById(111, TaskStatus.FAILED);
 }

 @Test
 public void testParseParameters() {
 // Prepare test data
 String parametersJson = "{\"dataArchiveTaskId\": 123}";

 // Execute test
 DataArchiveClearParameters result = dataArchiveDeleteJob.parseParameters(parametersJson);

 // Verify the results
 assertEquals(123, result.getDataArchiveTaskId());
 }
}