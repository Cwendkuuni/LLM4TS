import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

private static final long VALID_USER_ID = 1L;
private static final long VALID_TASK_ID = 2L;
private static final long VALID_ORGANIZATION_ID = 3L;
private static final String VALID_USER_ID_STR = "1";
private static final String VALID_TASK_ID_STR = "2";
private static final String VALID_ORGANIZATION_ID_STR = "3";
private static final String INVALID_KEY = "invalidKey";
private static final Map<String, String> VALID_CONTEXT;

static {
VALID_CONTEXT = new HashMap<>();
VALID_CONTEXT.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, VALID_USER_ID_STR);
VALID_CONTEXT.put(OnlineSchemaChangeContextHolder.TASK_ID, VALID_TASK_ID_STR);
VALID_CONTEXT.put(OdcConstants.ORGANIZATION_ID, VALID_ORGANIZATION_ID_STR);
}

@Before
public void setup() {
MDC.clear();
}

// trace(Long userId, Long taskId, Long organizationId)
@Test
public void testTraceWithLongArgs() {
OnlineSchemaChangeContextHolder.trace(VALID_USER_ID, VALID_TASK_ID, VALID_ORGANIZATION_ID);
assertEquals(VALID_USER_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(VALID_TASK_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(VALID_ORGANIZATION_ID_STR, MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testTraceWithNullArgs() {
OnlineSchemaChangeContextHolder.trace(null, null, null);
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
}

// trace(String userId, String taskId, String organizationId)
@Test
public void testTraceWithStringArgs() {
OnlineSchemaChangeContextHolder.trace(VALID_USER_ID_STR, VALID_TASK_ID_STR, VALID_ORGANIZATION_ID_STR);
assertEquals(VALID_USER_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(VALID_TASK_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(VALID_ORGANIZATION_ID_STR, MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testTraceWithEmptyStringArgs() {
OnlineSchemaChangeContextHolder.trace("", "", "");
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
}

// retrace(Map<String, String> context)
@Test
public void testRetrace() {
OnlineSchemaChangeContextHolder.retrace(VALID_CONTEXT);
assertEquals(VALID_USER_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(VALID_TASK_ID_STR, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(VALID_ORGANIZATION_ID_STR, MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testRetraceWithNullContext() {
OnlineSchemaChangeContextHolder.retrace(null);
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
}

// clear()
@Test
public void testClear() {
OnlineSchemaChangeContextHolder.trace(VALID_USER_ID, VALID_TASK_ID, VALID_ORGANIZATION_ID);
OnlineSchemaChangeContextHolder.clear();
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(null, MDC.get(OdcConstants.ORGANIZATION_ID));
}

// get(String key)
@Test
public void testGetWithValidKey() {
OnlineSchemaChangeContextHolder.trace(VALID_USER_ID, VALID_TASK_ID, VALID_ORGANIZATION_ID);
assertEquals(VALID_USER_ID_STR, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(VALID_TASK_ID_STR, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(VALID_ORGANIZATION_ID_STR, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGetWithInvalidKey() {
OnlineSchemaChangeContextHolder.trace(VALID_USER_ID, VALID_TASK_ID, VALID_ORGANIZATION_ID);
assertEquals(null, OnlineSchemaChangeContextHolder.get(INVALID_KEY));
}

}