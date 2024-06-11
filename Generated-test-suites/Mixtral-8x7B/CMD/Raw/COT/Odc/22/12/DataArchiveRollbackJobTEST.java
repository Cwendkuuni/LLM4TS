import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "jobThread", Thread.currentThread());
    }

    @Test
    public void testExecute() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJob");
        taskEntity.setJobGroup("testGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(null);
        when(JsonUtils.fromJson(any(), any())).thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId(any(), any(), any(), any())).thenReturn(3L);

        dataArchiveRollbackJob.execute(jobExecutionContext);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        taskUnits.get(1).setStatus(TaskStatus.PREPARING);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testExecuteTask() {
        Long taskId = 1L;
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.add(new DlmTask());

        dataArchiveRollbackJob.executeTask(taskId, taskUnits);
    }
}