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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JDOMAttributeIterator.class})
public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private Element testElement;
 private Attribute testAttribute;
 private QName testName;

 @Before
 public void setUp() {
 testElement = new Element("testElement");
 testAttribute = new Attribute("testAttribute", "testValue");
 testAttribute.setNamespace(Namespace.getNamespace("test", "http://test.com"));
 testName = new QName("test", "testAttribute");
 parentNode = new NodePointer(testElement);
 PowerMockito.mockStatic(Namespace.class);
 PowerMockito.when(Namespace.getNamespace("test", "http://test.com")).thenReturn(testAttribute.getNamespace());
 }

 @Test
 public void testJDOMAttributeIterator_EmptyList() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, testName);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_SingleAttribute() {
 testElement.addAttribute(testAttribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, testName);
 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(testAttribute, next.getNode());
 }

 @Test
 public void testJDOMAttributeIterator_MultipleAttributes() {
 testElement.addAttribute(testAttribute);
 testElement.addAttribute(new Attribute("testAttribute2", "testValue2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, testName);
 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(testAttribute, next.getNode());
 next = iterator.next();
 assertEquals(testAttribute2, next.getNode());
 assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testJDOMAttributeIterator_WrongNamespace() {
 testElement.addAttribute(testAttribute);
 testElement.addAttribute(new Attribute("wrong:testAttribute", "testValue"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, testName);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_WrongName() {
 testElement.addAttribute(testAttribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("test", "wrongAttribute"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }
}