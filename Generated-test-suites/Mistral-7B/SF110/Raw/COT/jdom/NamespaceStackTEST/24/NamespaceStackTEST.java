import org.jdom2.Namespaces;
import org.jdom2.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setup() {
 stack = new NamespaceStack();
 }

 @Test
 public void testConstructor() {
 NamespaceStack nsStack = new NamespaceStack();
 assertEquals(Namespace.NO_NAMESPACE, nsStack.getScope()[0]);
 assertNotNull(nsStack.getScope()[1]);
 assertArrayEquals(Namespace.XML_NAMESPACE, nsStack.getScope()[1]);
 }

 @Test
 public void testPushElement() {
 Element element = new Element("root", Namespaces.getQualifiedName("my:prefix", "my:local"));
 stack.push(element);
 Namespace[] expected = new Namespace[] { Namespaces.getQualifiedName("my:prefix", "my:local"), Namespace.XML_NAMESPACE };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushElementWithEmptyStack() {
 Element element = new Element("root", Namespace.NO_NAMESPACE);
 stack.push(element);
 fail(); // This should not reach here
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Element element = new Element("root", Namespaces.getQualifiedName("my:prefix1", "my:local1"));
 stack.push(element);
 Element element2 = new Element("child", Namespaces.getQualifiedName("my:prefix2", "my:local2"));
 stack.push(element2);
 stack.push(element); // This should not add any namespace since it is already in the stack

 Namespace[] expected = new Namespace[] { Namespaces.getQualifiedName("my:prefix2", "my:local2"), Namespaces.getQualifiedName("my:prefix1", "my:local1"), Namespaces.getQualifiedName("my:prefix", "my:local") };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 Attribute att = new Attribute("att", Namespaces.getQualifiedName("my:prefix3", "my:local3"));
 stack.push(att);
 Namespace[] expected = new Namespace[] { Namespaces.getQualifiedName("my:prefix3", "my:local3"), Namespace.XML_NAMESPACE };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPop() {
 stack.push(new Element("root", Namespace.NO_NAMESPACE));
 stack.push(new Element("child", Namespaces.getQualifiedName("my:prefix4", "my:local4")));
 stack.pop();
 Namespace[] expected = new Namespace[] { Namespace.NO_NAMESPACE, Namespaces.getQualifiedName("my:prefix4", "my:local4") };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmptyStack() {
 stack.pop();
 fail(); // This should not reach here
 }

 @Test
 public void testAddNamespaceAlreadyExists() {
 stack.push(new Element("root", Namespaces.getQualifiedName("my:prefix", "my:local")));
 stack.push(new Attribute("att", Namespaces.getQualifiedName("my:prefix", "my:local")));
 stack.push(new Element("child", Namespaces.getQualifiedName("my:prefix", "my:local")));
 Namespace newNamespace = Namespaces.getQualifiedName("my:prefix", "my:local");
 stack.push(new Element("grandChild", newNamespace));
 Namespace[] expected = new Namespace[] { newNamespace, Namespaces.getQualifiedName("my:prefix", "my:local"), Namespaces.getQualifiedName("my:prefix", "my:local"), Namespaces.getQualifiedName("my:prefix", "my:local") };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testAddNamespaceNotExists() {
 stack.push(new Element("root", Namespace.NO_NAMESPACE));
 stack.push(new Element("child", Namespace.XML_NAMESPACE));
 Namespace newNamespace = Namespaces.getQualifiedName("my:prefix5", "my:local5");
 stack.push(new Element("grandChild", newNamespace));
 Namespace[] expected = new Namespace[] { Namespaces.getQualifiedName("my:prefix5", "my:local5"), Namespaces.XML_NAMESPACE, Namespaces.getQualifiedName("my:prefix", "my:local") };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 stack.push(new Element("root", Namespace.NO_NAMESPACE));
 stack.push(new Element("child", Namespace.XML_NAMESPACE));
 stack.push(new Element("grandChild", Namespaces.getQualifiedName("my:prefix6", "my:local6")));
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertTrue(stack.isInScope(Namespaces.getQualifiedName("my:prefix6", "my:local6")));
 assertFalse(stack.isInScope(Namespaces.getQualifiedName("my:prefix7", "my:local7")));
 }

 @Test
 public void testAddedForwardIterable() {
 stack.push(new Element("root", Namespace.NO_NAMESPACE));
 stack.push(new Element("child", Namespace.XML_NAMESPACE));
 stack.push(new Element("grandChild", Namespaces.getQualifiedName("my:prefix7", "my:local7")));
 Iterable<Namespace> addedForward = stack.addedForward();
 Namespace[] expected = new Namespace[] { Namespaces.getQualifiedName("my:prefix7", "my:local7") };
 assertArrayEquals(expected, addedForward.toArray(new Namespace[addedForward.size()]));
 }

 @Test
 public void testAddedReverseIterable() {
 stack.push(new Element("root", Namespace.NO_NAMESPACE));
 stack.push(new Element("child", Namespace.XML_NAMESPACE));
 stack.push(new Element("grandChild", Namespaces.getQualifiedName("my:prefix7", "my:local7")));
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] expected = new Namespace[] { Namespaces.NO_NAMESPACE, Namespaces.XML_NAMESPACE };
 assertArrayEquals(expected, addedReverse.toArray(new Namespace[addedReverse.size()]));
 }
}