import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.dlm.model.DlmTask.DlmTaskBuilder;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    public ScheduleTaskEntity createScheduleTaskEntity(long id, String parametersJson, String resultJson) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(id);
        scheduleTaskEntity.setParametersJson(parametersJson);
        scheduleTaskEntity.setResultJson(resultJson);
        return scheduleTaskEntity;
    }

    @Test
    public void testExecute() throws Exception {
        String parametersJson = JsonUtils.toJson(new DataArchiveRollbackParameters(1L));
        String resultJson = JsonUtils.toJson(new TypeReference<List<DlmTask>>() {});
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, parametersJson, resultJson);
        when(context.getResult()).thenReturn(taskEntity);

        DlmTask dlmTask1 = new DlmTaskBuilder().id(1L).sourceDatabaseId(2L).targetDatabaseId(3L).build();
        DlmTask dlmTask2 = new DlmTaskBuilder().id(2L).sourceDatabaseId(3L).targetDatabaseId(2L).build();
        List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(createScheduleTaskEntity(rollbackParameters.getDataArchiveTaskId(), "", resultJson)));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
        verify(scheduleTaskRepository, times(1))
                .findById(rollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.DONE);

        assertEquals(3, dlmTask1.getSourceDatabaseId());
        assertEquals(2, dlmTask1.getTargetDatabaseId());
        assertEquals(JobType.ROLLBACK, dlmTask1.getJobType());
        assertEquals(TaskStatus.DONE, dlmTask1.getStatus());

        assertEquals(2, dlmTask2.getSourceDatabaseId());
        assertEquals(3, dlmTask2.getTargetDatabaseId());
        assertEquals(JobType.ROLLBACK, dlmTask2.getJobType());
        assertEquals(TaskStatus.PREPARING, dlmTask2.getStatus());
    }
}