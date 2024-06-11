import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

 private NamespaceResolver parentResolver;
 private NamespaceResolver childResolver;

 @Test
 public void testNamespaceRegistrationAndLookup() {
  parentResolver = new NamespaceResolver();
  childResolver = new NamespaceResolver(parentResolver);

  childResolver.registerNamespace("x", "http://example.com/x");
  childResolver.registerNamespace("y", "http://example.com/y");

  Assert.assertEquals("x", childResolver.getPrefix("http://example.com/x"));
  Assert.assertEquals("y", childResolver.getPrefix("http://example.com/y"));
  Assert.assertEquals(null, childResolver.getPrefix("http://different-namespace.com"));

  Assert.assertEquals("http://example.com/x", childResolver.getNamespaceURI("x"));
  Assert.assertEquals("http://example.com/y", childResolver.getNamespaceURI("y"));
  Assert.assertEquals(null, childResolver.getNamespaceURI(""));
  Assert.assertEquals(null, childResolver.getNamespaceURI("z"));
 }

 @Test
 public void testNamespaceRegistrationAndLookupWithParent() {
  String xNamespaceURI = "http://example.com/x";
  String yNamespaceURI = "http://example.com/y";

  parentResolver = new NamespaceResolver();
  parentResolver.registerNamespace("x", xNamespaceURI);
  parentResolver.registerNamespace("y", yNamespaceURI);
   childResolver = new NamespaceResolver(parentResolver);

  childResolver.registerNamespace("z", "http://example.com/z");

  Assert.assertEquals("x", childResolver.getPrefix("http://example.com/x"));
  Assert.assertEquals("y", childResolver.getPrefix("http://example.com/y"));
  Assert.assertEquals("z", childResolver.getPrefix("http://example.com/z"));

  Assert.assertEquals(xNamespaceURI, childResolver.getNamespaceURI("x"));
  Assert.assertEquals(yNamespaceURI, childResolver.getNamespaceURI("y"));
  Assert.assertEquals(xNamespaceURI, childResolver.getNamespaceURI("x:foo"));
  Assert.assertEquals(yNamespaceURI, childResolver.getNamespaceURI("y:bar"));
  Assert.assertEquals(null, childResolver.getNamespaceURI("z:qux"));

  Assert.assertEquals("http://example.com/x", childResolver.getNamespaceURI("x"));
  Assert.assertEquals("http://example.com/y", childResolver.getNamespaceURI("y"));
  Assert.assertEquals("http://example.com/z", childResolver.getNamespaceURI("z"));
 }

 @Test(expected = IllegalStateException.class)
 public void testNamespaceRegistrationOnSealedResolver() {
  NamespaceResolver parentResolver = new NamespaceResolver();
  NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
  childResolver.registerNamespace("x", "http://example.com/x");
  childResolver.seal();
  childResolver.registerNamespace("y", "http://example.com/y");
 }

 @Test
 public void testNamespaceIteration() {
  NamespaceResolver namespaceResolver = new NamespaceResolver();
  namespaceResolver.registerNamespace("x", "http://example.com/x");
  namespaceResolver.registerNamespace("y", "http://example.com/y");

  NodePointer nodePointer = new NodePointer();
  nodePointer.setNodeType(NodePointer.ELEMENT_NODE);
  nodePointer.setNamespaceURI("http://example.com/x");
  nodePointer.setNamespacePrefix("x");

  NodeIterator nodeIterator = new NodeIterator();
  nodeIterator.setNodePointer(nodePointer);

  namespaceResolver.setNamespaceContextPointer(nodeIterator);

  Iterator namespaceIterator = namespaceResolver.namespaceIterator();

  Assert.assertEquals("x", namespaceIterator.next().getNodeName().getNodeValue());
  Assert.assertEquals("y", namespaceIterator.next().getNodeName().getNodeValue());
  Assert.assertEquals(null, namespaceIterator.hasNext());
 }

}