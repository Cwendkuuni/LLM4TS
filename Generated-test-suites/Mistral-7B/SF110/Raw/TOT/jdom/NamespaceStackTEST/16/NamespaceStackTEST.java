import org.jdom2.*;
import org.jdom2.filter.Namespaces;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 @Test
 public void testPushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespaces.NO_NAMESPACE);
 stack.push(root);
 Namespace mns = new Namespace("test", "http://test.com");
 stack.push(new Element("element", mns).setText("Hello World!"));
 stack.push(new Attribute("attr", mns).setText("Attribute Value"));
 List<Namespace> toAdd = new ArrayList<>();
 stack.push(new Element("anotherElement", Namespaces.XML_NAMESPACE), toAdd);
 stack.push(new Attribute("anotherAttr", Namespace.NO_NAMESPACE), toAdd);
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 Assert.assertArrayEquals(new Namespace[]{}, stack.getScope());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPushDuplicateNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespaces.NO_NAMESPACE);
 stack.push(root);
 Namespace mns = new Namespace("test", "http://test.com");
 stack.push(new Element("element", mns).setText("Hello World!"));
 stack.push(new Element("element", mns).setText("Hello World!"));
 List<Namespace> toAdd = new ArrayList<>();
 stack.push(new Element("anotherElement", Namespaces.XML_NAMESPACE), toAdd);
 stack.push(new Attribute("attr", Namespace.NO_NAMESPACE), toAdd);
 stack.push(new Element("element", mns).setText("Hello World!")); // Duplicate namespace
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 Assert.assertArrayEquals(new Namespace[]{}, stack.getScope());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
 }
}