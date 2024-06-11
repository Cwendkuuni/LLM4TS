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

private MDCAdapter mdcAdapter;

@Before
public void setup() {
mdcAdapter = mock(MDCAdapter.class);
MDC.setContextMap(new HashMap<>());
MDC.setCopyOnLog(false);
MDC.setAdapter(mdcAdapter);
}

@Test
public void testTrace() {
MDC.clear();
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testTraceWithString() {
MDC.clear();
OnlineSchemaChangeContextHolder.trace("1", "2", "3");
assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
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
OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
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