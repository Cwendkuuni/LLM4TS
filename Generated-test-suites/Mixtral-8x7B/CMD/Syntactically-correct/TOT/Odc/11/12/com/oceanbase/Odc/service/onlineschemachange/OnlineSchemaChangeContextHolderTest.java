import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeContextHolderTest {

@Before
public void setUp() {
MDC.clear();
}

@Test
public void testTraceLongLongLong() {
Long userId = 1L;
Long taskId = 2L;
Long organizationId = 3L;
OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
}

@After
public void tearDown() {
MDC.clear();
}
}