import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;

 @Test
 public void testConstructor() {
 namespaceStack = new NamespaceStack();
 Assert.assertEquals(0, namespaceStack.getDepth());
 Assert.assertNull(namespaceStack.getScope());
 }

 @Test
 public void testConstructorWithSeed() {
 Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 namespaceStack = new NamespaceStack(seed);
 Assert.assertEquals(1, namespaceStack.getDepth());
 Assert.assertArrayEquals(seed, namespaceStack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(null);
 }

 @Test
 public void testPushWithElementAndAdditionalNamespaces() {
 Element element = new Element("root", "root");
 element.setName("root");
 element.setNamespace(Namespace.getXmlNamespace());
 List<Namespace> toAdd = new ArrayList<>();
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
 stack.push("http://example.com");
 stack.push("http://example.org");
 stack.push("http://example.net");
 toAdd.addAll(Arrays.asList(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com"), Namespace.getNamespace("http://example.org"), Namespace.getNamespace("http://example.net")));
 stack.pushStack(Namespace.NO_NAMESPACE, toAdd.toArray(new Namespace[toAdd.size()]), toAdd);
 Assert.assertEquals(5, stack.getDepth());
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithAttribute() {
 Attribute attribute = new Attribute("xmlns:x", "http://example.com");
 List<Namespace> toAdd = new ArrayList<>();
 NamespaceStack stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com")};
 stack.push("http://example.org");
 stack.push("http://example.net");
 toAdd.addAll(Arrays.asList(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com")));
 stack.pushStack(Namespace.NO_NAMESPACE, toAdd.toArray(new Namespace[toAdd.size()]), toAdd);
 Assert.assertEquals(4, stack.getDepth());
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 }

 @Test
 public void testPop() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(Namespace.getNamespace("http://example.com"));
 namespaceStack.push(Namespace.getNamespace("http://example.org"));
 namespaceStack.push(Namespace.getNamespace("http://example.net"));
 Assert.assertEquals(3, namespaceStack.getDepth());
 namespaceStack.pop();
 Assert.assertEquals(2, namespaceStack.getDepth());
 Assert.assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.org") }, namespaceStack.getScope());
 }

 @Test
 public void testGetAddedForward() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root", "root");
 element.setName("root");
 element.setNamespace(Namespace.getXmlNamespace());
 List<Namespace> toAdd = new ArrayList<>();
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] added = stack.addedForward().toArray(new Namespace[0]);
 Assert.assertEquals(1, added.length);
 Assert.assertEquals(Namespace.XML_NAMESPACE, added[0]);
 }

 @Test
 public void testGetAddedReverse() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root", "root");
 element.setName("root");
 element.setNamespace(Namespace.getXmlNamespace());
 List<Namespace> toAdd = new ArrayList<>();
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] added = stack.addedReverse().toArray(new Namespace[0]);
 Assert.assertEquals(1, added.length);
 Assert.assertEquals(Namespace.XML_NAMESPACE, added[0]);
 }

 @Test
 public void testIterator() {
 namespaceStack = new NamespaceStack();
 Element element = new Element("root", "root");
 element.setName("root");
 element.setNamespace(Namespace.getXmlNamespace());
 List<Namespace> toAdd = new ArrayList<>();
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
 stack.push("http://example.com");
 stack.push("http://example.org");
 stack.push("http://example.net");
 toAdd.addAll(Arrays.asList(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com"), Namespace.getNamespace("http://example.org"), Namespace.getNamespace("http://example.net")));
 stack.pushStack(Namespace.NO_NAMESPACE, toAdd.toArray(new Namespace[toAdd.size()]), toAdd);
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> result = new ArrayList<>();
 while (iterator.hasNext()) {
 result.add(iterator.next());
 }
 Assert.assertEquals(Arrays.asList(Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com"), Namespace.getNamespace("http://example.org"), Namespace.getNamespace("http://example.net")), result);
 }

 @Test
 public void testIsInScope() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(Namespace.getNamespace("http://example.com"));
 namespaceStack.push(Namespace.getNamespace("http://example.org"));
 Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("http://example.com")));
 Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("http://example.org")));
 Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
 }
}