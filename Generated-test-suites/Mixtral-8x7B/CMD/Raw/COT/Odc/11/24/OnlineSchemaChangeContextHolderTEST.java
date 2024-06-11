import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder underTest;
    private SecurityContextUtils securityContextUtilsMock;

    @Before
    public void setUp() {
        securityContextUtilsMock = mock(SecurityContextUtils.class);
        underTest = new OnlineSchemaChangeContextHolder();
        underTest.securityContextUtils = securityContextUtilsMock;
    }

    @Test
    public void testTrace_whenUserIdTaskIdAndOrganizationIdAreNotNull_thenCorrectMDCValuesAreSet() {
        // Arrange
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        // Act
        underTest.trace(userId, taskId, organizationId);

        // Assert
        assertEquals(userId.toString(), MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_whenUserIdTaskIdAndOrganizationIdAreNull_thenNoMDCValuesAreSet() {
        // Act
        underTest.trace(null, null, null);

        // Assert
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_whenUserIdIsNotNullAndTaskIdAndOrganizationIdAreNull_thenNoMDCValuesAreSet() {
        // Act
        underTest.trace(1L, null, null);

        // Assert
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_whenStringValuesAreProvided_thenCorrectMDCValuesAreSet() {
        // Arrange
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        // Act
        underTest.trace(userId, taskId, organizationId);

        // Assert
        assertEquals(userId, MDC.get(TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace_whenContextIsNotNull_thenCorrectMDCValuesAreSet() {
        // Arrange
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "1");
        context.put(TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        // Act
        underTest.retrace(context);

        // Assert
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("2", MDC.get(TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace_whenContextIsNotNullAndHasNoValues_thenMDCIsCleared() {
        // Arrange
        Map<String, String> context = new HashMap<>();

        // Act
        underTest.retrace(context);

        // Assert
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace_whenContextIsNotNullAndHasSomeValues_thenCorrectMDCValuesAreSet() {
        // Arrange
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "1");
        context.put(TASK_ID, "2");

        // Act
        underTest.retrace(context);

        // Assert
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("2", MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear_thenMDCIsCleared() {
        // Arrange
        underTest.trace(1L, 2L, 3L);

        // Act
        underTest.clear();

        // Assert
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet_whenKeyIsNotNull_thenCorrectValueIsReturned() {
        // Arrange
        underTest.trace(1L, 2L, 3L);

        // Act
        String result = underTest.get(TASK_WORK_SPACE);

        // Assert
        assertEquals("1", result);
    }

    @Test
    public void testGet_whenKeyIsNotNullAndMDCDoesNotContainKey_thenNullIsReturned() {
        // Act
        String result = underTest.get(TASK_WORK_SPACE);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGet_whenKeyIsNull_thenNullIsReturned() {
        // Act
        String result = underTest.get(null);

        // Assert
        assertNull(result);
    }
}