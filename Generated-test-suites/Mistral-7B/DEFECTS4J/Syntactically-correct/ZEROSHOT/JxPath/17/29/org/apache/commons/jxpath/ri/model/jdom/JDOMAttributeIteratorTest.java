package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class JDOMAttributeIteratorTest {

 private static final String XML_NAMESPACE_URI = "http://www.w3.org/XML/1998/namespace";
 private static final String EXAMPLE_PREFIX = "ex";
 private static final String EXAMPLE_URI = "http://example.com/";
 private static final String EXAMPLE_LOCAL_NAME = "exampleAttribute";
 private static final String EXAMPLE_NAME = new QName(EXAMPLE_URI, EXAMPLE_LOCAL_NAME);

 @org.junit.Test
 public void testAttributeIteratorEmpty() {
 final NodePointer emptyNodePointer = new JDOMNodePointer(null);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(emptyNodePointer, null);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testAttributeIteratorSingleAttribute() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute testAttribute = new Attribute("testAttribute", "testValue", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(testAttribute);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(1, iterator.size());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(testAttribute, iterator.next().getNode());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorMultipleAttributes() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute1 = new Attribute("attribute1", "value1", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute2 = new Attribute("attribute2", "value2", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute3 = new Attribute("attribute3", "value3", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(attribute1);
 testElement.addContent(attribute2);
 testElement.addContent(attribute3);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(3, iterator.size());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute1, iterator.next().getNode());
 assertSame(attribute2, iterator.next().getNode());
 assertSame(attribute3, iterator.next().getNode());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorDifferentNamespaces() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute1 = new Attribute("attribute1", "value1", new Namespace("ns1", "http://ns1.com/"));
 final Attribute attribute2 = new Attribute("attribute2", "value2", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(attribute1);
 testElement.addContent(attribute2);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(2, iterator.size());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.next().getNode().equals(attribute1));
 assertSame(attribute2, iterator.next().getNode());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorEmptyList() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(0, iterator.size());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorNullName() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute1 = new Attribute("attribute1", "value1", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(attribute1);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, null);

 assertEquals(1, iterator.size());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute1, iterator.next().getNode());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorMovingNext() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute1 = new Attribute("attribute1", "value1", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute2 = new Attribute("attribute2", "value2", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(attribute1);
 testElement.addContent(attribute2);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(2, iterator.size());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute1, iterator.next().getNode());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute2, iterator.next().getNode());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorMovingPrevious() {
 final Element testElement = new Element("testElement", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute1 = new Attribute("attribute1", "value1", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 final Attribute attribute2 = new Attribute("attribute2", "value2", new Namespace(EXAMPLE_PREFIX, EXAMPLE_URI));
 testElement.addContent(attribute1);
 testElement.addContent(attribute2);
 final NodePointer testNodePointer = new JDOMNodePointer(testElement);
 final JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, EXAMPLE_NAME);

 assertEquals(2, iterator.size());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute1, iterator.next().getNode());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next() instanceof JDOMAttributePointer);
 assertSame(attribute2, iterator.next().getNode());
 assertTrue(iterator.hasPrevious());
 assertTrue(iterator.previous() instanceof JDOMAttributePointer);
 assertSame(attribute1, iterator.previous().getNode());
 assertFalse(iterator.hasPrevious());
 }
}