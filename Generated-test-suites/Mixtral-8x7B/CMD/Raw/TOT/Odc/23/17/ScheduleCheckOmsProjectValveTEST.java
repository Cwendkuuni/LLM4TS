import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResultCheckerVerifyResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Create a mock ValveContext
        ValveContext valveContext = mock(ValveContext.class);
        
        // Create a mock ScheduleTaskEntity
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        
        // Set the mock ScheduleTaskEntity in the OscValveContext
        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        
        // Set the context in the valveContext
        when(valveContext.getContext()).thenReturn(context);
        
        // Call the invoke method
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ReflectionTestUtils.setField(valve, "projectOpenApiService", mock(ProjectOpenApiService.class));
        ReflectionTestUtils.setField(valve, "completeHandler", mock(OscTaskCompleteHandler.class));
        ReflectionTestUtils.setField(valve, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));
        ReflectionTestUtils.setField(valve, "onlineSchemaChangeProperties", mock(OnlineSchemaChangeProperties.class));
        valve.invoke(valveContext);
        
        // Verify that the method was called on the scheduleTaskRepository
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Create a mock ValveContext
        ValveContext valveContext = mock(ValveContext.class);
        
        // Create a mock ProjectStepResult
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        
        // Create a mock OnlineSchemaChangeScheduleTaskResult
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        
        // Create a mock SwapTableType
        SwapTableType swapTableType = mock(SwapTableType.class);
        
        // Create a mock ScheduleTaskEntity
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        
        // Set the projectStepResult status to DONE and the fullVerificationResult to CONSISTENT
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        
        // Set the isEnableSwapTable method to return false
        when(SwapTableUtil.isSwapTableEnable(swapTableType, scheduleTask.getStatus(), anyDouble(), any(), anyBoolean()))
                .thenReturn(false);
        
        // Call the handleOmsProjectStepResult method
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ReflectionTestUtils.setField(valve, "projectOpenApiService", mock(ProjectOpenApiService.class));
        ReflectionTestUtils.setField(valve, "completeHandler", mock(OscTaskCompleteHandler.class));
        ReflectionTestUtils.setField(valve, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));
        ReflectionTestUtils.setField(valve, "onlineSchemaChangeProperties", mock(OnlineSchemaChangeProperties.class));
        valve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);
        
        // Verify that the scheduleTaskRepository update method was called
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Create a mock ScheduleCheckOmsProjectValve
        ScheduleCheckOmsProjectValve valve = mock(ScheduleCheckOmsProjectValve.class);
        
        // Create a mock ListProjectFullVerifyResultRequest
        ListProjectFullVerifyResultRequest request = mock(ListProjectFullVerifyResultRequest.class);
        
        // Set the projectId, databaseName, and uid in the request
        when(request.getProjectId()).thenReturn("projectId");
        when(request.getSourceSchemas()).thenReturn(new String[] {"databaseName"});
        when(request.getDestSchemas()).thenReturn(new String[] {"databaseName"});
        when(request.getUid()).thenReturn("uid");
        
        // Create a mock ProjectFullVerifyResultResponse
        ProjectFullVerifyResultResponse response = mock(ProjectFullVerifyResultResponse.class);
        
        // Set the response to be returned from the projectOpenApiService
        when(valve.listProjectFullVerifyResult(request)).thenReturn(response);
        
        // Call the listProjectFullVerifyResult method
        ProjectFullVerifyResultResponse result = valve.listProjectFullVerifyResult("projectId", "databaseName", "uid");
        
        // Verify that the listProjectFullVerifyResult method was called on the valve
        verify(valve).listProjectFullVerifyResult(request);
        
        // Verify that the response is returned
        assertEquals(response, result);
    }
}