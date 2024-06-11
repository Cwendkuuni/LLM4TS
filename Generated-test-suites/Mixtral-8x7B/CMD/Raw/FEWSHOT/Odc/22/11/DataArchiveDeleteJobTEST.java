import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.slf4j.Logger;
import org.slartus.slf4j.test.LogInterceptor;
import org.slf4j.test.TestLogger;
import org.slf4j.test.TestLoggerFactory;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private TestLogger testLogger;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity taskEntity;
 private ScheduleTaskEntity dataArchiveTask;
 private DataArchiveClearParameters dataArchiveClearParameters;
 private List<DlmTask> taskUnits;
 private JobType jobType;
 private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
 private TypeReference<List<DlmTask>> typeReference;
 private JsonUtils jsonUtils;
 private DlmJobIdUtil dlmJobIdUtil;
 private Logger logger;
 private scheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 testLogger = TestLoggerFactory.init();
 dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
 jsonUtils = mock(JsonUtils.class);
 dlmJobIdUtil = mock(DlmJobIdUtil.class);
 scheduleTaskRepository = mock(scheduleTaskRepository.class);
 logger = mock(Logger.class);
 dataArchiveDeleteJob.setJsonUtils(jsonUtils);
 dataArchiveDeleteJob.setDlmJobIdUtil(dlmJobIdUtil);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setLogger(logger);

 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("1234");

 taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.add(new DlmTask());

 jobType = JobType.DELETE;

 typeReference = new TypeReference<List<DlmTask>>() {
 };

 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("1111");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": \"1234\"}");
 taskEntity.setStatus(TaskStatus.QUEUED);

 dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1234");
 dataArchiveTask.setResultJson("[{\"id\": \"1\", \"jobName\": \"1\", \"jobGroup\": \"1\"}]");
 dataArchiveTask.setStatus(TaskStatus.DONE);

 dataArchiveTaskOption = Optional.of(dataArchiveTask);

 jobExecutionContext = new JobExecutionContextImpl();
 jobExecutionContext.setResult(taskEntity);

 doReturn(jsonUtils).when(dataArchiveDeleteJob).getJsonUtils();
 doReturn(dlmJobIdUtil).when(dataArchiveDeleteJob).getDlmJobIdUtil();
 doReturn(scheduleTaskRepository).when(dataArchiveDeleteJob).getScheduleTaskRepository();
 doReturn(logger).when(dataArchiveDeleteJob).getLogger();
 doReturn(typeReference).when(jsonUtils).fromJson(anyString(), any(TypeReference.class));
 doReturn(dataArchiveTaskOption).when(scheduleTaskRepository).findById(anyString());
 doReturn(taskUnits).when(jsonUtils).fromJson(anyString(), any(TypeReference.class));
 }

 @Test
 public void testExecute() {
 LogInterceptor.init().on(logger);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any(TaskStatus.class));
 assertEquals(TestLogger.Level.WARN, testLogger.getLevel());
 testLogger.clear();

 dataArchiveTask.setStatus(TaskStatus.RUNNING);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(2)).updateStatusById(anyString(), any(TaskStatus.class));
 assertEquals(TestLogger.Level.WARN, testLogger.getLevel());
 testLogger.clear();

 dataArchiveTask.setStatus(TaskStatus.FAILED);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(3)).updateStatusById(anyString(), any(TaskStatus.class));
 assertEquals(TestLogger.Level.WARN, testLogger.getLevel());
 testLogger.clear();

 }
}