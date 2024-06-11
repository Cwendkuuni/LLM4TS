package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IFXObject objArray1 = new IFXObject[3];
 IFXObject objArray2 = new IFXObject[3];
 IBaseType baseType1 = new BaseType();
 IBaseType baseType2 = new BaseType();

 @Test
 public void testEquals() {
 // Test equals with null objects
 assertFalse(obj1.equals(null));
 assertFalse(null.equals(obj1));

 // Test equals with same object
 assertTrue(obj1.equals(obj1));

 // Test equals with different classes
 assertFalse(obj1.equals(new Object()));

 // Test equals with different instances of same class
 assertFalse(obj1.equals(obj2));

 // Test equals with same instances of same class
 obj2 = obj1;
 assertTrue(obj1.equals(obj2));

 // Test equals with different classes but equal getX methods
 IFXClass1 class1 = new IFXClass1();
 IFXClass2 class2 = new IFXClass2();
 IFXObject objClass1 = new IFXObject() {
 @Override
 public Class getClass() {
 return class1.getClass();
 }
 };
 IFXObject objClass2 = new IFXObject() {
 @Override
 public Class getClass() {
 return class2.getClass();
 }
 };
 assertFalse(obj1.equals(objClass1));
 assertFalse(objClass1.equals(obj1));
 assertFalse(obj1.equals(objClass2));
 assertFalse(objClass2.equals(obj1));

 // Test equals with equal getX methods and same values
 obj1.setX(1);
 obj2.setX(1);
 assertTrue(obj1.equals(obj2));

 // Test equals with equal getX methods and different values
 obj1.setX(1);
 obj2.setX(2);
 assertFalse(obj1.equals(obj2));

 // Test equals with equal getX methods and null values
 obj1.setX(null);
 obj2.setX(null);
 assertTrue(obj1.equals(obj2));

 // Test equals with different getX methods but same values
 obj1 = new IFXObject() {
 @Override
 public int getX() {
 return 1;
 }
 };
 obj2 = new IFXObject() {
 @Override
 public int getY() {
 return 1;
 }
 };
 assertFalse(obj1.equals(obj2));

 // Test equals with arrays
 objArray1[0] = obj1;
 objArray1[1] = obj2;
 objArray2[0] = obj1;
 objArray2[1] = obj2;
 assertTrue(Arrays.equals(objArray1, objArray2));

 // Test equals with IBaseType
 obj3.setX(baseType1);
 obj4.setX(baseType2);
 assertFalse(obj3.equals(obj4));
 obj3.setX(baseType1);
 obj4.setX(baseType1);
 assertTrue(obj3.equals(obj4));
 assertFalse(baseType1.equals(baseType2));
 assertTrue(baseType1.equals(baseType1));

 // Test stringify method
 assertEquals(obj1.toString(), "IFXObject = {\ngetX = NULL\n}");
 obj1.setX(1);
 assertEquals(obj1.toString(), "IFXObject = {\ngetX = 1\n}");
 objArray1[0] = obj1;
 assertEquals(objArray1.toString(), "[IFXObject = {\ngetX = 1\n}, IFXObject = {\ngetX = 1\n}, IFXObject = {\ngetX = 1\n}]");
 objArray1[1].setX(2);
 assertEquals(objArray1.toString(), "[IFXObject = {\ngetX = 1\n}, IFXObject = {\ngetX = 2\n}, IFXObject = {\ngetX = 1\n}]");
 objArray2[0] = obj1;
 objArray2[1] = new IFXObject() {
 @Override
 public int getX() {
 return 3;
 }
 };
 assertEquals(objArray1.toString(), "[IFXObject = {\ngetX = 1\n}, IFXObject = {\ngetX = 2\n}, IFXObject = {\ngetX = 1\n}]");
 assertEquals(objArray2.toString(), "[IFXObject = {\ngetX = 1\n}, IFXObject = {\ngetX = 3\n}, IFXObject = {\ngetX = 1\n}]");
 }

 @Test
 public void testDoCheckEquals() {
 // Test doCheckEquals with null objects
 assertFalse(obj1.doCheckEquals(null, obj1));
 assertFalse(obj1.doCheckEquals(obj1, null));

 // Test doCheckEquals with same objects
 assertTrue(obj1.doCheckEquals(obj1, obj1));

 // Test doCheckEquals with different classes
 assertFalse(obj1.doCheckEquals(obj1, new Object()));

 // Test doCheckEquals with different instances of same class
 assertFalse(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with same instances of same class
 obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with equal getX methods and same values
 obj1.setX(1);
 obj2.setX(1);
 assertTrue(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with equal getX methods and different values
 obj1.setX(1);
 obj2.setX(2);
 assertFalse(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with equal getX methods and null values
 obj1.setX(null);
 obj2.setX(null);
 assertTrue(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with different getX methods but same values
 obj1 = new IFXObject() {
 @Override
 public int getX() {
 return 1;
 }
 };
 obj2 = new IFXObject() {
 @Override
 public int getY() {
 return 1;
 }
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals with arrays
 objArray1[0] = obj1;
 objArray1[1] = obj2;
 objArray2[0] = obj1;
 objArray2[1] = obj2;
 assertTrue(obj1.doCheckEquals(objArray1, objArray2));

 // Test doCheckEquals with IBaseType
 obj3.setX(baseType1);
 obj4.setX(baseType2);
 assertFalse(obj1.doCheckEquals(obj1, obj4));
 obj3.setX(baseType1);
 obj4.setX(baseType1);
 assertTrue(obj1.doCheckEquals(obj3, obj4));
 assertFalse(baseType1.doCheckEquals(baseType2, baseType1));
 assertTrue(baseType1.doCheckEquals(baseType1, baseType1));

 // Test doCheckEquals with getX method that returns an array
 IFXObject arrayObj1 = new IFXObject() {
 @Override
 public int[] getX() {
 return new int[] {1, 2, 3};
 }
 };
 IFXObject arrayObj2 = new IFXObject() {
 @Override
 public int[] getX() {
 return new int[] {1, 2, 3};
 }
 };
 assertTrue(arrayObj1.doCheckEquals(arrayObj1, arrayObj2));
 IFXObject arrayObj3 = new IFXObject() {
 @Override
 public int[] getX() {
 return new int[] {1, 2, 3};
 }
 };
 IFXObject arrayObj4 = new IFXObject() {
 @Override
 public int[] getX() {
 return new int[] {1, 2, 4};
 }
 };
 assertFalse(arrayObj1.doCheckEquals(arrayObj1, arrayObj4));
 }

 @Test
 public void testTab() {
 assertEquals(obj1.tab(0), "");
 assertEquals(obj1.tab(1), "  ");
 assertEquals(obj1.tab(2), "  \t");
 }
}