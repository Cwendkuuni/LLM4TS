import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepositoryMock;

    @SpyBean
    private DataArchiveRollbackJob job;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteWithValidData() throws Exception {
        // Prepare valid data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("12345");
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);
        List<DlmTask> taskUnits = new ArrayList<>();
        // Add valid task units
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        // Execute the method
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.getMergedJobDataMap().put("scheduleTaskEntity", taskEntity);
        job.execute(context);

        // Verify the result
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(scheduleTaskRepositoryMock, times(1)).updateStatusById(idCaptor.capture(), statusCaptor.capture());
        assertEquals("12345", idCaptor.getValue());
        assertEquals(TaskStatus.DONE, statusCaptor.getValue());
    }

    @Test
    public void testExecuteWithNotFoundDataArchiveTask() throws Exception {
        // Prepare invalid data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("12345");
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);
        ReflectionTestUtils.setField(job, "scheduleTaskRepository", scheduleTaskRepositoryMock);
        when(scheduleTaskRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

        // Execute the method
        DataArchiveRollbackJob jobSpy = spy(job);
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.getMergedJobDataMap().put("scheduleTaskEntity", taskEntity);
        jobSpy.execute(context);

        // Verify the result
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(scheduleTaskRepositoryMock, times(1)).updateStatusById(idCaptor.capture(), statusCaptor.capture());
        assertEquals("12345", idCaptor.getValue());
        assertEquals(TaskStatus.FAILED, statusCaptor.getValue());
    }

    @Test(expected = Exception.class)
    public void testExecuteWithJsonParseException() throws Exception {
        // Prepare invalid data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        String invalidParametersJson = "{invalid json}";
        taskEntity.setParametersJson(invalidParametersJson);

        // Execute the method
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.getMergedJobDataMap().put("scheduleTaskEntity", taskEntity);
        job.execute(context);
    }
}