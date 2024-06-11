import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve valve;
    private ProjectOpenApiService mockProjectOpenApiService;
    private OscTaskCompleteHandler mockCompleteHandler;
    private ScheduleTaskRepository mockScheduleTaskRepository;
    private OnlineSchemaChangeProperties mockOnlineSchemaChangeProperties;

    @Before
    public void setup() {
        mockProjectOpenApiService = mock(ProjectOpenApiService.class);
        mockCompleteHandler = mock(OscTaskCompleteHandler.class);
        mockScheduleTaskRepository = mock(ScheduleTaskRepository.class);
        mockOnlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);

        valve = new ScheduleCheckOmsProjectValve() {
            @Autowired
            private ProjectOpenApiService projectOpenApiService;

            @Autowired
            private OscTaskCompleteHandler completeHandler;

            @Autowired
            private ScheduleTaskRepository scheduleTaskRepository;

            @Autowired
            private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

            @Override
            public void invoke(ValveContext valveContext) {
                this.projectOpenApiService = mockProjectOpenApiService;
                this.completeHandler = mockCompleteHandler;
                this.scheduleTaskRepository = mockScheduleTaskRepository;
                this.onlineSchemaChangeProperties = mockOnlineSchemaChangeProperties;
                super.invoke(valveContext);
            }
        };
    }

    @Test
    public void testInvoke_typicalUseCase() {
        ValveContext context = mock(ValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        List<ProjectStepVO> projectSteps = Arrays.asList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);

        when(mockProjectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(mockProjectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(context);

        // Assert that the schedule task is updated with the correct result.
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(0, result.getFullVerificationProgressPercentage());
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferProgressPercentage());
        assertEquals(OmsStepName.PRE_CHECK, result.getCurrentStep());
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
    }

    @Test
    public void testInvoke_edgeCase() {
        ValveContext context = mock(ValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(mockProjectOpenApiService.describeProjectSteps(projectRequest))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> valve.invoke(context));
    }

    @Test
    public void testInvoke_errorScenario() {
        ValveContext context = mock(ValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(mockProjectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Arrays.asList(
                new ProjectStepVO().setTaskStatus(TaskStatus.ERROR)));

        assertThrows(OscException.class, () -> valve.invoke(context));
    }

    @Test
    public void testHandleOmsProjectStepResult_typicalUseCase() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.UNCHECK);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        valve.handleOmsProjectStepResult(context, projectStepResult, result, swapTableType, scheduleTask);

        // Assert that the result is updated with the correct properties.
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(0, result.getFullVerificationProgressPercentage());
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferProgressPercentage());
        assertEquals(OmsStepName.FULL_TRANSFER, result.getCurrentStep());
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
    }

    @Test
    public void testHandleOmsProjectStepResult_edgeCase() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.FAILED);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        assertThrows(OscException.class, () -> valve.handleOmsProjectStepResult(context, projectStepResult, result,
                swapTableType, scheduleTask));
    }

    @Test
    public void testHandleOmsProjectStepResult_errorScenario() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.ERROR);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        assertThrows(OscException.class, () -> valve.handleOmsProjectStepResult(context, projectStepResult, result,
                swapTableType, scheduleTask));
    }
}