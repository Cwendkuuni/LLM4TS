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

 private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

 @Before
 public void setup() {
 onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
 }

 @Test
 public void testTraceLong() {
 Map<String, String> context = new HashMap<String, String>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");

 onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

 assertEquals(context, MDC.getCopyOfContextMap());
 }

 @Test
 public void testTraceString() {
 Map<String, String> context = new HashMap<String, String>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");

 onlineSchemaChangeContextHolder.trace("1", "2", "3");

 assertEquals(context, MDC.getCopyOfContextMap());
 }

 @Test
 public void testRetrace() {
 Map<String, String> context = new HashMap<String, String>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");

 onlineSchemaChangeContextHolder.retrace(context);

 assertEquals(context, MDC.getCopyOfContextMap());
 }

 @Test
 public void testClear() {
 onlineSchemaChangeContextHolder.clear();

 assertNull(MDC.getCopyOfContextMap());
 }

 @Test
 public void testGet() {
 MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");

 assertEquals("123", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }

 @Test
 public void testGetNull() {

 assertEquals(null, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 }

 @Test
 public void testSetSecurityContextUtils() {
 SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
 when(securityContextUtils.setCurrentUser(1L, 2L, null)).thenReturn(true);

 onlineSchemaChangeContextHolder.setSecurityContextUtils(securityContextUtils);

 onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

 assertEquals(true, SecurityContextUtils.setCurrentUser(1L, 2L, null));
 }

}