import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testInvoke() {
ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
ProjectControlRequest projectRequest = new ProjectControlRequest();
List<ProjectStepVO> projectSteps = new ArrayList<>();
ProjectProgressResponse progress = new ProjectProgressResponse();
ProjectStepResult projectStepResult = new ProjectStepResult();

when(scheduleTaskRepository.update(any()))
.thenReturn(scheduleTask);

when(onlineSchemaChangeProperties.isEnableFullVerify())
.thenReturn(true);

scheduleCheckOmsProjectValve.invoke(new BaseValve.ValveContext() {
@Override
public Object getParameter() {
return null;
}

@Override
public ScheduleTaskEntity getScheduleTask() {
return scheduleTask;
}

@Override
public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
return taskParameter;
}
});

verify(projectOpenApiService, times(1))
.describeProjectSteps(projectRequest);

verify(projectOpenApiService, times(1))
.describeProjectProgress(projectRequest);

verify(projectOpenApiService, times(1))
.resumeProject(projectRequest);

verify(scheduleTaskRepository, times(1))
.update(scheduleTask);
}

@Test
public void testAdaptResult() {
OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
ProjectStepResult projectStepResult = new ProjectStepResult();

scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
}

@Test
public void testGetProjectRequest() {
OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();

ProjectControlRequest result = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

assertNotNull(result);
}

@Test
public void testRecordCurrentProgress() {
String projectId = "testProject";
OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

verify(log, times(1))
.info(anyString());
}

@Test
public void testListProjectFullVerifyResult() {
String projectId = "testProject";
String databaseName = "testDatabase";
String uid = "testUid";

ListProjectFullVerifyResultRequest request = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

assertNotNull(request);
assertEquals(projectId, request.getProjectId());
assertEquals(databaseName, request.getSourceSchemas()[0]);
assertEquals(databaseName, request.getDestSchemas()[0]);
assertEquals("FINISHED", request.getStatus()[0]);
assertEquals("SUSPEND", request.getStatus()[1]);
assertEquals("RUNNING", request.getStatus()[2]);
assertEquals(1, request.getPageNumber());
assertEquals(10, request.getPageSize());
assertEquals(uid, request.getUid());
}

@Test
public void testContinueHandleProjectStepResult() {
ProjectStepResult projectStepResult = new ProjectStepResult();

scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

verify(log, times(1))
.warn(anyString());
}
}