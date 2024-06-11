import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 @Mock private NodePointer pointer;
 @Mock private NodeIterator nodeIterator;

 @Before
 public void setup() {
     namespaceResolver = new NamespaceResolver();
     when(pointer.namespaceIterator()).thenReturn(nodeIterator);
     when(nodeIterator.getNodePointer(anyInt())).thenReturn(pointer);
 }

 @Test
 public void testNewInstance() {
     NamespaceResolver instance = new NamespaceResolver();
     assertEquals(null, instance.getNamespaceURI("prefix"));
 }

 @Test
 public void testNewInstanceWithParent() {
     NamespaceResolver parent = new NamespaceResolver();
     NamespaceResolver instance = new NamespaceResolver(parent);
     assertEquals(null, instance.getNamespaceURI("prefix"));
     assertEquals(parent, instance.getParent());
 }

 @Test
 public void testRegisterNamespace() {
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
     namespaceResolver = new NamespaceResolver();
     namespaceResolver.seal();
     namespaceResolver.registerNamespace("prefix", "http://example.com");
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefixAndPointer() {
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     namespaceResolver.setNamespaceContextPointer(pointer);
     assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefixAndParent() {
     NamespaceResolver parent = new NamespaceResolver();
     parent.registerNamespace("prefix", "http://example.com");
     NamespaceResolver instance = new NamespaceResolver(parent);
     instance.setNamespaceContextPointer(pointer);
     assertEquals("http://example.com", instance.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefix() {
     assertEquals(null, namespaceResolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefixAndPointer() {
     assertEquals(null, namespaceResolver.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefixAndParent() {
     NamespaceResolver parent = new NamespaceResolver();
     assertEquals(null, parent.getNamespaceURI("prefix"));
 }

 @Test
 public void testGetPrefixWithRegisteredNamespaceURI() {
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     assertEquals("prefix", namespaceResolver.getPrefix("http://example.com"));
 }

 @Test
 public void testGetPrefixWithRegisteredNamespaceURIAndParent() {
     NamespaceResolver parent = new NamespaceResolver();
     parent.registerNamespace("prefix", "http://example.com");
     NamespaceResolver instance = new NamespaceResolver(parent);
     assertEquals("prefix", instance.getPrefix("http://example.com"));
 }

 @Test
 public void testGetPrefixWithUnregisteredNamespaceURI() {
     assertEquals(null, namespaceResolver.getPrefix("http://example.com"));
 }

 @Test
 public void testGetPrefixWithUnregisteredNamespaceURIAndParent() {
     NamespaceResolver parent = new NamespaceResolver();
     assertEquals(null, parent.getPrefix("http://example.com"));
 }

 @Test
 public void testSeal() {
     namespaceResolver = new NamespaceResolver();
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     namespaceResolver.seal();
     namespaceResolver.registerNamespace("prefix2", "http://example2.com"); // should not be possible
     assertEquals(true, namespaceResolver.isSealed());
 }

 @Test
 public void testClone() {
     namespaceResolver.registerNamespace("prefix", "http://example.com");
     NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
     assertEquals("http://example.com", clone.getNamespaceURI("prefix"));
     assertEquals(false, clone.isSealed());
 }
}