import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContextImpl;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@Component
class TestValveContextImpl extends ValveContextImpl {
}

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

 private ValveContext valveContext;

 private ScheduleTaskEntity scheduleTaskEntity;

 private OnlineSchemaChangeScheduleTaskParameters taskParameter;

 private ProjectControlRequest projectRequest;

 private ProjectProgressResponse progress;

 private ProjectStepResult projectStepResult;

 private OnlineSchemaChangeScheduleTaskResult result;

 private ListProjectFullVerifyResultRequest fullVerifyResultRequest;

 private ProjectFullVerifyResultResponse fullVerifyResultResponse;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 valveContext = new TestValveContextImpl();

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);

 taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid("uid");
 taskParameter.setOmsProjectId("omsProjectId");
 taskParameter.setDatabaseName("databaseName");

 projectRequest = new ProjectControlRequest();
 projectRequest.setUid("uid");
 projectRequest.setId("omsProjectId");

 progress = new ProjectProgressResponse();
 progress.setCurrentStep("currentStep");
 progress.setCurrentStepStatus("currentStepStatus");
 progress.setFullTransferEstimatedCount(10);
 progress.setFullTransferFinishedCount(5);
 progress.setFullTransferProgressPercentage(50);
 progress.setFullVerificationEstimatedCount(20);
 progress.setFullVerificationFinishedCount(15);
 progress.setFullVerificationProgressPercentage(75);
 progress.setPreCheckResult("preCheckResult");
 progress.setPreCheckResultDescription("preCheckResultDescription");

 projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("currentStep");
 projectStepResult.setCurrentStepStatus("currentStepStatus");
 projectStepResult.setFullTransferEstimatedCount(10);
 projectStepResult.setFullTransferFinishedCount(5);
 projectStepResult.setFullTransferProgressPercentage(50);
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 projectStepResult.setFullVerificationResultDescription("fullVerificationResultDescription");
 projectStepResult.setFullVerificationProgressPercentage(75);
 projectStepResult.setPreCheckResult(PrecheckResult.PASSED);

 result = new OnlineSchemaChangeScheduleTaskResult();
 result.setFullTransferEstimatedCount(10);
 result.setFullTransferFinishedCount(5);
 result.setFullTransferProgressPercentage(50);
 result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 result.setFullVerificationResultDescription("fullVerificationResultDescription");
 result.setFullVerificationProgressPercentage(75);
 result.setPrecheckResult(PrecheckResult.PASSED);

 fullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
 fullVerifyResultRequest.setProjectId("omsProjectId");
 fullVerifyResultRequest.setSourceSchemas(new String[] {"databaseName"});
 fullVerifyResultRequest.setDestSchemas(new String[] {"databaseName"});
 fullVerifyResultRequest.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
 fullVerifyResultRequest.setPageSize(10);
 fullVerifyResultRequest.setPageNumber(1);
 fullVerifyResultRequest.setUid("uid");

 fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
 fullVerifyResultResponse.setData(Lists.newArrayList(new ProjectFullVerifyResultResponse.Data()));

 ((TestValveContextImpl) valveContext).setScheduleTask(scheduleTaskEntity);
 ((TestValveContextImpl) valveContext).setTaskParameter(taskParameter);

 when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
 when(scheduleTaskRepository.update(scheduleTaskEntity)).thenReturn(null);
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Arrays.asList(new ProjectStepVO()));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);
 when(projectOpenApiService.listProjectFullVerifyResult(fullVerifyResultRequest))
 .thenReturn(fullVerifyResultResponse);
 }

 @Test
 public void testInvoke() {
 scheduleCheckOmsProjectValve.invoke(valveContext);

 verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
 verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(fullVerifyResultRequest);
 verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
 }

 @Test
 public void testAdaptResult() {
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 ProjectStepResult projectStepResult = new ProjectStepResult();

 scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

 assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
 assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
 assertEquals(projectStepResult.getFullTransferProgressPercentage(),
 result.getFullTransferProgressPercentage());
 assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
 assertEquals(projectStepResult.getFullVerificationResultDescription(),
 result.getFullVerificationResultDescription());
 assertEquals(projectStepResult.getFullVerificationProgressPercentage(),
 result.getFullVerificationProgressPercentage());
 assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
 assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
 assertEquals(projectStepResult.getPrecheckResult(), result.getPrecheckResult());
 }

 @Test
 public void testGetProjectRequest() {
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid("uid");
 taskParameter.setOmsProjectId("omsProjectId");

 ProjectControlRequest request = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

 assertEquals(taskParameter.getUid(), request.getUid());
 assertEquals(taskParameter.getOmsProjectId(), request.getId());
 }

 @Test
 public void testRecordCurrentProgress() {
 scheduleCheckOmsProjectValve.recordCurrentProgress("projectId", result);

 verify(log, times(1)).info(anyString());
 }

 @Test
 public void testGetHeader() {
 List<String> header = scheduleCheckOmsProjectValve.getHeader();

 assertEquals(5, header.size());
 }

}