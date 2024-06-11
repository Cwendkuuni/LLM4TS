import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setUp() {
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("12345");
        taskParameter.setDatabaseName("testdb");
        taskParameter.setUid("67890");

        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setResultJson("{}");

        ReflectionTestUtils.setField(scheduleCheckOmsProjectValve, "language", Locale.ENGLISH);
    }

    @Test
    public void testInvoke_success() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectSteps();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.CONSISTENT, "consistent", 50, 100, OmsStepName.PRE_CHECK, PrecheckResult.PASSED,
                null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTask, taskParameter));

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testInvoke_precheckFailed() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectSteps();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.CONSISTENT, "consistent", 50, 100, OmsStepName.PRE_CHECK, PrecheckResult.FAILED,
                "precheck failed");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTask, taskParameter));
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataCheckInconsistent() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectSteps();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.INCONSISTENT, "inconsistent", 50, 100, OmsStepName.FULL_VERIFIER, PrecheckResult.PASSED,
                null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(new OscValveContext(scheduleTask, taskParameter));
    }

    @Test
    public void testHandleOmsProjectStepResult_enableSwapTable() {
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.CONSISTENT, "consistent", 50, 100, OmsStepName.FULL_VERIFIER, PrecheckResult.PASSED,
                null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new OscValveContext(scheduleTask, taskParameter),
                projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        assertEquals(true, result.isManualSwapTableEnabled());
    }

    @Test
    public void testHandleOmsProjectStepResult_disableSwapTable() {
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.UNCHECK, "unchecked", 50, 100, OmsStepName.FULL_VERIFIER, PrecheckResult.PASSED,
                null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new OscValveContext(scheduleTask, taskParameter),
                projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        assertEquals(false, result.isManualSwapTableEnabled());
    }

    @Test
    public void testAdaptResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, 10, 5, 50, 100,
                FullVerificationResult.CONSISTENT, "consistent", 50, 100, OmsStepName.FULL_VERIFIER, PrecheckResult.PASSED,
                null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals(10, result.getFullTransferEstimatedCount());
        assertEquals(5, result.getFullTransferFinishedCount());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("consistent", result.getFullVerificationResultDescription());
        assertEquals(50, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.FULL_VERIFIER, result.getCurrentStep());
        assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
        assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
    }

    @Test
    public void testRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationProgressPercentage(100);
        result.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        result.setCurrentStepStatus(TaskStatus.DONE);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("consistent");

        scheduleCheckOmsProjectValve.recordCurrentProgress("12345", result);

        verify(log, times(1)).info(anyString());
    }

    private List<ProjectStepVO> createProjectSteps() {
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        projectSteps.add(new ProjectStepVO("1", "step1", TaskStatus.DONE, ""));
        projectSteps.add(new ProjectStepVO("2", "step2", TaskStatus.DONE, ""));
        projectSteps.add(new ProjectStepVO("3", "step3", TaskStatus.DONE, ""));
        return projectSteps;
    }
}