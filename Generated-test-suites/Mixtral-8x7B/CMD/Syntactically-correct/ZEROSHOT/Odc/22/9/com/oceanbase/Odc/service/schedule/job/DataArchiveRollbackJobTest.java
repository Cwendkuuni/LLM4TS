import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

    @Autowired
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        taskUnits.add(dlmTask);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecuteFailed() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteNullTask() throws JobExecutionException {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, TaskStatus.FAILED);
    }
}