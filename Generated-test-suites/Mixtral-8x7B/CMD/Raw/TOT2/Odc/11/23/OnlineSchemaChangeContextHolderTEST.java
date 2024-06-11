import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

private OnlineSchemaChangeContextHolder osch;

@Before
public void setUp() throws Exception {
osch = new OnlineSchemaChangeContextHolder();
}

@After
public void tearDown() throws Exception {
// clear MDC context after each test method
MDC.clear();
}

@Test
public void testTraceLongLongLong() {
// arrange
Long userId = 1L;
Long taskId = 2L;
Long organizationId = 3L;

// act
osch.trace(userId, taskId, organizationId);

// assert
assertEquals(String.valueOf(userId), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(String.valueOf(taskId), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(String.valueOf(organizationId), MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testTraceStringStringString() {
// arrange
String userId = "1";
String taskId = "2";
String organizationId = "3";

// act
osch.trace(userId, taskId, organizationId);

// assert
assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testRetrace() {
// arrange
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

// act
osch.retrace(context);

// assert
assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testClear() {
// arrange
osch.trace(1L, 2L, 3L);

// act
osch.clear();

// assert
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
// arrange
osch.trace(1L, 2L, 3L);

// act
String taskId = osch.get(OnlineSchemaChangeContextHolder.TASK_ID);

// assert
assertEquals("2", taskId);
}

@Test(expected = NullPointerException.class)
public void testGetNull() {
// arrange

// act
osch.get(null);

// assert exception is thrown
}

}