import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;

    @BeforeEach
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        envUtils = Mockito.mock(EnvUtils.class);
        parametersUtils = new ParametersUtils(objectMapper, envUtils);
    }

    @Test
    public void testGetTaskInput() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);
        TaskDef taskDef = TestUtils.createTaskDef();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${workflow.input.key3}");
        inputParams.put("key3", "${workflow.output.key4}");

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, taskDef, "12345");

        Assertions.assertEquals("value3", result.get("key2"));
        Assertions.assertEquals(null, result.get("key3"));
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);
        TaskDef taskDef = TestUtils.createTaskDef();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${workflow.input.key3}");
        inputParams.put("key3", "${workflow.output.key4}");

        Map<String, Object> result = parametersUtils.getTaskInputV2(
                inputParams, workflowModel, "12345", taskDef);

        Assertions.assertEquals("value3", result.get("key2"));
        Assertions.assertEquals(null, result.get("key3"));
    }

    @Test
    public void testReplace() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${workflow.input.key3}");
        inputParams.put("key3", "${workflow.output.key4}");

        Mockito.when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("envValue");

        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any()))
                .thenReturn(new byte[0]);

        Mockito.when(objectMapper.readValue(Mockito.any(), Mockito.eq(new TypeReference<>() {})))
                .thenReturn(Collections.singletonMap("key4", "value3"));

        Map<String, Object> result = parametersUtils.replace(inputParams);

        Assertions.assertEquals("value3", result.get("key2"));
        Assertions.assertEquals("envValue", result.get("key3"));
    }

    @Test
    public void testReplaceVariables() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);

        String paramString = "${workflow.input.key3}";

        Mockito.when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("envValue");

        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any()))
                .thenReturn(new byte[0]);

        Mockito.when(objectMapper.readValue(Mockito.any(), Mockito.eq(new TypeReference<>() {})))
                .thenReturn(Collections.singletonMap("key4", "value3"));

        String result = parametersUtils.replaceVariables(paramString, workflowModel);

        Assertions.assertEquals("value3", result);
    }

    @Test
    public void testReplaceVariablesException() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);

        String paramString = "${workflow.input.key3}";

        Mockito.when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("envValue");

        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any()))
                .thenReturn(new byte[0]);

        Mockito.when(objectMapper.readValue(Mockito.any(), Mockito.eq(new TypeReference<>() {})))
                .thenThrow(new IOException("Read error"));

        Assertions.assertThrows(IOException.class, () -> parametersUtils.replaceVariables(paramString, workflowModel));
    }

    @Test
    public void testReplaceList() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(workflowDef);

        List<String> paramStringList = new LinkedList<>();
        paramStringList.add("${workflow.input.key3}");
        paramStringList.add("${workflow.input.key5}");

        Mockito.when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("envValue");

        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any()))
                .thenReturn(new byte[0]);

        Mockito.when(objectMapper.readValue(Mockito.any(), Mockito.eq(new TypeReference<>() {})))
                .thenReturn(Collections.singletonList("value5"));

        List<Object> result = parametersUtils.replaceList(paramStringList, "12345", workflowModel);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("value3", result.get(0));
        Assertions.assertEquals("value5", result.get(1));
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${workflow.input.key3}");
        inputParams.put("key3", "${workflow.output.key4}");

        Mockito.when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("envValue");

        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any()))
                .thenReturn(new byte[0]);

        Mockito.when(objectMapper.readValue(Mockito.any(), Mockito.eq(new TypeReference<>() {})))
                .thenReturn(Collections.singletonMap("key4", "value3"));

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        Assertions.assertEquals("value3", result.get("key2"));
        Assertions.assertEquals("envValue", result.get("key3"));
    }

}