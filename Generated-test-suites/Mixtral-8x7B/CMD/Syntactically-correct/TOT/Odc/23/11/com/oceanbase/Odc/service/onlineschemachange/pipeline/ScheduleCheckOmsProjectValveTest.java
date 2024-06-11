import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;

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

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testInvoke() {
        // Setup mock objects and dependencies

        // Call the invoke method
        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTaskEntity));

        // Verify that the expected methods of the ScheduleTaskEntity object are called
        verify(scheduleTaskRepository, times(1)).update(scheduleTaskEntity);
    }

    @Test(expected = Exception.class)
    public void testInvokeWithProjectOpenApiServiceException() {
        // Setup mock objects and dependencies
        when(projectOpenApiService.describeProjectSteps(any())).thenThrow(new Exception());

        // Call the invoke method
        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTaskEntity));
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.ALTER_TABLE;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        // Call the handleOmsProjectStepResult method with different input parameters
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new OscValveContext(scheduleTask), projectStepResult,
                result, swapTableType, scheduleTask);

        // Verify that the method behaves as expected
        verify(projectOpenApiService, times(1)).resumeProject(any());
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResultWithPrecheckResultFailed() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.ALTER_TABLE;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        // Call the handleOmsProjectStepResult method
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new OscValveContext(scheduleTask), projectStepResult,
                result, swapTableType, scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResultWithFullVerificationResultInconsistent() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.ALTER_TABLE;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        // Call the handleOmsProjectStepResult method
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new OscValveContext(scheduleTask), projectStepResult,
                result, swapTableType, scheduleTask);
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.SUCCEED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        // Call the continueHandleProjectStepResult method
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        // Verify that the method behaves as expected
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultWithPrecheckResultFailed() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        // Call the continueHandleProjectStepResult method
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultWithFullVerificationResultInconsistent() {
        // Setup mock objects and dependencies
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // Call the continueHandleProjectStepResult method
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }
}