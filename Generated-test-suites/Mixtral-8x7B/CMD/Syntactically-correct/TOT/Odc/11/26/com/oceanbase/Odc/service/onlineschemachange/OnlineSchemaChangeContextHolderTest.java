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

 /**
 * Test trace method with valid input
 */
 @Test
 public void testTraceValidInput() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test trace method with null input
 */
 @Test
 public void testTraceNullInput() {
 OnlineSchemaChangeContextHolder.trace(null, null, null);
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test retrace method with valid input
 */
 @Test
 public void testRetraceValidInput() {
 Map<String, String> context = new HashMap<>();
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");
 OnlineSchemaChangeContextHolder.retrace(context);
 assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test retrace method with null input
 */
 @Test
 public void testRetraceNullInput() {
 OnlineSchemaChangeContextHolder.retrace(null);
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test clear method
 */
 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 OnlineSchemaChangeContextHolder.clear();
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test get method
 */
 @Test
 public void testGet() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @After
 public void tearDown() {
 MDC.clear();
 }
}