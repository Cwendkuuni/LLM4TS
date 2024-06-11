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

 private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

 @Before
 public void setup() {
 onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
 MDC.setContextMap(new HashMap<>());
 }

 @Test
 public void testTraceLongTaskIdLongOrganizationId() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;

 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 assertEquals(String.valueOf(userId), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(String.valueOf(taskId), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(String.valueOf(organizationId), MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringTaskIdStringOrganizationId() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";

 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 Long userIdL = (userId == null ? null : Long.parseLong(userId));
 Long taskIdL = (taskId == null ? null : Long.parseLong(taskId));
 Long organizationIdL = (organizationId == null ? null : Long.parseLong(organizationId));

 OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);

 assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

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

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.clear();

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 MDC.put(OdcConstants.ORGANIZATION_ID, "3");

 assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }

}