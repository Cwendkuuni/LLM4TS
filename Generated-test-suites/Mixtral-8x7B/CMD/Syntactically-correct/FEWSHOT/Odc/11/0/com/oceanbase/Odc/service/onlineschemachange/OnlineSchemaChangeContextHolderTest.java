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
}

@Test
public void testTraceLong() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

assertEquals(context, MDC.getCopyOfContextMap());
}

@Test
public void testTraceString() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

onlineSchemaChangeContextHolder.trace("1", "2", "3");

assertEquals(context, MDC.getCopyOfContextMap());
}

@Test
public void testRetrace() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

onlineSchemaChangeContextHolder.retrace(context);

assertEquals(context, MDC.getCopyOfContextMap());
}

@Test
public void testClear() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
onlineSchemaChangeContextHolder.clear();

assertNull(MDC.getCopyOfContextMap());
}

@Test
public void testGet() {
onlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

assertEquals("2", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
}

@Test
public void testGetNull() {
assertNull(onlineSchemaChangeContextHolder.get(null));
}

@Test
public void testMDCAdapter() {
MDCAdapter mdcAdapter = mock(MDCAdapter.class);
when(mdcAdapter.getCopyOfContextMap()).thenReturn(null);
MDC.setContextMap(mdcAdapter);

assertNull(OnlineSchemaChangeContextHolder.get(null));
}

}