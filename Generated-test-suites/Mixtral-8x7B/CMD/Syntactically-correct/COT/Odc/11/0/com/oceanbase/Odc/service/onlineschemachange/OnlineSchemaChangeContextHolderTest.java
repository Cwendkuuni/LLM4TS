import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), MDC.get(TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), MDC.get(TASK_ID));
        assertEquals(String.valueOf(organizationId), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("2", MDC.get(TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testReTrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "1");
        context.put(TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("2", MDC.get(TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNull() {
        assertNull(OnlineSchemaChangeContextHolder.get("non-existing-key"));
    }

    @Test
    public void testTraceNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testReTraceNull() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);

        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NullPointerException.class)
    public void testTraceException() {
        Map<String, String> context = null;
        OnlineSchemaChangeContextHolder.retrace(context);
    }
}