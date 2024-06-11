import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;

 @Test
 public void testConstructor() {
 namespaceStack = new NamespaceStack();
 Assert.assertArrayEquals(Namespaces.EMPTY_ARRAY, namespaceStack.getScope());
 Assert.assertNotNull(namespaceStack.iterator());
 }

 @Test
 public void testPushElementWithEmptyScope() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("", "root") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushElementWithNonEmptyStackAndSameNamespace() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 namespaceStack.push(element);
 }

 @Test
 public void testPushElementWithNonEmptyStackAndDifferentNamespaces() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace ns = Namespace.getNamespace("ns1", "ns1");
 namespaceStack.push(ns);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("ns1", "ns1"), Namespace.getNamespace("", "root") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test
 public void testPushAttributeWithEmptyStack() {
 namespaceStack = new NamespaceStack();
 Attribute attribute = new Attribute("a", "a", "ns");
 namespaceStack.push(attribute);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("ns", "a") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushAttributeWithNonEmptyStackAndSameNamespace() {
 namespaceStack = new NamespaceStack();
 Attribute attribute = new Attribute("a", "a", "ns");
 namespaceStack.push(attribute);
 Element element = new Element("root");
 namespaceStack.push(element);
 }

 @Test
 public void testPushAttributeWithNonEmptyStackAndDifferentNamespaces() {
 namespaceStack = new NamespaceStack();
 Attribute attribute = new Attribute("a", "a", "ns1");
 namespaceStack.push(attribute);
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace ns = Namespace.getNamespace("ns2", "ns2");
 namespaceStack.push(ns);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("ns2", "ns2"), Namespace.getNamespace("ns1", "a"), Namespace.getNamespace("", "root") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test
 public void testPushMultipleAttributesWithSameNamespace() {
 namespaceStack = new NamespaceStack();
 Attribute attribute1 = new Attribute("a1", "a1", "ns");
 Attribute attribute2 = new Attribute("a2", "a2", "ns");
 namespaceStack.push(attribute1);
 namespaceStack.push(attribute2);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("ns", "a1"), Namespace.getNamespace("ns", "a2"), Namespace.getNamespace("", "") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test
 public void testPushMultipleAttributesWithDifferentNamespaces() {
 namespaceStack = new NamespaceStack();
 Attribute attribute1 = new Attribute("a1", "a1", "ns1");
 Attribute attribute2 = new Attribute("a2", "a2", "ns2");
 namespaceStack.push(attribute1);
 namespaceStack.push(attribute2);
 Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("ns1", "a1"), Namespace.getNamespace("ns2", "a2"), Namespace.getNamespace("", "") };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test
 public void testPop() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace[] expectedScope = { Namespace.XML_NAMESPACE };
 namespaceStack.pop();
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWithEmptyStack() {
 namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 namespaceStack = new NamespaceStack();
 Element element1 = new Element("root1");
 Attribute attribute1 = new Attribute("a1", "a1", "ns1");
 namespaceStack.push(element1);
 namespaceStack.push(attribute1);
 Element element2 = new Element("root2");
 Attribute attribute2 = new Attribute("a2", "a2", "ns2");
 namespaceStack.push(element2);
 namespaceStack.push(attribute2);
 Namespace[] expectedScope = { Namespace.getNamespace("ns2", "a2"), Namespace.getNamespace("ns1", "a1"), Namespace.getNamespace("", "root2"), Namespace.getNamespace("", "root1") };
 namespaceStack.pop();
 Namespace[] expectedScope2 = { Namespace.getNamespace("ns1", "a1"), Namespace.XML_NAMESPACE };
 Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
 Assert.assertArrayEquals(expectedScope2, namespaceStack.getScope());
 }

 @Test
 public void testIterator() {
 namespaceStack = new NamespaceStack();
 Element element1 = new Element("root1");
 Attribute attribute1 = new Attribute("a1", "a1", "ns1");
 namespaceStack.push(element1);
 namespaceStack.push(attribute1);
 Element element2 = new Element("root2");
 Attribute attribute2 = new Attribute("a2", "a2", "ns2");
 namespaceStack.push(element2);
 namespaceStack.push(attribute2);
 Iterator<Namespace> iterator = namespaceStack.iterator();
 Namespace[] expectedScope = { Namespace.getNamespace("ns2", "a2"), Namespace.getNamespace("ns1", "a1"), Namespace.getNamespace("", "root2"), Namespace.getNamespace("", "root1") };
 Namespace[] namespaceArray = new Namespace[iterator.size()];
 int i = 0;
 while (iterator.hasNext()) {
 namespaceArray[i++] = iterator.next();
 }
 Assert.assertArrayEquals(expectedScope, namespaceArray);
 }

 @Test
 public void testAddedForward() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace[] expectedAdded = { Namespace.getNamespace("", "root") };
 Assert.assertArrayEquals(expectedAdded, namespaceStack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testAddedReverse() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace[] expectedAdded = { Namespace.getNamespace("", "root") };
 Assert.assertArrayEquals(expectedAdded, namespaceStack.addedReverse().toArray(new Namespace[0]));
 }

 @Test
 public void testIsInScope() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root");
 namespaceStack.push(element);
 Namespace ns = Namespace.getNamespace("ns", "ns");
 Assert.assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
 Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
 Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("", "root")));
 Assert.assertFalse(namespaceStack.isInScope(Namespace.getNamespace("ns", "ns")));
 }
}