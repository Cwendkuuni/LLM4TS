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

 /**
 * Test trace method with valid parameters.
 */
 @Test
 public void testTraceValidParameters() {
 long userId = 1L;
 long taskId = 2L;
 long organizationId = 3L;

 spyHolder.trace(userId, taskId, organizationId);

 assertEquals(userId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(taskId + "", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(organizationId + "", MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test trace method with null parameters.
 */
 @Test
 public void testTraceWithNullParameters() {
 spyHolder.trace(null, null, null);

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test retrace method with valid Map.
 */
 @Test
 public void testRetraceWithValidMap() {
 context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
 context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
 context.put(OdcConstants.ORGANIZATION_ID, "3");

 spyHolder.retrace(context);

 assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test retrace method with empty Map.
 */
 @Test
 public void testRetraceWithEmptyMap() {
 spyHolder.retrace(new HashMap<String, String>());

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test clear method.
 */
 @Test
 public void testClear() {
 spyHolder.trace(1L, 2L, 3L);
 spyHolder.clear();

 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test get method with valid key.
 */
 @Test
 public void testGetWithValidKey() {
 spyHolder.trace(1L, 2L, 3L);

 assertEquals("1", spyHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals("2", spyHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals("3", spyHolder.get(OdcConstants.ORGANIZATION_ID));
 }

 /**
 * Test get method with invalid key.
 */
 @Test
 public void testGetWithInvalidKey() {
 spyHolder.trace(1L, 2L, 3L);

 assertNull(spyHolder.get("invalidKey"));
 }

 /**
 * Test security context util set current user.
 */
 @Test
 public void testSecurityContextUtilsSetCurrentUser() {
 OnlineSchemaChangeContextHolder.SecurityContextUtils
mockUtils = mock(OnlineSchemaChangeContextHolder.SecurityContextUtils.class);

 OnlineSchemaChangeContextHolder.setSecurityContextUtils(mockUtils);

 long userId = 1L;
 long organizationId = 2L;

 spyHolder.trace(userId, null, organizationId);

 verify(mockUtils).setCurrentUser(userId, organizationId, null);
 }

}