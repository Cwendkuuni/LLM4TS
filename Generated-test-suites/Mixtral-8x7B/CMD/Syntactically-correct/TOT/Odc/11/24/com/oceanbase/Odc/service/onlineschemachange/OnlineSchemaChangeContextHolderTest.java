import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private static final String TEST_TASK_ID = "123456";
 private static final String TEST_USER_ID = "654321";
 private static final String TEST_ORGANIZATION_ID = "111111";
 private static final Long TEST_LONG_TASK_ID = Long.parseLong(TEST_TASK_ID);
 private static final Long TEST_LONG_USER_ID = Long.parseLong(TEST_USER_ID);
 private static final Long TEST_LONG_ORGANIZATION_ID = Long.parseLong(TEST_ORGANIZATION_ID);

 @Before
 public void setup() {
 MDC.clear();
 }

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(TEST_LONG_USER_ID, TEST_LONG_TASK_ID, TEST_LONG_ORGANIZATION_ID);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 Map<String, String> context = new HashMap<>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, TEST_USER_ID);
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
 context.put(OdcConstants.ORGANIZATION_ID, TEST_ORGANIZATION_ID);
 OnlineSchemaChangeContextHolder.retrace(context);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(TEST_LONG_USER_ID, TEST_LONG_TASK_ID, TEST_LONG_ORGANIZATION_ID);
 OnlineSchemaChangeContextHolder.clear();
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceInvalidStringUserId() {
 OnlineSchemaChangeContextHolder.trace("not a number", TEST_TASK_ID, TEST_ORGANIZATION_ID);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceInvalidStringTaskId() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, "not a number", TEST_ORGANIZATION_ID);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceInvalidStringOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, "not a number");
 }

 @After
 public void teardown() {
 MDC.clear();
 }
}