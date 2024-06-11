package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob abstractDlmJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private DlmJobIdUtil dlmJobIdUtil;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveRollbackJob.setJsonUtils(jsonUtils);
        dataArchiveRollbackJob.setDlmJobIdUtil(dlmJobIdUtil);
    }

    @Test
    public void testExecute_whenTaskEntityIsNull() {
        when(context.getResult()).thenReturn(null);

        dataArchiveRollbackJob.execute(context);

        verify(log, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=null");
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(log, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=1");
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound() throws Exception {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        when(jsonUtils.fromJson(anyString(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(dlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("historyJobId");
        doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), anyList());
        when(dataArchiveRollbackJob.getTaskStatus(anyList())).thenReturn(TaskStatus.DONE);

        dataArchiveRollbackJob.execute(context);

        verify(log, never()).warn(anyString());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreDone() {
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        when(taskUnits.stream().allMatch(task -> task.getStatus() == TaskStatus.DONE))
                .thenReturn(true);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenNotAllTasksAreDone() {
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        when(taskUnits.stream().allMatch(task -> task.getStatus() == TaskStatus.DONE))
                .thenReturn(false);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertFalse(TaskStatus.DONE.equals(taskStatus));
    }

    @Test
    public void testPrepareTaskUnits_whenTaskUnitsAreEmpty() throws Exception {
        List<DlmTask> taskUnits = Collections.emptyList();

        List<DlmTask> preparedTaskUnits = dataArchiveRollbackJob.prepareTaskUnits(taskUnits);

        assertNotNull(preparedTaskUnits);
        assertTrue(preparedTaskUnits.isEmpty());
    }

    @Test
    public void testPrepareTaskUnits_whenTaskUnitsAreNotEmpty() throws Exception {
        DlmTask taskUnit = new DlmTask();
        List<DlmTask> taskUnits = Collections.singletonList(taskUnit);

        List<DlmTask> preparedTaskUnits = dataArchiveRollbackJob.prepareTaskUnits(taskUnits);

        assertNotNull(preparedTaskUnits);
        assertEquals(1, preparedTaskUnits.size());
        assertEquals(taskUnit, preparedTaskUnits.get(0));
    }
}