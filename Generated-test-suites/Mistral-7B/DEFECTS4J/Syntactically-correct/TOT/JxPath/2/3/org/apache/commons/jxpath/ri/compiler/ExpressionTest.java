import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for org.apache.commons.jxpath.ri.compiler.Expression.
 */
public class ExpressionTest {

    private static final String XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<document>\n" +
            "<root>\n" +
            "<element1 xmlns:x='http://example.com/namespace1'>\n" +
            "<subElement1>Value1</subElement1>\n" +
            "</element1>\n" +
            "<element2 xmlns:x='http://example.com/namespace1'>\n" +
            "<subElement2>Value2</subElement2>\n" +
            "</element2>\n" +
            "</root>\n" +
            "</document>";

    @Test
    public void testContextDependentExpression() {
        Expression expression = new ContextDependentExpression();
        Document document = JAXPHelper.parseStringToDocument(XML_STRING);
        EvalContext context = new EvalContext(document.getDocumentElement());

        Assert.assertTrue(expression.isContextDependent());

        Object result1 = expression.computeValue(context);
        Assert.assertNotNull(result1);

        NodePointer currentNode = context.getCurrentNodePointer();
        Element element1 = (Element) currentNode.getTarget();
        context.setCurrentNodePointer(element1.getElementsByTagName("subElement1").item(0).getParentElement());

        Object result2 = expression.computeValue(context);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testContextIndependentExpression() {
        Expression expression = new ContextIndependentExpression();
        Document document = JAXPHelper.parseStringToDocument(XML_STRING);
        EvalContext context = new EvalContext(document.getDocumentElement());

        Assert.assertFalse(expression.isContextDependent());

        Object result1 = expression.computeValue(context);
        Assert.assertNotNull(result1);

        NodePointer currentNode = context.getCurrentNodePointer();
        Element element1 = (Element) currentNode.getTarget();
        context.setCurrentNodePointer(element1.getElementsByTagName("subElement2").item(0).getParentElement());

        Object result2 = expression.computeValue(context);
        Assert.assertSame(result1, result2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratePointerError() {
        Expression expression = new Expression();
        Document document = JAXPHelper.parseStringToDocument(XML_STRING);
        EvalContext context = new EvalContext(document.getDocumentElement());

        Iterator iterator = expression.iteratePointers(context);
        iterator.next();

        iterator.remove();
    }

    @Test
    public void testIterateValue() {
        Expression expression = new ContextIndependentExpression();
        Document document = JAXPHelper.parseStringToDocument(XML_STRING);
        EvalContext context = new EvalContext(document.getDocumentElement());

        List list = Lists.newArrayList(expression.iterate(context));
        Assert.assertEquals(3, list.size());

        Object first = list.get(0);
        Assert.assertNotNull(first);

        Object second = list.get(1);
        Assert.assertNotNull(second);

        Object third = list.get(2);
        Assert.assertNull(third);
    }

    @Test
    public void testIteratePointer() {
        Expression expression = new ContextDependentExpression();
        Document document = JAXPHelper.parseStringToDocument(XML_STRING);
        EvalContext context = new EvalContext(document.getDocumentElement());

        Iterator iterator = expression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());

        NodePointer currentNode = context.getCurrentNodePointer();
        context.setCurrentNodePointer(currentNode.getNextSibling());

        iterator = expression.iteratePointers(context);
        Assert.assertTrue(iterator.hasNext());

        Pointer nextPointer = (Pointer) iterator.next();
        Assert.assertNotNull(nextPointer);
        Assert.assertNotNull(nextPointer.getValue());
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer().getNodeName();
        }
    }

    private static class ContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ValueUtils.stringValueOf(context.getCurrentNodePointer().getNodeName(), Locale.ENGLISH);
        }
    }
}