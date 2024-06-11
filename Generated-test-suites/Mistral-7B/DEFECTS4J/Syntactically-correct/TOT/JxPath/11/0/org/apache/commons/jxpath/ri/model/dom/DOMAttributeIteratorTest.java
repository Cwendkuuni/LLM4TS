package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

 private Element testElement;

 @Before
 public void setUp() {
 testElement = createTestElement();
 }

 @After
 public void tearDown() {
 testElement = null;
 }

 /**
 * Test constructor with specific attribute name.
 */
 @Test
 public void testConstructorSpecificAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 DOMAttributePointer pointer = (DOMAttributePointer) iterator.getNodePointer();
 assertEquals("test", pointer.getName().getLocalPart());
 assertEquals("attribute", pointer.getName().getPrefix());
 }

 /**
 * Test constructor with wildcard attribute name.
 */
 @Test
 public void testConstructorWildcardAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("*"));
 DOMAttributePointer pointer = (DOMAttributePointer) iterator.getNodePointer();
 assertNull(pointer.getName());
 }

 /**
 * Test getNodePointer method with valid position.
 */
 @Test
 public void testGetNodePointerValidPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 DOMAttributePointer pointer = (DOMAttributePointer) iterator.getNodePointer();
 assertSame(pointer, iterator.getNodePointer());
 }

 /**
 * Test getNodePointer method with invalid position.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointerInvalidPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 iterator.setPosition(2);
 iterator.getNodePointer();
 }

 /**
 * Test setPosition method with valid position.
 */
 @Test
 public void testSetPositionValidPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 iterator.setPosition(2);
 assertTrue(iterator.setPosition(2));
 }

 /**
 * Test setPosition method with invalid position.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionInvalidPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 iterator.setPosition(3);
 }

 /**
 * Test getPosition method.
 */
 @Test
 public void testGetPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 iterator.setPosition(2);
 assertEquals(2, iterator.getPosition());
 }

 /**
 * Test equals method with same iterator.
 */
 @Test
 public void testEqualsSameIterator() {
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 DOMAttributeIterator iterator2 = iterator1;
 assertTrue(iterator1.equals(iterator2));
 }

 /**
 * Test equals method with different iterator.
 */
 @Test
 public void testEqualsDifferentIterator() {
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test2", "attribute"));
 assertFalse(iterator1.equals(iterator2));
 }

 /**
 * Test hashCode method.
 */
 @Test
 public void testHashCode() {
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test", "attribute"));
 assertEquals(iterator1.hashCode(), iterator2.hashCode());
 }

 /**
 * Test createTestElement method.
 */
 private Element createTestElement() {
 Element element = testElement = document.createElement("test");
 element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:test", "test-namespace");
 element.setAttributeNode(element.createAttribute("test", "attribute1"));
 element.setAttributeNode(element.createAttribute("test", "attribute2"));
 return element;
 }
}