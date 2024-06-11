package org.apache.commons.jxpath.ri.axes;

 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri. NodeTest;
 import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
 import org.apache.commons.jxpath.ri.model.NodeIterator;
 import org.apache.commons.jxpath.ri.model.NodePointer;
 import org.junit.Before;
 import org.junit.Test;
 import org.w3c.dom.Element;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;

 /**
 * Test cases for the AttributeContext class.
 */
 public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;
 private Element testElement;

 @Before
 public void setUp() {
 parentContext = new EvalContext(new EvalContext(new Object()));
 nodeTest = new NodeNameTest(new QName("test"));
 testElement = new Element("root");
 testElement.setAttribute("test", "value");
 testElement.setAttribute("other", "otherValue");
 }

 /**
 * Test that the AttributeContext returns the correct attribute when the position is set to 0.
 */
 @Test
 public void testAttributeContextPositionZero() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 attributeContext.setPosition(0);
 NodePointer expectedNodePointer = new NodePointer(testElement.getAttributeNode("test"));
 assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
 }

 /**
 * Test that the AttributeContext returns null when the position is set to a position greater than the number of attributes.
 */
 @Test
 public void testAttributeContextPositionGreaterThanNumberOfAttributes() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 attributeContext.setPosition(2);
 assertNull(attributeContext.getCurrentNodePointer());
 }

 /**
 * Test that the AttributeContext can iterate through all the attributes.
 */
 @Test
 public void testAttributeContextCanIterateThroughAllAttributes() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 attributeContext.nextNode();
 assertSame(testElement.getAttributeNode("test"), attributeContext.getCurrentNodePointer().getNode());
 attributeContext.nextNode();
 assertSame(testElement.getAttributeNode("other"), attributeContext.getCurrentNodePointer().getNode());
 assertNull(attributeContext.getCurrentNodePointer());
 }

 /**
 * Test that the AttributeContext returns the correct attribute when the position is set to a valid position.
 */
 @Test
 public void testAttributeContextPositionValid() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(1);
 NodePointer expectedNodePointer = new NodePointer(testElement.getAttributeNode("other"));
 assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
 }

 /**
 * Test that the AttributeContext returns an empty iterator when the attribute name is null.
 */
 @Test
 public void testAttributeContextNullAttributeName() {
 attributeContext = new AttributeContext(parentContext, null);
 assertNull(attributeContext.iterator);
 }

 /**
 * Test that the AttributeContext returns an empty iterator when the attribute name is an empty string.
 */
 @Test
 public void testAttributeContextEmptyAttributeName() {
 attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("")));
 assertNull(attributeContext.iterator);
 }
}