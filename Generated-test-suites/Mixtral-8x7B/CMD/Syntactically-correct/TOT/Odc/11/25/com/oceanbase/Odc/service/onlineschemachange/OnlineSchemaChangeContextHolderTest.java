
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
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
        Map<String, String> context = Collections.singletonMap(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}
