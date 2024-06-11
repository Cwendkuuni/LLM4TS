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
public void testTraceAndGet() {
Long userId = 1L;
Long taskId = 2L;
Long organizationId = 3L;

OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

OnlineSchemaChangeContextHolder.clear();
}

@Test
public void testTraceWithStringArgsAndGet() {
String userId = "1";
String taskId = "2";
String organizationId = "3";

OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

OnlineSchemaChangeContextHolder.clear();
}

@Test
public void testRetraceAndGet() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

OnlineSchemaChangeContextHolder.retrace(context);

assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

OnlineSchemaChangeContextHolder.clear();
}

@Test
public void testClear() {
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

OnlineSchemaChangeContextHolder.clear();

assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGetWithNullKey() {
assertNull(OnlineSchemaChangeContextHolder.get(null));
}

@Test(expected = UnsupportedOperationException.class)
public void testMDCContextMapUnmodifiable() {
MDC.setContextMap(mock(Map.class));
}

@Test
public void testMDCContextMapModifiable() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

when(MDC.getCopyOfContextMap()).thenReturn(context);

OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

OnlineSchemaChangeContextHolder.clear();
}

}