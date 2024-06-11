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

private MDCAdapter mdcAdapter;

@Before
public void setup() {
onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
mdcAdapter = mock(MDCAdapter.class);
MDC.setContextMap(new HashMap<>());
MDC.setAdapter(mdcAdapter);
}

@Test
public void testTraceWithLongParams() {
Long userId = 1L;
Long taskId = 2L;
Long organizationId = 3L;

onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testTraceWithStringParams() {
String userId = "1";
String taskId = "2";
String organizationId = "3";

onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

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

onlineSchemaChangeContextHolder.retrace(context);

assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testClear() {
onlineSchemaChangeContextHolder.clear();

assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
String key = "testKey";
String value = "testValue";

MDC.put(key, value);

assertEquals(value, onlineSchemaChangeContextHolder.get(key));
}

}