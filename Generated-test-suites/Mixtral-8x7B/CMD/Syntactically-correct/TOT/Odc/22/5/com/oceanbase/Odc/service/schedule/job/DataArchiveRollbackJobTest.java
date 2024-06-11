import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.model.DlmTask.Status;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

	private DataArchiveRollbackJob dataArchiveRollbackJob;
	private ScheduleTaskRepository scheduleTaskRepository;
	private JobExecutionContext jobExecutionContext;

	@Before
	public void setup() {
		scheduleTaskRepository = mock(ScheduleTaskRepository.class);
		dataArchiveRollbackJob = new DataArchiveRollbackJob(scheduleTaskRepository);
		jobExecutionContext = mock(JobExecutionContext.class);
	}

	@Test
	public void testExecuteSuccess() throws JobExecutionException {
		DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
		rollbackParameters.setDataArchiveTaskId(1L);
		ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
		scheduleTaskEntity.setId(1L);
		scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(1L);
		dlmTask.setSourceDatabaseId(1L);
		dlmTask.setTargetDatabaseId(2L);
		dlmTask.setJobType(JobType.ARCHIVE);
		dlmTask.setStatus(Status.PREPARING);
		dlmTasks.add(dlmTask);
		scheduleTaskEntity.setResultJson(JsonUtils.toJson(dlmTasks));
		doReturn(scheduleTaskEntity).when(jobExecutionContext).getMergedJobDataMap().get(any());
		Example<ScheduleTaskEntity> example = Example.of(scheduleTaskEntity);
		Page<ScheduleTaskEntity> scheduleTaskPage = Page.empty();
		doReturn(scheduleTaskPage).when(scheduleTaskRepository).findAll(example, any(PageRequest.class));
		DlmTask dlmTask2 = new DlmTask();
		dlmTask2.setId(2L);
		DlmTask dlmTask3 = new DlmTask();
		dlmTask3.setId(3L);
		dlmTasks.add(dlmTask2);
		dlmTasks.add(dlmTask3);
		doReturn(dlmTasks).when(scheduleTaskRepository).findById(anyLong());
		dataArchiveRollbackJob.execute(jobExecutionContext);
		verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
	}

	@Test
	public void testExecuteFailed() throws JobExecutionException {
		DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
		rollbackParameters.setDataArchiveTaskId(-1L);
		ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
		scheduleTaskEntity.setId(1L);
		scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
		doReturn(scheduleTaskEntity).when(jobExecutionContext).getMergedJobDataMap().get(any());
		dataArchiveRollbackJob.execute(jobExecutionContext);
		verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
	}

	@Test
	public void testExecuteTaskException() {
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(-1L);
		dlmTasks.add(dlmTask);
		assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.executeTask(1L, dlmTasks));
	}

	@Test
	public void testGetTaskStatus() {
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(1L);
		dlmTask.setStatus(Status.PREPARING);
		dlmTasks.add(dlmTask);
		DlmTask dlmTask2 = new DlmTask();
		dlmTask2.setId(2L);
		dlmTask2.setStatus(Status.DONE);
		dlmTasks.add(dlmTask2);
		assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.getTaskStatus(dlmTasks));
	}

	@Test
	public void testGetTaskStatusSuccess() {
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(1L);
		dlmTask.setStatus(Status.PREPARING);
		dlmTasks.add(dlmTask);
		DlmTask dlmTask2 = new DlmTask();
		dlmTask2.setId(2L);
		dlmTask2.setStatus(Status.DONE);
		dlmTasks.add(dlmTask2);
		assertEquals(TaskStatus.PARTIAL_SUCCESS, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
	}

	@Test
	public void testGetTaskStatusFailed() {
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(1L);
		dlmTask.setStatus(Status.PREPARING);
		dlmTasks.add(dlmTask);
		DlmTask dlmTask2 = new DlmTask();
		dlmTask2.setId(2L);
		dlmTask2.setStatus(Status.FAILED);
		dlmTasks.add(dlmTask2);
		assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
	}

	@Test
	public void testGetTaskStatusSuccessAll() {
		List<DlmTask> dlmTasks = new ArrayList<>();
		DlmTask dlmTask = new DlmTask();
		dlmTask.setId(1L);
		dlmTask.setStatus(Status.DONE);
		dlmTasks.add(dlmTask);
		DlmTask dlmTask2 = new DlmTask();
		dlmTask2.setId(2L);
		dlmTask2.setStatus(Status.DONE);
		dlmTasks.add(dlmTask2);
		assertEquals(TaskStatus.SUCCESS, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
	}

}