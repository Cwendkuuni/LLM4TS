package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 // Create a sample XML document
 String xml = "<root xmlns:x='http://example.com/x' xmlns:y='http://example.com/y'>" +
 " <child xmlns:z='http://example.com/z'> attribute1='value1' attribute2='value2'/>" +
 " <child2 xmlns:z='http://example.com/z'> attribute3='value3'/>" +
 "</root>";

 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 Document document = null;
 try {
 document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
 } catch (Exception e) {
 e.printStackTrace();
 fail("Document not created");
 }

 element = (Element) document.getElementsByTagName("child").item(0);
 }

 @Test
 public void testIterateThroughAttributes() {
 // Test iteration through all attributes for a specific element
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), null);
 int position = 0;
 Attr expectedAttribute = null;
 while (attributeIterator.hasNext()) {
 Attr attr = (Attr) attributeIterator.next();
 assertEquals("Attribute values should match", expectedAttribute.getValue(), attr.getValue());
 position++;
 expectedAttribute = attributeIterator.getNext();
 }
 assertEquals("Number of attributes should match", 2, position);
 }

 @Test
 public void testIterateThroughAttributesWithName() {
 // Test iteration through attributes with a specific name
 QName name = new QName("{http://example.com/x}", "xmlns");
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), name);
 Attr expectedAttribute = null;
 int position = 0;
 while (attributeIterator.hasNext()) {
 Attr attr = (Attr) attributeIterator.next();
 if (attr.getName().equals(name)) {
 assertEquals("Attribute values should match", expectedAttribute.getValue(), attr.getValue());
 position++;
 expectedAttribute = attributeIterator.getNext();
 }
 }
 assertEquals("Number of attributes with the specified name should match", 1, position);
 }

 @Test
 public void testIterateThroughAttributesWithNamespaceAndLocalName() {
 // Test iteration through attributes with a specific namespace and local name
 QName name = new QName("{http://example.com/x}", "xmlns:z");
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), name);
 Attr expectedAttribute = null;
 int position = 0;
 while (attributeIterator.hasNext()) {
 Attr attr = (Attr) attributeIterator.next();
 if (attr.getName().getLocalPart().equals("z")) {
 assertEquals("Attribute values should match", expectedAttribute.getValue(), attr.getValue());
 position++;
 expectedAttribute = attributeIterator.getNext();
 }
 }
 assertEquals("Number of attributes with the specified namespace and local name should match", 1, position);
 }

 @Test(expected = IllegalStateException.class)
 public void testIterateThroughAttributesWithInvalidName() {
 // Test iteration through attributes with an invalid name
 QName name = new QName("{http://example.com/x}", "xmlns:invalid");
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), name);
 attributeIterator.next(); // Advance to the first attribute
 try {
 attributeIterator.next();
 fail("Should throw an IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratePastEndOfAttributes() {
 // Test iteration past the end of the attributes
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), null);
 while (attributeIterator.hasNext()) {
 attributeIterator.next();
 }
 try {
 attributeIterator.next();
 fail("Should throw an IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetPosition() {
 // Test getting the current position
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(document, element), null);
 int position = 0;
 while (attributeIterator.hasNext()) {
 attributeIterator.next();
 position++;
 }
 assertEquals("Position should match the number of attributes", 2, attributeIterator.getPosition());
 }
}