package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testemptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertArrayEquals(new Namespace[0], stack.getScope());
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testpushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.pop();
 assertArrayEquals(new Namespace[0], stack.getScope());

 NamespaceStack stack2 = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE});
 stack2.push(new Element("root"));
 stack2.push(new Element("element"));
 stack2.pop();
 stack2.pop();
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack2.getScope());
 }

 @Test
 public void testpushMultipleElements() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("element1"));
 stack.push(new Element("element2"));
 stack.push(new Element("element3"));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testpushWhenStackIsFull() {
 NamespaceStack stack = new NamespaceStack();
 for (int i = 0; i < 11; i++) {
 stack.push(new Element("element" + i));
 }
 }

 @Test
 public void testpushElementsWithSamePrefixButDifferentLocalName() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("element1", "prefix", "localName1"));
 stack.push(new Element("element2", "prefix", "localName2"));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix", "localName1"), new Namespace("prefix", "localName2")}, stack.getScope());
 }

 @Test
 public void testpushElementsWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("element1"));
 stack.push(new Element("element2"));
 stack.push(new Element("element3"));
 stack.push(new Element("element4", Namespace.XML_NAMESPACE));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
 }

 @Test
 public void testpushAttributes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("root", "attr1", "value1"));
 stack.push(new Element("root", "attr2", "value2"));
 stack.push(new Element("root", "attr3", "value3"));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, stack.getScope());
 }

 @Test
 public void testpushAttributesWithSamePrefixButDifferentLocalName() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("root", "attr1", "prefix1:localName1"));
 stack.push(new Element("root", "attr2", "prefix1:localName2"));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "localName1"), new Namespace("prefix1", "localName2")}, stack.getScope());
 }

 @Test
 public void testpushAttributesWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("root", "attr1", ""));
 stack.push(new Element("root", "attr2", ""));
 stack.push(new Element("root", "attr3", ""));
 stack.push(new Element("root", "attr4", ""));
 assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, stack.getScope());
 }

}