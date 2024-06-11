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

public class OnlineSchemaChangeContextHolderTest {

 private static final String USER_ID = "123";
 private static final String TASK_ID = "456";
 private static final String ORGANIZATION_ID = "789";
 private static final String KEY = "testKey";
 private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
 private MDCAdapter mdcAdapter;

 @Before
 public void setup() {
 onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
 mdcAdapter = mock(MDCAdapter.class);
 MDC.setContextMap(new HashMap<>());
 MDC.setAdapter(mdcAdapter);
 when(mdcAdapter.get(KEY)).thenReturn(null);
 }

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 onlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID),
 Long.parseLong(ORGANIZATION_ID));
 assertEquals(USER_ID, MDC.get(TASK_WORK_SPACE));
 assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
 onlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
 assertEquals(USER_ID, MDC.get(TASK_WORK_SPACE));
 assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 Map<String, String> context = new HashMap<>();
 context.put(TASK_WORK_SPACE, USER_ID);
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, TASK_ID);
 context.put(OdcConstants.ORGANIZATION_ID, ORGANIZATION_ID);
 onlineSchemaChangeContextHolder.retrace(context);
 assertEquals(USER_ID, MDC.get(TASK_WORK_SPACE));
 assertEquals(TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 onlineSchemaChangeContextHolder.clear();
 assertNull(MDC.get(TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 assertEquals(null, onlineSchemaChangeContextHolder.get(KEY));
 }
}