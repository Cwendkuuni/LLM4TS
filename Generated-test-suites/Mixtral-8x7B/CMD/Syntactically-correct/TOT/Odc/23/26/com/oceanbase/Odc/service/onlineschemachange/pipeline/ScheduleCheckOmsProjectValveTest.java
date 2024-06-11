import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Initialize OnlineSchemaChangeScheduleTaskParameters
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ReflectionTestUtils.setField(scheduleCheckOmsProjectValve, "taskParameter", taskParameter);

        // Initialize ScheduleTaskEntity
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("{}");
        ReflectionTestUtils.setField(scheduleCheckOmsProjectValve, "scheduleTask", scheduleTask);
    }

    @Test
    public void testInvokeSuccess() {
        // Prepare test data
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        List<ProjectStepVO> projectSteps = mock(List.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);

        // Set up mock behavior
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepResult.getTaskPercentage()).thenReturn(100);

        // Call the method under test
        scheduleCheckOmsProjectValve.invoke(null);

        // Assert that the method updated the scheduleTask with correct data
        ScheduleTaskEntity scheduleTask = (ScheduleTaskEntity) ReflectionTestUtils.getField(
                scheduleCheckOmsProjectValve, "scheduleTask");
        assertEquals(100, scheduleTask.getProgressPercentage().intValue());
    }
}