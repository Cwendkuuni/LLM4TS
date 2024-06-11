import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.test.util.ReflectionTestUtils;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        jobExecutionContext = mock(JobExecutionContext.class);

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        String parametersJson = "{}";
        String resultJson = "[]";

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);
        taskEntity.setResultJson(resultJson);
        taskEntity.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        String dataArchiveTaskParametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setParametersJson(dataArchiveTaskParametersJson);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(resultJson);

        List<DlmTask> dlmTasks = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(dataArchiveTaskOption);

        dataArchiveDeleteJob.execute(jobExecutionContext);
    }
}