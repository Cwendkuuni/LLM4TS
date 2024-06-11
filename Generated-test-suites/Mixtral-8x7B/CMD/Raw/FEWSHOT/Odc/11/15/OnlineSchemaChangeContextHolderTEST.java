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

MDCAdapter mockMDC = mock(MDCAdapter.class);

@Before
public void setUp() {
MDC.setContextMap(new HashMap<>());
MDC.setCopyOnSave(false);
MDC.setAdapter(mockMDC);
}

@Test
public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

verifyContext(context);
}

@Test
public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

OnlineSchemaChangeContextHolder.trace("1", "2", "3");

verifyContext(context);
}

@Test
public void testRetrace() {
Map<String, String> context = new HashMap<>();
context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
context.put(OdcConstants.ORGANIZATION_ID, "3");

OnlineSchemaChangeContextHolder.retrace(context);

verifyContext(context);
}

@Test
public void testClear() {
OnlineSchemaChangeContextHolder.clear();

verifyContextEmpty();
}

@Test
public void testGet() {
when(mockMDC.get("TASK_WORK_SPACE")).thenReturn("1");

assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
}

@Test
public void testGetNull() {

assertNull(OnlineSchemaChangeContextHolder.get("unknownKey"));
}

private void verifyContext(Map<String, String> expectedContext) {
Map<String, String> actualContext = MDC.getCopyOfContextMap();

assertEquals(expectedContext.keySet().size(), actualContext.size());
for (String key : expectedContext.keySet()) {
assertEquals(expectedContext.get(key), actualContext.get(key));
}
}

private void verifyContextEmpty() {
assertEquals(0, MDC.getCopyOfContextMap().size());
}
}