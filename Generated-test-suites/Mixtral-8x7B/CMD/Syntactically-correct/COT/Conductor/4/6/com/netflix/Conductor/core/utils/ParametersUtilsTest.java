import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParamUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);
    private static final String TASK_REF_NAME = "testTaskRefName";
    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String TASK_ID = "testTaskId";
    private static final String TASK_TYPE = "testTaskType";
    private static final String TASK_INPUT = "{\"testInput\": \"testValue\"}";
    private static final String TASK_OUTPUT = "{\"testOutput\": \"testValue\"}";
    private static final String TEST_ENV_VAR = "TEST_ENV_VAR";
    private static final String TEST_ENV_VAR_VALUE = "testEnvVarValue";
    private static final String TEST_PARAM = "${testParam}";
    private static final String TEST_PARAM_VALUE = "testParamValue";
    private static final String TEST_JSON_PATH = "$['testJsonPath']";
    private static final String TEST_JSON_PATH_VALUE = "testJsonPathValue";
    private static final String TEST_NESTED_JSON_PATH = "$['testJsonPath1']['testJsonPath2']";
    private static final String TEST_NESTED_JSON_PATH_VALUE = "testNestedJsonPathValue";
    private static final String TEST_MAP_KEY = "testMapKey";
    private static final String TEST_MAP_VALUE = "testMapValue";
    private static final String TEST_LIST_ITEM = "testListItem";
    private static final String TEST_LIST_ITEM_VALUE = "testListItemValue";
    private static final String TEST_EMPTY_PARAM = "${}";
    private static final String TEST_MULTIPLE_PARAMS = "${param1}${param2}";
    private static final String TEST_MULTIPLE_PARAMS_VALUE = "param1Valueparam2Value";
    private static final String TEST_INVALID_JSON_PATH = "$[invalidJsonPath]";
    private static final String TEST_TASK_REF_NAME_WITH_ITERATION = "testTaskRefName.1";

    private ParamUtils paramUtils;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;
    private Map<String, Object> inputParams;
    private DocumentContext documentContext;

    @Before
    public void setUp() {
        paramUtils = new ParamUtils(objectMapper);

        ReflectionTestUtils.setField(paramUtils, "envUtils", mock(EnvUtils.class));

        taskDef = mock(TaskDef.class);
        when(taskDef.getInputTemplate()).thenReturn(createMap(TEST_MAP_KEY, TEST_MAP_VALUE));

        workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn(WORKFLOW_ID);
        when(workflowModel.getTasks())
                .thenReturn(
                        List.of(
                                createTaskModel(
                                        TASK_REF_NAME, TASK_ID, TASK_TYPE, TASK_INPUT, TASK_OUTPUT),
                                createTaskModel(
                                        TASK_REF_NAME, TASK_ID, TASK_TYPE, TASK_INPUT, TASK_OUTPUT)));

        inputParams = new HashMap<>();
        inputParams.put(TEST_PARAM, TEST_PARAM_VALUE);

        documentContext =
                JsonPath.using(Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))
                        .parse(createMap(TEST_JSON_PATH, TEST_JSON_PATH_VALUE));

        ReflectionTestUtils.setField(paramUtils, "pattern", PATTERN);
    }

    @Test
    public void testGetTaskInput() throws IOException {
        Map<String, Object> result =
                paramUtils.getTaskInput(
                        inputParams, workflowModel, taskDef, TASK_ID);

        assertThat(result.get(TEST_MAP_KEY), is(TEST_MAP_VALUE));
        assertThat(result.get(TEST_JSON_PATH), is(TEST_JSON_PATH_VALUE));
        assertThat(result.get(TEST_NESTED_JSON_PATH), is(TEST_NESTED_JSON_PATH_VALUE));
    }

    @Test
    public void testGetTaskInput_v1() throws IOException {
        ReflectionTestUtils.setField(paramUtils, "pattern", null);

        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> result =
                paramUtils.getTaskInput(
                        inputParams, workflowModel, taskDef, TASK_ID);

        assertThat(result.get(TEST_MAP_KEY), nullValue());
        assertThat(result.get(TEST_JSON_PATH), nullValue());
        assertThat(result.get(TEST_NESTED_JSON_PATH), nullValue());
    }

    @Test
    public void testReplace() {
        String result =
                paramUtils.replace(
                        "test${TEST_PARAM}test${TEST_JSON_PATH}test", documentContext, null);

        assertThat(result, is("testtestParamValuetestJsonPathValuetest"));
    }

    @Test
    public void testReplace_withEnvVar() {
        ReflectionTestUtils.setField(
                paramUtils, "envUtils", createEnvUtils(TEST_ENV_VAR, TEST_ENV_VAR_VALUE));

        String result =
                paramUtils.replace(TEST_ENV_VAR, documentContext, null);

        assertThat(result, is(TEST_ENV_VAR_VALUE));
    }

    @Test
    public void testReplace_nullValue() {
        String result = paramUtils.replace(null, documentContext, null);

        assertThat(result, is(""));
    }

    @Test
    public void testReplace_noMatch() {
        String result =
                paramUtils.replace(
                        "test",
                        JsonPath.using(Configuration.defaultConfiguration()
                                .addOptions(Option.SUPPRESS_EXCEPTIONS))
                                .parse(Collections.emptyMap()),
                        null);

        assertThat(result, is("test"));
    }

    @Test
    public void testReplaceVariables() {
        String result =
                paramUtils.replaceVariables(TEST_PARAM, documentContext, null);

        assertThat(result, is(TEST_PARAM_VALUE));
    }

    @Test
    public void testReplaceVariables_noMatch() {
        String result =
                paramUtils.replaceVariables(
                        "test", JsonPath.using(Configuration.defaultConfiguration()).parse(Collections.emptyMap()), null);

        assertThat(result, is("test"));
    }

    @Test
    public void testReplaceVariables_nullValue() {
        String result = paramUtils.replaceVariables(null, documentContext, null);

        assertThat(result, is(""));
    }

    @Test
    public void testReplaceVariables_envVar() {
        ReflectionTestUtils.setField(
                paramUtils, "envUtils", createEnvUtils(TEST_ENV_VAR, TEST_ENV_VAR_VALUE));

        String result = paramUtils.replaceVariables(TEST_ENV_VAR, documentContext, null);

        assertThat(result, is(TEST_ENV_VAR_VALUE));
    }

    @Test
    public void testReplaceVariables_invalidJsonPath() {
        String result =
                paramUtils.replaceVariables(
                        TEST_INVALID_JSON_PATH,
                        JsonPath.using(Configuration.defaultConfiguration())
                                .parse(Collections.emptyMap()),
                        null);

        assertThat(result, is(TEST_INVALID_JSON_PATH));
    }

    @Test
    public void testReplaceVariables_emptyParam() {
        String result = paramUtils.replaceVariables(TEST_EMPTY_PARAM, documentContext, null);

        assertThat(result, is(""));
    }

    @Test
    public void testReplaceVariables_multipleParams() {
        String result =
                paramUtils.replaceVariables(
                        TEST_MULTIPLE_PARAMS,
                        JsonPath.using(Configuration.defaultConfiguration())
                                .parse(createMap(TEST_PARAM, TEST_PARAM_VALUE)),
                        null);

        assertThat(result, is(TEST_MULTIPLE_PARAMS_VALUE));
    }

    @Test
    public void testReplaceList() {
        List<Object> list =
                List.of(TEST_PARAM, TASK_INPUT, List.of(TEST_JSON_PATH, TEST_NESTED_JSON_PATH));

        List<Object> result =
                paramUtils.replaceList(list, null, documentContext);

        assertThat(result, Matchers.containsInAnyOrder(TEST_PARAM_VALUE, TASK_INPUT,
                List.of(TEST_JSON_PATH_VALUE, TEST_NESTED_JSON_PATH_VALUE)));
    }

    @Test
    public void testReplaceList_nullValue() {
        List<Object> result = paramUtils.replaceList(null, null, documentContext);

        assertThat(result, Matchers.empty());
    }

    @Test
    public void testReplaceList_noMatch() {
        List<Object> list = List.of("test");

        List<Object> result =
                paramUtils.replaceList(list, null, JsonPath.using(Configuration.defaultConfiguration()).parse(Collections.emptyMap()));

        assertThat(result, Matchers.containsInAnyOrder("test"));
    }

    @Test
    public void testReplaceList_invalidJsonPath() {
        List<Object> list = List.of(TEST_INVALID_JSON_PATH);

        List<Object> result =
                paramUtils.replaceList(
                        list, null,
                        JsonPath.using(Configuration.defaultConfiguration()).parse(Collections.emptyMap()));

        assertThat(result, Matchers.containsInAnyOrder(TEST_INVALID_JSON_PATH));
    }

    @Test
    public void testReplaceList_emptyParam() {
        List<Object> list = List.of(TEST_EMPTY_PARAM);

        List<Object> result = paramUtils.replaceList(list, null, documentContext);

        assertThat(result, Matchers.containsInAnyOrder(""));
    }

    @Test
    public void testReplaceList_multipleParams() {
        List<Object> list = List.of(TEST_MULTIPLE_PARAMS);

        List<Object> result =
                paramUtils.replaceList(
                        list, null,
                        JsonPath.using(Configuration.defaultConfiguration())
                                .parse(createMap(TEST_PARAM, TEST_PARAM_VALUE)));

        assertThat(result, Matchers.containsInAnyOrder(TEST_MULTIPLE_PARAMS_VALUE));
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(createMap(TEST_MAP_KEY, TEST_MAP_VALUE));

        Map<String, Object> result =
                paramUtils.getWorkflowInput(workflowDef, inputParams);

        assertThat(result.get(TEST_MAP_KEY), is(TEST_MAP_VALUE));
    }

    @Test
    public void testGetWorkflowInput_nullValue() throws IOException {
        Map<String, Object> result = paramUtils.getWorkflowInput(null, inputParams);

        assertThat(result, Matchers.notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void testGetWorkflowInput_noInputTemplate() throws IOException {
        WorkflowDef workflowDef = mock(WorkflowDef.class);

        Map<String, Object> result =
                paramUtils.getWorkflowInput(workflowDef, inputParams);

        assertThat(result, Matchers.notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    private Map<String, Object> createMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private TaskModel createTaskModel(
            String referenceTaskName,
            String taskId,
            String taskType,
            String input,
            String output) {
        return new TaskModel(
                null,
                referenceTaskName,
                taskId,
                null,
                null,
                null,
                null,
                null,
                taskType,
                input,
                null,
                output,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private EnvUtils createEnvUtils(String envVar, String envVarValue) {
        EnvUtils envUtils = mock(EnvUtils.class);
        when(envUtils.getSystemParametersValue(envVar)).thenReturn(envVarValue);
        return envUtils;
    }
}