import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils. ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("com.netflix.conductor")
class TestConfiguration {}

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ApplicationContext context;
private ObjectMapper objectMapper;
private EnvUtils envUtils;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
context = new AnnotationConfigApplicationContext(TestConfiguration.class);
parametersUtils = context.getBean(ParametersUtils.class);
objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);
parametersUtils.setObjectMapper(objectMapper);
parametersUtils.setEnvUtils(envUtils);
}

@Test
public void testGetTaskInput() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testKey", "${workflow.input.testKey}");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(1);
workflowDef.setInputTemplate(Collections.singletonMap("testKey", "${workflow.input.testKey}"));

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("testKey", "testValue"));
workflowModel.setWorkflowDefinition(workflowDef);

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("testKey", "${workflow.input.testKey}"));

TaskModel taskModel = new TaskModel();
taskModel.setInputData(Collections.singletonMap("testKey", "taskValue"));

doReturn(objectMapper).when(envUtils).getObjectMapper();
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskModel.getTaskId());
assertEquals("testValue", result.get("testKey"));
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testKey", "$['workflow']['input']['testKey']");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(2);
workflowDef.setInputTemplate(
Collections.singletonMap("testKey", "$['workflow']['input']['testKey']"));

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("testKey", "testValue"));
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setTasks(
Collections.singletonList(
new TaskModel()
.setInputData(Collections.singletonMap("testKey", "taskValue"))
.setReferenceTaskName("testTask")));

TaskDef taskDef =
new TaskDef().setInputTemplate(Collections.singletonMap("testKey", "$['workflow']['input']['testKey']"));

doReturn(objectMapper).when(envUtils).getObjectMapper();
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "testTask");
assertEquals("testValue", result.get("testKey"));
}

@Test
public void testReplace() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testKey", "$['workflow']['input']['testKey']");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(2);
workflowDef.setInputTemplate(
Collections.singletonMap("testKey", "$['workflow']['input']['testKey']"));

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("testKey", "testValue"));
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setTasks(
Collections.singletonList(
new TaskModel()
.setInputData(Collections.singletonMap("testKey", "taskValue"))
.setReferenceTaskName("testTask")));

doReturn(objectMapper).when(envUtils).getObjectMapper();
String result =
parametersUtils.replace(
objectMapper.convertValue(inputParams, new TypeReference<Map<String, Object>>() {}));
assertEquals("{'testKey': 'testValue'}", result);
}

@Test
public void testReplaceVariables() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testKey", "${workflow.input.testKey}");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(1);
workflowDef.setInputTemplate(Collections.singletonMap("testKey", "${workflow.input.testKey}"));

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("testKey", "testValue"));
workflowModel.setWorkflowDefinition(workflowDef);

doReturn(objectMapper).when(envUtils).getObjectMapper();
String result =
parametersUtils.replace(
objectMapper.convertValue(inputParams, new TypeReference<Map<String, Object>>() {}),
workflowModel);
assertEquals("{'testKey': 'testValue'}", result);
}
}