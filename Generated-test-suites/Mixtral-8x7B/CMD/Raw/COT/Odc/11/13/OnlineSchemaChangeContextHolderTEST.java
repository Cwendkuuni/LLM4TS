import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.MDCBuilder;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    // Test for trace(Long userId, Long taskId, Long organizationId)
    @Test
    public void testTraceLongLongLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for trace(String userId, String taskId, String organizationId)
    @Test
    public void testTraceStringStringString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for retrace(Map<String, String> context)
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for clear()
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for get(String key) with existing key
    @Test
    public void testGetExistingKey() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    // Test for get(String key) with non-existing key
    @Test
    public void testGetNonExistingKey() {
        assertEquals(null, OnlineSchemaChangeContextHolder.get("non-existing-key"));
    }

    // Edge cases and exception handling scenarios

    // Test for trace(Long userId, Long taskId, Long organizationId) with null userId
    @Test(expected = NumberFormatException.class)
    public void testTraceLongNullTaskIdOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(null, 2L, 3L);
    }

    // Test for trace(Long userId, Long taskId, Long organizationId) with null taskId
    @Test(expected = NumberFormatException.class)
    public void testTraceLongUserIdNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, null, 3L);
    }

    // Test for trace(Long userId, Long taskId, Long organizationId) with null organizationId
    @Test(expected = NumberFormatException.class)
    public void testTraceLongUserIdTaskIdNull() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, null);
    }

    // Test for trace(String userId, String taskId, String organizationId) with null userId
    @Test
    public void testTraceStringNullTaskIdOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(null, "2", "3");

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for trace(String userId, String taskId, String organizationId) with null taskId
    @Test
    public void testTraceStringUserIdNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace("1", null, "3");

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for trace(String userId, String taskId, String organizationId) with null organizationId
    @Test
    public void testTraceStringUserIdTaskIdNull() {
        OnlineSchemaChangeContextHolder.trace("1", "2", null);

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for retrace(Map<String, String> context) with null context
    @Test
    public void testRetraceNull() {
        OnlineSchemaChangeContextHolder.retrace(null);

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for retrace(Map<String, String> context) with empty context
    @Test
    public void testRetraceEmpty() {
        OnlineSchemaChangeContextHolder.retrace(Collections.emptyMap());

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Test for get(String key) with empty key
    @Test
    public void testGetEmptyKey() {
        assertEquals(null, OnlineSchemaChangeContextHolder.get(""));
    }

}