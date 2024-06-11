package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.ThreadUtil;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    private Logger logger;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        logger = mock(Logger.class);

        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }

            @Override
            protected ThreadUtil getThreadUtil() {
                return null;
            }

            @Override
            protected Logger getLogger() {
                return logger;
            }
        };
    }

    @Test
    public void testExecuteWithNullTaskEntity() throws SchedulerException {
        when(jobExecutionContext.getResult()).thenReturn(null);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(logger, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}",
                anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithInvalidTaskEntity() throws SchedulerException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(logger, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}",
                anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithFailedDataArchiveTask() throws SchedulerException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(logger, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}",
                anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithNotDoneDataArchiveTask() throws SchedulerException {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setStatus(TaskStatus.PREPARING);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(2L);
        taskEntity.setResultJson("[]");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(2L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithValidDataArchiveTask() throws SchedulerException {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[{\"id\":1,\"jobName\":\"job1\",\"jobGroup\":\"group1\","
                + "\"jobType\":\"TYPE1\",\"status\":\"STATUS1\"}]");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(2L);
        taskEntity.setResultJson("[]");

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setJobName("job1");
        dlmTask1.setJobGroup("group1");
        dlmTask1.setJobType(JobType.TYPE1);
        dlmTask1.setStatus("STATUS1");

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask1);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(2L), any(TaskStatus.class));
    }

    @Test
    public void testExecuteWithThread() throws Exception {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[{\"id\":1,\"jobName\":\"job1\",\"jobGroup\":\"group1\","
                + "\"jobType\":\"TYPE1\",\"status\":\"STATUS1\"}]");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(2L);
        taskEntity.setResultJson("[]");

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setJobName("job1");
        dlmTask1.setJobGroup("group1");
        dlmTask1.setJobType(JobType.TYPE1);
        dlmTask1.setStatus("STATUS1");

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask1);

        ThreadUtil threadUtil = mock(ThreadUtil.class);
        when(threadUtil.getJobThread()).thenReturn(Thread.currentThread());

        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }

            @Override
            protected ThreadUtil getThreadUtil() {
                return threadUtil;
            }

            @Override
            protected Logger getLogger() {
                return logger;
            }
        };

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(threadUtil, times(1)).getJobThread();
    }

    @Test
    public void testExecuteWithException() throws SchedulerException {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[{\"id\":1,\"jobName\":\"job1\",\"jobGroup\":\"group1\","
                + "\"jobType\":\"TYPE1\",\"status\":\"STATUS1\"}]");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(2L);
        taskEntity.setResultJson("[]");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenThrow(new SchedulerException("Scheduler exception"));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(logger, times(1)).error("Delete data archive task failed,rollback task fast failed.", any());
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(2L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testDlmJobIdUtilGenerateHistoryJobId() {
        String jobName = "jobName";
        String jobGroup = "jobGroup";
        Long taskId = 1L;
        int taskUnitsSize = 10;

        String result = DlmJobIdUtil.generateHistoryJobId(jobName, jobGroup, taskId, taskUnitsSize);

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}