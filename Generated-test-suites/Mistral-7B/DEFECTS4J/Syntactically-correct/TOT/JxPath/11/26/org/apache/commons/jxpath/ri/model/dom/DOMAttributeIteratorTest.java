package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 @Test
 public void testDOMAttributeIterator_AttributeExists_ReturnsAttribute() {
 Element element = new org.w3c.dom.Element.Implementation().createElement("root", "http://test.com");
 element.setAttribute("test", "testValue");
 Attr attribute = new Attr().implementation.createAttribute("test", "http://test.com");
 attribute.setValue("testValue");
 element.setAttribNode(attribute);
 NodePointer parent = new NodePointer(new org.w3c.dom.Document().implementation.createDocument("", "", null).getDocumentElement());
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com"));
 NodePointer attributePointer = iterator.next();
 assertEquals(attribute, attributePointer.getNode());
 }

 @Test
 public void testDOMAttributeIterator_AttributeDoesNotExist_ReturnsNull() {
 Element element = new org.w3c.dom.Element.Implementation().createElement("root", "http://test.com");
 NodePointer parent = new NodePointer(element);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com"));
 assertNull(iterator.next().getNode());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_ReturnsAll() {
 Element element = new org.w3c.dom.Element.Implementation().createElement("root", "http://test.com");
 element.setAttribute("test1", "testValue1");
 element.setAttribute("test2", "testValue2");
 Attr attribute1 = new Attr().implementation.createAttribute("test1", "http://test.com");
 attribute1.setValue("testValue1");
 element.setAttribNode(attribute1);
 Attr attribute2 = new Attr().implementation.createAttribute("test2", "http://test.com");
 attribute2.setValue("testValue2");
 element.setAttribNode(attribute2);
 NodePointer parent = new NodePointer(element);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "http://test.com"));
 NodePointer attributePointer1 = iterator.next();
 assertEquals(attribute1, attributePointer1.getNode());
 NodePointer attributePointer2 = iterator.next();
 assertEquals(attribute2, attributePointer2.getNode());
 assertNull(iterator.next().getNode());
 }

 @Test
 public void testDOMAttributeIterator_NamespacePrefix_ReturnsAttribute() {
 Element element = new org.w3c.dom.Element.Implementation().createElement("root", "http://test.com");
 element.setAttributeNS("xmlns:p", "p:test", "testValue");
 Attr attribute = new Attr();
 attribute.setPrefix("p");
 attribute.setLocalName("test");
 attribute.setNamespaceURI("http://test.com");
 attribute.setValue("testValue");
 element.setAttribNode(attribute);
 NodePointer parent = new NodePointer(new org.w3c.dom.Document().implementation.createDocument("", "", null).getDocumentElement());
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("p:test", "http://test.com"));
 NodePointer attributePointer = iterator.next();
 assertEquals(attribute, attributePointer.getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIterator_InvalidQName_ThrowsIllegalArgumentException() {
 NodePointer parent = new NodePointer(new org.w3c.dom.Document().implementation.createDocument("", "", null).getDocumentElement());
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIterator_InvalidQName_ThrowsIllegalArgumentException() {
 NodePointer parent = new NodePointer(new org.w3c.dom.Document().implementation.createDocument("", "", null).getDocumentElement());
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null));
 }
}