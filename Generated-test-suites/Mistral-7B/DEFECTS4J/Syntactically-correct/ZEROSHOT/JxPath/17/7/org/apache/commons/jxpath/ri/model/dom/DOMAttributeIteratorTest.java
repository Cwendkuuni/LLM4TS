package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodePointer;

public class DOMAttributeIteratorTest {

 private static final String TEST_XML = "<root xmlns:x='http://test.com'><child xmlns:y='http://test.com' attr1='value1' attr2='value2'/></root>";

 @Test
 public void testCreateIteratorWithWildcardName() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  assertEquals(2, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
  iterator.next();
 }

 @Test
 public void testCreateIteratorWithSpecificName() {
  DOMAttributeIterator iterator = new DOMAttribeIterator(new DOMPointer(TEST_XML), new QName("xmlns"));
  assertEquals(1, iterator.size());
  assertNull(iterator.getNodePointer());
 }

 @Test
 public void testCreateIteratorWithSpecificNameFromRoot() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), new QName("root", "xmlns:x"));
  assertEquals(1, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
 }

 @Test
 public void testCreateIteratorWithSpecificNameFromChild() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML, "/child"), new QName("child", "xmlns:y"));
  assertEquals(1, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
 }

 @Test
 public void testCreateIteratorWithSpecificNameFromChildWithNamespace() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML, "/child"), new QName("y", "attr1"));
  assertEquals(1, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
 }

 @Test
 public void testCreateIteratorWithSpecificNameFromChildWithPrefix() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML, "/child"), new QName("y", null, "attr1"));
  assertEquals(1, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
 }

 @Test
 public void testCreateIteratorWithSpecificNameFromChildWithPrefixAndNamespace() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML, "/child"), new QName("y", "http://test.com", "attr1"));
  assertEquals(1, iterator.size());
  assertNotNull(iterator.getNodePointer());
  iterator.next();
 }

 @Test
 public void testGetNextNodePointer() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  NodePointer nodePointer1 = iterator.getNodePointer();
  iterator.next();
  NodePointer nodePointer2 = iterator.getNodePointer();
  assertNotSame(nodePointer1, nodePointer2);
 }

 @Test
 public void testGetPosition() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  iterator.next();
  iterator.next();
  assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testSetPosition() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  iterator.next();
  iterator.next();
  iterator.setPosition(1);
  NodePointer nodePointer = iterator.getNodePointer();
  assertEquals(1, iterator.getPosition());
  assertNotNull(nodePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionOutOfBounds() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  iterator.next();
  iterator.next();
  iterator.setPosition(3);
 }

 @Test
 public void testGetSize() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(TEST_XML), null);
  assertEquals(2, iterator.size());
 }
}