package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "xml";
 private static final String DEFAULT_NS = "default";
 private static final String TEST_NAMESPACE = "test";
 private static final String TEST_PREFIX = "tp";
 private static final String TEST_LOCAL_NAME = "testAttribute";

 @Test
 public void testEmptyIterator() throws JDOMException {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(new Element("root")), new QName(DEFAULT_NS, "*"));
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testSimpleAttributeIterator() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute(XML_NS, "xml:id", "id1"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(XML_NS, "xml:id"));
 assertEquals(1, iterator.getPosition());
 assertNotNull(iterator.next());
 assertEquals("id1", ((Attribute) iterator.next().getNode()).getValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute("ns1", "attr1", "value1"));
 element.addAttribute(new Attribute("ns1", "attr2", "value2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("ns1", "*"));
 assertEquals(2, iterator.getPosition());
 assertNotNull(iterator.next());
 assertEquals("attr1", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
 assertNotNull(iterator.next());
 assertEquals("attr2", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNamespacePrefixAttributeIterator() throws JDOMException {
 Element element = new Element("root");
 Namespace namespace = new Namespace(TEST_PREFIX, TEST_NAMESPACE);
 element.getNamespace().addNamespaceDeclaration(TEST_NAMESPACE, TEST_PREFIX);
 element.addAttribute(new Attribute(TEST_NS, TEST_LOCAL_NAME, "value1"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(TEST_NS, TEST_LOCAL_NAME));
 assertEquals(1, iterator.getPosition());
 assertNotNull(iterator.next());
 assertEquals(TEST_NS, iterator.next().getNode().getNamespace().getURI());
 assertEquals(TEST_LOCAL_NAME, iterator.next().getNode().getName().getLocalPart());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testEmptyAttributeListIterator() throws JDOMException {
 Element element = new Element("root");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("ns1", "nonExistentAttribute"));
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }
}