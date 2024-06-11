import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack namespaceStack = new NamespaceStack();
 // Test empty stack
 Assert.assertArrayEqual(NamespaceStack.EMPTY, namespaceStack.getScope());
 Assert.assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
 Assert.assertFalse(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
 Assert.assertFalse(namespaceStack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushPop() {
 NamespaceStack namespaceStack = new NamespaceStack();
 // Test push
 namespaceStack.push(new Element("root", "xmlns:test", "http://test.com/test"));
 namespaceStack.push(new Element("child", null, null));
 // Test pop
 namespaceStack.pop();
 Assert.assertArrayEqual(new Namespace[]{Namespace.XML_NAMESPACE, new Namespace("test", "test")}, namespaceStack.getScope());
 // Test pop again
 namespaceStack.pop();
 Assert.assertArrayEqual(new Namespace[]{Namespace.XML_NAMESPACE}, namespaceStack.getScope());
 }

 @Test
 public void testAdditionalNamespaces() {
 NamespaceStack namespaceStack = new NamespaceStack();
 // Test push with additional namespaces
 namespaceStack.push(new Element("root", "xmlns:test1", "http://test1.com/test1"));
 namespaceStack.push(new Element("root", "xmlns:test2", "http://test2.com/test2"));
 // Test getAddedNamespaces
 List<Namespace> addedNamespacesForward = new ArrayList<>();
 List<Namespace> addedNamespacesReverse = new ArrayList<>();
 Assert.assertEquals(0, namespaceStack.addedForward().size());
 Assert.assertEquals(0, namespaceStack.addedReverse().size());
 // Test getAddedNamespaces after popping all namespaces
 namespaceStack.pop();
 namespaceStack.pop();
 List<Namespace> addedNamespaces = Arrays.asList(namespaceStack.addedForward().iterator().next());
 Assert.assertEquals(2, addedNamespaces.size());
 Assert.assertEquals(addedNamespaces.get(0), new Namespace("test1", "test1"));
 Assert.assertEquals(addedNamespaces.get(1), new Namespace("test2", "test2"));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushBrokenStack() {
 NamespaceStack namespaceStack = new NamespaceStack();
 // Test pushing to a broken stack
 namespaceStack.push(new Element("root", "xmlns:test", "http://test.com/test"));
 namespaceStack.pop();
 namespaceStack.pop();
 namespaceStack.push(new Element("child", null, null));
 // Test exception
 namespaceStack.push(new Element("child2", null, null));
 }
}