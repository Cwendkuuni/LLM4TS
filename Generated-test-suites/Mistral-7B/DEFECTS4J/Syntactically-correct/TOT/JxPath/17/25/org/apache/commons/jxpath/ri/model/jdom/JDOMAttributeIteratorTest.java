package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 NodeIterator iterator = new JDOMAttributeIterator(null, null);
 assertFalse("Empty iterator should not have a node", iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 NodePointer parent = new NodePointer(context.getDocument(), context.getDocument().getRootElement());
 NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "xml:id", ""));
 assertTrue("Single attribute iterator should have a node", iterator.hasNext());
 NodePointer attributePointer = iterator.next();
 assertEquals("Single attribute pointer should point to the expected attribute", "attribute1", attributePointer.getNode().getValue());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 Element root = context.getDocument().getRootElement();
 NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(context, root), new QName("", "class", ""));
 assertTrue("Multiple attributes iterator should have nodes", iterator.hasNext());
 int count = 0;
 while (iterator.hasNext()) {
 NodePointer attributePointer = iterator.next();
 assertEquals("Multiple attribute pointers should point to expected attributes", "attribute" + (++count), attributePointer.getNode().getName());
 }
 assertEquals("Multiple attribute iterator should have the correct number of attributes", 3, count);
 }

 @Test
 public void testIteratorAfterLastElement() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 NodePointer parent = new NodePointer(context, context.getDocument().getRootElement());
 NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "xml:id", ""));
 iterator.next();
 assertFalse("Iterator should not have a node after the last element", iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParent() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 new JDOMAttributeIterator(null, new QName("xml", "xml:id", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 new JDOMAttributeIterator(new NodePointer(context, context.getDocument().getRootElement()), null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionBeforeFirstElement() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 NodePointer parent = new NodePointer(context, context.getDocument().getRootElement());
 NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "xml:id", ""));
 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionAfterLastElement() throws JDOMException {
 JXPathContext context = new JXPathContext(createTestDocument());
 NodePointer parent = new NodePointer(context, context.getDocument().getRootElement());
 NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "xml:id", ""));
 iterator.next();
 iterator.setPosition(iterator.getPosition() + 1);
 iterator.setPosition(iterator.getPosition() + 1);
 }

 private Element createTestDocument() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 root.addAttribute("attribute3", "value3");
 root.addAttribute("xml:id", "root");
 return root;
 }

}