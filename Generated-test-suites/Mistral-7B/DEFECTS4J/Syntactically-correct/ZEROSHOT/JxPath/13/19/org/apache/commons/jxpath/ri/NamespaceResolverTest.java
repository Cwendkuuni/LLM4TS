import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentsResolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 parentsResolver = new NamespaceResolver(resolver);
 pointer = new NodePointer();
 }

 @Test
 public void testNewInstance() {
 NamespaceResolver instance = new NamespaceResolver();
 assertNotNull(instance);
 }

 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
 assertNull(resolver.getNamespaceURI("prefix2"));
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NodePointer contextPointer = new NodePointer();
 contextPointer.setNamespaceURI("http://test.com", "test");
 resolver.setNamespaceContextPointer(contextPointer);
 assertEquals(contextPointer.getNamespaceURI("test"), resolver.getNamespaceURI("test"));
 assertNull(resolver.getNamespaceURI("xsi"));
 }

 @Test
 public void testGetNamespaceURI() {
 NodePointer contextPointer = new NodePointer();
 contextPointer.setNamespaceURI("http://test.com", "test");
 resolver.setNamespaceContextPointer(contextPointer);
 resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
 assertEquals("http://test.com", resolver.getNamespaceURI("test"));
 assertNull(resolver.getNamespaceURI("prefix2"));
 }

 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
 assertNull(resolver.getPrefix("prefix2"));
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver sealedResolver = new NamespaceResolver(resolver);
 sealedResolver.seal();
 assertFalse(resolver.isSealed());
 assertTrue(sealedResolver.isSealed());
 }

 @Test
 public void testSeal() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver sealedResolver = new NamespaceResolver();
 sealedResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 sealedResolver.seal();
 sealedResolver.registerNamespace("prefix2", "http://prefix2.com");
 }

 @Test
 public void testClone() {
 NamespaceResolver clone = (NamespaceResolver) resolver.clone();
 assertEquals(resolver, clone);
 assertFalse(resolver == clone);
 }

 @Test
 public void testCloneWithParent() {
 NamespaceResolver parentClone = (NamespaceResolver) parentsResolver.clone();
 NamespaceResolver clone = (NamespaceResolver) resolver.clone();
 clone.setParent(parentClone);
 assertFalse(resolver == clone);
 assertEquals(parentsResolver, clone.getParent());
 assertEquals(resolver, clone.getParent().getParent());
 }

}