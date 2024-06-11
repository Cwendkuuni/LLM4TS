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

 private static final String TEST_TASK_ID = "12345";
 private static final String TEST_USER_ID = "67890";
 private static final String TEST_ORGANIZATION_ID = "11111";
 private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
 private MDCAdapter mdcAdapter;

 @Before
 public void setup() {
 onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
 mdcAdapter = mock(MDCAdapter.class);
 MDC.setAdapter(mdcAdapter);
 when(mdcAdapter.getCopyOfContextMap()).thenReturn(new HashMap<>());
 }

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 onlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID),
 Long.parseLong(TEST_ORGANIZATION_ID));
 assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
 onlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
 assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 Map<String, String> context = new HashMap<>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, TEST_USER_ID);
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
 context.put(OdcConstants.ORGANIZATION_ID, TEST_ORGANIZATION_ID);
 onlineSchemaChangeContextHolder.retrace(context);
 assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 onlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID),
 Long.parseLong(TEST_ORGANIZATION_ID));
 onlineSchemaChangeContextHolder.clear();
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 onlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID),
 Long.parseLong(TEST_ORGANIZATION_ID));
 assertEquals(TEST_USER_ID, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, onlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

}