import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

@Before
public void setUp() {
onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
}

@Test
public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
Map<String, String> expectedContext = new HashMap<>();
expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "0");
expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "0");
expectedContext.put(OdcConstants.ORGANIZATION_ID, "0");

onlineSchemaChangeContextHolder.trace(0L, 0L, 0L);
assertEquals(expectedContext, MDC.getCopyOfContextMap());
}

@Test
public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
Map<String, String> expectedContext = new HashMap<>();
expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "0");
expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, "0");
expectedContext.put(OdcConstants.ORGANIZATION_ID, "0");

onlineSchemaChangeContextHolder.trace("0", "0", "0");
assertEquals(expectedContext, MDC.getCopyOfContextMap());
}

@Test
public void testRetrace() {
Map<String, String> inputContext = new HashMap<>();
inputContext.put("key1", "value1");
inputContext.put("key2", "value2");

onlineSchemaChangeContextHolder.retrace(inputContext);
assertEquals(inputContext, MDC.getCopyOfContextMap());
}

@Test
public void testClear() {
Map<String, String> inputContext = new HashMap<>();
inputContext.put("key1", "value1");
inputContext.put("key2", "value2");

MDC.setContextMap(inputContext);
onlineSchemaChangeContextHolder.clear();
assertEquals(Collections.emptyMap(), MDC.getCopyOfContextMap());
}

@Test
public void testGet() {
MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "12345");

String result = onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);

assertEquals("12345", result);
}

@Test(expected = NullPointerException.class)
public void testGetNullKey() {
onlineSchemaChangeContextHolder.get(null);
}

@Test
public void testGetEmptyKey() {
String result = onlineSchemaChangeContextHolder.get("");

assertEquals("", result);
}
}