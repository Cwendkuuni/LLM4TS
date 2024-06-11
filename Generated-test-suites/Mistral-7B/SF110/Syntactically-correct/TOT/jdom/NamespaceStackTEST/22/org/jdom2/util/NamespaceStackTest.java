import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test
 public void testSinglePushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 assertEquals(1, stack.getScope().length);
 assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[0]);
 stack.pop();
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testMultiplePushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XML_NAMESPACE_2));
 assertEquals(2, stack.getScope().length);
 assertEquals(Namespace.XML_NAMESPACE_2, stack.getScope()[1]);
 stack.pop();
 assertEquals(1, stack.getScope().length);
 assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[0]);
 stack.pop();
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE, new Namespace[]{"x", "y"}));
 assertEquals(3, stack.getScope().length);
 assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[0]);
 assertEquals("x", stack.getScope()[1].getPrefix());
 assertEquals("y", stack.getScope()[2].getPrefix());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithInvalidNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", null));
 fail("Should throw an IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testAddIterableToStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE, new Namespace[]{"x", "y"}));
 List<Namespace> namespaces = new ArrayList<>();
 namespaces.add(new Namespace("z"));
 stack.push(new Element("element", Namespace.NO_NAMESPACE, namespaces.toArray(new Namespace[0])));
 assertEquals(4, stack.getScope().length);
 assertEquals("z", stack.getScope()[3].getPrefix());
 }

 @Test
 public void testPushPopMultipleTimesWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 assertEquals(2, stack.getScope().length);
 stack.pop();
 assertEquals(1, stack.getScope().length);
 stack.pop();
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testPushPopMultipleTimesWithDifferentNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("root", Namespace.XML_NAMESPACE_2));
 assertEquals(2, stack.getScope().length);
 stack.pop();
 assertEquals(1, stack.getScope().length);
 stack.pop();
 assertTrue(stack.isEmpty());
 }
}