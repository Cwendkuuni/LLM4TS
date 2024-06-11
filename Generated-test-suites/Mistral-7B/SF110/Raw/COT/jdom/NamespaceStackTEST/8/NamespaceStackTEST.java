import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testNamespaceStackConstructor() {
 stack = new NamespaceStack();
 Assert.assertNotNull(stack);
 Assert.assertEquals(0, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test
 public void testNamespaceStackWithSeed() {
 stack = new NamespaceStack(new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE });
 Assert.assertEquals(2, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE }, stack.getScope());
 }

 @Test
 public void testPushElement() {
 stack = new NamespaceStack();
 stack.push(createElement("test", "testPrefix"));
 stack.push(createElement("test2", "testPrefix2"));
 stack.push(createElement("test3", "testPrefix3"));

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix3"), Namespace.createNamespace("testPrefix2"), Namespace.createNamespace("testPrefix") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 stack = new NamespaceStack();
 stack.push(createAttribute("test", "testPrefix", "testAttribute"));
 stack.push(createAttribute("test2", "testPrefix2", "testAttribute2"));
 stack.push(createAttribute("test3", "testPrefix3", "testAttribute3"));

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix3"), Namespace.createNamespace("testPrefix2"), Namespace.createNamespace("testPrefix") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushNullElement() {
 stack = new NamespaceStack();
 Assert.assertNull(stack.push(null));
 Assert.assertEquals(0, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test
 public void testPushNullAttribute() {
 stack = new NamespaceStack();
 Assert.assertNull(stack.push(null));
 Assert.assertEquals(0, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test
 public void testPushSameNamespace() {
 stack = new NamespaceStack();
 Element element = createElement("test", "testPrefix");
 stack.push(element);
 stack.push(element);

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushDifferentNamespacesForElement() {
 stack = new NamespaceStack();
 Element element1 = createElement("test1", "testPrefix1");
 Element element2 = createElement("test2", "testPrefix2");
 stack.push(element1);
 stack.push(element2);

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix2"), Namespace.createNamespace("testPrefix1") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushDifferentNamespacesForAttributes() {
 stack = new NamespaceStack();
 Attribute attribute1 = createAttribute("test1", "testPrefix1", "testAttribute1");
 Attribute attribute2 = createAttribute("test2", "testPrefix2", "testAttribute2");
 stack.push(attribute1);
 stack.push(attribute2);

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix2"), Namespace.createNamespace("testPrefix1") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithEmptyList() {
 stack = new NamespaceStack();
 Element element = createElement("test", "testPrefix");
 List<Namespace> toadd = new ArrayList<>();
 stack.pushStack(NamespaceStack.createNamespace("testPrefix"), toadd.toArray(new Namespace[0]), toadd);

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithNonEmptyList() {
 stack = new NamespaceStack();
 Element element = createElement("test", "testPrefix");
 List<Namespace> toadd = new ArrayList<>();
 Namespace namespace1 = NamespaceStack.createNamespace("testPrefix1");
 toadd.add(namespace1);
 stack.pushStack(NamespaceStack.createNamespace("testPrefix"), toadd.toArray(new Namespace[0]), toadd);

 Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.createNamespace("testPrefix1"), Namespace.createNamespace("testPrefix") };
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 Element element = createElement("test", "testPrefix");
 stack.push(element);
 stack.pop();

 Assert.assertEquals(0, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 stack.push(createElement("test", "testPrefix1"));
 stack.push(createElement("test2", "testPrefix2"));
 stack.push(createElement("test3", "testPrefix3"));

 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.createNamespace("testPrefix3")));
 assertTrue(stack.isInScope(Namespace.createNamespace("testPrefix2")));
 assertTrue(stack.isInScope(Namespace.createNamespace("testPrefix1")));
 assertFalse(stack.isInScope(Namespace.createNamespace("testPrefix4")));
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 stack.push(createElement("test", "testPrefix"));
 stack.push(createElement("test2", "testPrefix2"));
 stack.push(createElement("test3", "testPrefix3"));

 List<Namespace> actualNamespaces = new ArrayList<>();
 for (Namespace namespace : stack.addedForward()) {
 actualNamespaces.add(namespace);
 }

 List<Namespace> expectedNamespaces = new ArrayList<>();
 expectedNamespaces.add(Namespace.createNamespace("testPrefix3"));
 expectedNamespaces.add(Namespace.createNamespace("testPrefix2"));
 expectedNamespaces.add(Namespace.createNamespace("testPrefix"));
 Assert.assertEquals(expectedNamespaces, actualNamespaces);
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 stack.push(createElement("test", "testPrefix"));
 stack.push(createElement("test2", "testPrefix2"));
 stack.push(createElement("test3", "testPrefix3"));

 List<Namespace> actualNamespaces = new ArrayList<>();
 for (Namespace namespace : stack.addedReverse()) {
 actualNamespaces.add(namespace);
 }

 List<Namespace> expectedNamespaces = new ArrayList<>();
 expectedNamespaces.add(Namespace.createNamespace("testPrefix"));
 expectedNamespaces.add(Namespace.createNamespace("testPrefix2"));
 expectedNamespaces.add(Namespace.createNamespace("testPrefix3"));
 Assert.assertEquals(expectedNamespaces, actualNamespaces);
 }

 private static Element createElement(String name, String prefix) {
 return new DefaultElement(name, Namespaces.getNamespace(prefix), null);
 }

 private static Attribute createAttribute(String name, String prefix, String value) {
 return new AttributeImpl(name, Namespace.getNamespace(prefix), value, null);
 }
}