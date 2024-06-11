import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 Namespace xml = Namespaces.XML_NAMESPACE;
 Namespace ns1 = new Namespace("http://test.com", "test1");
 Namespace ns2 = new Namespace("http://test.com", "test2");
 Namespace ns3 = new Namespace("http://test.com", "test3");

 element = new Element("root", ns1);
 element.addNamespaceDeclaration("test2", ns2);
 element.addNamespaceDeclaration("test3", ns3);
 attribute = new Attribute("attr", "value", ns2);
 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertNull(stack.getScope());
 assertFalse(stack.isInScope(Namespaces.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "test")));
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(Namespace.XML_NAMESPACE, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns1,Ns2,Ns3}, scope));
 assertTrue(stack.isInScope(Ns1));
 assertTrue(stack.isInScope(Ns2));
 assertTrue(stack.isInScope(Ns3));
 }

 @Test
 public void testPushAttribute() {
 stack.push(attribute);
 stack.push(attribute); // test over-pushing
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(Ns2, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns2}, scope));
 assertTrue(stack.isInScope(Ns2));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack.pop();
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(attribute);
 stack.push(attribute);
 stack.push(attribute);
 stack.pop();
 stack.pop();
 stack.pop();
 Namespace[] scope = stack.getScope();
 assertNull(scope);
 }

 @Test
 public void testPushMultipleElements() {
 Element root1 = new Element("root1", ns1);
 Element root2 = new Element("root2", ns1);
 stack.push(root1);
 stack.push(root2);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(new Namespace[] {Ns1}, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns1,Ns1}, scope));
 }

 @Test
 public void testPushElementsWithSameNamespace() {
 Element root1 = new Element("root1", ns1);
 Element root2 = new Element("root2", ns1);
 stack.push(root1);
 stack.push(root2);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(new Namespace[] {Ns1}, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns1}, scope[1]));
 }

 @Test
 public void testPushElementsWithSamePrefixButDifferentNamespaces() {
 Element root1 = new Element("root1", new Namespace("http://test.com", "testPrefix1"));
 Element root2 = new Element("root2", new Namespace("http://test.com/other", "testPrefix1"));
 stack.push(root1);
 stack.push(root2);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(new Namespace[] {new Namespace("http://test.com", "testPrefix1")}, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {new Namespace("http://test.com", "testPrefix1"), new Namespace("http://test.com/other", "testPrefix1")}, scope));
 }

 @Test
 public void testPushMultipleAttributes() {
 Attribute attr1 = new Attribute("attr1", "value1", ns2);
 Attribute attr2 = new Attribute("attr2", "value2", ns2);
 stack.push(attribute);
 stack.push(attr1);
 stack.push(attr2);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(Ns2, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns2,Ns2}, scope));
 }

 @Test
 public void testPushSameAttributeMultipleTimes() {
 stack.push(attribute);
 stack.push(attribute);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(Ns2, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns2}, scope));
 }

 @Test
 public void testPushAttributesWithSameNamespace() {
 Attribute attr1 = new Attribute("attr1", "value1", ns2);
 Attribute attr2 = new Attribute("attr2", "value2", ns2);
 stack.push(attribute);
 stack.push(attr1);
 stack.push(attr2);
 Namespace[] scope = stack.getScope();
 assertTrue(Arrays.equals(Ns2, scope[0]));
 assertTrue(Arrays.equals(new Namespace[] {Ns2}, scope[1]));
 }

}