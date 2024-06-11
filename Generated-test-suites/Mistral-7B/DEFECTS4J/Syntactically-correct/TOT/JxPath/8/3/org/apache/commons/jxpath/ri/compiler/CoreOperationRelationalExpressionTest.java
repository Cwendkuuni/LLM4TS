package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathFactory;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIterator;
import org.junit.Test;

/**
 * Test for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b > /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compute(evalCtx);
        Object rightValue = right.compile(evalCtx).compute(evalCtx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { left, right })
                .compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testGreaterThanWithDoubleValues() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b > 3.5");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { JXPathFactory
                .createParseExpression("/a[1]/b"), JXPathFactory.createValueExpression(3.5) })
                .compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testGreaterThanOrEqual() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b >= /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compute(evalCtx);
        Object rightValue = right.compile(evalCtx).compute(evalCtx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { left, right })
                .compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testGreaterThanOrEqualWithDoubleValues() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b >= 3.5");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { JXPathFactory
                .createParseExpression("/a[1]/b"), JXPathFactory.createValueExpression(3.5) })
                .compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testLessThan() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b < /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compute(evalCtx);
        Object rightValue = right.compile(evalCtx).compute(evalCtx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { left, right })
                .compile(evalCtx).compute(evalCtx);

        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testLessThanOrEqual() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b <= /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compute(evalCtx);
        Object rightValue = right.compile(evalCtx).compute(evalCtx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { left, right })
                .compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b = /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compute(evalCtx);
        Object rightValue = right.compile(evalCtx).compute(evalCtx);

        new CoreOperationRelationalExpression(new Expression[] { left, right })
                .compile(evalCtx).compute(evalCtx);
    }

    @Test
    public void testComparisonWithIterators() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a/b[1]/@foo > /a/c[1]/@bar");
        Expression right = JXPathFactory.createParseExpression("/a/c[1]/@bar");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compile(evalCtx);
        Object rightValue = right.compile(evalCtx).compile(evalCtx);

        Object result = new CoreOperationRelationalExpression(new Expression[] { leftValue, rightValue })
                .compile(evalCtx).compile(evalCtx).compute(evalCtx);

        assertTrue(result == Boolean.TRUE);
    }

    @Test(expected = IllegalStateException.class)
    public void testComparisonWithMismatchedTypes() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b > /a[1]/c");
        Expression right = JXPathFactory.createParseExpression("/a[1]/b");
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compile(evalCtx);
        Object rightValue = right.compile(evalCtx).compile(evalCtx);

        new CoreOperationRelationalExpression(new Expression[] { leftValue, new StringExpression("abc") })
                .compile(evalCtx).compile(evalCtx).compute(evalCtx);
    }

    @Test(expected = IllegalStateException.class)
    public void testComparisonWithNullValues() throws Exception {
        Expression left = JXPathFactory.createParseExpression("/a[1]/b > null");
        Expression right = JXPathFactory.createValueExpression(null);
        InfoSet is = createInfoSet();
        JXPathContext ctx = new JXPathContext(is);
        EvalContext evalCtx = new EvalContext(ctx);

        Object leftValue = left.compile(evalCtx).compile(evalCtx);
        Object rightValue = right.compile(evalCtx).compile(evalCtx);

        new CoreOperationRelationalExpression(new Expression[] { leftValue, rightValue })
                .compile(evalCtx).compile(evalCtx).compute(evalCtx);
    }

    private InfoSet createInfoSet() {
        List nodes = new ArrayList();
        nodes.add(createNode("a", 1, Collections.singletonMap("foo", "bar")));
        nodes.add(createNode("a", 1, Collections.singletonMap("foo", "baz")));
        nodes.add(createNode("a", 1, Collections.singletonMap("foo", "qux")));
        nodes.add(createNode("c", 1, Collections.singletonMap("bar", 3.5)));
        nodes.add(createNode("c", 1, Collections.singletonMap("bar", 2.5)));
        nodes.add(createNode("c", 1, Collections.singletonMap("bar", 4.5)));
        InfoSet is = new InfoSet();
        is.setDocument(createDocument(nodes.toArray(new Object[0])));
        is.setIterator(new IterateIterator(is.iterate("/", null, null)));
        return is;
    }

    private Object createNode(String tag, int index, Map attributes) {
        MappedNode node = new MappedNode();
        node.setNodeName(tag);
        node.setNodeIndex(index);
        node.setNodeValue("node" + index);
        node.setAttributes(attributes);
        return node;
    }

    private Document createDocument(Object[] nodes) {
        Document doc = DocumentFactory.newInstance().createDocument();
        doc.setDocumentElement(doc.createElement("root"));
        Element root = (Element) doc.getDocumentElement();
        for (Object node : nodes) {
            root.appendChild(node);
        }
        return doc;
    }
}