import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private OnlineSchemaChangeContextHolder underTest;

 @Before
 public void setUp() {
 underTest = new OnlineSchemaChangeContextHolder();
 }

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 Map<String, String> expectedContext = new HashMap<>();
 expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "0");
 expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "0");
 expectedContext.put(OdcConstants.ORGANIZATION_ID, "0");

 underTest.trace(0L, 0L, 0L);

 assertEquals(expectedContext, MDC.getCopyOfContextMap());
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
 Map<String, String> expectedContext = new HashMap<>();
 expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "0");
 expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "0");
 expectedContext.put(OdcConstants.ORGANIZATION_ID, "0");

 underTest.trace("0", "0", "0");

 assertEquals(expectedContext, MDC.getCopyOfContextMap());
 }

 @Test
 public void testRetrace() {
 Map<String, String> inputContext = new HashMap<>();
 inputContext.put("key1", "value1");
 inputContext.put("key2", "value2");

 underTest.retrace(inputContext);

 Map<String, String> expectedContext = new HashMap<>(inputContext);

 assertEquals(expectedContext, MDC.getCopyOfContextMap());
 }

 @Test
 public void testClear() {
 Map<String, String> initialContext = new HashMap<>();
 initialContext.put("key1", "value1");
 initialContext.put("key2", "value2");

 MDC.setContextMap(initialContext);

 underTest.clear();

 assertTrue(MDC.getCopyOfContextMap().isEmpty());
 }

 @Test
 public void testGet() {
 MDC.put("key1", "value1");

 String result = underTest.get("key1");

 assertEquals("value1", result);
 }

 @Test
 public void testGetWithMissingKey() {
 String result = underTest.get("missingKey");

 assertNull(result);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceStringUserIdStringTaskIdStringOrganizationIdWithNonNumericUserId() {
 underTest.trace("notAnumber", "0", "0");
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceStringUserIdStringTaskIdStringOrganizationIdWithNonNumericTaskId() {
 underTest.trace("0", "notAnumber", "0");
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceStringUserIdStringTaskIdStringOrganizationIdWithNonNumericOrganizationId() {
 underTest.trace("0", "0", "notAnumber");
 }

 @Test
 public void testRetraceWithNullContext() {
 underTest.retrace(null);

 assertTrue(MDC.getCopyOfContextMap().isEmpty());
 }

 @Test
 public void testGetWithEmptyContext() {
 underTest.clear();

 String result = underTest.get("key1");

 assertNull(result);
 }

 @Test
 public void testEdgeCaseGetWithMultipleValues() {
 MDC.put("key1", "value1");
 MDC.put("key2", "value2");

 String resultKey1 = underTest.get("key1");
 String resultKey2 = underTest.get("key2");

 assertEquals("value1", resultKey1);
 assertEquals("value2", resultKey2);
 }

 @Test
 public void testEdgeCaseRetraceWithMultipleValues() {
 Map<String, String> inputContext = new HashMap<>();
 inputContext.put("key1", "value1");
 inputContext.put("key2", "value2");

 underTest.retrace(inputContext);

 Map<String, String> expectedContext = new HashMap<>(inputContext);

 assertEquals(expectedContext, MDC.getCopyOfContextMap());
 }

 @Test
 public void testEdgeCaseClearWithMultipleValues() {
 MDC.put("key1", "value1");
 MDC.put("key2", "value2");

 underTest.clear();

 assertTrue(MDC.getCopyOfContextMap().isEmpty());
 }
}