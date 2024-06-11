import org.jdom2.*;
import org.jdom2. Namespace;
import org.junit.Test;
import org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 Assert.assertEquals(NamespaceStack.EMPTY, stack.getScope());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
 Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 @Test
 public void testSingleElementStack() {
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 NamespaceStack stack = new NamespaceStack(new Namespace[] { xmlNs });
 assertFalse(stack.isEmpty());
 assertEquals(new Namespace[] { xmlNs }, stack.getScope());
 assertEquals(0, stack.depth);

 List<Namespace> toAdd = new ArrayList<Namespace>();
 NamespaceStack.checkNamespace(toAdd, xmlNs, NamespaceStack.EMPTY);
 assertArrayEquals(NamespaceStack.EMPTY, toAdd.toArray(new Namespace[0]));
 }

 @Test
 public void testPushAndPop() {
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 Namespace mns = new Namespace("test");
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", xmlNs));
 stack.push(new Element("elem", mns));
 stack.push(new Attribute("attr", mns));
 stack.pop();
 stack.pop();

 List<Namespace> toAdd = new ArrayList<Namespace>();
 Namespace[] newscope = NamespaceStack.checkNamespace(toAdd, mns, stack.getScope()[stack.depth]);
 assertEquals(newscope, stack.scope[stack.depth]);

 assertTrue(stack.isEmpty());
 assertEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnderflow() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPushMultipleNamespaces() {
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 Namespace mns = new Namespace("test1");
 Namespace nms = new Namespace("test2");
 Element element = new Element("root", xmlNs);
 element.setName("elem", mns);
 Attribute attr = new Attribute("attr", nms);
 element.addAttribute(attr);

 NamespaceStack stack = new NamespaceStack();
 stack.push(element);

 List<Namespace> toAdd = new ArrayList<Namespace>();
 Namespace[] newscope = NamespaceStack.checkNamespace(toAdd, nms, stack.getScope()[stack.depth]);
 assertEquals(new Namespace[] { mns, nms }, newscope);
 }

 @Test
 public void testPushMultipleNamespacesInReversedOrder() {
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 Namespace mns = new Namespace("test1");
 Namespace nms = new Namespace("test2");
 Attribute attr = new Attribute("attr", mns);
 Attribute attr2 = new Attribute("attr2", nms);
 Element element = new Element("root", xmlNs);
 element.addAttribute(attr2);
 element.setName("elem", attr);

 NamespaceStack stack = new NamespaceStack();
 stack.push(element);

 List<Namespace> toAdd = new ArrayList<Namespace>();
 Namespace[] newscope = NamespaceStack.checkNamespace(toAdd, mns, stack.getScope()[stack.depth]);
 assertEquals(new Namespace[] { nms, mns }, newscope);
 }

 @Test
 public void testPushDuplicate() {
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 Namespace mns = new Namespace("test");
 Element element = new Element("root", xmlNs);
 element.setName("elem", mns);
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 stack.push(element);

 List<Namespace> toAdd = new ArrayList<Namespace>();
 Namespace[] newscope = NamespaceStack.checkNamespace(toAdd, mns, stack.getScope()[stack.depth]);
 assertEquals(newscope, stack.scope[stack.depth]);
 }
}