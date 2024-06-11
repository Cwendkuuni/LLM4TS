import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

 private static final String TEST_TASK_ID = "123456";
 private static final String TEST_USER_ID = "987654";
 private static final String TEST_ORGANIZATION_ID = "345678";
 private static final String TEST_KEY = "testKey";
 private static final String TEST_VALUE = "testValue";

 private Map<String, String> context;

 @Before
 public void setUp() {
 context = new HashMap<>();
 context.put(TASK_ID, TEST_TASK_ID);
 context.put(TASK_WORK_SPACE, TEST_USER_ID);
 context.put(OdcConstants.ORGANIZATION_ID, TEST_ORGANIZATION_ID);
 OnlineSchemaChangeContextHolder.retrace(context);
 }

 @Test
 public void testTraceLongUserIdAndTaskIdAndOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID), Long.parseLong(TEST_ORGANIZATION_ID));
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 OnlineSchemaChangeContextHolder.clear();
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals("", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdAndTaskIdAndOrganizationId() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 OnlineSchemaChangeContextHolder.clear();
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals("", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 OnlineSchemaChangeContextHolder.retrace(context);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals(TEST_ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
 OnlineSchemaChangeContextHolder.clear();
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals("", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);
 assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 OnlineSchemaChangeContextHolder.clear();
 assertEquals("", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 }

 @After
 public void tearDown() {
 context.clear();
 }
}