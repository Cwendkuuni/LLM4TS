import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testEmptyStack() {
 stack = new NamespaceStack();
 assertNull(stack.getScope());
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushSingleElement() {
 stack = new NamespaceStack();
 stack.push(new Element("root", null));
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 Element child1 = new Element("child1", null);
 Element child2 = new Element("child2", null);
 root.addContent(child1);
 root.addContent(child2);
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("root", ""), scope[2]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("root", "")));
 }

 @Test
 public void testPushMultipleElementsWithDifferentNamespaces() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 Element child1 = new Element("child1", new Namespace("test1", "test1"));
 Element child2 = new Element("child2", new Namespace("test2", "test2"));
 root.addContent(child1);
 root.addContent(child2);
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("test1", "test1"), scope[2]);
 assertEquals(new Namespace("test2", "test2"), scope[3]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("test1", "test1")));
 assertTrue(stack.isInScope(new Namespace("test2", "test2")));
 }

 @Test
 public void testPushMultipleAttributesWithSameNamespace() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 root.setAttribute("attr1", new Attribute("attr1", null, new Namespace("test", "test")));
 root.setAttribute("attr2", new Attribute("attr2", null, new Namespace("test", "test")));
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("test", "test"), scope[2]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushMultipleAttributesWithDifferentNamespaces() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 root.setAttribute("attr1", new Attribute("attr1", null, new Namespace("test1", "test1")));
 root.setAttribute("attr2", new Attribute("attr2", null, new Namespace("test2", "test2")));
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("test1", "test1"), scope[2]);
 assertEquals(new Namespace("test2", "test2"), scope[3]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("test1", "test1")));
 assertTrue(stack.isInScope(new Namespace("test2", "test2")));
 }

 @Test(expected = IllegalStateException.class)
 public void testOverPop() {
 stack = new NamespaceStack();
 stack.pop();
 stack.pop();
 }

 @Test
 public void testPushAndPopSameNamespaceMultipleTimes() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 Namespace namespace = new Namespace("test", "test");
 stack.push(root);
 stack.push(new Element("child", namespace));
 stack.pop();
 stack.push(new Element("grandchild", namespace));
 stack.pop();
 stack.push(new Element("great-grandchild", namespace));
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("test", "test"), scope[2]);
 assertEquals(new Namespace("test", "test"), scope[3]);
 assertEquals(new Namespace("test", "test"), scope[4]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushAndPopDifferentNamespacesMultipleTimes() {
 stack = new NamespaceStack();
 Element root = new Element("root", null);
 Namespace namespace1 = new Namespace("test1", "test1");
 Namespace namespace2 = new Namespace("test2", "test2");
 stack.push(root);
 stack.push(new Element("child1", namespace1));
 stack.push(new Element("child2", namespace2));
 stack.pop();
 stack.push(new Element("grandchild1", namespace1));
 stack.pop();
 stack.push(new Element("grandchild2", namespace2));
 Namespace[] scope = stack.getScope();
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE, scope[1]);
 assertEquals(new Namespace("test1", "test1"), scope[2]);
 assertEquals(new Namespace("test2", "test2"), scope[3]);
 assertEquals(new Namespace("test1", "test1"), scope[4]);
 assertEquals(new Namespace("test2", "test2"), scope[5]);
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(new Namespace("test1", "test1")));
 assertTrue(stack.isInScope(new Namespace("test2", "test2")));
 }

}