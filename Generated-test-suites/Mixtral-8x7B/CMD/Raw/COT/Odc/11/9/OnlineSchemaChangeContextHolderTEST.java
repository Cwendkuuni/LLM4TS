import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.MDCClearer;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
        SecurityContextUtils.setCurrentUser(null, null, null);
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

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

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetWithNullKey() {
        assertNull(OnlineSchemaChangeContextHolder.get(null));
    }

    @Test
    public void testGetWithEmptyKey() {
        assertNull(OnlineSchemaChangeContextHolder.get(""));
    }

    @Test
    public void testGetWithAbsentKey() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertNull(OnlineSchemaChangeContextHolder.get("absentKey"));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringWithInvalidNumber() {
        OnlineSchemaChangeContextHolder.trace("notAnumber", "2", "3");
    }

    @Test
    public void testRetraceWithNullContext() {
        OnlineSchemaChangeContextHolder.retrace(null);

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetraceWithEmptyContext() {
        OnlineSchemaChangeContextHolder.retrace(Collections.emptyMap());

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

}