import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertArrayEquals(NamespaceStack.EMPTY, stack.getAddedForward().toArray(new Namespace[0]));
 assertArrayEquals(NamespaceStack.EMPTY, stack.getAddedReverse().toArray(new Namespace[0]));
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test
 public void testSimplePushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] expected = {Namespace.XML_NAMESPACE};
 assertEquals(expected.length, stack.getScope().length);
 assertArrayEquals(expected, stack.getScope());
 stack.pop();
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root");
 Element child1 = new Element("child1", "prefix1");
 Element child2 = new Element("child2", "prefix1");
 root.addContent(child1);
 root.addContent(child2);
 stack.push(root);
 List<Namespace> expected = new ArrayList<>();
 expected.add(Namespace.XML_NAMESPACE);
 expected.add(new Namespace("prefix1"));
 assertArrayEquals(expected.toArray(new Namespace[0]), stack.getAddedForward().toArray(new Namespace[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(null);
 }

 @Test
 public void testPushMultipleElementsWithDifferentNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root");
 Element child1 = new Element("child1", "prefix1");
 Element child2 = new Element("child2", "prefix2");
 root.addContent(child1);
 root.addContent(child2);
 stack.push(root);
 List<Namespace> expected = new ArrayList<>();
 expected.add(Namespace.XML_NAMESPACE);
 expected.add(new Namespace("prefix1"));
 expected.add(new Namespace("prefix2"));
 assertArrayEquals(expected.toArray(new Namespace[0]), stack.getAddedForward().toArray(new Namespace[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPushWithNullNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"), null);
 }

 @Test
 public void testPushAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root");
 root.setNamespace(new Namespace("prefix1"));
 root.addNamespaceDeclaration("prefix2");
 stack.push(root);
 List<Namespace> expected = new ArrayList<>();
 expected.add(Namespace.XML_NAMESPACE);
 expected.add(new Namespace("prefix1"));
 expected.add(new Namespace("prefix2"));
 assertArrayEquals(expected.toArray(new Namespace[0]), stack.getAddedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testPushMultipleElementsWithSameNamespaceAndAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root");
 Element child1 = new Element("child1", "prefix1");
 child1.setNamespace(new Namespace("prefix3"));
 child1.addNamespaceDeclaration("prefix2");
 Element child2 = new Element("child2", "prefix1");
 child2.setNamespace(new Namespace("prefix4"));
 child2.addNamespaceDeclaration("prefix3");
 root.addContent(child1);
 root.addContent(child2);
 stack.push(root);
 List<Namespace> expected = new ArrayList<>();
 expected.add(Namespace.XML_NAMESPACE);
 expected.add(new Namespace("prefix1"));
 expected.add(new Namespace("prefix2"));
 expected.add(new Namespace("prefix3"));
 expected.add(new Namespace("prefix4"));
 assertArrayEquals(expected.toArray(new Namespace[0]), stack.getAddedForward().toArray(new Namespace[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenStackIsEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPopMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("child"));
 stack.pop();
 assertFalse(stack.getAddedForward().hasNext());
 assertArrayEquals(new Namespace[0], stack.getAddedForward().toArray(new Namespace[0]));
 stack.pop();
 assertTrue(stack.isEmpty());
 }
}