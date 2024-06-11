package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Document;
import org.jdom2.JDOMWriter;
import org.jdom2.output.Format;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private JDOMAttributeIterator iterator;

 @Before
 public void setUp() throws JDOMException, InstantiationException, IllegalAccessException {
 Element rootElement = new Element("root");
 rootElement.addAttribute("id", "1");
 rootElement.addAttribute("attr1", "value1");
 rootElement.addAttribute("attr2", "value2");
 rootElement.addAttribute("attr3", "value3");
 rootElement.addAttribute("xml:lang", "en");
 Document document = new Document(rootElement);
 parentNode = new NodePointer(document.getRootElement());
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(null, null);
 assertEquals(0, emptyIterator.getPosition());
 assertNull(emptyIterator.getNodePointer());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parentNode, new QName("attr1"));
 assertEquals(1, iterator1.getPosition());
 assertSame("value1", iterator1.getNodePointer().getNodeName().getValue());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parentNode, new QName("*"));
 assertEquals(4, iterator2.getPosition());
 assertSame("id", iterator2.getNodePointer().getNodeName().getLocalPart());
 iterator2.next();
 assertSame("attr1", iterator2.getNodePointer().getNodeName().getLocalPart());
 iterator2.next();
 assertSame("attr2", iterator2.getNodePointer().getNodeName().getLocalPart());
 iterator2.next();
 assertSame("attr3", iterator2.getNodePointer().getNodeName().getLocalPart());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorOutOfBounds() {
 JDOMAttributeIterator iterator3 = new JDOMAttributeIterator(parentNode, new QName("attr1"));
 iterator3.next();
 iterator3.next();
 iterator3.next();
 iterator3.next();
 iterator3.next();
 }

 @Test
 public void testIteratorWithNamespace() {
 JDOMAttributeIterator iterator4 = new JDOMAttributeIterator(parentNode, new QName("xml:lang"));
 assertEquals(1, iterator4.getPosition());
 assertSame("en", iterator4.getNodePointer().getNodeName().getValue());
 }

 @Test
 public void testIteratorWithEmptyNamespace() {
 JDOMAttributeIterator iterator5 = new JDOMAttributeIterator(parentNode, new QName("", "xml:lang"));
 assertEquals(1, iterator5.getPosition());
 assertSame("en", iterator5.getNodePointer().getNodeName().getValue());
 }

 @Test
 public void testIteratorWithNonExistingAttribute() {
 JDOMAttributeIterator iterator6 = new JDOMAttributeIterator(parentNode, new QName("nonExistingAttribute"));
 assertEquals(0, iterator6.getPosition());
 assertNull(iterator6.getNodePointer());
 }

 @Test
 public void testIteratorWithNullParentNode() {
 JDOMAttributeIterator iterator7 = new JDOMAttributeIterator(null, new QName("attr1"));
 assertNull(iterator7);
 }

 @Test
 public void testIteratorWithNullQName() {
 JDOMAttributeIterator iterator8 = new JDOMAttributeIterator(parentNode, null);
 assertNull(iterator8);
 }
}