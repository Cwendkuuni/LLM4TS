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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(null);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}