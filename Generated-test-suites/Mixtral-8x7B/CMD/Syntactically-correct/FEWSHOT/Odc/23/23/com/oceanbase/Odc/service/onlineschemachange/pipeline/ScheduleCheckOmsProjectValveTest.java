import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

 @Mock
 private ProjectOpenApiService projectOpenApiService;
 @Mock
 private OscTaskCompleteHandler completeHandler;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 @InjectMocks
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @Test
 public void testInvoke() {
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
 when(scheduleTask.getId()).thenReturn(1L);
 when(scheduleTask.getResultJson()).thenReturn("{\"manualSwapTableEnabled\": true, \"manualSwapTableStarted\": true}");

 OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
 when(taskParameter.getOmsProjectId()).thenReturn("projectId");
 when(taskParameter.getDatabaseName()).thenReturn("databaseName");
 when(taskParameter.getUid()).thenReturn("uid");
 when(taskParameter.getSwapTableType()).thenReturn(SwapTableType.AUTO);

 ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
 when(projectRequest.getUid()).thenReturn("uid");
 when(projectRequest.getId()).thenReturn("projectId");

 List<ProjectStepVO> projectSteps = mock(List.class);

 ProjectProgressResponse progress = mock(ProjectProgressResponse.class);

 ProjectStepResultChecker projectStepResultChecker = mock(ProjectStepResultChecker.class);
 ProjectStepResult projectStepResult = mock(ProjectStepResult.class);

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(projectSteps);
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(progress);
 when(projectStepResultChecker.getCheckerResult())
 .thenReturn(projectStepResult);

 when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

 scheduleCheckOmsProjectValve.invoke(new OscValveContext(taskParameter, scheduleTask));
 }

 @Test
 public void testHandleOmsProjectStepResult_fullVerificationResultConsistent() {
 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setTaskStatus(TaskStatus.DONE);
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 SwapTableType swapTableType = SwapTableType.AUTO;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, swapTableType, scheduleTask);

 assertEquals(true, result.isManualSwapTableEnabled());
 assertEquals(true, result.isManualSwapTableStarted());
 }

 @Test
 public void testHandleOmsProjectStepResult_fullVerificationResultUncheck() {
 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setTaskStatus(TaskStatus.DONE);
 projectStepResult.setFullVerificationResult(FullVerificationResult.UNCHECK);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 SwapTableType swapTableType = SwapTableType.AUTO;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, swapTableType, scheduleTask);

 assertEquals(true, result.isManualSwapTableEnabled());
 assertEquals(true, result.isManualSwapTableStarted());
 }

 @Test
 public void testHandleOmsProjectStepResult_fullVerificationResultInconsistent() {
 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setTaskStatus(TaskStatus.DONE);
 projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

 SwapTableType swapTableType = SwapTableType.AUTO;
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, swapTableType, scheduleTask);

 assertEquals(null, result.getManualSwapTableEnabled());
 assertEquals(null, result.getManualSwapTableStarted());
 }

 @Test
 public void testListProjectFullVerifyResult() {
 ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
 request.setProjectId("projectId");
 request.setSourceSchemas(new String[] {"databaseName"});
 request.setDestSchemas(new String[] {"databaseName"});
 request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
 request.setPageSize(10);
 request.setPageNumber(1);
 request.setUid("uid");

 ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
 response.setFullVerifyResultList(Lists.newArrayList(mock(ProjectFullVerifyResultEntity.class)));

 when(projectOpenApiService.listProjectFullVerifyResult(request))
 .thenReturn(response);

 assertEquals(1, response.getFullVerifyResultList().size());
 }

 @Test
 public void testAdaptResult() {
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 ProjectStepResult projectStepResult = new ProjectStepResult();

 scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

 assertEquals(null, result.getFullTransferEstimatedCount());
 assertEquals(null, result.getFullTransferFinishedCount());
 assertEquals(null, result.getFullTransferProgressPercentage());
 assertEquals(null, result.getFullVerificationResult());
 assertEquals(null, result.getFullVerificationResultDescription());
 assertEquals(null, result.getFullVerificationProgressPercentage());
 assertEquals(null, result.getCurrentStep());
 assertEquals(null, result.getCurrentStepStatus());
 assertEquals(null, result.getPrecheckResult());
 }

 @Test
 public void testGetProjectRequest() {
 OnlineSchemaChangeScheduleTaskParameters taskParam = mock(OnlineSchemaChangeScheduleTaskParameters.class);
 when(taskParam.getOmsProjectId()).thenReturn("projectId");
 when(taskParam.getUid()).thenReturn("uid");

 ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

 assertEquals("uid", projectRequest.getUid());
 assertEquals("projectId", projectRequest.getId());
 }

 @Test
 public void testRecordCurrentProgress() {
 ScheduleCheckOmsProjectValve.recordCurrentProgress("projectId", new OnlineSchemaChangeScheduleTaskResult());
 }

 @Test
 public void testGetHeader() {
 List<String> header = scheduleCheckOmsProjectValve.getHeader();

 assertEquals(5, header.size());
 }
}