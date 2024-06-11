import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterExpression() {
        Expression left = new LiteralExpression(new InitialContext(1.0));
        Expression right = new LiteralExpression(new InitialContext(2.0));
        CoreOperationRelationalExpression expr = new GreaterExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterOrEqualExpression() {
        Expression left = new LiteralExpression(new InitialContext(1.0));
        Expression right = new LiteralExpression(new InitialContext(1.0));
        CoreOperationRelationalExpression expr = new GreaterOrEqualExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessExpression() {
        Expression left = new LiteralExpression(new InitialContext(1.0));
        Expression right = new LiteralExpression(new InitialContext(2.0));
        CoreOperationRelationalExpression expr = new LessExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessOrEqualExpression() {
        Expression left = new LiteralExpression(new InitialContext(1.0));
        Expression right = new LiteralExpression(new InitialContext(1.0));
        CoreOperationRelationalExpression expr = new LessOrEqualExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterExpressionWithIterators() {
        Iterator leftIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        Iterator rightIterator = Arrays.asList(2.0, 3.0, 4.0).iterator();
        CoreOperationRelationalExpression expr = new GreaterExpression(new CoreOperation[]{
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        expr.setArguments(new CoreOperation[]{
                new LiteralExpression(new InitialContext(leftIterator)),
                new LiteralExpression(new InitialContext(rightIterator))
        });

        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertFalse(result.equals(Boolean.TRUE));

        while (leftIterator.hasNext()) {
            Object left = leftIterator.next();
            while (rightIterator.hasNext()) {
                Object right = rightIterator.next();
                Left:
                {
                    if (left instanceof InitialContext) {
                        left = ((InitialContext) left).getValue();
                    }
                    if (right instanceof InitialContext) {
                        right = ((InitialContext) right).getValue();
                    }

                    CoreOperationRelationalExpression innerExpr = new GreaterExpression(new CoreOperation[]{
                            new VariableExpression("left"),
                            new VariableExpression("right")
                    });
                    innerExpr.setArguments(new CoreOperation[]{
                            new LiteralExpression(new InitialContext(left)),
                            new LiteralExpression(new InitialContext(right))
                    });
                    Object innerResult = innerExpr.computeValue(new EvalContext(new SelfContext()));

                    if (innerResult.equals(Boolean.TRUE)) {
                        result = innerResult;
                        break Left;
                    }
                }
            }
        }

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterOrEqualExpressionWithIterators() {
        Iterator leftIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        Iterator rightIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        CoreOperationRelationalExpression expr = new GreaterOrEqualExpression(new CoreOperation[]{
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        expr.setArguments(new CoreOperation[]{
                new LiteralExpression(new InitialContext(leftIterator)),
                new LiteralExpression(new InitialContext(rightIterator))
        });

        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);

        while (leftIterator.hasNext()) {
            Object left = leftIterator.next();
            while (rightIterator.hasNext()) {
                Object right = rightIterator.next();
                if (left.equals(right)) {
                    Left:
                    {
                        if (left instanceof InitialContext) {
                            left = ((InitialContext) left).getValue();
                        }
                        if (right instanceof InitialContext) {
                            right = ((InitialContext) right).getValue();
                        }

                        CoreOperationRelationalExpression innerExpr = new GreaterOrEqualExpression(new CoreOperation[]{
                                new VariableExpression("left"),
                                new VariableExpression("right")
                        });
                        innerExpr.setArguments(new CoreOperation[]{
                                new LiteralExpression(new InitialContext(left)),
                                new LiteralExpression(new InitialContext(right))
                        });

                        Object innerResult = innerExpr.computeValue(new EvalContext(new SelfContext()));
                        if (!innerResult.equals(Boolean.TRUE) && !innerResult.equals(Boolean.FALSE)) {
                            result = innerResult;
                            break Left;
                        }
                    }
                }
            }
        }

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessExpressionWithIterators() {
        Iterator leftIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        Iterator rightIterator = Arrays.asList(2.0, 3.0, 4.0).iterator();
        CoreOperationRelationalExpression expr = new LessExpression(new CoreOperation[]{
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        expr.setArguments(new CoreOperation[]{
                new LiteralExpression(new InitialContext(leftIterator)),
                new LiteralExpression(new InitialContext(rightIterator))
        });

        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.FALSE, result);

        while (leftIterator.hasNext()) {
            Object left = leftIterator.next();
            while (rightIterator.hasNext()) {
                Object right = rightIterator.next();
                Left:
                {
                    if (left instanceof InitialContext) {
                        left = ((InitialContext) left).getValue();
                    }
                    if (right instanceof InitialContext) {
                        right = ((InitialContext) right).getValue();
                    }

                    CoreOperationRelationalExpression innerExpr = new LessExpression(new CoreOperation[]{
                            new VariableExpression("left"),
                            new VariableExpression("right")
                    });
                    innerExpr.setArguments(new CoreOperation[]{
                            new LiteralExpression(new InitialContext(left)),
                            new LiteralExpression(new InitialContext(right))
                    });
                    Object innerResult = innerExpr.computeValue(new EvalContext(new SelfContext()));

                    if (innerResult.equals(Boolean.TRUE)) {
                        result = innerResult;
                        break Left;
                    }
                }
            }
        }

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessOrEqualExpressionWithIterators() {
        Iterator leftIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        Iterator rightIterator = Arrays.asList(1.0, 2.0, 3.0).iterator();
        CoreOperationRelationalExpression expr = new LessOrEqualExpression(new CoreOperation[]{
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        expr.setArguments(new CoreOperation[]{
                new LiteralExpression(new InitialContext(leftIterator)),
                new LiteralExpression(new InitialContext(rightIterator))
        });

        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);

        while (leftIterator.hasNext()) {
            Object left = leftIterator.next();
            while (rightIterator.hasNext()) {
                Object right = rightIterator.next();
                if (left.equals(right)) {
                    Left:
                    {
                        if (left instanceof InitialContext) {
                            left = ((InitialContext) left).getValue();
                        }
                        if (right instanceof InitialContext) {
                            right = ((InitialContext) right).getValue();
                        }

                        CoreOperationRelationalExpression innerExpr = new LessOrEqualExpression(new CoreOperation[]{
                                new VariableExpression("left"),
                                new VariableExpression("right")
                        });
                        innerExpr.setArguments(new CoreOperation[]{
                                new LiteralExpression(new InitialContext(left)),
                                new LiteralExpression(new InitialContext(right))
                        });

                        Object innerResult = innerExpr.computeValue(new EvalContext(new SelfContext()));
                        if (innerResult.equals(Boolean.FALSE)) {
                            result = innerResult;
                            break Left;
                        }
                    }
                }
            }
        }

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparingDoubleValues() {
        Expression left = new LiteralExpression(new InitialContext(1.5));
        Expression right = new LiteralExpression(new InitialContext(2.0));
        CoreOperationRelationalExpression expr = new GreaterExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.FALSE, result);

        expr = new GreaterOrEqualExpression(new CoreOperation[]{left, right});
        result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new InitialContext(1.5));
        right = new LiteralExpression(new InitialContext(1.5));
        expr = new GreaterExpression(new CoreOperation[]{left, right});
        result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.FALSE, result);

        expr = new GreaterOrEqualExpression(new CoreOperation[]{left, right});
        result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(new InitialContext(1.5));
        right = new LiteralExpression(new InitialContext(1.4));
        expr = new GreaterExpression(new CoreOperation[]{left, right});
        result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);

        expr = new GreaterOrEqualExpression(new CoreOperation[]{left, right});
        result = expr.computeValue(new EvalContext(new SelfContext()));
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputingValueWithInvalidArguments() {
        Expression left = new LiteralExpression(new InitialContext("InvalidArgument"));
        Expression right = new LiteralExpression(new InitialContext(1.0));
        CoreOperationRelationalExpression expr = new GreaterExpression(new CoreOperation[]{left, right});
        Object result = expr.computeValue(new EvalContext(new SelfContext()));
    }
}