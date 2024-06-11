import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperation(new LiteralExpression(5.0)),
                new CoreOperation(new LiteralExpression(3.0))
        });

        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperation(new LiteralExpression(3.0)),
                new CoreOperation(new LiteralExpression(5.0))
        });

        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperation(new LiteralExpression(5.0)),
                new CoreOperation(new LiteralExpression(5.0))
        });

        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperation(new LiteralExpression(3.0)),
                new CoreOperation(new LiteralExpression(3.0))
        });

        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
                new CoreOperation(new LiteralExpression(5.0)),
                new CoreOperation(new CoreOperationRelationalExpression(new CoreOperation[] {
                        new CoreOperation(new LiteralExpression(3.0)),
                        new CoreOperation(new LiteralExpression(4.0))
                }))
        });

        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        expression.computeValue(context);
    }
}