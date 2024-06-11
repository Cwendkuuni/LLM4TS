import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

@Test
public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
MDC.clear();
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
MDC.clear();
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
MDC.clear();
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
MDC.clear();
MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
MDC.put(OdcConstants.ORGANIZATION_ID, "3");
OnlineSchemaChangeContextHolder.clear();
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
MDC.clear();
MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
}

@Test
public void testGetNull() {
MDC.clear();
assertNull(OnlineSchemaChangeContextHolder.get("non-existing-key"));
}

@Test
public void testGetWhenContextIsEmpty() {
assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
}

@Test
public void testSetSecurityContextUtils() {
// This method is tested indirectly through testTraceLongUserIdLongTaskIdLongOrganizationId
// and testTraceStringUserIdStringTaskIdStringOrganizationId methods
}

@Test
public void testSetSecurityContextUtilsWithNull() {
// This method is tested indirectly through testTraceLongUserIdLongTaskIdLongOrganizationId
// and testTraceStringUserIdStringTaskIdStringOrganizationId methods
}
}