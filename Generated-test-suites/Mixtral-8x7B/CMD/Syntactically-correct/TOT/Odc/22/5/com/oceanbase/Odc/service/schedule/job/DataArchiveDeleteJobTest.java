
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.JobInstance;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobFactory jobFactory;

    @Mock
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private DlmTask dlmTask;

    @Before
    public void setUp() throws Exception {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "jobFactory", jobFactory);
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "autowireCapableBeanFactory", autowireCapableBeanFactory);
    }

    @Test
    public void testExecuteWithValidTaskEntityAndFinishedDataArchiveTask() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("{}");
        taskEntity.setStatus(TaskStatus.DONE);
        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(3L);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        scheduleTask.setParametersJson(parametersJson);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setJobType(JobType.DELETE);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setJobType(JobType.DELETE);
        dlmTask2.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask2);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(3L, TaskStatus.FAILED)).thenReturn(true);
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleTaskRepository.executeTask(3L, taskUnits)).thenReturn(true);

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(3L, TaskStatus.DONE);
    }

    @Test
    public void testExecuteWithValidTaskEntityAndNotFoundDataArchiveTask() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("{}");
        taskEntity.setStatus(TaskStatus.DONE);
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWithValidTaskEntityAndNotFinishedDataArchiveTask() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("{}");
        taskEntity.setStatus(TaskStatus.DONE);
        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.PREPARING);
        dataArchiveTask.setResultJson("[]");

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

}
