import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob() {
            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        });
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("DataArchiveRollbackJob");
        taskEntity.setJobGroup("DEFAULT");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.PREPARING);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        doReturn(dataArchiveTaskOption).when(dataArchiveRollbackJob).scheduleTaskRepository;

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        doReturn(taskUnits).when(dataArchiveRollbackJob).findDataArchiveTaskById(rollbackParameters.getDataArchiveTaskId());

        dataArchiveRollbackJob.execute(null);

        verify(dataArchiveRollbackJob, times(1)).findDataArchiveTaskById(rollbackParameters.getDataArchiveTaskId());
        verify(dataArchiveRollbackJob, times(1))
                .executeTask(taskEntity.getId(), taskUnits);
        verify(dataArchiveRollbackJob, times(1)).getTaskStatus(taskUnits);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    private List<DlmTask> findDataArchiveTaskById(Long dataArchiveTaskId) {
        return dataArchiveRollbackJob.findDataArchiveTaskById(dataArchiveTaskId);
    }

    private List<DlmTask> executeTask(Long taskId, List<DlmTask> taskUnits) {
        return dataArchiveRollbackJob.executeTask(taskId, taskUnits);
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return dataArchiveRollbackJob.getTaskStatus(taskUnits);
    }
}