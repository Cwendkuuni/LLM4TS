import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setMongoTemplate(mongoTemplate);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        JobExecutionContext context = createJobExecutionContext();

        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId("invalidTaskId");
        String parametersJson = JsonUtils.toJson(parameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskId");
        taskEntity.setParametersJson(parametersJson);

        when(scheduleTaskRepository.findById("invalidTaskId")).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        // Verify the task status was updated to failed
        assertEquals(TaskStatus.FAILED, context.getResult());
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
        JobExecutionContext context = createJobExecutionContext();

        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId("validTaskId");
        String parametersJson = JsonUtils.toJson(parameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskId");
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("validTaskId");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById("validTaskId")).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        // Verify the task status was updated to failed
        assertEquals(TaskStatus.FAILED, context.getResult());
    }

    @Test
    public void testExecute_ValidParameters() throws JobExecutionException {
        JobExecutionContext context = createJobExecutionContext();

        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId("validTaskId");
        String parametersJson = JsonUtils.toJson(parameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskId");
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("validTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = List.of(new DlmTask());
        when(scheduleTaskRepository.findById("validTaskId")).thenReturn(Optional.of(dataArchiveTask));
        when(mongoTemplate.find(any(Query.class), any(TypeReference.class), anyString()))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        // Verify the task status was updated
        assertEquals(TaskStatus.PREPARING, context.getResult());
    }

    private JobExecutionContext createJobExecutionContext() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        QuartzJobBean quartzJobBean = mock(QuartzJobBean.class);
        when(context.getMergedJobDataMap()).thenReturn(quartzJobBean.getJobDetail().getJobDataMap());
        when(context.getResult()).thenReturn(TaskStatus.PENDING);
        return context;
    }
}