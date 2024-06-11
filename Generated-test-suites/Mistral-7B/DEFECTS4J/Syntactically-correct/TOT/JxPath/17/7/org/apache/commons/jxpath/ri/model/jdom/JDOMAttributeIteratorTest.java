import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JDOMAttributeIteratorTest {

 private NodePointer rootNodePointer;
 private Element rootElement;

 @Before
 public void setUp() {
 String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
 "<child1 attr1=\"value1\" attr2=\"value2\" xsi:attr3=\"value3\"/>" +
 "<child2 attr1=\"setValue1\"/>" +
 "</root>";

 rootElement = new org.jdom.JDOMParser().parse(new java.io.StringReader(xml)).getDocument().getRootElement();
 rootNodePointer = new NodePointer(rootElement);
 }

 @Test
 public void testAttributeIteratorWithNameAndNamespace() {
 QName attrName = new QName("xsi", "attr3");
 NodeIterator nodeIterator = new JDOMAttributeIterator(rootNodePointer, attrName);
 NodePointer nodePointer = nodeIterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertEquals("attr3", nodePointer.getNode().getName());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nodePointer.getNode().getNamespaceURI());
 }

 @Test
 public void testAttributeIteratorWithNameOnly() {
 QName attrName = new QName("attr1");
 NodeIterator nodeIterator = new JDOMAttributeIterator(rootNodePointer, attrName);
 NodePointer nodePointer = nodeIterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertEquals("attr1", nodePointer.getNode().getName());
 assertEquals("", nodePointer.getNode().getNamespaceURI());
 }

 @Test
 public void testAttributeIteratorWithEmptyList() {
 NodeIterator nodeIterator = new JDOMAttributeIterator(rootNodePointer, new QName("nonExistentName"));
 assertNull(nodeIterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorWithNegativePosition() {
 NodeIterator nodeIterator = new JDOMAttributeIterator(rootNodePointer, new QName("attr1"));
 nodeIterator.setPosition(-1);
 assertNull(nodeIterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorWithOutOfBoundPosition() {
 NodeIterator nodeIterator = new JDOMAttributeIterator(rootNodePointer, new QName("attr1"));
 nodeIterator.setPosition(3);
 assertNull(nodeIterator.getNodePointer());
 }
}