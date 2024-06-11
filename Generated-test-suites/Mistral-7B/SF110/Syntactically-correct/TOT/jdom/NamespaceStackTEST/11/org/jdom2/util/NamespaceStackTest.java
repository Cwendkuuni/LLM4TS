import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack namespaceStack = new NamespaceStack();
 assertTrue(namespaceStack.isEmpty());
 assertNull(namespaceStack.getScope());
 assertNull(namespaceStack.iterator().next());
 }

 @Test
 public void testPushAndPopElement() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element = new Element("testElement");
 Namespace mns = Namespace.getXmlNamespace();
 namespaceStack.push(element);
 Namespace[] scope = namespaceStack.getScope();
 Namespace expected = mns;
 assertEquals(1, scope.length);
 assertEquals(expected, scope[0]);
 namespaceStack.pop();
 assertEquals(0, namespaceStack.depth);
 assertNull(namespaceStack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesElement() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element = new Element("testElement");
 Namespace mns1 = Namespace.getXmlNamespace();
 Namespace mns2 = new Namespace("test", "http://test.com");
 namespaceStack.push(element);
 namespaceStack.push(mns2);
 Namespace[] scope = namespaceStack.getScope();
 Namespace[] expected = {mns2, mns1};
 assertEquals(expected.length, scope.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], scope[i]);
 }
 namespaceStack.pop();
 namespaceStack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 }

 @Test
 public void testPushAndPopAttribute() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Attribute attribute = new Attribute("testAttribute", "testValue");
 Namespace mns = Namespace.getXmlNamespace();
 namespaceStack.push(attribute);
 Namespace[] scope = namespaceStack.getScope();
 Namespace expected = mns;
 assertEquals(1, scope.length);
 assertEquals(expected, scope[0]);
 namespaceStack.pop();
 assertEquals(0, namespaceStack.depth);
 assertNull(namespaceStack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesAttribute() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Attribute attribute = new Attribute("testAttribute", "testValue");
 Namespace mns1 = Namespace.getXmlNamespace();
 Namespace mns2 = new Namespace("test", "http://test.com");
 namespaceStack.push(attribute);
 namespaceStack.push(mns2);
 Namespace[] scope = namespaceStack.getScope();
 Namespace[] expected = {mns2, mns1};
 assertEquals(expected.length, scope.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], scope[i]);
 }
 namespaceStack.pop();
 namespaceStack.pop();
 }

 @Test
 public void testPushAndPopMultipleElements() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element1 = new Element("testElement1");
 Element element2 = new Element("testElement2");
 Namespace mns = Namespace.getXmlNamespace();
 namespaceStack.push(element1);
 namespaceStack.push(element2);
 namespaceStack.push(mns);
 Namespace[] scope = namespaceStack.getScope();
 Namespace[] expected = {mns, mns, element2.getNamespace(), element1.getNamespace()};
 assertEquals(expected.length, scope.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], scope[i]);
 }
 namespaceStack.pop();
 namespaceStack.pop();
 namespaceStack.pop();
 }

 @Test
 public void testIsInScope() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element = new Element("testElement");
 Namespace mns = Namespace.getXmlNamespace();
 namespaceStack.push(element);
 assertFalse(namespaceStack.isInScope(null));
 assertFalse(namespaceStack.isInScope(new Namespace("test", "http://test.com")));
 assertTrue(namespaceStack.isInScope(mns));
 assertTrue(namespaceStack.isInScope(element.getNamespace()));
 }

 @Test
 public void testAddedForward() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element = new Element("testElement");
 Namespace mns = Namespace.getXmlNamespace();
 Namespace mns2 = new Namespace("test", "http://test.com");
 namespaceStack.push(element);
 namespaceStack.push(mns2);
 namespaceStack.push(mns);
 assertFalse(namespaceStack.addedForward().hasNext());
 namespaceStack.push(new Namespace("test3", "http://test3.com"));
 Iterator<Namespace> iterator = namespaceStack.addedForward();
 assertTrue(iterator.hasNext());
 Namespace namespace = iterator.next();
 assertEquals(mns.getPrefix(), namespace.getPrefix());
 iterator.hasNext();
 assertEquals(mns2.getPrefix(), iterator.next().getPrefix());
 }

 @Test
 public void testAddedReverse() {
 NamespaceStack namespaceStack = new NamespaceStack();
 Element element = new Element("testElement");
 Namespace mns = Namespace.getXmlNamespace();
 Namespace mns2 = new Namespace("test", "http://test.com");
 namespaceStack.push(element);
 namespaceStack.push(mns2);
 namespaceStack.push(mns);
 assertFalse(namespaceStack.addedReverse().hasNext());
 namespaceStack.push(new Namespace("test3", "http://test3.com"));
 Iterator<Namespace> iterator = namespaceStack.addedReverse();
 assertTrue(iterator.hasNext());
 Namespace namespace = iterator.next();
 assertEquals(mns.getPrefix(), namespace.getPrefix());
 iterator.hasNext();
 assertEquals(mns2.getPrefix(), iterator.next().getPrefix());
 }
}