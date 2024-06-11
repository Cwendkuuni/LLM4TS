package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NodePointer nodePointer;
 private NullPropertyPointer nullPropertyPointer;

 @Before
 public void setUp() {
 nodePointer = new NodePointer(new Object());
 nullPropertyPointer = new NullPropertyPointer(nodePointer);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException: Cannot set property "
 + nullPropertyPointer.asPath() + ", the target object is null");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueParentNotPropertyOwner() {
 NodePointer parent = new DomNodePointer(new Object());
 NullPropertyPointer propertyPointer = new NullPropertyPointer(parent);
 try {
 propertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException: Cannot set property "
 + propertyPointer.asPath() + ", path does not match a changeable location");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNotProperty() {
 PropertyOwnerPointer parentPropertyOwner = new PropertyOwnerPointer(nodePointer);
 nullPropertyPointer = new NullPropertyPointer(parentPropertyOwner);
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException: Cannot set property "
 + nullPropertyPointer.asPath() + ", this property does not exist");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new StaticContext();
 NodePointer newParent = nullPropertyPointer.createPath(context);
 assertEquals("Expected new parent to be a NullPointer",
 new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName())), newParent);
 }

 @Test
 public void testCreatePathValue() {
 JXPathContext context = new StaticContext();
 NodePointer newParent = nullPropertyPointer.createPath(context, "testValue");
 fail("Expected JXPathInvalidAccessException: Cannot set property "
 + nullPropertyPointer.asPath() + ", path does not match a changeable location");
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new StaticContext();
 NodePointer newChild = nullPropertyPointer.createChild(context, new QName("testChild"));
 fail("Expected JXPathInvalidAccessException: Cannot create child "
 + nullPropertyPointer.asPath() + " because it is a leaf node");
 }

 @Test
 public void testCreateChildValue() {
 JXPathContext context = new StaticContext();
 NodePointer newChild = nullPropertyPointer.createChild(context, new QName("testChild"), "testValue");
 fail("Expected JXPathInvalidAccessException: Cannot set property "
 + nullPropertyPointer.asPath() + ", path does not match a changeable location");
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("*", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 nullPropertyPointer.setNameAttributeValue("testAttributeName");
 assertEquals("testAttributeName", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testAsPath() {
 assertEquals("/", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 String string = "test'string\"";
 assertEquals("test&apos;string&quot;", nullPropertyPointer.escape(string));
 }
}