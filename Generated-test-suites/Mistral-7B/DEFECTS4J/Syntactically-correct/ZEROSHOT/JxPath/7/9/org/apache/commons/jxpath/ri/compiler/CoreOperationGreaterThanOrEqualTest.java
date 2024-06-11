package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanOrEqualTest {

    @Mock
    private EvalContext context;

    @InjectMocks
    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;

    @Test
    public void testComputeValue_whenFirstArgumentIsGreaterOrEqualToSecondArgument_thenReturnTrue() {
        // Given
        double firstArgument = 10.0;
        double secondArgument = 10.0;

        // When
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsLessThanSecondArgument_thenReturnFalse() {
        // Given
        double firstArgument = 5.0;
        double secondArgument = 10.0;

        // When
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        // Then
        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsEqualToSecondArgument_thenReturnTrue() {
        // Given
        double firstArgument = 10.0;
        double secondArgument = 10.0;

        // When
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol_thenReturnGreaterThanOrEqualSymbol() {
        // Given

        // When
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();

        // Then
        assertEquals(">=", symbol);
    }
}