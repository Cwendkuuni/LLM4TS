package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContextImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;
import tech.simter.quartz.SpringQuartzJobFactory;

/**
 * Test for DataArchiveDeleteJob
 *
 * @author Tinker
 */
@Slf4j
public class DataArchiveDeleteJobTest {
  // Mocks
  private MongoTemplate mongoTemplateMock;
  private ScheduleTaskRepository scheduleTaskRepositoryMock;

  // System Under Test
  private DataArchiveDeleteJob job;

  @Before
  public void setup() {
    mongoTemplateMock = mock(MongoTemplate.class);
    scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);

    job =
        spy(
            new DataArchiveDeleteJob() {
              @Override
              protected MongoTemplate getMongoTemplate() {
                return mongoTemplateMock;
              }

              @Override
              protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepositoryMock;
              }
            });

    // Use a SpringQuartzJobFactory to correctly initialize the JobDataMap
    SpringQuartzJobFactory jobFactory = new SpringQuartzJobFactory();
    JobExecutionContext context =
        new JobExecutionContextImpl(
            null, null, null, null, null, null, null, GroupMatcher.jobGroupEquals("group1"), null);
    jobFactory.createJobDetail(context, job, null, null);
  }

  @Test
  public void testExecuteWithMissingTask() throws JobExecutionException {
    String parametersJson =
        "{\"dataArchiveTaskId\": \"taskId1\", \"someOtherParameter\": \"someValue\"}";
    JobExecutionContext context = createJobContext(parametersJson);

    // Set up mocks
    doReturn(Optional.empty())
        .when(job)
        .getScheduleTaskRepository()
        .findById(anyString());

    // Execute
    job.execute(context);

    // Verify
    verify(job, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
  }

  @Test
  public void testExecuteWithUnfinishedTask() throws JobExecutionException {
    String parametersJson =
        "{\"dataArchiveTaskId\": \"taskId1\", \"someOtherParameter\": \"someValue\"}";
    JobExecutionContext context = createJobContext(parametersJson);

    // Set up mocks
    ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
    dataArchiveTask.setId("taskId1");
    dataArchiveTask.setStatus(TaskStatus.RUNNING);
    doReturn(Optional.of(dataArchiveTask))
        .when(job)
        .getScheduleTaskRepository()
        .findById(anyString());

    // Execute
    job.execute(context);

    // Verify
    verify(job, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
  }

  @Test
  public void testExecuteWithSuccess() throws JobExecutionException {
    String parametersJson =
        "{\"dataArchiveTaskId\": \"taskId1\", \"someOtherParameter\": \"someValue\"}";
    JobExecutionContext context = createJobContext(parametersJson);

    // Set up mocks
    ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
    dataArchiveTask.setId("taskId1");
    dataArchiveTask.setStatus(TaskStatus.DONE);
    DataArchiveClearParameters dataArchiveClearParameters =
        new DataArchiveClearParameters();
    dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
    dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

    DlmTask dlmTask1 = new DlmTask();
    dlmTask1.setId("id1");
    DlmTask dlmTask2 = new DlmTask();
    dlmTask2.setId("id2");
    List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);

    doReturn(Optional.of(dataArchiveTask))
        .when(job)
        .getScheduleTaskRepository()
        .findById(anyString());
    doReturn(taskUnits)
        .when(job)
        .parseTaskUnitsFromTaskResultJson(
            eq(dataArchiveTask.getResultJson()), any(TypeReference.class));

    // Execute
    job.execute(context);

    // Verify
    verify(job, times(1)).executeTask(eq("taskId1"), anyList());
    verify(job, times(1))
        .getScheduleTaskRepository()
        .updateStatusById(eq("taskId1"), any(TaskStatus.class));
  }

  // Helper methods
  private JobExecutionContext createJobContext(String parametersJson) {
    JobExecutionContext context = new JobExecutionContextImpl();
    ReflectionTestUtils.setField(
        context, "mergedJobDataMap", new JobDataMapWrapper(new JobDataMap()));
    ReflectionTestUtils.setField(context, "fireInstanceId", "instanceId1");
    ReflectionTestUtils.setField(context, "fireTime", new Date());
    ReflectionTestUtils.setField(context, "jobDetail", getJobDetail());
    ReflectionTestUtils.setField(context, "jobRunTime", 0L);
    ReflectionTestUtils.setField(context, "scheduler", null);
    ReflectionTestUtils.setField(context, "trigger", null);
    ReflectionTestUtils.setField(context, "triggerInst", null);

    ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
    taskEntity.setId("taskId1");
    taskEntity.setParametersJson(parametersJson);
    context.setResult(taskEntity);
    return context;
  }

  private JobDetail getJobDetail() {
    JobDetail jobDetail = new JobDetail();
    jobDetail.setJobClass(DataArchiveDeleteJob.class);
    jobDetail.setKey(new JobKey("jobName1", "group1"));
    jobDetail.setDescription("description1");
    jobDetail.setDurability(true);
    jobDetail.setRequestsRecovery(true);
    return jobDetail;
  }

  private List<DlmTask> parseTaskUnitsFromTaskResultJson(String resultJson) {
    TypeReference<List<DlmTask>> typeReference = new TypeReference<List<DlmTask>>() {};
    return JsonUtils.fromJson(resultJson, typeReference);
  }

  private List<DlmTask> parseTaskUnitsFromTaskResultJson(String resultJson, TypeReference<List<DlmTask>> typeReference) {
    return JsonUtils.fromJson(resultJson, typeReference);
  }

  private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
    // Implement your logic here
    return TaskStatus.SUCCESS;
  }

  private void executeTask(String taskId, List<DlmTask> taskUnits) throws JobExecutionException {
    doNothing().when(job).executeTask(eq(taskId), anyList());
  }
}