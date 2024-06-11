import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;
import org.junit.Assert.*;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testEmptyStack() {
 NamespaceStack emptyStack = new NamespaceStack();
 assertTrue(emptyStack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(emptyStack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(emptyStack.isInScope(new Namespace("test", "http://test.com")));
 }

 @Test
 public void testPushPop() {
 stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("child"));
 stack.pop();
 Namespace expected = new Namespace("http://test.com", "test");
 stack.push(new Element("grandchild", new Namespace("http://test.com", "test")));
 stack.push(new Attribute("attr", null, new Namespace("http://test.com", "test")));
 Namespace actual = stack.getScope()[stack.depth];
 assertEquals(expected, actual);
 stack.pop();
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPushMultipleNamespaces() {
 stack = new NamespaceStack();
 Element element = new Element("root", new Namespace("http://test1.com", "test1"));
 Attribute attribute = new Attribute("attr", null, new Namespace("http://test2.com", "test2"));
 stack.push(element);
 stack.push(attribute);
 Namespace[] expected = new Namespace[] { new Namespace("http://test1.com", "test1"), new Namespace("http://test2.com", "test2") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushSameNamespaceTwice() {
 stack = new NamespaceStack();
 Element element = new Element("root", new Namespace("http://test.com", "test"));
 stack.push(element);
 stack.push(element);
 Namespace[] expected = new Namespace[] { new Namespace("http://test.com", "test") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushAdditionalNamespaces() {
 stack = new NamespaceStack();
 Element element = new Element("root", new Namespace("http://test1.com", "test1"));
 element.setAdditionalNamespaces(new HashSet<Namespace>() {{
 add(new Namespace("http://test2.com", "test2"));
 add(new Namespace("http://test3.com", "test3"));
 }});
 stack.push(element);
 Namespace[] expected = new Namespace[] { new Namespace("http://test1.com", "test1"), new Namespace("http://test2.com", "test2"), new Namespace("http://test3.com", "test3") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushAdditionalNamespacesWithSamePrefix() {
 stack = new NamespaceStack();
 Element element = new Element("root", new Namespace("http://test1.com", "test"));
 element.setAdditionalNamespaces(new HashSet<Namespace>() {{
 add(new Namespace("http://test2.com", "test"));
 add(new Namespace("http://test3.com", "test"));
 add(new Namespace("http://test1.com", "test"));
 }});
 Namespace[] expected = new Namespace[] { new Namespace("http://test1.com", "test") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushAttributesWithAdditionalNamespaces() {
 stack = new NamespaceStack();
 Element element = new Element("root");
 element.addContent(new Attribute("attr1", null, new Namespace("http://test1.com", "test1")));
 element.addContent(new Attribute("attr2", null, new Namespace("http://test2.com", "test2")));
 stack.push(element);
 Namespace[] expected = new Namespace[] { new Namespace("http://test1.com", "test1"), new Namespace("http://test2.com", "test2") };
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }
}