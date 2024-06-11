import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest {

    private EvalContext context;
    private CompilerContext compilerContext;
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        compilerContext = mock(CompilerContext.class);
        context = mock(EvalContext.class);
        coreOperationEqual = new CoreOperationEqual(Mockito.mock(Expression.class), Mockito.mock(Expression.class));
        coreOperationEqual.setCompilerContext(compilerContext);
    }

    @Test
    public void testEqualTrue() {
        when(args[0].computeValue(context)).thenReturn("5");
        when(args[1].computeValue(context)).thenReturn("5");

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualFalse() {
        when(args[0].computeValue(context)).thenReturn("5");
        when(args[1].computeValue(context)).thenReturn("6");

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualNull() {
        when(args[0].computeValue(context)).thenReturn(null);
        when(args[1].computeValue(context)).thenReturn(null);

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualDifferentTypes() {
        when(args[0].computeValue(context)).thenReturn(5);
        when(args[1].computeValue(context)).thenReturn("5");

        try {
            coreOperationEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEqualWithInvalidExpressions() {
        when(args[0].computeValue(context)).thenThrow(new RuntimeException());

        try {
            coreOperationEqual.computeValue(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    private Expression[] args = new Expression[2];

    public void setArgs(Expression arg1, Expression arg2) {
        this.args[0] = arg1;
        this.args[1] = arg2;
        coreOperationEqual.setArgs(args);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}