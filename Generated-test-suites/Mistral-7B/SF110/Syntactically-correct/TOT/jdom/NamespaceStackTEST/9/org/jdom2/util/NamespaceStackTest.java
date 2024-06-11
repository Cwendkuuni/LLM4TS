import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jdom2.util.NamespaceStack;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack namespaceStack = new NamespaceStack();
 assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(namespaceStack.isInScope(new Namespace("test", "test")));
 assertFalse(namespaceStack.isInScope(null));
 assertFalse(namespaceStack.addedForward().hasNext());
 assertFalse(namespaceStack.addedReverse().hasNext());
 }

 @Test
 public void testPushPop() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root"));
 namespaceStack.pop();
 assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(namespaceStack.isInScope(new Namespace("test", "test")));
 assertFalse(namespaceStack.addedForward().hasNext());
 assertFalse(namespaceStack.addedReverse().hasNext());

 namespaceStack.push(new Element("root"));
 namespaceStack.push(new Element("element"));
 namespaceStack.push(new Attribute("attr", "value"));
 assertEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("element", "element") }, namespaceStack.getScope());
 assertEquals(new Namespace[] { new Namespace("element", "element") }, namespaceStack.addedForward().toArray());
 assertEquals(new Namespace[] { new Namespace("element", "element") }, namespaceStack.addedReverse().toArray());
 namespaceStack.pop();
 assertEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("element", "element") }, namespaceStack.getScope());
 namespaceStack.pop();
 assertEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, namespaceStack.getScope());
 }

 @Test
 public void testPushMultipleElements() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root"));
 namespaceStack.push(new Element("element1"));
 namespaceStack.push(new Element("element2"));
 namespaceStack.push(new Attribute("attr1", "value1"));
 namespaceStack.push(new Attribute("attr2", "value2"));
 assertEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("element2", "element2"), new Namespace("element1", "element1") }, namespaceStack.getScope());
 assertEquals(new Namespace[] { new Namespace("element2", "element2"), new Namespace("element1", "element1") }, namespaceStack.addedForward().toArray());
 assertEquals(new Namespace[] { new Namespace("element1", "element1"), new Namespace("element2", "element2") }, namespaceStack.addedReverse().toArray());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushEmptyElement() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push((Element) null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPushNullNamespace() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push((Namespace) null);
 }

 @Test
 public void testPushSameNamespaceTwice() {
 NamespaceStack namespaceStack = new NamespaceStack();
 namespaceStack.push(new Element("root"));
 namespaceStack.push(new Element("element", "test"));
 namespaceStack.push(new Element("element", "test"));
 assertEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("element", "test") }, namespaceStack.getScope());
 assertEquals(new Namespace[] { new Namespace("element", "test") }, namespaceStack.addedForward().toArray());
 assertEquals(new Namespace[] { new Namespace("element", "test") }, namespaceStack.addedReverse().toArray());
 }

 @Test
 public void testCheckNamespace() {
 List<Namespace> toadd = new ArrayList<Namespace>();
 Namespace[] scope = new Namespace[] { new Namespace("test1", "test1"), new Namespace("test2", "test2") };
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, new Namespace("test1", "test1"), scope);
 assertArrayEquals(new Namespace[] { new Namespace("test1", "test1"), new Namespace("test1", "test1") }, newscope);
 toadd.clear();
 newscope = NamespaceStack.checkNamespace(toadd, new Namespace("test1", "test1"), scope);
 assertArrayEquals(new Namespace[] { new Namespace("test1", "test1"), new Namespace("test1", "test1"), new Namespace("test2", "test2") }, newscope);
 }
}