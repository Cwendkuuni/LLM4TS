package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeIterator;

public class DOMAttributeIteratorTest {

 private Node createElementWithAttribute(String tagName, String attrName, String attrValue) {
  Document doc = TestHelper.newDocument();
  Element element = doc.createElement(tagName);
  Attr attr = doc.createAttribute(attrName);
  attr.setValue(attrValue);
  element.setAttributeNode(attr);
  return element;
 }

 @Test
 public void testEmptyIterator() {
  DOMAttributeIterator iterator = new DOMAttributeIterator(TestHelper.getDocumentNodePointer(null), null);
  assertFalse(iterator.hasNext());
  assertNull(iterator.next());
 }

 @Test
 public void testIteratorSingleAttribute() {
  String tagName = "test";
  String attrName = "testAttr";
  String attrValue = "testValue";

  NodePointer parentNodePointer = TestHelper.getDocumentNodePointer(null);
  NodeIterator it = new DOMAttributeIterator(parentNodePointer, new QName(null, null, attrName));

  Attr expectedAttribute = createElementWithAttribute(tagName, attrName, attrValue).getAttributes().item(0);
  NodePointer expectedNodePointer = new DOMAttributePointer(parentNodePointer, expectedAttribute);

  assertTrue(it.hasNext());
  NodePointer nextNodePointer = it.next();
  assertEquals(expectedNodePointer, nextNodePointer);
  assertFalse(it.hasNext());
 }

 @Test
 public void testIteratorMultipleAttributes() {
  String tagName = "test";
  String attrName1 = "testAttr1";
  String attrValue1 = "testValue1";
  String attrName2 = "testAttr2";
  String attrValue2 = "testValue2";

  NodePointer parentNodePointer = TestHelper.getDocumentNodePointer(null);
  NodeIterator it = new DOMAttributeIterator(parentNodePointer, new QName(null, null, "*"));

  Element testElement = createElementWithAttribute(tagName, attrName1, attrValue1);
  testElement.setAttributeNodeNS(testElement.createAttribute("xmlns:x", "x"), "xmlns:x");
  testElement.setAttributeNodeNS(testElement.createAttribute("xmlns:y", "y"), "xmlns:y");
  Element childElement = createElementWithAttribute(tagName, attrName2, attrValue2);
  testElement.appendChild(childElement);

  List<Attr> expectedAttributes = new ArrayList<>();
  expectedAttributes.add(testElement.getAttributes().item(0));
  expectedAttributes.add(testElement.getAttributes().item(1));
  expectedAttributes.add(childElement.getAttributes().item(0));

  for (int i = 0; i < expectedAttributes.size(); i++) {
    Attr expectedAttribute = expectedAttributes.get(i);
    NodePointer expectedNodePointer = new DOMAttributePointer(parentNodePointer, expectedAttribute);

    assertTrue(it.hasNext());
    NodePointer nextNodePointer = it.next();
    assertEquals(expectedNodePointer, nextNodePointer);
  }

  assertFalse(it.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorNullParentNodePointer() {
  new DOMAttributeIterator(null, new QName(null, null, "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorNullQName() {
  new DOMAttributeIterator(TestHelper.getDocumentNodePointer(null), null);
 }

 @Test
 public void testToString() {
  String tagName = "test";
  String attrName1 = "testAttr1";
  String attrValue1 = "testValue1";
  String attrName2 = "testAttr2";
  String attrValue2 = "testValue2";

  NodePointer parentNodePointer = TestHelper.getDocumentNodePointer(null);
  NodeIterator it = new DOMAttributeIterator(parentNodePointer, new QName(null, null, "*"));

  Element testElement = createElementWithAttribute(tagName, attrName1, attrValue1);
  testElement.setAttributeNodeNS(testElement.createAttribute("xmlns:x", "x"), "xmlns:x");
  testElement.setAttributeNodeNS(testElement.createAttribute("xmlns:y", "y"), "xmlns:y");
  Element childElement = createElementWithAttribute(tagName, attrName2, attrValue2);
  testElement.appendChild(childElement);

  List<Attr> attributes = new ArrayList<>();
  attributes.addAll(Arrays.asList(testElement.getAttributes()));
  attributes.addAll(Arrays.asList(childElement.getAttributes()));

  StringBuilder stringBuilder = new StringBuilder();
  for (int i = 0; i < attributes.size(); i++) {
    Attr attribute = attributes.get(i);
    stringBuilder.append(attribute.getNodeName());
    stringBuilder.append("=");
    stringBuilder.append(attribute.getValue());
    if (i < attributes.size() - 1) {
      stringBuilder.append(" ");
    }
  }

  String expectedString = "xmlns:x xmlns:y " + tagName + "." + attrName1 + "=" + attrValue1 + " " + tagName + "." + attrName2 + "=" + attrValue2;
  assertEquals(expectedString, it.toString());
 }
}