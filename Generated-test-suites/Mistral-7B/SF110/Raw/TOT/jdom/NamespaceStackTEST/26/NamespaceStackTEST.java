import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class NamespaceStackTest {

 @Test
 public void testNamespaceStackEmpty() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "test")));
 assertFalse(stack.isInScope(null));
 assertFalse(stack.isInScope(new Namespace("", "test")));
 }

 @Test
 public void testNamespaceStackPushPop() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", new Namespace("test", "test")));
 stack.push(new Attribute("attribute", Namespace.XML_NAMESPACE, "value"));
 stack.pop();
 stack.pop();

 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "test")));
 assertFalse(stack.isInScope(new Namespace("test2", "test2")));
 }

 @Test(expected = IllegalStateException.class)
 public void testNamespaceStackPopEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testNamespaceStackAddMultiple() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespace.XML_NAMESPACE);
 Element element1 = new Element("element1", new Namespace("test1", "test1"));
 Element element2 = new Element("element2", new Namespace("test2", "test2"));
 Attribute attr1 = new Attribute("attr1", Namespace.XML_NAMESPACE, "value1");
 Attribute attr2 = new Attribute("attr2", new Namespace("test1", "test1"), "value2");
 stack.push(root);
 stack.push(element1);
 stack.push(element2);
 stack.push(attr1);
 stack.push(attr2);

 Namespace[] expected = new Namespace[] {
 Namespace.XML_NAMESPACE, new Namespace("test1", "test1"), new Namespace("test2", "test2")
 };

 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testNamespaceStackAddDuplicate() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespace.XML_NAMESPACE);
 Element element = new Element("element", new Namespace("test", "test"));
 stack.push(root);
 stack.push(element);
 stack.push(element);

 Namespace[] expected = new Namespace[] {
 Namespace.XML_NAMESPACE, new Namespace("test", "test")
 };

 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testNamespaceStackGetIterators() throws JDOMException {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespace.XML_NAMESPACE);
 Element element1 = new Element("element1", new Namespace("test1", "test1"));
 Element element2 = new Element("element2", new Namespace("test2", "test2"));
 Attribute attr1 = new Attribute("attr1", Namespace.XML_NAMESPACE, "value1");
 Attribute attr2 = new Attribute("attr2", new Namespace("test1", "test1"), "value2");
 stack.push(root);
 stack.push(element1);
 stack.push(element2);
 stack.push(attr1);
 stack.push(attr2);

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Iterator<Namespace> iterator = stack.iterator();

 assertEquals(4, addedForward.size());
 assertEquals(4, addedReverse.size());
 assertEquals(4, iterator.size());

 Namespace[] expected = new Namespace[] {
 Namespace.XML_NAMESPACE, new Namespace("test1", "test1"), new Namespace("test2", "test2"), new Namespace("test1", "test1")
 };

 for (Namespace namespace : addedForward) {
 assertEquals("test1", namespace.getPrefix());
 }

 for (Namespace namespace : addedReverse) {
 assertEquals("test1", namespace.getPrefix());
 }

 for (Namespace namespace : iterator) {
 assertEquals("test1", namespace.getPrefix());
 }
 }

}