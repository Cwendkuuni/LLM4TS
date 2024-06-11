
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataArchiveRollbackJobTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @BeforeEach
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteValid() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(3L);
        taskUnit.setSourceDatabaseId(4L);
        taskUnit.setTargetDatabaseId(5L);
        taskUnit.setJobType(JobType.DATA_ARCHIVE);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(null);

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteNullParametersJson() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(null);

        Assertions.assertThrows(JobExecutionException.class, () -> {
            dataArchiveRollbackJob.execute(context);
        });
    }

    @Test
    public void testExecuteEmptyParametersJson() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("");

        Assertions.assertThrows(JobExecutionException.class, () -> {
            dataArchiveRollbackJob.execute(context);
        });
    }

    @Test
    public void testExecuteInvalidDataArchiveTaskId() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": \"invalid\"}");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteNonExistingDataArchiveTaskId() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteInvalidResultJson() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setResultJson("{\"invalid\": \"json\"}");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteNullResultJson() throws JobExecutionException {
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setResultJson(null);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        Assertions.assertThrows(JobExecutionException.class, () -> {
            dataArchiveRollbackJob.execute(context);
        });
    }
}

