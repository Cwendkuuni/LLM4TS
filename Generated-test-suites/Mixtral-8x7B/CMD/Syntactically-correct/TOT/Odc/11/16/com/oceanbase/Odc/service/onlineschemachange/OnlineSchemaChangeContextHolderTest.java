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

 private OnlineSchemaChangeContextHolder spyHolder;
 private Map<String, String> context;

 @Before
 public void setup() {
 spyHolder = spy(OnlineSchemaChangeContextHolder.class);
 context = new HashMap<String, String>();
 }

 @After
 public void teardown() {
 MDC.clear();
 }

 @Test
 public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;

 spyHolder.trace(userId, taskId, organizationId);

 assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";

 spyHolder.trace(userId, taskId, organizationId);

 assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");

 spyHolder.retrace(context);

 assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 spyHolder.trace(1L, 2L, 3L);

 spyHolder.clear();

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 spyHolder.trace(1L, 2L, 3L);

 assertEquals("1", spyHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", spyHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", spyHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGetNull() {
 assertNull(spyHolder.get("NonExistentKey"));
 }

 @Test
 public void testSetSecurityContextUtils() {
 SecurityContextUtils securityContextUtilsMock = mock(SecurityContextUtils.class);
 when(securityContextUtilsMock.setCurrentUser(1L, 2L, null)).thenReturn(null);

 spyHolder.setSecurityContextUtils(securityContextUtilsMock);

 spyHolder.trace(1L, 2L, 3L);

 assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
 }

}