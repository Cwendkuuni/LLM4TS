import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class ExpressionTest {

 private Expression expression;
 private EvalContext context;
 private Node root;

 @Before
 public void setup() {
 root = createTestDocument().getDocumentElement();
 context = new EvalContext(root, null);
 expression = Mockito.spy(new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 });
 }

 @Test
 public void testIsContextDependent() {
 assertTrue(expression.isContextDependent());
 expression.contextDependencyKnown = true;
 expression.contextDependent = false;
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testIterate() {
 NodeList nodeList = root.getElementsByTagName("test");
 Iterator<Node> nodeIterator = nodeList.iterator();
 expression.iterate(context);
 Node node = nodeIterator.next();
 assertEquals(expression.computeValue(context), node.getNodeValue());
 }

 @Test
 public void testIteratePointers() {
 Node node = root.getElementsByTagName("test").item(0);
 Mockito.when(expression.compute(context)).thenReturn(node);
 Iterator<Pointer> pointerIterator = expression.iteratePointers(context);
 Pointer pointer = pointerIterator.next();
 assertEquals(new QName("test", null, null), pointer.getQName());
 assertEquals(node, pointer.getNode());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratePointersWithNullContext() {
 expression.iteratePointers(null);
 }

 @Test
 public void testComputeValue() {
 Mockito.when(expression.compute(context)).thenReturn(1);
 assertEquals(1, expression.computeValue(context).doubleValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComputeValueWithNullContext() {
 expression.computeValue(null);
 }

 @Test
 public void testCompute() {
 Mockito.when(expression.compute(context)).thenReturn(new Double(1.0));
 assertEquals(1.0, expression.compute(context).doubleValue(), 0.01);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComputeWithNullContext() {
 expression.compute(null);
 }

 private Document createTestDocument() {
 Document document = DocumentBuilderFactory.newDocumentBuilder().newDocument();
 Element rootElement = document.createElement("root");
 rootElement.setAttribute("test", "1");
 document.appendChild(rootElement);
 Element testElement = document.createElement("test");
 testElement.setTextContent("2");
 rootElement.appendChild(testElement);
 return document;
 }
}