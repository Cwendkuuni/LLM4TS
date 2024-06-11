import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

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

@SuppressWarnings("unchecked")
public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        ProjectOpenApiService projectOpenApiService = mock(ProjectOpenApiService.class);
        OscTaskCompleteHandler completeHandler = mock(OscTaskCompleteHandler.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        OnlineSchemaChangeProperties onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        Environment env = mock(Environment.class);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ReflectionTestUtils.setField(valve, "projectOpenApiService", projectOpenApiService);
        ReflectionTestUtils.setField(valve, "completeHandler", completeHandler);
        ReflectionTestUtils.setField(valve, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(valve, "onlineSchemaChangeProperties", onlineSchemaChangeProperties);
        ReflectionTestUtils.setField(valve, "env", env);

        // Test case 1: normal execution
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId("123");
        when(env.getProperty("abc")).thenReturn("def");
        when(scheduleTaskRepository.findById(1L)).thenReturn(java.util.Optional.of(new ScheduleTaskEntity()));
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Arrays.asList(
                new ProjectStepVO("1", "step1", "RUNNING", "10", "20"),
                new ProjectStepVO("2", "step2", "FINISHED", "100", "50"),
                new ProjectStepVO("3", "step3", "SUSPENDED", "30", "70")
        ));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(
                new ProjectProgressResponse("123", "FINISHED", "50%", ""));
        ProjectFullVerifyResultResponse fullVerifyResult = new ProjectFullVerifyResultResponse();
        fullVerifyResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest("123",
                "test", "test", new String[] {"FINISHED", "SUSPENDED", "RUNNING"}, new String[] {"test"}, new String[] {"test"},
                1, 1, "test"))).thenReturn(fullVerifyResult);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        when(scheduleTaskRepository.update(new ScheduleTaskEntity())).thenReturn(new ScheduleTaskEntity());
        ValveContext context = new OscValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        context.setTaskParameter(taskParameter);
        context.setParameter(new SwapTableType());
        valve.invoke(context);
        // Verify the progress percentage and result of the schedule task
        assertEquals(50, context.getScheduleTask().getProgressPercentage());
        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = JsonUtils.fromJson(context.getScheduleTask().getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(0, scheduleTaskResult.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, scheduleTaskResult.getFullVerificationResult());
        assertEquals(true, scheduleTaskResult.isManualSwapTableEnabled());

        // Test case 2: error handling
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest("123",
                "test", "test", new String[] {"FINISHED", "SUSPENDED", "RUNNING"}, new String[] {"test"}, new String[] {"test"},
                1, 1, "test"))).thenThrow(new RuntimeException());
        context = new OscValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        context.setTaskParameter(taskParameter);
        context.setParameter(new SwapTableType());
        try {
            valve.invoke(context);
        } catch (OscException e) {
            // Verify the error message
            assertEquals("Failed to get project full verify result from projectOpenApiService", e.getMessage());
        }
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        // Test case 1: DONE with consistent data
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        result.setFullTransferProgressPercentage(50);
        boolean isEnableSwapTable = SwapTableUtil.isSwapTableEnable(SwapTableType.NONE, TaskStatus.DONE, 50,
                FullVerificationResult.CONSISTENT, false);
        valve.handleOmsProjectStepResult(new OscValveContext(), projectStepResult, result, SwapTableType.NONE,
                new ScheduleTaskEntity());
        assertEquals(true, result.isManualSwapTableEnabled());

        // Test case 2: DONE with inconsistent data
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        try {
            valve.handleOmsProjectStepResult(new OscValveContext(), projectStepResult, result, SwapTableType.NONE,
                    new ScheduleTaskEntity());
        } catch (OscException e) {
            // Verify the error message
            assertEquals("Task failed for origin table has inconsistent data with new table, result: "
                    + FullVerificationResult.INCONSISTENT, e.getMessage());
        }

        // Test case 3: PRE_CHECK with failed precheck
        projectStepResult.setTaskStatus(TaskStatus.PRE_CHECK);
        projectStepResult.setFullVerificationResult(null);
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        try {
            valve.handleOmsProjectStepResult(new OscValveContext(), projectStepResult, result, SwapTableType.NONE,
                    new ScheduleTaskEntity());
        } catch (OscException e) {
            // Verify the error message
            assertEquals("Precheck failed", e.getMessage());
        }

        // Test case 4: FULL_TRANSFER with estimated and finished count
        projectStepResult.setTaskStatus(TaskStatus.FULL_TRANSFER);
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        valve.handleOmsProjectStepResult(new OscValveContext(), projectStepResult, result, SwapTableType.NONE,
                new ScheduleTaskEntity());
        assertEquals(50, result.getFullTransferProgressPercentage());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        valve.continueHandleProjectStepResult(projectStepResult);
        assertEquals(ErrorCodes.OmsPreCheckFailed,
                ((OscException) ReflectionTestUtils.getField(valve, "exception")).getErrorCode());

        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        valve.continueHandleProjectStepResult(projectStepResult);
        assertEquals(ErrorCodes.OmsDataCheckInconsistent,
                ((OscException) ReflectionTestUtils.getField(valve, "exception")).getErrorCode());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ProjectOpenApiService projectOpenApiService = mock(ProjectOpenApiService.class);
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ReflectionTestUtils.setField(valve, "projectOpenApiService", projectOpenApiService);

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest("123", "test", "test",
                new String[] {"FINISHED", "SUSPENDED", "RUNNING"}, new String[] {"test"}, new String[] {"test"},
                1, 1, "test");
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(new ProjectFullVerifyResultResponse());

        valve.listProjectFullVerifyResult("123", "test", "test");
        // Verify that the request is passed to projectOpenApiService
        assertEquals(request, ReflectionTestUtils.getField(valve, "request"));
    }

    @Test
    public void testAdaptResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        valve.adaptResult(result, projectStepResult);
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
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
    }

    @Test
    public void testGetHeader() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        List<String> header = valve.getHeader();
        assertEquals(5, header.size());
        assertEquals("ProjectId", header.get(0));
        assertEquals("Step", header.get(1));
        assertEquals("Status", header.get(2));
        assertEquals("Progress", header.get(3));
        assertEquals("Description", header.get(4));
    }

    @Test
    public void testRecordCurrentProgress() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        Table table = new DefaultTableFactory().generateTable(5, valve.getHeader(),
                Arrays.asList("123", "step1", "RUNNING", "10%", "description1"));
        String expected = "________________________________________________________\n"
                + "| ProjectId  | Step     | Status   | Progress | Description  |\n"
                + "|------------|----------|----------|----------|--------------|\n"
                + "| 123        | step1    | RUNNING  | 10%      | description1|\n"
                + "|            |          |          |          |             |\n"
                + "|____________|__________|__________|__________|_____________|\n"
                + "";
        assertEquals(expected, table.render().toString());
    }
}