import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, null, null);
        assertEquals(0, result.size());
    }

    @Test
    public void testClone() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");
        Map<String, Object> clonedMap = parametersUtils.clone(inputParams);
        assertEquals("value", clonedMap.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "${key}");
        Map<String, Object> result = parametersUtils.replace(inputParams, "{ \"key\": \"value\" }");
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${key}";
        DocumentContext documentContext = JsonPath.parse("{ \"key\": \"value\" }");
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariablesWithTaskId() {
        String paramString = "${key}";
        DocumentContext documentContext = JsonPath.parse("{ \"key\": \"value\" }");
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("value", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        workflowDef.setInputTemplate(inputTemplate);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value", result.get("key"));
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        return workflowModel;
    }
}

class Utils {
    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}

@Component
class EnvUtils {
    public static String getSystemParametersValue(String paramPath, String taskId) {
        return null;
    }

    public static boolean isEnvironmentVariable(String paramPath) {
        return false;
    }
}