import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.MDCAdapter;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

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

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        OnlineSchemaChangeContextHolder.get(null);
    }

    @Test
    public void testGetEmptyKey() {
        assertEquals("", OnlineSchemaChangeContextHolder.get(""));
    }

    @Test
    public void testGetNoValue() {
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testMDCAdapterGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        assertEquals("1", mdcAdapter.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testMDCAdapterClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        mdcAdapter.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testMDCAdapterSetContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        mdcAdapter.setContextMap(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testMDCAdapterGetCopyOfContextMap() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        Map<String, String> copyOfContextMap = mdcAdapter.getCopyOfContextMap();

        assertTrue(copyOfContextMap.containsKey(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", copyOfContextMap.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testMDCAdapterGetContextMap() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        Map<String, String> contextMap = mdcAdapter.getContextMap();

        assertTrue(contextMap.containsKey(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", contextMap.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}