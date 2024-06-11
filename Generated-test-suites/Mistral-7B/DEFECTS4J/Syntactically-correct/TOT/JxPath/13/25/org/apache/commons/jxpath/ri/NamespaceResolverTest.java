import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 @Test
 public void testNamespaceResolverEmpty() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertEquals(0, resolver.getNamespaceMap().size());
 assertEquals(null, resolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testNamespaceResolverWithParent() {
 NamespaceResolver childResolver = new NamespaceResolver(new NamespaceResolver());
 childResolver.registerNamespace("prefix", "http://example.com");
 assertEquals("http://example.com", childResolver.getNamespaceURI("prefix"));
 NamespaceResolver parentResolver = childResolver;
 childResolver = new NamespaceResolver(parentResolver);
 assertNull(childResolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testNamespaceResolverWithContext() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodePointer("root", new HashMap(), "prefix", "http://example.com"));
 assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testNamespaceResolverWithContextAndRegistration() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodePointer("root", new HashMap(), "prefix1", "http://example1.com"));
 resolver.registerNamespace("prefix2", "http://example2.com");
 assertEquals("http://example2.com", resolver.getNamespaceURI("prefix2"));
 assertEquals("http://example1.com", resolver.getNamespaceURI("prefix1"));
 }

 @Test(expected = IllegalStateException.class)
 public void testSealedNamespaceResolverRegistration() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.seal();
 resolver.registerNamespace("prefix", "http://example.com");
 }

 @Test
 public void testNamespaceResolverReverseMap() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("prefix1", "http://example1.com");
 resolver.registerNamespace("prefix2", "http://example2.com");
 assertEquals("prefix1", resolver.getPrefix("http://example1.com"));
 assertEquals("prefix2", resolver.getPrefix("http://example2.com"));
 }
}