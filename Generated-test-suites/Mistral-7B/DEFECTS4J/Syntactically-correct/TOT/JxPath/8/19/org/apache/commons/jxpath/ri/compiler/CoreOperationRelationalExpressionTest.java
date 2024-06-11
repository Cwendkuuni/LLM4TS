import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Iterateiter;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))});

        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))});

        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testLessThan() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testLessThanOrEqual() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(2.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testIteratorComparison() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);
        Path path = new Path("/test");
        Path expressionPath = new Path("/test/list");
        Iterateiter iter = new Iterateiter(context, path, expressionPath);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new Variable("$v", new NumberValue(1))},
                new Expression[]{new Variable("$w", new NumberValue(2))});
        expr.setContext(context);
        expr.setVariableNamespace(context.getVariableContext());
        expr.setIterateiter(iter);

        expr.bindVariable("$v", new VariableSource(new NumberValue(1), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(2), true));

        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr.bindVariable("$v", new VariableSource(new NumberValue(2), true));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        result = expr.computeValue(context);
        result = iter.next();
        assertNotNull(result);
        assertEquals(3, ((NodeInfo) result).getNode().getNodeValue().intValue());
        assertTrue(result instanceof NodeInfo);
        assertTrue(result instanceof NumberValue);

        expr.bindVariable("$v", new VariableSource(new NumberValue(4), true));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIteratorsEqual() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);
        Path path = new Path("/test");
        Path expressionPath = new Path("/test/list");
        Iterateiter iter1 = new Iterateiter(context, path, expressionPath);
        Iterateiter iter2 = new Iterateiter(context, path, expressionPath);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new Variable("$v", new NumberValue(1))},
                new Expression[]{new Variable("$w", new NumberValue(1))});
        expr.setContext(context);
        expr.setVariableNamespace(context.getVariableContext());
        expr.setIterateiter(iter1);

        expr.bindVariable("$v", new VariableSource(new NumberValue(1), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(1), true));

        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        iter1.next();
        iter2.next();

        expr.bindVariable("$v", new VariableSource(new NumberValue(2), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(2), true));

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(3), true));

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        iter1.next();
        iter2.next();

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(4), true));

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIteratorsDifferentSize() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);
        Path path = new Path("/test");
        Path expressionPath = new Path("/test/list1");
        Iterateiter iter1 = new Iterateiter(context, path, expressionPath);
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        iter1.setSequence(list1.iterator());

        Path expressionPath2 = new Path("/test/list2");
        Iterateiter iter2 = new Iterateiter(context, path, expressionPath2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        iter2.setSequence(list2.iterator());

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new Variable("$v", new NumberValue(1))},
                new Expression[]{new Variable("$w", new NumberValue(1))});
        expr.setContext(context);
        expr.setVariableNamespace(context.getVariableContext());
        expr.setIterateiter(iter1);

        expr.bindVariable("$v", new VariableSource(new NumberValue(1), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(1), true));

        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        iter1.next();
        iter2.next();

        expr.bindVariable("$v", new VariableSource(new NumberValue(2), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(1), true));

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        iter1.next();
        iter2.next();

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(1), true));

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        iter1.next();
        iter2.next();

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(2), true));

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(3), true));

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr.bindVariable("$v", new VariableSource(new NumberValue(3), true));
        expr.bindVariable("$w", new VariableSource(new NumberValue(4), true));

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testNullComparison() throws EvaluationException {
        CompilerContext compilerContext = new CompilerContext();
        JXPathContext context = new JXPathContext(compilerContext, new Node[]{new DummyNode(1)}, null);

        Expression expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(null)});

        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(null)},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))},
                new Expression[]{new CoreOperationLiteral(null)});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(null)},
                new Expression[]{new CoreOperationLiteral(new NumberValue(1.0))});

        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationRelationalExpression(
                new Expression[]{new CoreOperationLiteral(null)},
                new Expression[]{new CoreOperationLiteral(null)});

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
}

class DummyNode implements Node {
    private int value;

    public DummyNode(int value) {
        this.value = value;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public Node getFirstChild() {
        return null;
    }

    @Override
    public Node getLastChild() {
        return null;
    }

    @Override
    public Node getPreviousSibling() {
        return null;
    }

    @Override
    public Node getNextSibling() {
        return null;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return null;
    }

    @Override
    public String getNodeValue() {
        return String.valueOf(value);
    }

    @Override
    public short getNodeType() {
        return 1;
    }

    @Override
    public String getNodeName() {
        return "#text";
    }
}