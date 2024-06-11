package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

@Test
public void testGetNamespaceURI() {
NamespaceResolver resolver = new NamespaceResolver();
String prefix = "xmlns";
String namespaceURI = "http://www.w3.org/2000/xmlns/";
resolver.registerNamespace(prefix, namespaceURI);
String result = resolver.getNamespaceURI(prefix);
assertEquals(namespaceURI, result);
}

@Test
public void testGetNamespaceURIWithNullPrefix() {
NamespaceResolver resolver = new NamespaceResolver();
String result = resolver.getNamespaceURI(null);
assertNull(result);
}

@Test
public void testGetNamespaceURIWithEmptyPrefix() {
NamespaceResolver resolver = new NamespaceResolver();
String result = resolver.getNamespaceURI("");
assertNull(result);
}

@Test
public void testGetNamespaceURIWithInvalidNamespaceURI() {
NamespaceResolver resolver = new NamespaceResolver();
String prefix = "xmlns";
String namespaceURI = "";
resolver.registerNamespace(prefix, namespaceURI);
String result = resolver.getNamespaceURI(prefix);
assertNull(result);
}

@Test
public void testGetNamespaceURIWithEmptyNamespaceURI() {
NamespaceResolver resolver = new NamespaceResolver();
String prefix = "xmlns";
String namespaceURI = "";
resolver.registerNamespace(prefix, namespaceURI);
String result = resolver.getNamespaceURI(prefix);
assertNull(result);
}

@Test
public void testGetNamespaceURIWithNonExistentPrefix() {
NamespaceResolver resolver = new NamespaceResolver();
String prefix = "xmlns:foo";
String result = resolver.getNamespaceURI(prefix);
assertNull(result);
}

@Test
public void testGetNamespaceURIWithSealedNamespaceResolver() {
NamespaceResolver parent = new NamespaceResolver();
NamespaceResolver resolver = new NamespaceResolver(parent);
parent.seal();
String prefix = "xmlns";
String namespaceURI = "http://www.w3.org/2000/xmlns/";
resolver.registerNamespace(prefix, namespaceURI);
String result = resolver.getNamespaceURI(prefix);
assertEquals(namespaceURI, result);
resolver.seal();
String result2 = resolver.getNamespaceURI(prefix);
assertNull(result2);
}

@Test
public void testGetNamespaceURIWithoutRegisteredPrefixOrNamespaceURI() {
NamespaceResolver resolver = new NamespaceResolver();
String prefix = "xmlns";
String result = resolver.getNamespaceURI(prefix);
assertNull(result);
}

@Test
public void testClone() {
NamespaceResolver original = new NamespaceResolver();
original.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
NamespaceResolver clone = (NamespaceResolver) original.clone();
String result = clone.getNamespaceURI("xmlns");
assertEquals(original.getNamespaceURI("xmlns"), result);
}
}