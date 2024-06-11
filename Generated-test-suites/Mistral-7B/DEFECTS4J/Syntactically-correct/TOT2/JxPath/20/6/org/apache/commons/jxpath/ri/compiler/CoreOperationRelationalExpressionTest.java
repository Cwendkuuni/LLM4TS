import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterationAxis;
import org.apache.commons.jxpath.ri.axes.AxisIterator;
import org.apache.commons.jxpath.ri.axes.SelfAxisIterator;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.apache.commons.jxpath.ri.iter.FilterIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.iter.VariableReferenceIterator;
import org.apache.commons.jxpath.ri.literals.BooleanExpression;
import org.apache.commons.jxpath.ri.literals.NumberExpression;
import org.apache.commons.jxpath.ri.literals.StringExpression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructorWithNull() {
        try {
            new CoreOperationRelationalExpression(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeValueWithNullContext() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(false);
        CoreOperationRelationalExpression expression = new AndExpression(left, right);
        assertFalse(expression.computeValue(null) instanceof Boolean);
    }

    @Test
    public void testDifferentTypes() {
        Expression left = new NumberExpression(1.0);
        Expression right = new StringExpression("true");
        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithBooleanOperands() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(false);
        CoreOperationRelationalExpression expression = new AndExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithStringOperands() {
        Expression left = new StringExpression("true");
        Expression right = new StringExpression("false");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithNumberOperands() {
        Expression left = new NumberExpression(1.0);
        Expression right = new NumberExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithCollectionOperands() {
        Expression left = new VariableReferenceExpression("numbers");
        Expression right = new VariableReferenceExpression("strings");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("numbers", new HashSet<>());
        context.setVariable("strings", new HashSet<>());
        context.getVariables().add(new VariableExpression("numbers"));
        context.getVariables().add(new VariableExpression("strings"));
        context.getVariables().add(new VariableExpression("x"));
        context.getVariables().add(new VariableExpression("y"));
        context.getVariables().add(new VariableExpression("z"));
        context.setVariable("x", new NumberExpression(1.0));
        context.setVariable("y", new NumberExpression(1.0));
        context.setVariable("z", new StringExpression("true"));
        context.getVariables().get(0).setValue(new Integer(1));
        context.getVariables().get(1).setValue(new Integer(1));
        context.getVariables().get(2).setValue(new Boolean(true));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithEmptyCollections() {
        Expression left = new VariableReferenceExpression("numbers");
        Expression right = new VariableReferenceExpression("strings");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("numbers", new HashSet<>());
        context.setVariable("strings", new HashSet<>());
        context.getVariables().add(new VariableExpression("numbers"));
        context.getVariables().add(new VariableExpression("strings"));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithIteratorsOfDifferentSizes() {
        Expression left = new VariableReferenceExpression("iter1");
        Expression right = new VariableReferenceExpression("iter2");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("iter1", new IteratorExpression(new SelfAxisIterator(IterationAxis.SELF)));
        context.setVariable("iter2", new IteratorExpression(new SelfAxisIterator(IterationAxis.SELF)));
        context.getVariables().add(new VariableExpression("iter1"));
        context.getVariables().add(new VariableExpression("iter2"));
        context.getVariables().add(new VariableExpression("x"));
        context.getVariables().add(new VariableExpression("y"));
        context.getVariables().get(0).setValue(new SelfAxisIterator(IterationAxis.SELF));
        context.getVariables().get(1).setValue(new SelfAxisIterator(IterationAxis.SELF));
        context.getVariables().get(2).setValue(new NumberExpression(1));
        context.getVariables().get(3).setValue(new NumberExpression(2));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithIteratorsContainingTheSameElementsInDifferentOrders() {
        Expression left = new VariableReferenceExpression("iter1");
        Expression right = new VariableReferenceExpression("iter2");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("iter1", new IteratorExpression(new SingletonIterator(new NumberExpression(1.0))));
        context.setVariable("iter2", new IteratorExpression(new SingletonIterator(new NumberExpression(1.0))));
        context.getVariables().add(new VariableExpression("iter1"));
        context.getVariables().add(new VariableExpression("iter2"));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithIteratorsContainingNullElements() {
        Expression left = new VariableReferenceExpression("iter1");
        Expression right = new VariableReferenceExpression("iter2");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("iter1", new IteratorExpression(new FilterIterator(new SelfAxisIterator(IterationAxis.SELF), new PredicateExpression(new LiteralExpression(null)))));
        context.setVariable("iter2", new IteratorExpression(new SelfAxisIterator(IterationAxis.SELF)));
        context.getVariables().add(new VariableExpression("iter1"));
        context.getVariables().add(new VariableExpression("iter2"));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithInfiniteIterators() {
        Expression left = new VariableReferenceExpression("iter1");
        Expression right = new VariableReferenceExpression("iter2");
        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        context.setVariable("iter1", new IteratorExpression(new EmptyIterator()));
        context.setVariable("iter2", new IteratorExpression(new SelfAxisIterator(IterationAxis.SELF)));
        context.getVariables().add(new VariableExpression("iter1"));
        context.getVariables().add(new VariableExpression("iter2"));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithNaNValues() {
        Expression left = new NumberExpression(Double.NaN);
        Expression right = new NumberExpression(Double.NaN);
        CoreOperationRelationalExpression expression = new NeExpression(left, right);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testPrecedence() {
        Expression left = new NumberExpression(1.0);
        Expression right = new NumberExpression(2.0);
        Expression op1 = new AddExpression(left, new NumberExpression(1.0));
        Expression op2 = new GtExpression(op1, new NumberExpression(2.0));
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = op2.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSymmetric() {
        Expression left = new NumberExpression(1.0);
        Expression right = new NumberExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        CoreOperationRelationalExpression symmetricExpression = new LtExpression(right, left);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        Object symmetricResult = symmetricExpression.computeValue(context);
        assertFalse(symmetricResult instanceof Boolean);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new FilterIterator(new SelfAxisIterator(IterationAxis.SELF), new PredicateExpression(new LiteralExpression(1.0)));
        Expression value = new NumberExpression(1.0);
        CoreOperationRelationalExpression expression = new ContainsExpression(it, value);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testIntersection() {
        Iterator lit = new SingletonIterator(new NumberExpression(1.0));
        Iterator rit = new SingletonIterator(new NumberExpression(1.0));
        CoreOperationRelationalExpression expression = new IntersectionExpression(lit, rit);
        EvalContext context = new EvalContext();
        context.setNode(InfoSet.EMPTY_NODE);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

}