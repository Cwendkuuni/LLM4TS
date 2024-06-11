import org.jdom2.*;
import org.jdom2.util.NamespaceStack;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 }

 @Test
 public void testNewStackWithDefaultSeed() {
 NamespaceStack stack = new NamespaceStack();
 Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testNewStackWithCustomSeed() {
 Namespace[] seed = {new Namespace("foo", "http://example.com"), Namespace.XML_NAMESPACE};
 NamespaceStack stack = new NamespaceStack(seed);
 Namespace[] expected = {new Namespace("foo", "http://example.com"), Namespace.XML_NAMESPACE};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushElement() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://example.com", "root"));
 stack.push(element);
 Namespace[] expected = {Namespace.XML_NAMESPACE};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushElementWithMultipleNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://example.com", "root"));
 element.setName(new QName("http://another-example.com", "root"));
 stack.push(element);
 Namespace[] expected = {Namespace.XML_NAMESPACE, new Namespace("another-example.com", "http://another-example.com")};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://example.com", "root"));
 Attribute attribute = new Attribute("attr", "value", new QName("http://another-example.com", "attr"));
 stack.push(attribute);
 Namespace[] expected = {Namespace.XML_NAMESPACE, new Namespace("another-example.com", "http://another-example.com")};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", new QName("http://example.com", "root"));
 stack.push(element);
 stack.pop();
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testAddNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("http://example.com", "root")));
 stack.push(new Namespace("new-namespace", "http://new-example.com"));
 Namespace[] expected = {Namespace.XML_NAMESPACE, new Namespace("new-namespace", "http://new-example.com")};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testAddNamespaceAlreadyPresent() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("http://example.com", "root")));
 stack.push(new Namespace("xmlns", "http://example.com"));
 stack.push(new Namespace("xmlns", "http://example.com"));
 Namespace[] expected = {Namespace.XML_NAMESPACE, new Namespace("xmlns", "http://example.com")};
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("http://example.com", "root")));
 stack.push(new Namespace("xmlns", "http://example.com"));
 assertTrue(stack.isInScope(new Namespace("xmlns", "http://example.com")));
 assertFalse(stack.isInScope(new Namespace("foo", "http://example.com")));
 }
}