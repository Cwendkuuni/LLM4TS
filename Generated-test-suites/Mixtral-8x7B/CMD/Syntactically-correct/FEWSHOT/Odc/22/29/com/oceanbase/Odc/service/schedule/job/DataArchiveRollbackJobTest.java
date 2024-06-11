import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class DataArchiveRollbackJobTest {

@Before
public void setUp() throws SchedulerException {
SchedulerFactory schedulerFactory = new SchedulerFactoryBean();
PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
schedulerFactory.setQuartzProperties(propertiesFactoryBean.getObject());
schedulerFactory.afterPropertiesSet();
scheduler = schedulerFactory.getScheduler();
scheduler.start();
}

@Test
public void testExecute() throws JobExecutionException, SchedulerException {
// Initialize mocks
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
when(context.getResult()).thenReturn(taskEntity);
DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);
List<DlmTask> taskUnits = mock(List.class);
when(dataArchiveTaskOption.get().getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

// Call the method under test
new DataArchiveRollbackJob().execute(context);

// Verify the result
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

// Add more test cases to cover other scenarios

}