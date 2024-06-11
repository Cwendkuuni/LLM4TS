package org.jdom2.util;

import org.jdom2.*;
import org.jdom2. Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 element = new Element("test", new QName("http://test.com", "test"));
 attribute = new Attribute("attr", new QName("http://test.com/attr", "attr"));
 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertEquals(0, stack.size());
 assertNull(stack.getScope());
 }

 @Test
 public void testPushElementWithSingleNamespace() {
 stack.push(element);
 Namespace[] expected = {element.getNamespace()};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushElementWithMultipleNamespaces() {
 stack.push(element);
 element.getAdditionalNamespaces().add(new Namespace("test2", "http://test2.com"));
 stack.push(element);
 Namespace[] expected = {element.getNamespace(), element.getAdditionalNamespaces().get(0)};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushAttributeWithSingleNamespace() {
 stack.push(attribute);
 Namespace[] expected = {attribute.getNamespace()};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPushAttributeWithMultipleNamespaces() {
 stack.push(attribute);
 Attribute anotherAttribute = new Attribute("anotherAttr", new QName("http://test2.com/anotherAttr", "anotherAttr"));
 stack.push(anotherAttribute);
 Namespace[] expected = {attribute.getNamespace(), anotherAttribute.getNamespace()};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 stack.push(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullAttribute() {
 stack.push(null);
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(element);
 stack.pop();
 Namespace[] expected = {element.getNamespace()};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testPopMultipleTimes() {
 stack.push(element);
 stack.push(element);
 stack.push(element);
 stack.pop();
 stack.pop();
 Namespace[] expected = {element.getNamespace()};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack.pop();
 }

 @Test
 public void testIsInScope() {
 stack.push(element);
 Namespace test = new Namespace("test", "http://test.com");
 assertTrue(stack.isInScope(test));
 }

 @Test
 public void testIsInScopeWhenNotInScope() {
 stack.push(element);
 Namespace notInScope = new Namespace("notInScope", "http://not-in-scope.com");
 assertFalse(stack.isInScope(notInScope));
 }

 @Test
 public void testAddedForward() {
 stack.push(element);
 stack.push(element);
 List<Namespace> namespaces = Arrays.asList(stack.addedForward().toArray(new Namespace[0]));
 List<Namespace> expected = new ArrayList<>();
 expected.add(element.getNamespace());
 expected.addAll(element.getAdditionalNamespaces());
 assertEquals(expected, namespaces);
 }

 @Test
 public void testAddedReverse() {
 stack.push(element);
 stack.push(element);
 List<Namespace> namespaces = Arrays.asList(stack.addedReverse().toArray(new Namespace[0]));
 List<Namespace> expected = new ArrayList<>();
 expected.addAll(element.getAdditionalNamespaces());
 expected.add(element.getNamespace());
 assertEquals(expected, namespaces);
 }
}