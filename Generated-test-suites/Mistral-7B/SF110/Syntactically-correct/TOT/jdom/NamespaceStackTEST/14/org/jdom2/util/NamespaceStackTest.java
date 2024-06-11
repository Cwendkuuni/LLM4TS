import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 }

 @Test
 public void testPushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("b", "2", new Namespace("test", "http://test.com")));
 stack.pop();
 stack.pop();
 stack.pop();

 assertEquals(0, stack.getScope().length);
 assertNull(stack.getScope());
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1");
 stack.push(element1);
 stack.push(new Element("child2"));
 stack.push(element1);

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPushWithAdditionalNamespaces() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test", "http://test.com");
 stack.push(element1);
 stack.push(new Element("child2"));
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("b", "2", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("c", "3", new Namespace("test2", "http://test2.com")));

 Namespace[] expected = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com"), new Namespace("test2", "http://test2.com") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushWithAdditionalNamespacesSamePrefix() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test", "http://test.com");
 stack.push(element1);
 stack.push(new Element("child2"));
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("b", "2", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("c", "3", new Namespace("test", "http://test.com")));

 Namespace[] expected = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushWithSameNamespaceTwice() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test", "http://test.com");
 stack.push(element1);
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(element1);

 Namespace[] expected = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushWithDifferentNamespaces() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test1", "http://test1.com");
 stack.push(element1);
 Element element2 = new Element("child2", "test2", "http://test2.com");
 stack.push(element2);
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("b", "2", Namespace.XML_NAMESPACE));

 Namespace[] expected = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test1", "http://test1.com"), new Namespace("test2", "http://test2.com") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyScopeIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 Namespace xmlNamespace = Namespace.XML_NAMESPACE;
 Namespace ns = new Namespace("test", "http://test.com");
 assertTrue(stack.isInScope(xmlNamespace));
 assertTrue(stack.isInScope(ns));
 }

 @Test
 public void testNamespaceInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test", "http://test.com");
 stack.push(element1);
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("b", "2", new Namespace("test2", "http://test2.com")));

 Namespace xmlNamespace = Namespace.XML_NAMESPACE;
 Namespace testNamespace = new Namespace("test", "http://test.com");
 Namespace test2Namespace = new Namespace("test2", "http://test2.com");

 assertTrue(stack.isInScope(xmlNamespace));
 assertTrue(stack.isInScope(testNamespace));
 assertFalse(stack.isInScope(test2Namespace));
 }

 @Test
 public void testNamespaceNotInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Element element1 = new Element("child1", "test", "http://test.com");
 stack.push(element1);
 stack.push(new Attribute("a", "1", Namespace.XML_NAMESPACE));

 Namespace xmlNamespace = Namespace.XML_NAMESPACE;
 Namespace testNamespace = new Namespace("test", "http://test.com");
 Namespace test2Namespace = new Namespace("test2", "http://test2.com");

 assertTrue(stack.isInScope(xmlNamespace));
 assertTrue(stack.isInScope(testNamespace));
 assertFalse(stack.isInScope(test2Namespace));
 }
}