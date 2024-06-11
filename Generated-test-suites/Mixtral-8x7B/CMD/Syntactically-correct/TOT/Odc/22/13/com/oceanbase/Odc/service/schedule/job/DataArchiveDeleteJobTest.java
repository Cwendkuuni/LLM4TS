
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId("taskId");
        taskUnit.setJobType(JobType.DELETE);
        taskUnits.add(taskUnit);

        String resultJson = JsonUtils.toJson(taskUnits);
        dataArchiveTask.setResultJson(resultJson);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        dataArchiveDeleteJob.execute(null);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWithNotFoundDataArchiveTask() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        dataArchiveDeleteJob.execute(null);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWithNotFinishedDataArchiveTask() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        dataArchiveDeleteJob.execute(null);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }
}
