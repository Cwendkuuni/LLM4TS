package org.jdom2.util;

import org.jdom2.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;

 @Before
 public void setup() {
 Namespace xmlNamespace = Namespace.getXmlNamespace();
 Namespace myNamespace = new Namespace("my", "http://example.com");
 Namespace otherNamespace = new Namespace("other", "http://otherexample.com");

 element = new Element("root", null, null);
 element.setNamespace(xmlNamespace);
 element.addNamespaceDeclaration(myNamespace.getNamespaceString());
 element.addNamespaceDeclaration(otherNamespace.getNamespaceString());

 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 assertFalse(stack.isInScope(myNamespace));
 assertFalse(stack.isInScope(otherNamespace));
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 Namespace[] expectedScope = {xmlNamespace, myNamespace, otherNamespace};
 assertArrayEquals(expectedScope, stack.getScope());
 assertTrue(stack.isInScope(xmlNamespace));
 assertTrue(stack.isInScope(myNamespace));
 assertTrue(stack.isInScope(otherNamespace));
 }

 @Test
 public void testPushSameNamespaceTwice() {
 Element sameNamespaceElement = new Element("root", null, null);
 sameNamespaceElement.setNamespace(myNamespace);
 stack.push(element);
 stack.push(sameNamespaceElement);
 assertArrayEquals(expectedScope, stack.getScope());
 // The stack should not contain duplicate namespaces
 assertArrayEquals(Arrays.copyOfRange(expectedScope, 1, expectedScope.length), stack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testPushWithNoAdditionalNamespaces() {
 stack.push(element);
 stack.push(new Element("root2", null, null));
 assertArrayEquals(expectedScope2, stack.getScope());
 // The stack should not contain duplicate namespaces
 assertArrayEquals(Arrays.copyOfRange(expectedScope2, 1, expectedScope2.length), stack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 Element elementWithAdditionalNamespaces = new Element("root3", null, null);
 Namespace additionalNamespace = new Namespace("additional", "http://additionalexample.com");
 elementWithAdditionalNamespaces.setNamespace(xmlNamespace);
 elementWithAdditionalNamespaces.addNamespaceDeclaration(myNamespace.getNamespaceString());
 elementWithAdditionalNamespaces.addNamespaceDeclaration(otherNamespace.getNamespaceString());
 elementWithAdditionalNamespaces.addNamespaceDeclaration(additionalNamespace.getNamespaceString());

 stack.push(element);
 stack.push(elementWithAdditionalNamespaces);
 assertArrayEquals(expectedScope3, stack.getScope());
 // The stack should contain all namespaces, including additional ones
 assertArrayEquals(Arrays.copyOfRange(expectedScope3, 1, expectedScope3.length), stack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(elementWithAdditionalNamespaces);
 stack.pop();
 assertArrayEquals(expectedScope2, stack.getScope());
 // The popped namespace should be removed from the stack
 assertFalse(stack.isInScope(additionalNamespace));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 stack.pop();
 }

 private Namespace[] expectedScope = {xmlNamespace, myNamespace, otherNamespace};
 private Namespace[] expectedScope2 = {xmlNamespace, myNamespace};
 private Namespace[] expectedScope3 = {xmlNamespace, myNamespace, otherNamespace, additionalNamespace};

}