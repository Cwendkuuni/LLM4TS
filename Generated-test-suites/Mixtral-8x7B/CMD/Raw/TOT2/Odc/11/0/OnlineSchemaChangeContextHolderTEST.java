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

@Test(expected = NumberFormatException.class)
public void testTraceInvalidUserId() {
String userId = "invalid";
String taskId = "2";
String organizationId = "3";

OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
}

@Test(expected = NumberFormatException.class)
public void testTraceInvalidTaskId() {
String userId = "1";
String taskId = "invalid";
String organizationId = "3";

OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
}

@Test(expected = NumberFormatException.class)
public void testTraceInvalidOrganizationId() {
String userId = "1";
String taskId = "2";
String organizationId = "invalid";

OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
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
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
OnlineSchemaChangeContextHolder.clear();

assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGetNull() {
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

MDC.remove(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
}

}