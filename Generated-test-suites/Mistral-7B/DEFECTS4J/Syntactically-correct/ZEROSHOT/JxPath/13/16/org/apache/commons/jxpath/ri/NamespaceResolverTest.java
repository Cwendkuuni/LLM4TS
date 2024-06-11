package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespacesResolver;

public class NamespaceResolverTest {

 private NamespacesResolver parentResolver;
 private NamespacesResolver currentResolver;

 @Before
 public void setUp() {
 parentResolver = new NamespacesResolver();
 currentResolver = new NamespacesResolver(parentResolver);
 }

 @Test
 public void testNewInstance() {
 NamespacesResolver resolver = new NamespacesResolver();
 assertFalse(resolver.isSealed());
 }

 @Test
 public void testNewInstanceWithParent() {
 NamespacesResolver resolver = new NamespacesResolver(parentResolver);
 assertSame(parentResolver, resolver.getParent());
 }

 @Test
 public void testRegisterNamespace() {
 currentResolver.registerNamespace("x", "http://test.com/x");
 String uri = currentResolver.getNamespaceURI("x");
 assertEquals("http://test.com/x", uri);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWhenSealed() {
 currentResolver.seal();
 currentResolver.registerNamespace("x", "http://test.com/x");
 }

 @Test
 public void testGetNamespaceURI() {
 currentResolver.registerNamespace("x", "http://test.com/x");
 String uri = currentResolver.getNamespaceURI("x");
 assertEquals("http://test.com/x", uri);

 currentResolver.setNamespaceContextPointer(new NodePointer(""));
 uri = currentResolver.getNamespaceURI("x");
 assertNull(uri);
 }

 @Test
 public void testGetNamespaceURIUnknownPrefix() {
 String uri = currentResolver.getNamespaceURI("y");
 assertNull(uri);
 }

 @Test
 public void testGetPrefix() {
 currentResolver.registerNamespace("x", "http://test.com/x");
 String prefix = currentResolver.getPrefix("http://test.com/x");
 assertEquals("x", prefix);

 currentResolver.seal();
 prefix = currentResolver.getPrefix("http://test.com/x");
 assertEquals("x", prefix);
 }

 @Test
 public void testGetPrefixUnknownUri() {
 String prefix = currentResolver.getPrefix("http://test.com/y");
 assertNull(prefix);
 }

 @Test
 public void testIsSealed() {
 currentResolver.seal();
 assertTrue(currentResolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespacesResolver clone = (NamespacesResolver) currentResolver.clone();
 assertFalse(clone.isSealed());
 assertSame(currentResolver.getParent(), clone.getParent());
 }

 @Test
 public void testGetParent() {
 assertSame(parentResolver, currentResolver.getParent());
 }

 @Test
 public void testGetNamespaceContextPointer() {
 currentResolver.setNamespaceContextPointer(new NodePointer(""));
 Pointer pointer = currentResolver.getNamespaceContextPointer();
 assertSame(new NodePointer(""), pointer);
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NodePointer pointer = new NodePointer("test");
 currentResolver.setNamespaceContextPointer(pointer);
 Pointer result = currentResolver.getNamespaceContextPointer();
 assertSame(pointer, result);
 }

 @Test
 public void testGetNamespaceContextPointerWithParent() {
 NodePointer pointer = new NodePointer("test");
 parentResolver.setNamespaceContextPointer(pointer);
 Pointer result = currentResolver.getNamespaceContextPointer();
 assertSame(pointer, result);
 }

 @Test
 public void testNamespaceIterator() {
 currentResolver.registerNamespace("x", "http://test.com/x");
 NodePointer root = new NodePointer("root");
 currentResolver.setNamespaceContextPointer(root);
 NodeIterator ni = root.getChildren();
 for (int i = 0; i < 10; i++) {
 ni.addChild(new NodePointer("elem" + i, "elem" + i, "http://test.com/elem"));
 }
 currentResolver.registerNamespace("elem", "http://test.com/elem");
 Map<String, String> namespaceMap = currentResolver.getNamespaceMap();
 assertEquals(2, namespaceMap.size());
 assertEquals("x", namespaceMap.keySet().iterator().next());
 assertEquals("http://test.com/x", namespaceMap.get("x"));
 assertEquals("elem", namespaceMap.keySet().iterator().next());
 assertEquals("http://test.com/elem", namespaceMap.get("elem"));
 }

}