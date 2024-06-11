import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

Map<String, String> context;

@Before
public void setup() {
context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "456");
context.put(OdcConstants.ORGANIZATION_ID, "789");
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
OnlineSchemaChangeContextHolder.trace("1", "2", "3");

assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testRetrace() {
OnlineSchemaChangeContextHolder.retrace(context);

assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testClear() {
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
OnlineSchemaChangeContextHolder.clear();

assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
}
}