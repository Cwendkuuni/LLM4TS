import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
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
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.OperableTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);
    }

    @Test
    public void testExecute() throws SchedulerException {
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters();
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(createScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = createDlmTaskList();
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws SchedulerException {
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_prepareTasksForRollback_emptyTaskUnits() throws SchedulerException {
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters();
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(createScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(new ArrayList<>());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_executeTask() throws SchedulerException {
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters();
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(createScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = createDlmTaskList();
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        when(
                ReflectionTestUtils.invokeMethod(dataArchiveRollbackJob, "executeTask", anyLong(), anyList()))
                .thenReturn(true);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_getTaskStatus() {
        List<DlmTask> taskUnits = createDlmTaskList();

        TaskStatus taskStatus = DataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        scheduleTaskEntity.setResultJson("[]");

        return scheduleTaskEntity;
    }

    private DataArchiveRollbackParameters createDataArchiveRollbackParameters() {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(1L);

        return parameters;
    }

    private List<DlmTask> createDlmTaskList() {
        DlmTask task1 = new DlmTask();
        task1.setId(1L);
        task1.setSourceDatabaseId(1L);
        task1.setTargetDatabaseId(2L);
        task1.setJobType(JobType.MIGRATE);
        task1.setStatus(TaskStatus.PREPARING);

        DlmTask task2 = new DlmTask();
        task2.setId(2L);
        task2.setSourceDatabaseId(2L);
        task2.setTargetDatabaseId(1L);
        task2.setJobType(JobType.MIGRATE);
        task2.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(task1);
        taskUnits.add(task2);

        return taskUnits;
    }
}