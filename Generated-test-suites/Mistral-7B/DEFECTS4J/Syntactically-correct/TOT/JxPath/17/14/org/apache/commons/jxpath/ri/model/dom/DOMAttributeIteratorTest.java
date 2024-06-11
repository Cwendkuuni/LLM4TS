package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator iterator;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = DOMParser.newDocument();
 String xml = "<root xmlns:x='http://example.com/'>\n" +
 "\t<element x:attr1='value1'>\n" +
 "\t\t<subElement attr2='value2' xmlns:y='http://example.org/'>\n" +
 "\t\t\t<grandSubElement attr3='value3'/>\n" +
 "\t\t</subElement>\n" +
 "\t</element>\n" +
 "</root>";
 document.loadXML(xml);
 parentNode = new DOMPointer(document.getDocumentElement());
 }

 @Test
 public void testIteratorWithWildcardName() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 assertNotNull(iterator);
 Attr attr = iterator.getNodePointer().getNode();
 assertNotNull(attr);
 }

 @Test
 public void testIteratorWithSpecificName() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "attr1"));
 assertNotNull(iterator);
 Attr attr = iterator.getNodePointer().getNode();
 assertNotNull(attr);
 assertEquals("value1", attr.getNodeValue());
 }

 @Test
 public void testIteratorWithNamespacePrefix() {
 iterator = new DOMAttributeIterator(parentNode, new QName("x", "*"));
 assertNotNull(iterator);
 Attr attr = iterator.getNodePointer().getNode();
 assertNotNull(attr);
 assertEquals("attr1", attr.getNodeName().getLocalPart());
 assertEquals("value1", attr.getNodeValue());
 }

 @Test
 public void testIteratorWithNamespaceURI() {
 iterator = new DOMAttributeIterator(parentNode, new QName("http://example.com/", "*"));
 assertNotNull(iterator);
 Attr attr = iterator.getNodePointer().getNode();
 assertNotNull(attr);
 assertEquals("attr1", attr.getNodeName().getLocalPart());
 assertEquals("value1", attr.getNodeValue());
 }

 @Test
 public void testIteratorWithNoMatchingAttribute() {
 iterator = new DOMAttributeIterator(parentNode, new QName("z", "*"));
 assertNull(iterator.getNodePointer().getNode());
 }

 @Test
 public void testIteratorWithNoAttributes() {
 Element element = document.createElement("element");
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("", "*"));
 assertNull(iterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParent() {
 iterator = new DOMAttributeIterator(null, new QName("", "*"));
 // the exception will be thrown in the constructor
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullName() {
 iterator = new DOMAttributeIterator(parentNode, null);
 // the exception will be thrown in the constructor
 }

}