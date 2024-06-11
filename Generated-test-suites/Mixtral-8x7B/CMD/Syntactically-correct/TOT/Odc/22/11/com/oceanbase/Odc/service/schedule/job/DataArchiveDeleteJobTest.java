import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl. matchers.GroupMatcher;
import org.quartz.impl. triggers.CronTriggerImpl;
import org.quartz.simpl.PropertySettingFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.simpl.TriggerUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private Scheduler scheduler;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() throws SchedulerException {
        Properties properties = new Properties();
        properties.setProperty(RAMJobStore.OBJECT_STORE_CLASS, RAMJobStore.class.getName());
        properties.setProperty(RAMJobStore.DATA_SOURCE_CLASS, PropertySettingFactory.class.getName());
        properties.setProperty(RAMJobStore.PROPERTIES_CLASS, PropertySettingFactory.class.getName());

        RAMJobStore jobStore = new RAMJobStore();
        jobStore.setProperties(properties);

        SimpleThreadPool threadPool = new SimpleThreadPool(10, SimpleThreadPool.ThreadType.SERVICES);
        threadPool.initialize();

        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
        schedulerFactory.setJobStore(jobStore);
        schedulerFactory.setThreadPool(threadPool);
        scheduler = schedulerFactory.getScheduler();

        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("dataArchiveDeleteJob");
        taskEntity.setJobGroup("group1");

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(3L);
        taskUnits.add(taskUnit);
    }

    @Test
    public void testExecute() throws SchedulerException, JsonProcessingException {
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveDeleteJob.JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), any());
    }
}