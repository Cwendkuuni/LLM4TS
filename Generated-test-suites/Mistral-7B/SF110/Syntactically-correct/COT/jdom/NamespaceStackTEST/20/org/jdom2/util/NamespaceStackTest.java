import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testConstructor() {
 stack = new NamespaceStack();
 Assert.assertNotNull(stack);
 Assert.assertEquals(0, stack.getDepth());
 }

 @Test
 public void testPushElement() {
 Element element = new Element("root");
 stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 Namespace[] expectedAdded = {};
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 Assert.assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 Assert.assertEquals(1, stack.getDepth());
 }

 @Test
 public void testPushElementWithNamespaces() {
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = {Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.XML_NAMESPACE};
 Namespace[] expectedAdded = {};
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 Assert.assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 Assert.assertEquals(1, stack.getDepth());
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("attr", "value", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = {Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.XML_NAMESPACE};
 Namespace[] expectedAdded = {};
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 Assert.assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 Assert.assertEquals(1, stack.getDepth());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithEmptyStackAndNullNamespace() {
 stack = new NamespaceStack();
 stack.push(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithEmptyStackAndNullElement() {
 stack = new NamespaceStack();
 stack.push(null);
 }

 @Test
 public void testPushWithScopeAndAddedNamespaces() {
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack = new NamespaceStack();
 stack.push(element);
 Namespace[] namespaceToAdd = {Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.NS_FOR_SOAP_BODY};
 List<Namespace> toadd = new ArrayList<>(Arrays.asList(namespaceToAdd));
 Namespace[] expectedScope = {Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.NS_FOR_SOAP_BODY, Namespace.XML_NAMESPACE};
 Namespace[] expectedAdded = namespaceToAdd.clone();
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 Assert.assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 Assert.assertEquals(2, stack.getDepth());
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 stack.pop();
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
 Namespace[] expectedAdded = {};
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 Assert.assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 Assert.assertEquals(0, stack.getDepth());
 }

 @Test
 public void testGetScope() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 Namespace[] expectedScope = {Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.XML_NAMESPACE};
 Assert.assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 Assert.assertTrue(stack.isInScope(Namespace.NS_FOR_SOAP_ENVELOPE));
 Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testAddedForwardIterator() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 Iterator<Namespace> iterator = stack.addedForward().iterator();
 Namespace[] expected = {Namespace.NS_FOR_SOAP_ENVELOPE};
 while (iterator.hasNext()) {
 Namespace ns = iterator.next();
 Assert.assertEquals(ns.getPrefix(), "ns0");
 Assert.assertEquals(ns.getURI(), Namespace.NS_FOR_SOAP_ENVELOPE.getURI());
 }
 }

 @Test
 public void testAddedReverseIterator() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 Iterator<Namespace> iterator = stack.addedReverse().iterator();
 Namespace[] expected = {Namespace.NS_FOR_SOAP_ENVELOPE};
 while (iterator.hasNext()) {
 Namespace ns = iterator.next();
 Assert.assertEquals(ns.getPrefix(), "ns0");
 Assert.assertEquals(ns.getURI(), Namespace.NS_FOR_SOAP_ENVELOPE.getURI());
 }
 }

 @Test
 public void testIterator() {
 stack = new NamespaceStack();
 Element element = new Element("root", Namespace.NS_FOR_SOAP_ENVELOPE);
 stack.push(element);
 Iterator<Namespace> iterator = stack.iterator();
 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.NS_FOR_SOAP_ENVELOPE};
 int i = 0;
 while (iterator.hasNext()) {
 Namespace ns = iterator.next();
 Assert.assertEquals(ns.getPrefix(), expected[i++].getPrefix());
 }
 }

 @Test
 public void testBinarySearch() {
 Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.NS_FOR_SOAP_BODY};
 int left = 0;
 int right = data.length - 1;
 Namespace key = Namespace.NS_FOR_SOAP_ENVELOPE;
 int expectedResult = 1;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testBinarySearchWithInvalidKey() {
 Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NS_FOR_SOAP_ENVELOPE, Namespace.NS_FOR_SOAP_BODY};
 int left = 0;
 int right = data.length - 1;
 Namespace key = new Namespace("non-existent-namespace");
 int expectedResult = -1;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 Assert.assertEquals(expectedResult, result);
 }
}