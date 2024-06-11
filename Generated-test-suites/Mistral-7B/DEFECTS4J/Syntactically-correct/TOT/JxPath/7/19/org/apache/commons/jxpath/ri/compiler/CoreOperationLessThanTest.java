package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoreOperationLessThanTest {

    private CoreOperationLessThan lessThan;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @BeforeEach
    public void setUp() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        context = mock(EvalContext.class);
        lessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @DisplayName("Test less than with double values: positive case")
    @Test
    void testLessThanDoublePositive() {
        double arg1Double = 2.5;
        double arg2Double = 3.5;

        when(arg1.computeValue(context)).thenReturn(arg1Double);
        when(arg2.computeValue(context)).thenReturn(arg2Double);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> invocationOnMock.getArgument(0).doubleValue());

        Object result = lessThan.computeValue(context);

        verify(context, times(2)).pushNode(any());
        verify(context, times(2)).popNode();
        assertEquals(Boolean.FALSE, result);

        // Replace arg1Double and arg2Double with values small than each other
        arg1Double = 1.5;
        arg2Double = 2.5;
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @DisplayName("Test less than with double values: edge cases")
    @ParameterizedTest
    @MethodSource("edgeCasesDoubleLessThanProvider")
    void testLessThanDoubleEdgeCases(double arg1Double, double arg2Double, boolean expectedResult) {
        when(arg1.computeValue(context)).thenReturn(arg1Double);
        when(arg2.computeValue(context)).thenReturn(arg2Double);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> invocationOnMock.getArgument(0).doubleValue());

        Object result = lessThan.computeValue(context);
        assertEquals(expectedResult, result);
    }

    private static Stream<Object[]> edgeCasesDoubleLessThanProvider() {
        return Stream.of(new Object[] { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Boolean.TRUE },
                new Object[] { Double.NEGATIVE_INFINITY, Double.NaN, Boolean.FALSE },
                new Object[] { Double.NaN, Double.NEGATIVE_INFINITY, Boolean.FALSE },
                new Object[] { Double.NaN, Double.NaN, Boolean.FALSE },
                new Object[] { Double.POSITIVE_0, Double.POSITIVE_0, Boolean.FALSE },
                new Object[] { Double.POSITIVE_0, Double.NEGATIVE_0, Boolean.TRUE },
                new Object[] { Double.MAX_VALUE, Double.MAX_VALUE, Boolean.FALSE },
                new Object[] { Double.MAX_VALUE, Double.MIN_VALUE, Boolean.TRUE });
    }

    @DisplayName("Test less than with integer values: positive case")
    @Test
    void testLessThanIntegerPositive() {
        int arg1Int = 2;
        int arg2Int = 3;

        when(arg1.computeValue(context)).thenReturn(arg1Int);
        when(arg2.computeValue(context)).thenReturn(arg2Int);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> ((Number) invocationOnMock.getArgument(0)).doubleValue());

        Object result = lessThan.computeValue(context);

        verify(context, times(2)).pushNode(any());
        verify(context, times(2)).popNode();
        assertEquals(Boolean.FALSE, result);

        arg1Int = 1;
        arg2Int = 2;
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @DisplayName("Test less than with integer values: edge cases")
    @ParameterizedTest
    @MethodSource("edgeCasesIntegerLessThanProvider")
    void testLessThanIntegerEdgeCases(int arg1Int, int arg2Int, boolean expectedResult) {
        when(arg1.computeValue(context)).thenReturn(arg1Int);
        when(arg2.computeValue(context)).thenReturn(arg2Int);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> ((Number) invocationOnMock.getArgument(0)).doubleValue());

        Object result = lessThan.computeValue(context);
        assertEquals(expectedResult, result);
    }

    private static Stream<Object[]> edgeCasesIntegerLessThanProvider() {
        return Stream.of(new Object[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { Integer.MIN_VALUE, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Integer.MAX_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { 0, 0, Boolean.FALSE },
                new Object[] { 0, 1, Boolean.TRUE },
                new Object[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { Integer.MIN_VALUE, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Integer.MAX_VALUE, Integer.MIN_VALUE, Boolean.TRUE });
    }

    @DisplayName("Test less than with mixed values: positive case")
    @Test
    void testLessThanMixedValuesPositive() {
        double arg1Double = 2.5;
        int arg2Int = 3;

        when(arg1.computeValue(context)).thenReturn(arg1Double);
        when(arg2.computeValue(context)).thenReturn(arg2Int);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> invocationOnMock.getArgument(0).doubleValue());

        Object result = lessThan.computeValue(context);

        verify(context, times(2)).pushNode(any());
        verify(context, times(2)).popNode();
        assertEquals(Boolean.FALSE, result);

        arg1Double = 1.5;
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @DisplayName("Test less than with mixed values: edge cases")
    @ParameterizedTest
    @MethodSource("edgeCasesMixedLessThanProvider")
    void testLessThanMixedValuesEdgeCases(double arg1Double, int arg2Int, boolean expectedResult) {
        when(arg1.computeValue(context)).thenReturn(arg1Double);
        when(arg2.computeValue(context)).thenReturn(arg2Int);
        when(InfoSetUtil.doubleValue(any(NodeInfo.class))).then(invocationOnMock -> invocationOnMock.getArgument(0).doubleValue());

        Object result = lessThan.computeValue(context);
        assertEquals(expectedResult, result);
    }

    private static Stream<Object[]> edgeCasesMixedLessThanProvider() {
        return Stream.of(new Object[] { Double.NEGATIVE_INFINITY, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { Double.NEGATIVE_INFINITY, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Double.MAX_VALUE, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Double.MAX_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { 0.0, 0, Boolean.FALSE },
                new Object[] { 0.0, 1, Boolean.TRUE },
                new Object[] { Double.MIN_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { Double.MIN_VALUE, Integer.MAX_VALUE, Boolean.FALSE },
                new Object[] { Double.MAX_VALUE, Integer.MIN_VALUE, Boolean.TRUE },
                new Object[] { Double.MAX_VALUE, Integer.MIN_VALUE + 1, Boolean.FALSE });
    }
}