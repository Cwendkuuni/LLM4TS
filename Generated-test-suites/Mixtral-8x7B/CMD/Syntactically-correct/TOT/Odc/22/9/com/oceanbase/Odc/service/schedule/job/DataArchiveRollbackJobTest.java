import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        jobExecutionContext = new JobExecutionContext();
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(1L);
        dataArchiveTaskEntity.setResultJson(JsonUtils.toJson(getDlmTasks()));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTaskEntity));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteNullScheduleTaskEntity() throws JobExecutionException {
        jobExecutionContext.setMergedJobDataMap(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // no exception should be thrown
    }

    @Test
    public void testExecuteEmptyDataArchiveTaskOption() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    private List<DlmTask> getDlmTasks() {
        // TODO: return a list of mock DlmTask objects
    }
}