import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.MDCAdapter;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    private MDCAdapter mdcAdapter;

    @Before
    public void setup() {
        mdcAdapter = mock(MDCAdapter.class);
        MDC.setContextMap(new HashMap<>());
        MDC.setAdapter(mdcAdapter);
    }

    @Test
    public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
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
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        when(mdcAdapter.get("nonExistentKey")).thenReturn(null);

        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));

        when(mdcAdapter.get(OnlineSchemaChangeContextHolder.TASK_ID)).thenReturn("1");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidLongUserId() {
        OnlineSchemaChangeContextHolder.trace("invalidUserId", "1", "1");
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidLongTaskId() {
        OnlineSchemaChangeContextHolder.trace("1", "invalidTaskId", "1");
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceInvalidLongOrganizationId() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "invalidOrganizationId");
    }

    @Test(expected = NumberFormatException.class)
    public void testRetraceInvalidUserId() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "invalidUserId");
        context.put(OdcConstants.ORGANIZATION_ID, "1");

        OnlineSchemaChangeContextHolder.retrace(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testRetraceInvalidTaskId() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "invalidTaskId");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OdcConstants.ORGANIZATION_ID, "1");

        OnlineSchemaChangeContextHolder.retrace(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testRetraceInvalidOrganizationId() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OdcConstants.ORGANIZATION_ID, "invalidOrganizationId");

        OnlineSchemaChangeContextHolder.retrace(context);
    }
}