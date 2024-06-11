import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 stack = new NamespaceStack();
 }

 // Test constructor with empty argument
 @Test
 public void testNamespaceStackEmptyConstructor() {
 Namespace[] expectedEmpty = new Namespace[0];
 Assert.assertArrayEquals(expectedEmpty, stack.getScope());
 }

 // Test constructor with seed
 @Test
 public void testNamespaceStackWithSeedConstructor() {
 Namespace[] seed = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
 stack = new NamespaceStack(seed);
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test push method with single Namespace
 @Test
 public void testPushSingleNamespace() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "value", "http://example.com"));
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI, new Namespace("http://example.com") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test push method with multiple Namespaces
 @Test
 public void testPushMultipleNamespaces() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "value1", "http://example1.com"));
 stack.push(new Attribute("attr2", "value2", "http://example2.com"));
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI, new Namespace("http://example1.com"), new Namespace("http://example2.com") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test pop method
 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 stack.pop();
 }

 // Test pop method with one element
 @Test
 public void testPopOneElement() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "value", "http://example.com"));
 stack.pop();
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test pop method with multiple elements
 @Test
 public void testPopMultipleElements() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "value1", "http://example1.com"));
 stack.push(new Attribute("attr2", "value2", "http://example2.com"));
 stack.pop();
 stack.pop();
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test isInScope method with namespace in scope
 @Test
 public void testIsInScope() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "value", "http://example.com"));stack.push(new Attribute("attr2", "value2", "http://example.com"));
 Assert.assertTrue(stack.isInScope(new Namespace("http://example.com")));
 }

 // Test isInScope method with namespace not in scope
 @Test
 public void testIsInScopeNotInScope() {
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "value", "http://example.com"));stack.push(new Attribute("attr2", "value2", "http://example2.com"));
 Assert.assertFalse(stack.isInScope(new Namespace("http://example3.com")));
 }

 // Test binarySearch method
 @Test
 public void testBinarySearch() {
 Namespace[] data = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI, new Namespace("http://example1.com"), new Namespace("http://example2.com") };
 int left = 0;
 int right = data.length - 1;
 Namespace key = new Namespace("http://example1.com");
 int expectedResult = 3;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 Assert.assertEquals(expectedResult, result);
 }

 // Test checkNamespace method with namespace already in scope
 @Test
 public void testCheckNamespaceAlreadyInScope() {
 List<Namespace> toadd = new ArrayList<>();
 Namespace mns = Namespace.XML_NAMESPACE;
 Namespace[] scope = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns, scope);
 Assert.assertArrayEquals(scope, newscope);
 }

 // Test checkNamespace method with namespace not in scope but with same prefix
 @Test
 public void testCheckNamespaceSamePrefixNotInScope() {
 List<Namespace> toadd = new ArrayList<>();
 Namespace mns = new Namespace("xml", "http://example.com");
 Namespace[] scope = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns, scope);
 Assert.assertEquals(2, toadd.size());
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, mns };
 Assert.assertArrayEquals(expected, newscope);
 }

 // Test checkNamespace method with namespace not in scope and with different prefix
 @Test
 public void testCheckNamespaceDifferentPrefixNotInScope() {
 List<Namespace> toadd = new ArrayList<>();
 Namespace mns = new Namespace("example", "http://example.com");
 Namespace[] scope = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns, scope);
 Assert.assertEquals(2, toadd.size());
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, mns };
 Assert.assertArrayEquals(expected, newscope);
 }

 // Test pushStack method with namespace already in scope
 @Test
 public void testPushStackWithNamespaceAlreadyInScope() {
 List<Namespace> toadd = new ArrayList<>();
 Namespace mns = Namespace.XML_NAMESPACE;
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns, new Namespace[0]);
 stack.pushStack(mns, newscope, toadd);
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 // Test pushStack method with namespace not in scope
 @Test
 public void testPushStackWithNamespaceNotInScope() {
 List<Namespace> toadd = new ArrayList<>();
 Namespace mns = new Namespace("example", "http://example.com");
 Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns, new Namespace[0]);
 stack.pushStack(mns, newscope, toadd);
 Namespace[] expected = { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_SPACE_1_0, Namespace.XML_NAMESPACE, Namespace.XML_XMLNS, Namespace.XML_PREFIX_XMLNS, Namespace.XML_PREFIX_XML, Namespace.XML_PREFIX_XLINK, Namespace.XML_PREFIX_XSI, mns };
 Assert.assertArrayEquals(expected, stack.getScope());
 }
}