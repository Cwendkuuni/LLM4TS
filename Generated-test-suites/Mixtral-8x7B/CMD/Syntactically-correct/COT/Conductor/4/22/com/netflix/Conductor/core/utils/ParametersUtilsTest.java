import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParamUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ParametersUtilsTest {

    private ParametersUtils paramUtils;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ApplicationContext context =
                new AnnotationConfigApplicationContext(TestConfig.class);

        paramUtils = context.getBean(ParametersUtils.class);
        objectMapper = context.getBean(ObjectMapper.class);

        EnvUtils.setSystemParameters(Collections.singletonMap("key", "value"));
    }

    @Test
    public void testGetTaskInput() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        workflowModel.setInput(new HashMap<>());
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("test", "${workflow.input.test}");

        Map<String, Object> result =
                paramUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertEquals(inputParams.get("test"), result.get("test"));
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        workflowModel.setInput(new HashMap<>());
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("test", "${workflow.input.test}");

        Map<String, Object> result =
                paramUtils.getTaskInputV2(
                        inputParams, workflowModel, "taskId", taskDef);

        assertEquals(inputParams.get("test"), result.get("test"));
    }

    @Test
    public void testReplace() throws Exception {
        String input = "{\"test\": \"${workflow.input.test}\"}";
        DocumentContext context =
                JsonPath.parse(input, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("test", "testValue");

        String result = paramUtils.replace(inputParams, context).toString();

        assertTrue(result.contains("testValue"));
    }

    @Test
    public void testReplaceVariables() throws Exception {
        String input = "test-${key}";
        DocumentContext context =
                JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = paramUtils.replaceVariables(input, context, null).toString();

        assertTrue(result.contains("test-value"));
    }

    @Test
    public void testReplaceVariablesDepth() throws Exception {
        String input = "test-${key-${key2}}";
        DocumentContext context =
                JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = paramUtils.replaceVariables(input, context, null).toString();

        assertTrue(result.contains("test-value-value"));
    }

    @Test(expected = Exception.class)
    public void testReplaceVariablesException() throws Exception {
        String input = "test-${key-${key2}";
        DocumentContext context =
                JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        paramUtils.replaceVariables(input, context, null);
    }

    @Test
    public void testReplaceWithList() throws Exception {
        String input = "{\"test\": [\"${workflow.input.test}\"]}";
        DocumentContext context =
                JsonPath.parse(input, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("test", "testValue");

        String result = paramUtils.replace(inputParams, context).toString();

        assertTrue(result.contains("testValue"));
    }

    @Test
    public void testGetWorkflowInput() throws Exception {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("test", "${workflow.input.test}");

        Map<String, Object> result = paramUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(inputParams.get("test"), result.get("test"));
    }

    @Configuration
    @ComponentScan("com.netflix.conductor.core.utils")
    public static class TestConfig {
    }
}