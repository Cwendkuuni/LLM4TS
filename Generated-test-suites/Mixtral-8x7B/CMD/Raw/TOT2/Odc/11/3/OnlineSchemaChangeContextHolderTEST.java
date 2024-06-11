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

 /**
 * Test method for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#trace(java.lang.Long, java.lang.Long, java.lang.Long)}.
 */
 @Test
 public void testTraceLongLongLong() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test method for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#trace(java.lang.String, java.lang.String, java.lang.String)}.
 */
 @Test
 public void testTraceStringStringString() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test method for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#retrace(java.util.Map)}.
 */
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

 /**
 * Test method for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#clear()}.
 */
 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 OnlineSchemaChangeContextHolder.clear();

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test method for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#get(java.lang.String)}.
 */
 @Test
 public void testGet() {
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

 assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test method for edge case when passing null value to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#trace(java.lang.String, java.lang.String, java.lang.String)}.
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceStringStringStringEdgeCase() {
 String userId = null;
 String taskId = null;
 String organizationId = null;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 }

 /**
 * Test method for edge case when passing empty string to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#trace(java.lang.String, java.lang.String, java.lang.String)}.
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceStringStringStringEdgeCaseEmpty() {
 String userId = "";
 String taskId = "";
 String organizationId = "";
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 }

 /**
 * Test method for edge case when passing null value to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#retrace(java.util.Map)}.
 */
 @Test(expected = NullPointerException.class)
 public void testRetraceEdgeCase() {
 Map<String, String> context = null;
 OnlineSchemaChangeContextHolder.retrace(context);
 }

 /**
 * Test method for edge case when passing empty map to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#retrace(java.util.Map)}.
 */
 @Test
 public void testRetraceEdgeCaseEmptyMap() {
 Map<String, String> context = new HashMap<>();
 OnlineSchemaChangeContextHolder.retrace(context);

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test method for edge case when passing null value to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#get(java.lang.String)}.
 */
 @Test
 public void testGetEdgeCase() {
 MDC.clear();

 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 }

 /**
 * Test method for exception handling when passing null value to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#get(java.lang.String)}.
 */
 @Test
 public void testGetExceptionCase() {
 MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, null);

 assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 }

 /**
 * Test method for edge case when passing empty map to {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder#clear()}.
 */
 @Test
 public void testClearEdgeCase() {
 OnlineSchemaChangeContextHolder.retrace(Collections.emptyMap());

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

}