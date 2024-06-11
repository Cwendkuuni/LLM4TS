import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private List<DlmTask> taskUnits;
    private DlmTask taskUnit;
    private TypeReference<List<DlmTask>> typeReference;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setup() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        taskUnits = new ArrayList<>();
        taskUnit = mock(DlmTask.class);
        typeReference = new TypeReference<List<DlmTask>>() {
        };
        rollbackParameters = new DataArchiveRollbackParameters();

        doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
        doReturn(JsonUtils.toJson(rollbackParameters)).when(scheduleTaskEntity).getParametersJson();
        doReturn(dataArchiveTask).when(dataArchiveRollbackJob).findDataArchiveTaskById(anyLong());
        doReturn(taskUnits).when(dataArchiveRollbackJob).prepareTasksForRollback(any(DataArchiveRollbackParameters.class));
        doReturn(scheduleTaskRepository).when(dataArchiveRollbackJob).getScheduleTaskRepository();
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn(0L);
        when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(2L);
        when(taskUnit.getJobType()).thenReturn(JobType.NORMAL);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenStatusUpdatedToFailed() throws Exception {
        doReturn(Optional.ofNullable(null)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTaskUnitsNull_thenStatusUpdatedToFailed() throws Exception {
        doReturn(null).when(dataArchiveRollbackJob)
                .prepareTasksForRollback(rollbackParameters);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTaskUnitsNotEmpty_thenTaskStatusUpdatedToDONE() throws Exception {
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testFindDataArchiveTaskById_whenIdExists_thenReturnsDataArchiveTask() {
        long id = 1L;

        Optional<ScheduleTaskEntity> result = dataArchiveRollbackJob.findDataArchiveTaskById(id);

        assertNotNull(result);
        assertEquals(dataArchiveTask, result.get());
    }

    @Test
    public void testFindDataArchiveTaskById_whenIdDoesNotExist_thenReturnsEmptyOptional() {
        long id = 1L;

        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Optional<ScheduleTaskEntity> result = dataArchiveRollbackJob.findDataArchiveTaskById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPrepareTasksForRollback_whenRollbackParametersNotNull_thenReturnsListOfDlmTasks() {
        when(dataArchiveTask.getResultJson()).thenReturn("{\"tasks\":[{\"id\":1,\"name\":\"task1\"}]}");

        List<DlmTask> result = dataArchiveRollbackJob.prepareTasksForRollback(rollbackParameters);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals("task1", result.get(0).getName());
    }

    @Test
    public void testPrepareTasksForRollback_whenRollbackParametersNull_thenReturnsEmptyListOfDlmTasks() {
        List<DlmTask> result = dataArchiveRollbackJob.prepareTasksForRollback(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testExecuteTask_whenTaskUnitsNotEmpty_thenTaskUnitsProcessed() throws Exception {
        when(dataArchiveRollbackJob.getJobThread()).thenReturn(Thread.currentThread());

        dataArchiveRollbackJob.executeTask(1L, taskUnits);

        verify(taskUnit, times(1)).prepare();
        verify(taskUnit, times(1)).execute();
        verify(taskUnit, times(1)).cleanup();
        verify(dataArchiveRollbackJob.getJobThread(), times(1)).interrupt();
    }

    @Test
    public void testExecuteTask_whenTaskUnitsEmpty_thenNoActionTaken() throws Exception {
        dataArchiveRollbackJob.executeTask(1L, new ArrayList<>());

        verify(taskUnit, never()).prepare();
        verify(taskUnit, never()).execute();
        verify(taskUnit, never()).cleanup();
        verify(dataArchiveRollbackJob.getJobThread(), never()).interrupt();
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreDONE_thenReturnsDONE() throws Exception {
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit);

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.DONE, result);
    }

    @Test
    public void testGetTaskStatus_whenAtLeastOneTaskIsNOTDONE_thenReturnsNOTDONE() throws Exception {
        taskUnit.setStatus(TaskStatus.FAILED);
        taskUnits.add(taskUnit);

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, result);
    }

}