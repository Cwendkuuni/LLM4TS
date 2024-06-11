import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JDOMAttributeIteratorTest {

 private static final String PREFIX = "xml";
 private static final String NAME = "attr";
 private static final String URI = "http://www.w3.org/2000/xmlns/";
 private static final String VALUE = "test";

 @Test
 public void testEmptyIterator() {
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(null), new QName(PREFIX, "*", URI));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("attr", VALUE, new Namespace(PREFIX, URI));
 element.addAttribute(attribute);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(PREFIX, NAME, URI));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());

 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertInstanceOf(Attribute.class, nodePointer.getNode());
 assertEquals(VALUE, ((Attribute) nodePointer.getNode()).getValue());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "value1", new Namespace(PREFIX, URI));
 Attribute attribute2 = new Attribute("attr2", "value2", new Namespace(PREFIX, URI));
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(PREFIX, "*", URI));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());

 iterator.next();
 NodePointer nodePointer1 = iterator.getNodePointer();
 assertNotNull(nodePointer1);
 assertNotNull(nodePointer1.getNode());
 assertInstanceOf(Attribute.class, nodePointer1.getNode());
 assertEquals("value1", ((Attribute) nodePointer1.getNode()).getValue());

 iterator.next();
 NodePointer nodePointer2 = iterator.getNodePointer();
 assertNotNull(nodePointer2);
 assertNotNull(nodePointer2.getNode());
 assertInstanceOf(Attribute.class, nodePointer2.getNode());
 assertEquals("value2", ((Attribute) nodePointer2.getNode()).getValue());
 }

 @Test
 public void testIteratorWithEmptyNamespace() {
 Element element = new Element("root");
 Attribute attribute = new Attribute(NAME, VALUE, null);
 element.addAttribute(attribute);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("", NAME));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());

 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertInstanceOf(Attribute.class, nodePointer.getNode());
 assertEquals(VALUE, ((Attribute) nodePointer.getNode()).getValue());
 }

 @Test
 public void testIteratorWithNamespace() {
 Element element = new Element("root");
 Attribute attribute = new Attribute(NAME, VALUE, new Namespace(PREFIX, URI));
 element.addAttribute(attribute);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(PREFIX, NAME, URI));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());

 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertInstanceOf(Attribute.class, nodePointer.getNode());
 assertEquals(VALUE, ((Attribute) nodePointer.getNode()).getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithNegativePosition() {
 Element element = new Element("root");
 Attribute attribute = new Attribute(NAME, VALUE, new Namespace(PREFIX, URI));
 element.addAttribute(attribute);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(PREFIX, NAME, URI));
 assertNotNull(iterator);
 iterator.next();
 iterator.next();
 iterator.next(); // there is only one attribute
 iterator.next(); // next call should fail
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithPositionGreaterThanNumberOfAttributes() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "value1", new Namespace(PREFIX, URI));
 Attribute attribute2 = new Attribute("attr2", "value2", new Namespace(PREFIX, URI));
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(PREFIX, "*", URI));
 assertNotNull(iterator);
 iterator.next();
 iterator.next();
 iterator.next(); // there are two attributes
 iterator.next(); // next call should fail
 }

 @Test
 public void testIteratorWithNullParent() {
 try {
 new JDOMAttributeIterator(null, new QName(PREFIX, "*", URI));
 assertFalse("Exception should be thrown", true);
 } catch (IllegalArgumentException e) {
 assertFalse("Unexpected exception", false);
 } catch (Exception e) {
 assertEquals("Parent node cannot be null", e.getMessage());
 assertFalse("Unexpected exception", false);
 }
 }

 @Test
 public void testIteratorWithNullName() {
 try {
 new JDOMAttributeIterator(new JDOMPointer(new Element("root")), null);
 assertFalse("Exception should be thrown", true);
 } catch (IllegalArgumentException e) {
 assertFalse("Unexpected exception", false);
 } catch (Exception e) {
 assertEquals("Name cannot be null", e.getMessage());
 assertFalse("Unexpected exception", false);
 }
 }
}