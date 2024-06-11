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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private static final Logger logger = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        scheduleTaskEntity.setResultJson("[]");
        scheduleTaskEntity.setJobName("testJob");
        scheduleTaskEntity.setJobGroup("testGroup");
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(2L);
        dataArchiveTaskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(
                new DlmTask(1L, 2L, 3L, JobType.ARCHIVE, TaskStatus.PREPARING),
                new DlmTask(4L, 5L, 6L, JobType.ARCHIVE, TaskStatus.PREPARING)
        )));

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTaskEntity));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
        List<DlmTask> dlmTasks = JsonUtils.fromJson(dataArchiveTaskEntity.getResultJson(),
                new TypeReference<List<DlmTask>>() {});
        assertEquals(4, dlmTasks.size());
        for (DlmTask dlmTask : dlmTasks) {
            Long temp = dlmTask.getSourceDatabaseId();
            assertEquals(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(),
                            scheduleTaskEntity.getJobGroup(), scheduleTaskEntity.getId(), dlmTasks.size()),
                    dlmTask.getId());
            dlmTask.setSourceDatabaseId(dlmTask.getTargetDatabaseId());
            dlmTask.setTargetDatabaseId(temp);
            dlmTask.setJobType(JobType.ROLLBACK);
            dlmTask.setStatus(dlmTask.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING);
        }
    }

    @Test
    public void testExecuteFailed() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(3L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
}