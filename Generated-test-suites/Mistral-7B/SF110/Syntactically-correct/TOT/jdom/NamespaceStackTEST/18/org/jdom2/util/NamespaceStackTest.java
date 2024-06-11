package org.jdom2.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.attribute.Attribute;
import org.jdom2.jaxb.JDOMFromXML;
import org.jdom2.jaxb.JDOMToXML;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertNull(stack.getScope());
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("foo", "bar")));
 }

 @Test
 public void testPushAndPop() {
 stack.push(new Element("root"));
 stack.pop();
 assertNull(stack.getScope());

 stack.push(new Element("root"));
 stack.push(new Element("child"));
 stack.pop();
 stack.pop();
 assertNull(stack.getScope());

 stack.push(new Element("root"));
 stack.push(new Element("child"));
 stack.push(new Element("grandchild"));
 stack.pop();
 stack.pop();
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleElementsWithSamePrefix() {
 stack.push(new Element("root"));
 stack.push(new Element("root"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("", "root")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleElementsWithDifferentPrefixes() {
 stack.push(new Element("root"));
 stack.push(new Element("foo:bar"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("foo", "bar")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleElementsWithDifferentPrefixesAndSameNamespace() {
 stack.push(new Element("root"));
 stack.push(new Element("foo:bar"));
 stack.push(new Element("foo:baz"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("foo", "bar"), new Namespace("foo", "baz")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithEmptyAttributes() {
 stack.push(new Element("root"));
 assertNull(stack.getScope());

 stack.push(new Element("root", "name", "value"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithAttributesWithSamePrefix() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "attr1", "value1"));
 stack.push(new Element("root", "attr1", "value2"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("", "attr1")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithAttributesWithDifferentPrefixes() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "prefix1:attr1", "value1"));
 stack.push(new Element("root", "prefix2:attr2", "value2"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "attr1"), new Namespace("prefix2", "attr2")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithElementsAndAttributes() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "name", "value1"));
 stack.push(new Element("child"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("", "name")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithElementsAndAttributesWithSamePrefix() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "prefix:name1", "value1"));
 stack.push(new Element("root", "prefix:name2", "value2"));
 stack.push(new Element("child"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix", "name1"), new Namespace("prefix", "name2")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithElementsAndAttributesWithDifferentPrefixes() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "prefix1:name1", "value1"));
 stack.push(new Element("root", "prefix2:name2", "value2"));
 stack.push(new Element("child"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "name1"), new Namespace("prefix2", "name2")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushWithCustomNamespace() {
 stack.push(new Element("root"));
 stack.push(new Element("root", "foo:bar", "value"));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("foo", "bar")};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testSerializeDeserialize() throws Exception {
 String xml = "<root xmlns:foo=\"foo\" xmlns:bar=\"bar\"><child><grandchild/></child></root>";
 stack.push(JDOMFromXML.parse(new StringReader(xml)));
 Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("foo", "foo"), new Namespace("bar", "bar")};
 assertArrayEquals(expectedScope, stack.getScope());

 StringWriter writer = new StringWriter();
 JDOMToXML.serialize(stack.getScope()[0], stack.getAddedForward(), writer);
 String output = writer.toString();
 assertEquals(xml, output);
 }
}