
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = projectOpenApiService;
        scheduleCheckOmsProjectValve.completeHandler = completeHandler;
        scheduleCheckOmsProjectValve.scheduleTaskRepository = scheduleTaskRepository;
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    @Test
    public void testInvoke() {
        // Initialize mocks and stubs
        ValveContext valveContext = mock(ValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);

        // Call the method under test
        new ScheduleCheckOmsProjectValve().invoke(valveContext);

        // Verify the interactions and results
        verify(valveContext, times(1)).getScheduleTask();
        verify(valveContext, times(1)).getTaskParameter();
        // Add more verifications based on the specific behavior of the method
    }

    @Test
    public void testHandleOmsProjectStepResult_doneWithConsistentOrUncheck() {
        // Initialize mocks, stubs, and test data
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);

        // Set the task status and full verification result and call the method under test
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        new ScheduleCheckOmsProjectValve().handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Verify the interactions and results
        verify(valveContext, times(1)).getScheduleTask();
        // Add more verifications based on the specific behavior of the method for this scenario
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Initialize the class under test and test data
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId("test_project_id");
        request.setSourceSchemas(new String[] {"test_database"});
        request.setDestSchemas(new String[] {"test_database"});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid("test_uid");

        // Set the expected response from the API
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        // Add necessary data to the response

        // Mock the API call and set the expected behavior
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        // Call the method under test
        ProjectFullVerifyResultResponse actualResponse = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(request);

        // Verify the interactions and results
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
        assertEquals(response, actualResponse);
    }
}
