package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private static final String TABSTOP = "  ";

 @Test
 public void testEquals() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();

 // Test equality with null objects
 assertFalse(obj1.equals(null));
 assertFalse(null.equals(obj1));

 // Test self-equality
 assertTrue(obj1.equals(obj1));

 // Test equality with same instance
 assertTrue(obj1.equals(obj2));

 // Test equality with different instances of same class
 assertTrue(obj1.equals(obj3));

 // Test inequality with different classes
 assertFalse(obj1.equals(new Object()));

 // Test inequality with different classes and null
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = new IFXObject();
 String expected = "IFXObject = {\n}";
 String actual;

 // Test toString with empty object
 actual = obj.toString();
 assertTrue(expected.equals(actual));
 }

 @Test(expected = Exception.class)
 public void testToStringWithException() {
 IFXObject obj = new IFXObject();
 obj.toString(); // Call the method without assigning the result to a variable
 }

 @Test
 public void testStringify_EmptyObject() {
 IFXObject obj = new IFXObject();
 String expected = "IFXObject = {\n}";
 String actual;

 try {
 actual = obj.stringify(obj, 0);
 }
 catch (Exception e) {
 fail("ToString method should not throw an exception when called from stringify method");
 }

 assertTrue(expected.equals(actual));
 }

 @Test
 public void testStringify_ArrayObject() {
 IFXObject[] arrayObj = new IFXObject[0];
 String expected = "IFXObject[] = [\n]";
 String actual;

 try {
 actual = ((IFXObject)arrayObj.getClass().getDeclaredConstructor().newInstance()).stringify(arrayObj, 0);
 }
 catch (Exception e) {
 fail("Constructor of array class should be accessible for testing");
 }

 assertTrue(expected.equals(actual));
 }

 @Test
 public void testStringify_ArrayObjectWithOneElement() {
 IFXObject[] arrayObj = new IFXObject[1];
 arrayObj[0] = new IFXObject();
 String expected = "IFXObject[] = [\n  IFXObject = {\n}\n]";
 String actual;

 try {
 actual = ((IFXObject)arrayObj.getClass().getDeclaredConstructor().newInstance()).stringify(arrayObj, 0);
 }
 catch (Exception e) {
 fail("Constructor of array class should be accessible for testing");
 }

 assertTrue(expected.equals(actual));
 }

 @Test
 public void testStringify_ArrayObjectWithMultipleElements() {
 IFXObject[] arrayObj = new IFXObject[2];
 arrayObj[0] = new IFXObject();
 arrayObj[1] = new IFXObject();
 String expected = "IFXObject[] = [\n  IFXObject = {\n}\n  IFXObject = {\n}\n]";
 String actual;

 try {
 actual = ((IFXObject)arrayObj.getClass().getDeclaredConstructor().newInstance()).stringify(arrayObj, 0);
 }
 catch (Exception e) {
 fail("Constructor of array class should be accessible for testing");
 }

 assertTrue(expected.equals(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringify_NullArrayObject() {
 IFXObject[] arrayObj = null;
 String actual;

 try {
 actual = ((IFXObject)arrayObj.getClass().getDeclaredConstructor().newInstance()).stringify(arrayObj, 0);
 }
 catch (NoSuchMethodException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InstantiationException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InvocationTargetException e) {
 actual = e.getTarget().toString();
 }
 catch (IllegalAccessException e) {
 fail("Constructor of array class should be accessible for testing");
 }

 assertNull(actual);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringify_NegativeIndex() {
 IFXObject[] arrayObj = new IFXObject[1];
 arrayObj[0] = new IFXObject();
 String actual;

 try {
 actual = ((IFXObject)arrayObj.getClass().getDeclaredConstructor().newInstance()).stringify(arrayObj, -1);
 }
 catch (NoSuchMethodException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InstantiationException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InvocationTargetException e) {
 actual = e.getTarget().toString();
 }
 catch (IllegalAccessException e) {
 fail("Constructor of array class should be accessible for testing");
 }

 fail("Should throw ArrayIndexOutOfBoundsException with negative index");
 }

 @Test
 public void testStringify_IBaseType() {
 IBaseType baseTypeObj = new SomeIBaseType();
 String expected = "SomeIBaseType";
 String actual;

 try {
 actual = new IFXObject().stringify(new IFXObject(), 0);
 ((IFXObject)actual.substring(0, actual.lastIndexOf(" = {")).length() + 1).setClass(SomeIBaseType.class);
 }
 catch (Exception e) {
 fail("ToString method should not throw an exception when called from stringify method");
 }

 assertEquals(expected, actual.substring(actual.lastIndexOf(" = ")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringify_NullIBaseType() {
 IBaseType baseTypeObj = null;
 String actual;

 try {
 actual = new IFXObject().stringify(new IFXObject(), 0);
 ((IFXObject)actual.substring(0, actual.lastIndexOf(" = {")).length() + 1).setClass(IBaseType.class);
 }
 catch (NoSuchMethodException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InstantiationException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InvocationTargetException e) {
 actual = e.getTarget().toString();
 }
 catch (IllegalAccessException e) {
 fail("Constructor of array class should be accessible for testing");
 }

 assertNull(actual);
 }

 @Test(expected = ClassCastException.class)
 public void testStringify_NonIBaseType() {
 Object nonIBaseTypeObj = new SomeObject();
 String actual;

 try {
 actual = new IFXObject().stringify(new IFXObject(), 0);
 ((IFXObject)actual.substring(0, actual.lastIndexOf(" = {")).length() + 1).setClass(nonIBaseTypeObj.getClass());
 }
 catch (NoSuchMethodException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InstantiationException e) {
 fail("Constructor of array class should be accessible for testing");
 }
 catch (InvocationTargetException e) {
 actual = e.getTarget().toString();
 }
 catch (IllegalAccessException e) {
 fail("Constructor of array class should be accessible for testing");
 }

 fail("Should throw ClassCastException when trying to cast non-IBaseType object to IFXObject");
 }

 @Test(expected = StackOverflowError.class)
 public void testDoCheckEquals_RecursiveObjects() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 Object obj3 = new Object() {
 @Override
 public boolean equals(Object obj) {
 return obj == this;
 }
 };

 obj1.setField(obj2);
 obj2.setField(obj3);

 assertFalse(obj1.doCheckEquals(obj2));
 }

 private void setField(Object field) {
 try {
 Method setFieldMethod = this.getClass().getDeclaredMethod("setField", Object.class);
 setFieldMethod.setAccessible(true);
 setFieldMethod.invoke(this, field);
 }
 catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 }
}