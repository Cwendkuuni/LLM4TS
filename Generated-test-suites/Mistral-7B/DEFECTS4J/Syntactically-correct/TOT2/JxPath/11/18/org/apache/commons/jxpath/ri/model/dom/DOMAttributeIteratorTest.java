package org.junit.test;

import org.apache.commons.jxpath.ri.model.DOMPoint;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.QName;
import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {
   @Test
   public void testDOMAttributeIteratorConstructor() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      assertNull(iterator.getNodePointer());
      // Add a test attribute to the element
      Attr testAttribute = element.setAttributeNode(new Attr("testAttribute", "testValue"));
      iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
      assertEquals("testAttribute", attributePointer.getName().getLocalPart());
      assertEquals("testValue", attributePointer.getValue());
      assertEquals(element, attributePointer.getNode());
   }

   @Test
   public void testgetNodePointer() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      iterator.next();
      DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
      assertEquals("testName", attributePointer.getName().getLocalPart());
      assertEquals("testValue", attributePointer.getValue());
      assertEquals(element, attributePointer.getNode());
   }

   @Test(expected = NoSuchElementException.class)
   public void testgetNodePointerEndOfIterator() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      iterator.next();
      iterator.next();
      iterator.getNodePointer();
   }

   @Test
   public void testSetPosition() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      iterator.next();
      iterator.next();
      assertTrue(iterator.setPosition(1));
      DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
      assertEquals("testName", attributePointer.getName().getLocalPart());
      assertEquals("testValue", attributePointer.getValue());
      assertEquals(element, attributePointer.getNode());
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testSetPositionNegative() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      iterator.next();
      iterator.next();
      iterator.setPosition(-1);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testSetPositionBeyondEnd() {
      Element element = new Element("testElement");
      QName name = new QName("testName");
      DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);
      iterator.next();
      iterator.next();
      iterator.next();
      iterator.setPosition(3);
   }
}