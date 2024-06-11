package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@SuppressWarnings("unused")
public class ScheduleCheckOmsProjectValveTest {

 @Mock
 ProjectOpenApiService projectOpenApiService;

 @Mock
 OscTaskCompleteHandler completeHandler;

 @Mock
 ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 OnlineSchemaChangeProperties onlineSchemaChangeProperties;

 @InjectMocks
 ScheduleCheckOmsProjectValve valve;

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInvoke() {
 // Arrange
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 List<ProjectStepVO> projectSteps = Lists.newArrayList();
 ProjectProgressResponse progress = new ProjectProgressResponse();
 ProjectStepResult projectStepResult = new ProjectStepResult();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

 when(valve.getProjectRequest(taskParameter)).thenReturn(projectRequest);
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 when(new ProjectStepResultChecker(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify())
 .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
 taskParameter.getDatabaseName(), taskParameter.getUid()))
 .withResumeProject(() -> {
 projectOpenApiService.resumeProject(projectRequest);
 return null;
 })
 .getCheckerResult()).thenReturn(projectStepResult);

 // Act
 valve.invoke(new BaseValveContext() {
 @Override
 public ScheduleTaskEntity getScheduleTask() {
 return scheduleTask;
 }

 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });

 // Assert
 verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
 verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 verify(projectOpenApiService, times(1))
 .resumeProject(projectRequest);
 }

 private ProjectFullVerifyResultResponse listProjectFullVerifyResult(String projectId, String databaseName,
 String uid) {
 // Mock implementation
 ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
 request.setProjectId(projectId);
 request.setSourceSchemas(new String[] {databaseName});
 request.setDestSchemas(new String[] {databaseName});
 request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
 request.setPageSize(10);
 request.setPageNumber(1);
 request.setUid(uid);

 ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
 return response;
 }

}