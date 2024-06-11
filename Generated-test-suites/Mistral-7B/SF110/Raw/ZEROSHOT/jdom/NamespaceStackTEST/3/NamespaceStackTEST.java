import org.jdom2.*;
import org.jdom2.internal.NamespaceStack;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testNamespaceStackEmpty() {
 NamespaceStack namespaceStack = new NamespaceStack();
 assertTrue(namespaceStack.isEmpty());
 assertNull(namespaceStack.getScope());
 }

 @Test
 public void testNamespaceStackPush() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 Namespace[] expectedScope = {xmlNs};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushMultiple() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace xsiNs = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 namespaceStack.push(new Element("child", xsiNs));
 Namespace[] expectedScope = {xmlNs, xsiNs};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushDuplicate() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 namespaceStack.push(new Element("child", xmlNs));
 Namespace[] expectedScope = {xmlNs};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushAttributes() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace xsiNs = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
 Namespace ns = Namespace.getNamespace("http://example.com");
 Element root = new Element("root", ns);
 Attribute attr1 = new Attribute("attr1", xmlNs);
 Attribute attr2 = new Attribute("attr2", xsiNs);
 root.addContent(attr1);
 root.addContent(attr2);
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(root);
 Namespace[] expectedScope = {xmlNs, xsiNs, ns};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushAdditionalNamespaces() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace xsiNs = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
 Namespace prefix1 = Namespace.getNamespace("http://example.com/prefix1");
 Namespace prefix2 = Namespace.getNamespace("http://example.com/prefix2");
 Element root = new Element("root", xmlNs);
 root.setAdditionalNamespaces(Collections.singletonMap("prefix1", prefix1));
 root.setAdditionalNamespaces(Collections.singletonMap("prefix2", prefix2));
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(root);
 Namespace[] expectedScope = {xmlNs, prefix1, prefix2};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPop() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace xsiNs = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 namespaceStack.push(new Element("child", xsiNs));
 namespaceStack.pop();
 Namespace[] expectedScope = {xmlNs};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPopMultiple() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace xsiNs = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 namespaceStack.push(new Element("child1", xsiNs));
 namespaceStack.push(new Element("child2", xmlNs));
 namespaceStack.pop();
 Namespace[] expectedScope = {xmlNs, xsiNs};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test (expected = IllegalStateException.class)
 public void testNamespaceStackPopEmpty() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 }

 @Test
 public void testNamespaceStackIsInScope() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace ns = Namespace.getNamespace("http://example.com");
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 namespaceStack.push(new Element("child", ns));
 assertTrue(namespaceStack.isInScope(xmlNs));
 assertTrue(namespaceStack.isInScope(ns));
 }

 @Test
 public void testNamespaceStackIsNotInScope() {
 Namespace xmlNs = Namespace.getXmlNamespace();
 Namespace ns = Namespace.getNamespace("http://example.com");
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root", xmlNs));
 assertFalse(namespaceStack.isInScope(ns));
 }

}