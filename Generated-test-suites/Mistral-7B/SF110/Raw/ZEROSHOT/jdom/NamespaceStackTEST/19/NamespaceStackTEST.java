package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.InternalNames;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 Namespace defaultNamespace = new Namespace("default", "");
 Namespace xmlNamespace = new Namespace("xmlns", "http://www.w3.org/2000/xmlns/");
 Namespace[] seed = {defaultNamespace, xmlNamespace};
 stack = new NamespaceStack(seed);
 }

 @Test
 public void testEmptyStack() {
 assertEquals(0, stack.depth);
 assertArrayEquals(new Namespace[0], stack.getScope());
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testPushAndPop() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns2));
 stack.pop();
 stack.pop();

 assertEquals(-1, stack.depth);
 assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns1));
 stack.push(new Element("element3", ns1));

 assertEquals(3, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(4, scope.length);
 assertArrayEquals(new Namespace[]{ns1, ns1, ns1, ns1}, scope);
 }

 @Test
 public void testPushMultipleElementsWithDifferentNamespaces() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns2));
 stack.push(new Element("element3", ns1));

 assertEquals(3, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(4, scope.length);
 assertArrayEquals(new Namespace[]{ns2, ns1, ns1, ns1}, scope);
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 Namespace ns3 = new Namespace("ns3", "http://www.example.com/ns3");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1, InternalNames.XSI_NAMESPACE, ns2));
 stack.push(new Element("element2", ns1, ns3));

 assertEquals(3, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(5, scope.length);
 assertArrayEquals(new Namespace[]{ns2, ns1, ns3, ns1, ns1}, scope);
 }

 @Test
 public void testPushAttributeWithAdditionalNamespace() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Attribute("attribute1", "value1", ns1));
 stack.push(new Attribute("attribute2", "value2", ns2));

 assertEquals(2, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(3, scope.length);
 assertArrayEquals(new Namespace[]{ns2, ns1, ns1}, scope);
 }

 @Test
 public void testPushMultipleAttributesWithSameNamespace() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 stack.push(new Element("root"));
 stack.push(new Attribute("attribute1", "value1", ns1));
 stack.push(new Attribute("attribute2", "value2", ns1));
 stack.push(new Attribute("attribute3", "value3", ns1));

 assertEquals(3, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(4, scope.length);
 assertArrayEquals(new Namespace[]{ns1, ns1, ns1, ns1}, scope);
 }

 @Test
 public void testPushMultipleAttributesWithDifferentNamespaces() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Attribute("attribute1", "value1", ns1));
 stack.push(new Attribute("attribute2", "value2", ns2));
 stack.push(new Attribute("attribute3", "value3", ns1));

 assertEquals(3, stack.depth);
 Namespace[] scope = stack.getScope();
 assertEquals(4, scope.length);
 assertArrayEquals(new Namespace[]{ns2, ns1, ns1, ns1}, scope);
 }

 @Test
 public void testIterator() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns2));
 stack.push(new Element("element3", ns1));

 List<Namespace> expectedResult = new ArrayList<>();
 expectedResult.add(ns2);
 expectedResult.add(ns1);
 expectedResult.add(ns1);
 expectedResult.add(ns1);

 List<Namespace> actualResult = new ArrayList<>();
 for (Namespace namespace : stack.iterator()) {
 actualResult.add(namespace);
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddedForward() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns2));
 stack.push(new Element("element3", ns1));

 List<Namespace> expectedResult = new ArrayList<>();
 expectedResult.addAll(Arrays.asList(ns1, ns2));
 List<Namespace> actualResult = new ArrayList<>();
 actualResult.addAll(stack.addedForward());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddedReverse() {
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");
 stack.push(new Element("root"));
 stack.push(new Element("element1", ns1));
 stack.push(new Element("element2", ns2));
 stack.push(new Element("element3", ns1));

 List<Namespace> expectedResult = new ArrayList<>();
 expectedResult.addAll(Arrays.asList(ns1, ns2));
 List<Namespace> actualResult = new ArrayList<>();
 actualResult.addAll(stack.addedReverse());
 assertEquals(expectedResult, actualResult);
 }
}