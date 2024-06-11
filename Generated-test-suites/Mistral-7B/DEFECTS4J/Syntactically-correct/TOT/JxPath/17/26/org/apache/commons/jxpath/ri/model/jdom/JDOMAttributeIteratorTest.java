import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDOMAttributeIterator.class)
public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private Element testElement;

 @Before
 public void setUp() {
 testElement = new Element("root");
 testElement.addAttribute("testAttribute1", "testValue1");
 testElement.addAttribute("testAttribute2", null);
 testElement.addAttribute("xml:xmlns:test", "testNamespace");
 parentNode = new NodePointer(testElement);
 }

 @Test
 public void testJDOMAttributeIteratorEmptyNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testJDOMAttributeIteratorSingleAttribute() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("testAttribute1"));
 assertEquals(1, iterator.getPosition());
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof JDOMAttributePointer);
 assertEquals("testAttribute1", attributePointer.getNodeName().getName());
 assertEquals("testValue1", attributePointer.getNodeName().getValue());
 assertEquals("testNamespace", attributePointer.getNodeName().getNamespaceURI());
 }

 @Test
 public void testJDOMAttributeIteratorMultipleAttributes() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
 assertEquals(3, iterator.getPosition());
 NodePointer firstAttributePointer = iterator.getNodePointer();
 assertTrue(firstAttributePointer instanceof JDOMAttributePointer);
 assertEquals("testAttribute1", firstAttributePointer.getNodeName().getName());
 assertEquals("testValue1", firstAttributePointer.getNodeName().getValue());
 assertEquals("testNamespace", firstAttributePointer.getNodeName().getNamespaceURI());
 assertEquals(2, iterator.getPosition());
 NodePointer secondAttributePointer = iterator.getNodePointer();
 assertTrue(secondAttributePointer instanceof JDOMAttributePointer);
 assertEquals("testAttribute2", secondAttributePointer.getNodeName().getName());
 assertNull(secondAttributePointer.getNodeName().getValue());
 assertEquals("testNamespace", secondAttributePointer.getNodeName().getNamespaceURI());
 }

 @Test
 public void testJDOMAttributeIteratorNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("xml:xmlns:test"));
 assertEquals(1, iterator.getPosition());
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof JDOMAttributePointer);
 assertEquals("xml:xmlns:test", attributePointer.getNodeName().getName());
 assertEquals("testNamespace", attributePointer.getNodeName().getValue());
 assertEquals("testNamespace", attributePointer.getNodeName().getNamespaceURI());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIteratorInvalidPosition() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("testAttribute1"));
 PowerMockito.mockStatic(JDOMAttributeIterator.class);
 PowerMockito.whenNew(JDOMAttributeIterator.class)
 .withArguments(parentNode, new QName("testAttribute1"))
 .thenReturn(iterator);
 PowerMockito.when(iterator.getNodePointer()).thenReturn(null);
 PowerMockito.when(iterator.getPosition()).thenReturn(5);
 NodePointer pointer = iterator.getNodePointer();
 }
}