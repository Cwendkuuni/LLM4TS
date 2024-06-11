//Corresponding JUnit 4 Test Cases for OnlineSchemaChangeFlowableTask class:
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.jobexecutor.JobExecutorConfiguration;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.job.api.Job;
import org.flowable.job.api.JobQuery;
import org.flowable.job.api.TimeoutJobHandler;
import org.flowable.job.service.impl.asyncexecutor.AsyncJobExecutor;
import org.flowable.job.service.impl.persistence.entity.JobEntity;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.FlowableEngineAutoConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.flowable.spring.boot.SecurityConfiguration;
import org.flowable.spring.boot.configurers.SpringProcessEngineConfigurationConfigurer;
import org.flowable.spring.boot.job.JobAcquisitionThreadFactory;
import org.flowable.spring.boot.job.JobExecutorConfigurationProperties;
import org.flowable.spring.boot.job.JobRegistry;
import org.flowable.spring.boot.job.JobRegistryConfigurationProperties;
import org.flowable.spring.boot.job.JobScheduler;
import org.flowable.spring.boot.job.SpringAsyncJobExecutor;
import org.flowable.spring.boot.service.DefaultProcessEngineConfigurationService;
import org.flowable.spring.boot.service.ProcessEngineConfigurationService;
import org.flowable.spring.boot.service.SpringProcessEngineServices;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@SpringBootTest(classes = { OnlineSchemaChangeFlowableTask.class, TestConfig.class })
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { FlowableEngineAutoConfiguration.class, SecurityConfiguration.class })
@PropertySource("classpath:application-test.properties")
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Value("${spring.task.async-executor.thread-count:10}")
    private int threadCount;

    private ThreadPoolTaskExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("FlowableAsyncJobExecutor-");
        executor.initialize();

        SpringProcessEngineConfiguration config = (SpringProcessEngineConfiguration) onlineSchemaChangeFlowableTask
                .getProcessEngineConfiguration();
        config.setAsyncExecutor(new SpringAsyncJobExecutor(executor, config));
        config.setAsyncExecutorActivate(true);
        config.setJobExecutor(new AsyncJobExecutor(config));

        CommandContext commandContext = CommandContextUtil.getCommandContext();
        commandContext.setJobExecutor((JobExecutor) config.getJobExecutor());
    }

    @After
    public void tearDown() {
        executor.shutdown();
    }

    @Test
    public void testStart() throws Exception {
        // Arrange
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "testSchema";
        TaskEntity taskEntity = new TaskEntityImpl();

        when(taskService.start(taskId)).thenReturn(taskEntity);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(mock(User.class));
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mock(Page.class));

        // Act
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        // Assert
        verify(taskService, times(1)).start(taskId);
        verify(taskEntity, times(1)).setResultJson(anyString());
        verify(taskEntity, times(1)).setStatus(any(TaskStatus.class));
        verify(taskEntity, times(1)).setProgressPercentage(anyDouble());
        verify(taskService, times(1)).update(taskEntity);
    }

    // Add more test methods to test the other methods in the OnlineSchemaChangeFlowableTask class
}

@Configuration
@ProcessEngineConfigurationConfigurer
class TestConfig implements SpringProcessEngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration config) {
        config.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setJobExecutorActivate(false);
        config.setAsyncExecutorActivate(false);

        config.setActivityFontName("Arial");
        config.setLabelFontName("Arial");
        config.setAnnotationFontName("Arial");

        config.setActivityFontSize(12);
        config.setLabelFontSize(12);
        config.setAnnotationFontSize(12);

        config.setHistoryLevel(StandaloneInMemProcessEngineConfiguration.HISTORY_FULL);

        // Configure mock services
        config.setTaskService(mock(TaskService.class));
        config.setExecutionService(mock(org.flowable.engine.runtime.ExecutionService.class));
        config.setIdentityService(mock(org.flowable.idm.api.IdentityService.class));
        config.setProcessEngineConfigurationExtender(mock(EngineConfigurationConfigurer.class));
        config.setProcessEngineConfigurationExtender(mock(ProcessEngineConfigurationService.class));
        config.setProcessEngineConfigurationExtender(mock(SpringProcessEngineConfigurationService.class));
        config.setProcessEngineConfigurationExtender(mock(SpringProcessEngineServices.class));
        config.setProcessEngineConfigurationExtender(mock(JobExecutorConfiguration.class));
        config.setProcessEngineConfigurationExtender(mock(JobRegistryConfigurationProperties.class));
        config.setProcessEngineConfigurationExtender(mock(JobRegistry.class));
        config.setProcessEngineConfigurationExtender(mock(JobScheduler.class));
        config.setProcessEngineConfigurationExtender(mock(JobExecutorConfigurationProperties.class));
        config.setProcessEngineConfigurationExtender(mock(JobAcquisitionThreadFactory.class));
        config.setProcessEngineConfigurationExtender(mock(TimeoutJobHandler.class));
        config.setProcessEngineConfigurationExtender(mock(AsyncJobExecutor.class));
        config.setProcessEngineConfigurationExtender(mock(CommandContext.class));
        config.setProcessEngineConfigurationExtender(mock(ExecutionListener.class));
        config.setProcessEngineConfigurationExtender(mock(TaskListener.class));
    }

    @Bean
    @Primary
    public TaskService taskService() {
        return mock(TaskService.class);
    }

    @Bean
    @Primary
    public org.flowable.engine.runtime.ExecutionService executionService() {
        return mock(org.flowable.engine.runtime.ExecutionService.class);
    }

    @Bean
    @Primary
    public org.flowable.idm.api.IdentityService identityService() {
        return mock(org.flowable.idm.api.IdentityService.class);
    }

    @Bean
    @Primary
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> processEngineConfigurationExtender() {
        return mock(EngineConfigurationConfigurer.class);
    }

    @Bean
    @Primary
    public ProcessEngineConfigurationService processEngineConfigurationService() {
        return mock(ProcessEngineConfigurationService.class);
    }

    @Bean
    @Primary
    public SpringProcessEngineServices springProcessEngineServices() {
        return mock(SpringProcessEngineServices.class);
    }

    @Bean
    @Primary
    public JobExecutorConfiguration jobExecutorConfiguration() {
        return mock(JobExecutorConfiguration.class);
    }

    @Bean
    @Primary
    public JobRegistryConfigurationProperties jobRegistryConfigurationProperties() {
        return mock(JobRegistryConfigurationProperties.class);
    }

    @Bean
    @Primary
    public JobRegistry jobRegistry() {
        return mock(JobRegistry.class);
    }

    @Bean
    @Primary
    public JobScheduler jobScheduler() {
        return mock(JobScheduler.class);
    }

    @Bean
    @Primary
    public JobExecutorConfigurationProperties jobExecutorConfigurationProperties() {
        return mock(JobExecutorConfigurationProperties.class);
    }

    @Bean
    @Primary
    public JobAcquisitionThreadFactory jobAcquisitionThreadFactory() {
        return mock(JobAcquisitionThreadFactory.class);
    }

    @Bean
    @Primary
    public TimeoutJobHandler timeoutJobHandler() {
        return mock(TimeoutJobHandler.class);
    }

    @Bean
    @Primary
    public AsyncJobExecutor asyncJobExecutor() {
        return mock(AsyncJobExecutor.class);
    }

    @Bean
    @Primary
    public CommandContext commandContext() {
        return mock(CommandContext.class);
    }

    @Bean
    @Primary
    public ExecutionListener executionListener() {
        return mock(ExecutionListener.class);
    }

    @Bean
    @Primary
    public TaskListener taskListener() {
        return mock(TaskListener.class);
    }

}