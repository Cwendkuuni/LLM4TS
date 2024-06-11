import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.includelib.SaxonHelper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(1.5, "a"), null));
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.0, "b"), null));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(1.5, "a"), null));
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.5, "b"), null));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testEqual() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.0, "a"), null));
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.0, "b"), null));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedTypes() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(1.5, "a"), null));
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode("b[1]", context), null)); // String

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        expression.computeValue(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullInputs() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(null);
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.0, "b"), null));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        expression.computeValue(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullRightInput() {
        EvaluationContext context = new EvaluationContext();
        context.setNodeInfo(new NodeInfo(SaxonHelper.newNode(2.0, "a"), null));
        context.setNodeInfo(null);

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                SaxonHelper.newExpression("a[1]", context),
                SaxonHelper.newExpression("b[1]", context));

        expression.computeValue(context);
    }
}