import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testEmptyStack() {
 stack = new NamespaceStack();
 assertEquals(0, stack.size());
 assertFalse(stack.hasNextNamespace());
 }

 @Test
 public void testBasicPushAndPop() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 stack.pop();
 assertEquals(1, stack.size());
 assertEquals(Namespace.XML_NAMESPACE, stack.getCurrentNamespace());
 }

 @Test
 public void testMultiplePushAndPop() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.NO_NAMESPACE));
 stack.push(new Element("element3", Namespace.XML_NAMESPACE));
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 assertEquals(0, stack.size());
 }

 @Test
 public void testPushAndPopSameNamespace() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.pop();
 stack.push(new Element("element2", Namespace.XML_NAMESPACE));
 assertEquals(1, stack.size());
 assertEquals(Namespace.XML_NAMESPACE, stack.getCurrentNamespace());
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativePop() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 }

 @Test
 public void testPushWithMultipleNamespaces() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.NO_NAMESPACE));
 stack.push(new Element("element3", Namespace.XML_NAMESPACE));
 stack.push(new Element("element4", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr", null, Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr2", null, Namespace.NO_NAMESPACE));
 stack.push(new Attribute("attr3", null, Namespace.XML_NAMESPACE));
 assertEquals(7, stack.size());
 }

 @Test
 public void testPushWithDuplicateNamespaces() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.XML_NAMESPACE));
 stack.push(new Element("element3", Namespace.XML_NAMESPACE));
 stack.push(new Element("element4", Namespace.XML_NAMESPACE));
 stack.push(new Element("element5", Namespace.XML_NAMESPACE));
 assertEquals(5, stack.size());
 }

 @Test
 public void testAdditionalNamespaces() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 Element element2 = new Element("element2", Namespace.NO_NAMESPACE);
 element2.setAttribute(new Attribute("attr1", "value1", Namespace.NO_NAMESPACE));
 stack.push(element2);
 stack.push(new Element("element3", Namespace.NO_NAMESPACE));
 stack.push(new Element("element4", Namespace.XML_NAMESPACE));
 stack.push(new Element("element5", Namespace.XML_NAMESPACE));
 assertEquals(5, stack.size());
 assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE }, stack.getScope());
 }

 @Test
 public void testRemoveDuplicateNamespaces() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.XML_NAMESPACE));
 stack.push(new Element("element3", Namespace.XML_NAMESPACE));
 stack.push(new Element("element4", Namespace.XML_NAMESPACE));
 stack.push(new Element("element5", Namespace.XML_NAMESPACE));
 stack.pop();
 stack.pop();
 assertEquals(3, stack.size());
 assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE }, stack.getScope());
 }
}