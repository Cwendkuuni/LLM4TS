import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResultCheckerVerifyResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveParameter;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

@Mock
private ProjectOpenApiService projectOpenApiService;

@Mock
private OscTaskCompleteHandler completeHandler;

@InjectMocks
private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

private ValveContext valveContext;
private ScheduleTaskEntity scheduleTask;
private OscValveContext context;
private OnlineSchemaChangeScheduleTaskParameters taskParameter;
private ProjectControlRequest projectRequest;
private List<ProjectStepVO> projectSteps;
private ProjectProgressResponse progress;
private ProjectStepResult projectStepResult;
private OnlineSchemaChangeScheduleTaskResult result;
private String projectId;
private String databaseName;
private String uid;
private ProjectFullVerifyResultResponse response;

@Before
public void setup() {
valveContext = new ValveContext();
scheduleTask = new ScheduleTaskEntity();
context = new OscValveContext();
context.setScheduleTask(scheduleTask);
taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
projectRequest = new ProjectControlRequest();
projectSteps = new ArrayList<ProjectStepVO>();
progress = new ProjectProgressResponse();
projectStepResult = new ProjectStepResult();
result = new OnlineSchemaChangeScheduleTaskResult();
valveContext.setParameter(new ValveParameter());
valveContext.setTaskParameter(taskParameter);
valveContext.setContext(context);

ReflectionTestUtils.setField(scheduleCheckOmsProjectValve, "oscTaskCompleteHandler", completeHandler);

response = new ProjectFullVerifyResultResponse();
}

// Expert 1's test case
@Test
public void testInvoke() {
when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

scheduleCheckOmsProjectValve.invoke(valveContext);

verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
}

// Expert 2's test cases
@Test
public void testHandleOmsProjectStepResultSuccess() {
when(projectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(response);

scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, SwapTableType.AUTO, scheduleTask);

verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
}

@Test(expected = OscException.class)
public void testHandleOmsProjectStepResultFailed() {
projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, SwapTableType.AUTO, scheduleTask);
}

// Expert 3's test cases
@Test
public void testListProjectFullVerifyResult() {
scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);
}

@Test
public void testAdaptResult() {
projectStepResult.setFullTransferEstimatedCount(10);
projectStepResult.setFullTransferFinishedCount(5);
projectStepResult.setFullTransferProgressPercentage(50);

scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

assertEquals(10, result.getFullTransferEstimatedCount());
assertEquals(5, result.getFullTransferFinishedCount());
assertEquals(50, result.getFullTransferProgressPercentage());
}
}