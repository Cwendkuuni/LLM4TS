import static org.junit.Assert.*;

import java.util.Collections;
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

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
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

 @Test(expected = NumberFormatException.class)
 public void testTraceLongUserIdNullTaskIdLongOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(1L, null, 3L);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceLongUserIdLongTaskIdNullOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceStringUserIdNullTaskIdStringOrganizationId() {
 OnlineSchemaChangeContextHolder.trace("1", null, "3");
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceStringUserIdStringTaskIdNullOrganizationId() {
 OnlineSchemaChangeContextHolder.trace("1", "2", null);
 }

 @Test(expected = NullPointerException.class)
 public void testRetraceWithNullContext() {
 OnlineSchemaChangeContextHolder.retrace(null);
 }

 @Test
 public void testRetraceWithEmptyContext() {
 OnlineSchemaChangeContextHolder.retrace(Collections.<String, String>emptyMap());
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

}