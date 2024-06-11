package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private Object testObject;
 private PropertyPointer propertyPointer;

 @Before
 public void setup() {
 testObject = new TestBean();
 propertyPointer = new PropertyPointer(new NodePointer(new TestCollection())) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setBean(testObject);
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(0, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 assertEquals(testObject, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyCount() {
 TestBean testBean = (TestBean) propertyPointer.getBean();
 assertEquals(3, testBean.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 TestBean testBean = (TestBean) propertyPointer.getBean();
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertNotNull(propertyNames);
 assertEquals(3, propertyNames.length);
 assertEquals("property1", propertyNames[0]);
 assertEquals("property2", propertyNames[1]);
 assertEquals("testProperty", propertyNames[2]);
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 TestBean testBean = (TestBean) propertyPointer.getBean();
 assertEquals("testValue1", propertyPointer.getImmediateNode().toString());
 }

 @Test
 public void testIsCollection() {
 assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 TestCollection testCollection = (TestCollection) propertyPointer.getImmediateParentPointer().getNode();
 assertEquals(2, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals("testValue1", immediateValuePointer.getNode().toString());
 }

 @Test
 public void testCreatePath() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 NodePointer nodePointer = propertyPointer.createPath(jxpathContext);
 assertEquals(nodePointer.getNode().getClass(), TestBean.class);
 }

 @Test
 public void testCreatePathWithValue() {
 Object value = new Object();
 JXPathContext jxpathContext = new JXPathContext(testObject);
 propertyPointer.createPath(jxpathContext, value);
 assertEquals(value, propertyPointer.getImmediateNode());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidValue() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 propertyPointer.createPath(jxpathContext, new TestCollection());
 fail("Should throw an exception");
 }

 @Test
 public void testCreateChild() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 String name = "testChildProperty";
 NodePointer childNodePointer = propertyPointer.createChild(jxpathContext, new QName(null, name), 0);
 assertEquals(name, childNodePointer.getName().getLocalPart());
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 String name = "testChildProperty";
 Object value = new Object();
 NodePointer childNodePointer = propertyPointer.createChild(jxpathContext, new QName(null, name), 0, value);
 assertEquals(value, childNodePointer.getNode());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreateChildWithInvalidName() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 NodePointer childNodePointer = propertyPointer.createChild(jxpathContext, null, 0);
 fail("Should throw an exception");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildWithInvalidIndex() {
 JXPathContext jxpathContext = new JXPathContext(testObject);
 String name = "testChildProperty";
 NodePointer childNodePointer = propertyPointer.createChild(jxpathContext, new QName(null, name), 3);
 fail("Should throw an exception");
 }

}