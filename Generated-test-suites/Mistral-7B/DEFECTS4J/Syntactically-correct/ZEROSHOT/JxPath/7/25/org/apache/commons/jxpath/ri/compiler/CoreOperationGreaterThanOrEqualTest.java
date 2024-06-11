package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.util.SimpleNamespaceContext;
import org.apache.commons.jxpath.ri.util.TestData;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationGreaterThanOrEqual}.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual expression;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setUp() {
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("a"),
                new LiteralExpression(new Double(2.0), null)
        );
        context = new EvalContext(new SimpleNamespaceContext(), null, new Path(""), null);
        infoSet = TestUtils.createInfoSet(TestData.SIMPLE_XML);
    }

    @Test
    public void testComputeValueTrue() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(1.5));
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueRightSideLiteral() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        Double rightSideLiteral = new Double(2.0);
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("a"),
                new LiteralExpression(rightSideLiteral, null)
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueRightSideVariable() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        context.setVariable("b", new Double(2.0));
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("a"),
                new VariableExpression("b")
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesVariable() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        context.setVariable("b", new Double(2.0));
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("b"),
                new VariableExpression("a")
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesLiterals() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        Double rightSideLiteral = new Double(2.0);
        expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(2.5), null),
                new LiteralExpression(rightSideLiteral, null)
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesLiteralsDifferentType() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Integer(3));
        Double rightSideLiteral = new Double(2.0);
        expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Integer(2), null),
                new LiteralExpression(rightSideLiteral, null)
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesVariablesDifferentType() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Integer(3));
        context.setVariable("b", new Double(2.0));
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("a"),
                new VariableExpression("b")
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesVariablesSameType() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Integer(3));
        context.setVariable("b", new Integer(2));
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("a"),
                new VariableExpression("b")
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesVariablesDifferentNames() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        context.setVariable("b", new Double(2.0));
        expression = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("b"),
                new VariableExpression("a")
        );
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesConstants() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Double(3.0));
        Double rightSideLiteral = new Double(2.0);
        expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(3.5), null),
                rightSideLiteral
        );
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesConstantsSameType() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Integer(3));
        Integer rightSideLiteral = new Integer(2);
        expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Integer(3), null),
                rightSideLiteral
        );
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueBothSidesConstantsDifferentType() {
        context.setInfoSet(infoSet);
        context.setVariable("a", new Integer(3));
        Double rightSideLiteral = new Double(2.0);
        expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Integer(3), null),
                rightSideLiteral
        );
        assertTrue(expression.computeValue(context).booleanValue());
    }
}