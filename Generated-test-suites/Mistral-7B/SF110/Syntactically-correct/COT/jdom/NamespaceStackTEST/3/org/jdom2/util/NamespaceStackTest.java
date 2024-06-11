import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testConstructor() {
 stack = new NamespaceStack();
 Assert.assertNotNull(stack);
 }

 @Test
 public void testPushElementWithEmptyStack() {
 stack = new NamespaceStack();
 Element element = new Element("root");
 stack.push(element);
 Namespace[] expected = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushElementWithSingleNamespace() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Namespace[] expected = { new Namespace("http://www.test.com/test") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushElementWithMultipleNamespaces() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test1"));
 stack.push(element);
 Element element2 = new Element("child", new QName("http://www.test.com/test2"));
 stack.push(element2);
 Namespace[] expected = { new Namespace("http://www.test.com/test2"), new Namespace("http://www.test.com/test1") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushElementWhenStackIsFull() {
 stack = new NamespaceStack();
 for (Namespace namespace : Namespaces.getNamespaceTable().values()) {
 stack.push(new Element("test", new QName(namespace.getURI())));
 }
 stack.push(new Element("test", new QName("http://www.test.com/test1000")));
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 stack.pop();
 Namespace[] expected = NamespaceStack.EMPTY;
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testGetScope() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Element element2 = new Element("child", new QName("http://www.test.com/test2"));
 stack.push(element2);
 Namespace[] expected = { new Namespace("http://www.test.com/test2"), new Namespace("http://www.test.com/test") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Element element2 = new Element("child", new QName("http://www.test.com/test2"));
 stack.push(element2);
 Assert.assertTrue(stack.isInScope(new Namespace("http://www.test.com/test2")));
 Assert.assertTrue(stack.isInScope(new Namespace("http://www.test.com/test")));
 Assert.assertFalse(stack.isInScope(new Namespace("http://www.test.com/test3")));
 }

 @Test
 public void testIterator() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Element element2 = new Element("child1", new QName("http://www.test.com/test1"));
 stack.push(element2);
 Element element3 = new Element("child2", new QName("http://www.test.com/test2"));
 stack.push(element3);
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> namespaces = new ArrayList<>();
 while (iterator.hasNext()) {
 namespaces.add(iterator.next());
 }
 Namespace[] expected = { new Namespace("http://www.test.com/test2"), new Namespace("http://www.test.com/test1"), new Namespace("http://www.test.com/test") };
 Assert.assertArrayEquals(expected, namespaces.toArray(new Namespace[0]));
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Element element2 = new Element("child1", new QName("http://www.test.com/test1"));
 stack.push(element2);
 Element element3 = new Element("child2", new QName("http://www.test.com/test2"));
 stack.push(element3);
 Iterable<Namespace> addedForward = stack.addedForward();
 List<Namespace> namespaces = new ArrayList<>();
 for (Namespace namespace : addedForward) {
 namespaces.add(namespace);
 }
 Namespace[] expected = { new Namespace("http://www.test.com/test"), new Namespace("http://www.test.com/test1"), new Namespace("http://www.test.com/test2") };
 Assert.assertArrayEquals(expected, namespaces.toArray(new Namespace[0]));
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://www.test.com/test"));
 stack.push(element);
 Element element2 = new Element("child1", new QName("http://www.test.com/test1"));
 stack.push(element2);
 Element element3 = new Element("child2", new QName("http://www.test.com/test2"));
 stack.push(element3);
 Iterable<Namespace> addedReverse = stack.addedReverse();
 List<Namespace> namespaces = new ArrayList<>();
 for (Namespace namespace : addedReverse) {
 namespaces.add(namespace);
 }
 Namespace[] expected = { new Namespace("http://www.test.com/test2"), new Namespace("http://www.test.com/test1"), new Namespace("http://www.test.com/test") };
 Assert.assertArrayEquals(expected, namespaces.toArray(new Namespace[0]));
 }
}