import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodeContext;
import org.apache.commons.jxpath.RITestBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest extends RITestBase {

 @Test
 public void testNamespaceRegistration() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 assertEquals("xsd", prefix);
 }

 @Test
 public void testGetNamespaceURI_explicitlyRegisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test
 public void testGetNamespaceURI_contextNode() {
 NodeContext nodeContext = createNodeContextFromString("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"/>");
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(nodeContext.getPointer());
 String uri = resolver.getNamespaceURI("xsi");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test
 public void testGetNamespaceURI_parentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 String uri = resolver.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test
 public void testGetPrefix_explicitlyRegisteredNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", prefix);
 }

 @Test
 public void testGetPrefix_contextNode() {
 NodeContext nodeContext = createNodeContextFromString("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"/>");
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(nodeContext.getPointer());
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 assertEquals("xsi", prefix);
 }

 @Test
 public void testGetPrefix_parentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", prefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNamespaceURI_nullPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 try {
 resolver.getNamespaceURI(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPrefix_nullNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 try {
 resolver.getPrefix(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertFalse(resolver.isSealed());
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 String uri = cloned.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }
}