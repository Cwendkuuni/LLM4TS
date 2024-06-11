import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<a>1</a>\n" +
         "<b>2</b>\n" +
         "<c>\n" +
         "<d>3</d>\n" +
         "</c>\n" +
         "</root>";

 @Test
 public void testContextDependentExpression() {
 String xpath = "count(./a)";
 Expression expression = new LiteralExpression(1);
 expression.bindVariable("count", expression);
 expression.setContextDependent(true);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);

 Object value = expression.computeValue(context);
 assertEquals(2, value);

 context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());
 value = expression.computeValue(context);
 assertEquals(1, value);
 }

 @Test
 public void testContextIndependentExpression() {
 String xpath = "count(.)";
 Expression expression = new LiteralExpression(3);
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);

 Object value = expression.computeValue(context);
 assertEquals(5, value);

 context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());
 value = expression.computeValue(context);
 assertEquals(5, value);
 }

 @Test
 public void testIterate() {
 String xpath = "./a/../*";
 Expression expression = new AxisStepExpression(AxisType.PARENT, new QName(""), new QName(""));
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Iterator iterator = expression.iterate(context);
 assertFalse(iterator.hasNext());

 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));
 iterator = expression.iterate(context);
 assertSame(document.getDocumentElement(), iterator.next());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratePointers() {
 String xpath = "count(./a/../*)";
 Expression expression = new AxisStepExpression(AxisType.PARENT, new QName(""), new QName(""));
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Iterator iterator = expression.iteratePointers(context);
 assertNull(iterator.next());

 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));
 iterator = expression.iteratePointers(context);
 assertSame(document.getDocumentElement().getElementsByTagName("root")
         .item(0), iterator.next());
 assertNull(iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromValueIterator() {
 String xpath = "./a";
 Expression expression = new AxisStepExpression(AxisType.CHILD, new QName(""), new QName("a"));
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Iterator iterator = expression.iterate(context);
 iterator.next();
 iterator.remove();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromPointerIterator() {
 String xpath = "./a";
 Expression expression = new AxisStepExpression(AxisType.CHILD, new QName(""), new QName("a"));
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Iterator iterator = expression.iteratePointers(context);
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testComputeValue() {
 String xpath = "./a/text()";
 Expression expression = new TextExpression();
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));

 Object value = expression.computeValue(context);
 assertEquals("1", value.toString());
 }

 @Test
 public void testCompute() {
 String xpath = "./a/text()";
 Expression expression = new TextExpression();
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));

 Object value = expression.compute(context);
 assertEquals("1", value.toString());
 }

 @Test
 public void testEvaluateExpressionWithContext() {
 String xpath = "./a/text()";
 Expression expression = new TextExpression();
 expression.setContextDependent(false);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));

 Object value = expression.evaluate(context);
 assertEquals("1", value.toString());
 }

 @Test
 public void testEvaluateExpressionWithContextDependent() {
 String xpath = "./count(./a)";
 Expression expression = new CountExpression();
 expression.setContextDependent(true);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Object value = expression.evaluate(context);
 assertEquals(2, value);

 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));
 value = expression.evaluate(context);
 assertEquals(1, value);
 }

 @Test
 public void testEvaluateExpressionWithContextDependentAndNonContextDependent() {
 String xpath = "./count(./a) + 1";
 Expression expression1 = new CountExpression();
 expression1.setContextDependent(true);
 Expression expression2 = new LiteralExpression(1);
 expression2.setContextDependent(false);
 expression1.add(expression2);

 Document document = JXPathContext.newDocument(XML);
 EvaluationContext context = new EvaluationContext(document);
 context.setCurrentNode(document.getDocumentElement());

 Object value = expression1.evaluate(context);
 assertEquals(3, value);

 context.setCurrentNode(document.getDocumentElement().getElementsByTagName("a").item(0));
 value = expression1.evaluate(context);
 assertEquals(2, value);
 }
}