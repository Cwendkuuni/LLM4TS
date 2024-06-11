package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextTestHelper;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

 private AttributeContextTestHelper helper;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 helper = new AttributeContextTestHelper();
 }

 @Test
 public void testNextNodeAttributeContext() {
 AttributeContext attributeContext = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:attr")));
 int initialPosition = attributeContext.getCurrentPosition();

 // typical use case: next node is an attribute with the given name
 Assert.assertTrue(attributeContext.nextNode());
 Assert.assertEquals(initialPosition + 1, attributeContext.getCurrentPosition());

 // typical use case: next node is the next attribute with the given name
 Assert.assertTrue(attributeContext.nextNode());
 Assert.assertEquals(initialPosition + 2, attributeContext.getCurrentPosition());

 // edge case: next node is the last attribute with the given name
 AttributeContext attributeContextLastAttribute = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:attr")));
 attributeContextLastAttribute.setPosition(helper.getContext().getChildCount());
 Assert.assertTrue(attributeContextLastAttribute.nextNode());
 Assert.assertEquals(helper.getContext().getChildCount(), attributeContextLastAttribute.getCurrentPosition());

 // error case: next node is not an attribute
 AttributeContext attributeContextNonAttribute = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:nonExistentAttr")));
 Assert.assertFalse(attributeContextNonAttribute.nextNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionAttributeContextOutOfBounds() {
 AttributeContext attributeContext = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:attr")));
 attributeContext.setPosition(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionAttributeContextParentIsNull() {
 AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest(new QName("test:attr")));
 attributeContext.setPosition(0);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionAttributeContextIteratorNull() {
 AttributeContext attributeContext = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:attr")));
 attributeContext.reset();
 attributeContext.setPosition(0);
 helper.setContextAttributeIterator(null);
 }

 @Test
 public void testResetAttributeContext() {
 AttributeContext attributeContext = new AttributeContext(helper.getContext(), new NodeNameTest(new QName("test:attr")));
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.reset();
 Assert.assertEquals(0, attributeContext.getCurrentPosition());
 }
}