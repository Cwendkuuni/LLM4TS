import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationScalarExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationScalarExpression.IntegerExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationScalarExpression.StringExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.InitialContext;
import org.apache.commons.jxpath.ri.iter.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Tests for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new IntegerExpression(ExpressionFactory.createVariable("x"));
        Expression right = new IntegerExpression(ExpressionFactory.createVariable("y"));
        Expression operation = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, ">", null, false);

        EvalContext context = new EvalContext(new Object[]{new Integer(5), new Integer(3)});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(5)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.0), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.0)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new IntegerExpression(ExpressionFactory.createVariable("x"));
        Expression right = new IntegerExpression(ExpressionFactory.createVariable("y"));
        Expression operation = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, ">=", null, false);

        EvalContext context = new EvalContext(new Object[]{new Integer(5), new Integer(3)});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(5), new Integer(5)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(5), new Integer(4)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(5)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(3.0), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.0)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        Expression left = new IntegerExpression(ExpressionFactory.createVariable("x"));
        Expression right = new IntegerExpression(ExpressionFactory.createVariable("y"));
        Expression operation = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, "<", null, false);

        EvalContext context = new EvalContext(new Object[]{new Integer(3), new Integer(5)});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Integer(5), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.0), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.0)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new IntegerExpression(ExpressionFactory.createVariable("x"));
        Expression right = new IntegerExpression(ExpressionFactory.createVariable("y"));
        Expression operation = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, "<=", null, false);

        EvalContext context = new EvalContext(new Object[]{new Integer(3), new Integer(5)});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(3), new Integer(4)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(5), new Integer(3)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(3.0), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.1)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.0)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(3.1), new Double(3.2)});
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithIterators() {
        Expression left = ExpressionFactory.createVariable("x");
        Expression right = ExpressionFactory.createVariable("y");
        Expression op = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, ">=", null, false);

        EvalContext context = new EvalContext(new Object[]{
                new InitialContext(Arrays.asList(1, 2, 3).iterator()),
                new InitialContext(Arrays.asList(2, 3, 4).iterator())
        });

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{
                new InitialContext(Arrays.asList(1, 2, 3).iterator()),
                new InitialContext(Arrays.asList(2, 1, 3).iterator())
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new InitialContext(Arrays.asList(1, 2, 3).iterator()),
                new InitialContext(Arrays.asList(1, 2, 3).iterator())
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{
                new InitialContext(Arrays.asList(1, 2, 3).iterator()),
                new InitialContext(Collections.singletonList(4).iterator())
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new InitialContext(Arrays.asList(1, 2, 3).iterator()),
                new SelfContext()
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{
                new SelfContext(),
                new InitialContext(Arrays.asList(1, 2, 3).iterator())
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new SelfContext(),
                new SelfContext()
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithCollection() {
        Expression left = new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new IntegerExpression(Integer.valueOf(1)));
        Expression right = new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new IntegerExpression(Integer.valueOf(1)));
        Expression op = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}, ">", null, false);

        EvalContext context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new InitialContext(Collections.singletonList(1).iterator())),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new InitialContext(Collections.singletonList(1).iterator()))
        });

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new InitialContext(Arrays.asList(1, 2, 3).iterator())),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new InitialContext(Arrays.asList(1, 2, 3).iterator()))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new IntegerExpression(Integer.valueOf(1)))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new IntegerExpression(Integer.valueOf(1))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new IntegerExpression(Integer.valueOf(1)))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new IntegerExpression(Integer.valueOf(2))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new InitialContext(Collections.singletonList("a").iterator())),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new InitialContext(Collections.singletonList("b").iterator()))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new InitialContext(Arrays.asList("a", "b").iterator())),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new InitialContext(Arrays.asList("b", "a").iterator()))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a")))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("b"))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a")))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a"))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a")))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("b"))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("x"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a")))),
                new CoreOperationScalarExpression(ExpressionFactory.createVariable("y"), new CoreOperationScalarExpression(ExpressionFactory.createVariable("z"), new StringExpression(String.valueOf("a"))))
        });

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}