package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.JdomNamespaces;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Namespace[] seed = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE};

 @Before
 public void setUp() {
 stack = new NamespaceStack(seed);
 }

 @After
 public void tearDown() {
 stack.pop();
 }

 @Test
 public void testConstructor() {
 stack = new NamespaceStack();
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 assertArrayEquals(NamespaceStack.EMPTY, stack.addedForward().toArray());
 }

 @Test
 public void testPushElement() {
 Element element = new Element("root", "root");
 stack.push(element);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, element.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.add(element.getNamespace());
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("attr", "attrValue");
 stack.push(attribute);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, attribute.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.add(attribute.getNamespace());
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleNamespaces() {
 Element element = new Element("root", "root");
 element.setName("root:root");
 Attribute attribute = new Attribute("attr", "attrValue");
 attribute.setNamespace(JdomNamespaces.XPATH_NAMESPACE);
 stack.push(element);
 stack.push(attribute);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, element.getNamespace(), attribute.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(element.getNamespace(), attribute.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() {
 Element element1 = new Element("root", "root");
 Element element2 = new Element("root", "root");
 stack.push(element1);
 stack.push(element2);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, element1.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(element1.getNamespace(), element2.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleAttributesWithSameNamespace() {
 Attribute attribute1 = new Attribute("attr1", "attrValue1");
 attribute1.setNamespace(JdomNamespaces.XPATH_NAMESPACE);
 Attribute attribute2 = new Attribute("attr2", "attrValue2");
 attribute2.setNamespace(JdomNamespaces.XPATH_NAMESPACE);
 stack.push(attribute1);
 stack.push(attribute2);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, attribute1.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(attribute1.getNamespace(), attribute2.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleElementsWithDifferentNamespaces() {
 Element element1 = new Element("root1", "root1");
 Element element2 = new Element("root2", "root2");
 stack.push(element1);
 stack.push(element2);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, element1.getNamespace(), element2.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(element1.getNamespace(), element2.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleAttributesWithDifferentNamespaces() {
 Attribute attribute1 = new Attribute("attr1", "attrValue1");
 attribute1.setNamespace(JdomNamespaces.XPATH_NAMESPACE);
 Attribute attribute2 = new Attribute("attr2", "attrValue2");
 attribute2.setNamespace(Namespace.NO_NAMESPACE);
 stack.push(attribute1);
 stack.push(attribute2);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, attribute1.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(attribute1.getNamespace(), attribute2.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testPushMultipleElementsAndAttributes() {
 Element element = new Element("root", "root");
 element.setName("root:root");
 Attribute attribute = new Attribute("attr", "attrValue");
 attribute.setNamespace(JdomNamespaces.XPATH_NAMESPACE);
 stack.push(element);
 stack.push(attribute);
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, element.getNamespace(), attribute.getNamespace()};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 List<Namespace> expectedAdded = new ArrayList<>();
 expectedAdded.addAll(Arrays.asList(element.getNamespace(), attribute.getNamespace()));
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testIterableAddedForward() {
 stack.push(new Element("root", "root"));
 stack.push(new Attribute("attr", "attrValue"));
 List<Namespace> expectedAdded = Arrays.asList(Namespace.NO_NAMESPACE, JdomNamespaces.XML_NAMESPACE, Namespace.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE);
 List<Namespace> actualAddedForward = stack.addedForward().toList();
 assertEquals(expectedAdded, actualAddedForward);
 }

 @Test
 public void testIterableAddedReverse() {
 stack.push(new Element("root", "root"));
 stack.push(new Attribute("attr", "attrValue"));
 List<Namespace> expectedAdded = Arrays.asList(JdomNamespaces.XPATH_NAMESPACE, JdomNamespaces.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE);
 List<Namespace> actualAddedReverse = stack.addedReverse().toList();
 assertEquals(expectedAdded, actualAddedReverse);
 }

 @Test
 public void testIterator() {
 stack.push(new Element("root", "root"));
 stack.push(new Attribute("attr", "attrValue"));
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> expected = Arrays.asList(JdomNamespaces.XML_NAMESPACE, Namespace.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE, Namespace.NO_NAMESPACE);
 List<Namespace> actual = new ArrayList<>();
 while (iterator.hasNext()) {
 actual.add(iterator.next());
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testPop() {
 stack.push(new Element("root", "root"));
 stack.push(new Attribute("attr", "attrValue"));
 stack.pop();
 Namespace[] expectedScope = {JdomNamespaces.XML_NAMESPACE, Namespace.XML_NAMESPACE, JdomNamespaces.XPATH_NAMESPACE};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }
}