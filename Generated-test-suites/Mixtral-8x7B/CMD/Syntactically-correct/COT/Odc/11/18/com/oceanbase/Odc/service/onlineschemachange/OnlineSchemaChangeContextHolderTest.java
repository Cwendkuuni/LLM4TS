import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    // Test method: trace(Long userId, Long taskId, Long organizationId)
    @Test
    public void testTraceLongLongLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test method: trace(String userId, String taskId, String organizationId)
    @Test
    public void testTraceStringStringString() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test method: retrace(Map<String, String> context)
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test method: clear()
    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test method: get(String key)
    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    // Edge case: trace methods with null arguments
    @Test
    public void testTraceWithNullArguments() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        OnlineSchemaChangeContextHolder.trace(null, "2", null);
        OnlineSchemaChangeContextHolder.trace("1", null, null);

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Edge case: retrace with null argument
    @Test
    public void testRetraceWithNullArgument() {
        OnlineSchemaChangeContextHolder.retrace(null);

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    // Edge case: get with null or non-existing key
    @Test
    public void testGetWithNullOrNonExistingKey() {
        assertEquals(null, OnlineSchemaChangeContextHolder.get(null));
        assertEquals(null, OnlineSchemaChangeContextHolder.get("nonExistingKey"));
    }

    // Exception handling: trace with non-number String arguments
    @Test(expected = NumberFormatException.class)
    public void testTraceWithNonNumberStringArguments() {
        OnlineSchemaChangeContextHolder.trace("a", "b", "c");
    }

    // Exception handling: retrace with non-map argument
    @Test(expected = ClassCastException.class)
    public void testRetraceWithNonMapArgument() {
        Object nonMapObject = new Object();
        OnlineSchemaChangeContextHolder.retrace((Map<String, String>) nonMapObject);
    }
}