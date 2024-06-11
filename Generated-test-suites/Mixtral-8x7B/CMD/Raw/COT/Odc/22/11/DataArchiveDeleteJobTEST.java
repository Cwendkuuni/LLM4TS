import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.simpl.SimpleJob;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.utils.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

    @MockBean
    private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobFactory jobFactory = new SimpleJobFactory() {
            @Override
            protected SimpleJob newJob(Class<? extends SimpleJob> jobClass, Key key, JobDataMap jobDataMap)
                    throws SchedulerException {
                return (SimpleJob) super.newJob(jobClass, key, jobDataMap);
            }
        };
        schedulerFactoryBean.setJobFactory(jobFactory);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        String jsonParameters = JsonUtils.toJson(dataArchiveClearParameters);
        when(taskEntity.getParametersJson()).thenReturn(jsonParameters);
        when(taskEntity.getResultJson()).thenReturn("[]");
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("456");
        dlmTask.setJobType(JobType.DELETE);
        List<DlmTask> taskUnits = Collections.singletonList(dlmTask);
        when(JsonUtils.fromJson(any(), any())).thenReturn(taskUnits);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(any(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotDone() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenPrepareTasksForClearFails() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        String jsonParameters = JsonUtils.toJson(dataArchiveClearParameters);
        when(taskEntity.getParametersJson()).thenReturn(jsonParameters);
        when(taskEntity.getResultJson()).thenReturn("[]");
        when(JsonUtils.fromJson(any(), any())).thenThrow(new RuntimeException());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenExecuteTaskFails() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        String jsonParameters = JsonUtils.toJson(dataArchiveClearParameters);
        when(taskEntity.getParametersJson()).thenReturn(jsonParameters);
        when(taskEntity.getResultJson()).thenReturn("[]");
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("456");
        dlmTask.setJobType(JobType.DELETE);
        List<DlmTask> taskUnits = Collections.singletonList(dlmTask);
        when(JsonUtils.fromJson(any(), any())).thenReturn(taskUnits);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(any(), any()))
                .thenThrow(new RuntimeException());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenGetTaskStatusFails() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(true);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        String jsonParameters = JsonUtils.toJson(dataArchiveClearParameters);
        when(taskEntity.getParametersJson()).thenReturn(jsonParameters);
        when(taskEntity.getResultJson()).thenReturn("[]");
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("456");
        dlmTask.setJobType(JobType.DELETE);
        List<DlmTask> taskUnits = Collections.singletonList(dlmTask);
        when(JsonUtils.fromJson(any(), any())).thenReturn(taskUnits);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
                .thenReturn(true);
        when(getTaskStatus(taskUnits)).thenThrow(new RuntimeException());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return TaskStatus.PREPARING;
    }
}