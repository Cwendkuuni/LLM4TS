import org.junit.Assert;
import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.jdom2.util.NamespacesStack;

public class NamespaceStackTest {

 private NamespacesStack stack;

 @Test
 public void testNamespaceStackConstructor() {
 stack = new NamespacesStack();
 Assert.assertEquals(0, stack.getDepth());
 Assert.assertArrayEquals(Namespaces.NO_NAMESPACE, stack.getScope());
 Assert.assertArrayEquals(Namespaces.NO_NAMESPACE, stack.getAdded());
 }

 @Test
 public void testNamespaceStackWithInitialNamespaces() {
 stack = new NamespacesStack(new Namespace[] { Namespaces.XML_NAMESPACE, Namespaces.XPATH_NAMESPACE });
 Assert.assertEquals(2, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { Namespaces.XML_NAMESPACE, Namespaces.XPATH_NAMESPACE }, stack.getScope());
 Assert.assertArrayEquals(new Namespace[0], stack.getAdded());
 }

 @Test
 public void testPushElement() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 Namespace mns = element.getNamespace();
 Namespace[] newscope = checkNamespaces(stack.getAdded(), mns, stack.getScope()[stack.getDepth()]);
 Assert.assertEquals(1, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { mns }, stack.getScope());
 Assert.assertArrayEquals(new Namespace[]{ mns }, stack.getAdded());
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 Namespace additionalNamespace = new Namespace("http://example.com/");
 stack.push(element);
 stack.push(additionalNamespace);
 Namespace[] newscope = checkNamespaces(stack.getAdded(), additionalNamespace, stack.getScope()[stack.getDepth()]);
 Assert.assertEquals(2, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { mns, additionalNamespace }, stack.getScope());
 Assert.assertArrayEquals(new Namespace[] { mns, additionalNamespace }, stack.getAdded());
 }

 @Test
 public void testPushAttribute() {
 stack = new NamespacesStack();
 Attribute attribute = new Attribute("attr", "attr", "http://example.com/");
 stack.push(attribute);
 Namespace mns = attribute.getNamespace();
 Namespace[] newscope = checkNamespaces(stack.getAdded(), mns, stack.getScope()[stack.getDepth()]);
 Assert.assertEquals(1, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { mns }, stack.getScope());
 Assert.assertArrayEquals(new Namespace[]{ mns }, stack.getAdded());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack = new NamespacesStack();
 stack.pop();
 }

 @Test
 public void testPop() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(new Namespace("http://example.com/"));
 stack.pop();
 Assert.assertEquals(1, stack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { mns }, stack.getScope());
 Assert.assertArrayEquals(new Namespace[0], stack.getAdded());
 }

 @Test
 public void testGetAddedForward() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(new Namespace("http://example.com/"));
 Assert.assertEquals(1, stack.getDepth());
 Iterable<Namespace> added = stack.addedForward();
 Assert.assertArrayEquals(new Namespace[] { mns, new Namespace("http://example.com/") }, added.toArray(new Namespace[2]));
 }

 @Test
 public void testGetAddedReverse() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(new Namespace("http://example.com/"));
 Assert.assertEquals(1, stack.getDepth());
 Iterable<Namespace> added = stack.addedReverse();
 Assert.assertArrayEquals(new Namespace[] { new Namespace("http://example.com/"), mns }, added.toArray(new Namespace[2]));
 }

 @Test
 public void testIterator() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(new Namespace("http://example.com/"));
 Assert.assertEquals(1, stack.getDepth());
 Iterator<Namespace> iterator = stack.iterator();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(mns, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(new Namespace("http://example.com/"), iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIsInScope() {
 stack = new NamespacesStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(new Namespace("http://example.com/"));
 Assert.assertTrue(stack.isInScope(mns));
 Assert.assertTrue(stack.isInScope(new Namespace("http://example.com/")));
 Assert.assertFalse(stack.isInScope(new Namespace("http://another-example.com/")));
 }

 private Namespace[] checkNamespaces(Namespace[] added, Namespace namespace, Namespace[] scope) {
 // The checkNamespace method is static and final, so we cannot test it directly.
 // Instead, we can mock its behavior by creating a new array with the expected values.
 return new Namespace[] { namespace };
 }

}