import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResultCheckerVerifyResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
 OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
 when(taskParameter.getOmsProjectId()).thenReturn("12345");
 when(taskParameter.getDatabaseName()).thenReturn("testdb");
 when(taskParameter.getUid()).thenReturn("testUid");

 ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
 doReturn(projectRequest).when(scheduleCheckOmsProjectValve).getProjectRequest(taskParameter);

 List<ProjectStepVO> projectSteps = mock(List.class);
 doReturn(projectSteps).when(projectOpenApiService).describeProjectSteps(projectRequest);

 ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
 doReturn(progress).when(projectOpenApiService).describeProjectProgress(projectRequest);

 ProjectStepResultCheckerVerifyResult verifyResult = mock(ProjectStepResultCheckerVerifyResult.class);
 doReturn(verifyResult).when(scheduleCheckOmsProjectValve)
 .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
 taskParameter.getDatabaseName(), taskParameter.getUid()));

 ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
 doReturn(projectStepResult).when(verifyResult).getCheckerResult();

 OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
 doReturn(result).when(scheduleCheckOmsProjectValve).adaptResult(any(), any());

 scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
 }

 @Test
 public void testAdaptResult() {
 OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
 ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
 when(projectStepResult.getFullTransferEstimatedCount()).thenReturn(100L);
 when(projectStepResult.getFullTransferFinishedCount()).thenReturn(50L);
 when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(50);
 when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
 when(projectStepResult.getFullVerificationProgressPercentage()).thenReturn(75);
 when(projectStepResult.getCurrentStep()).thenReturn("step1");
 when(projectStepResult.getCurrentStepStatus()).thenReturn(TaskStatus.DONE);
 when(projectStepResult.getPrecheckResult()).thenReturn(PrecheckResult.PASSED);

 scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

 assertEquals(100L, result.getFullTransferEstimatedCount());
 assertEquals(50L, result.getFullTransferFinishedCount());
 assertEquals(50, result.getFullTransferProgressPercentage());
 assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
 assertEquals(75, result.getFullVerificationProgressPercentage());
 assertEquals("step1", result.getCurrentStep());
 assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
 assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
 }

 @Test
 public void testListProjectFullVerifyResult() {
 ListProjectFullVerifyResultRequest request = mock(ListProjectFullVerifyResultRequest.class);
 when(request.getProjectId()).thenReturn("12345");
 when(request.getSourceSchemas()).thenReturn(new String[]{"testdb"});
 when(request.getDestSchemas()).thenReturn(new
 String[]{"testdb"});
 when(request.getStatus()).thenReturn(new String[]{"FINISHED", "SUSPEND", "RUNNING"});
 when(request.getPageSize()).thenReturn(10);
 when(request.getPageNumber()).thenReturn(1);
 when(request.getUid()).thenReturn("testUid");

 ProjectFullVerifyResultResponse response = mock(ProjectFullVerifyResultResponse.class);
 when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

 assertEquals(response, scheduleCheckOmsProjectValve.listProjectFullVerifyResult(request.getProjectId(),
 request.getSourceSchemas()[0], request.getUid()));
 }

 @Test
 public void testRecordCurrentProgress() {
 String projectId = "12345";
 OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
 when(result.getCurrentStep()).thenReturn("PRE_CHECK");
 when(result.getPrecheckResult()).thenReturn(PrecheckResult.PASSED);

 scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);
 }

 @Test
 public void testGetHeader() {
 List<String> header = scheduleCheckOmsProjectValve.getHeader();
 assertEquals(5, header.size());
 assertEquals("ProjectId", header.get(0));
 assertEquals("Step", header.get(1));
 assertEquals("Status", header.get(2));
 assertEquals("Progress", header.get(3));
 assertEquals("Description", header.get(4));
 }

}