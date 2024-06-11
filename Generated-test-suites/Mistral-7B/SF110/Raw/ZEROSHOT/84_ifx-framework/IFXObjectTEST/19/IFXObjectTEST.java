package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject objArray1 = new IFXObject[3];
 IBaseType baseType1 = new BaseType();
 IBaseType baseType2 = new BaseType();

 @Test
 public void testEqualsObjectNull() {
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testEqualsObjectDifferentType() {
 assertFalse(obj1.equals(new Object()));
 }

 @Test
 public void testEqualsObjectSameInstance() {
 assertTrue(obj1.equals(obj1));
 }

 @Test
 public void testEqualsObjectSameClassDifferentValues() {
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEqualsObjectSameClassSameValues() {
 IFXObject obj4 = new IFXObject();
 obj1.setField1(baseType1);
 obj2.setField1(baseType1);
 obj4.setField1(baseType2);
 assertTrue(obj1.equals(obj2));
 assertFalse(obj1.equals(obj4));
 }

 @Test
 public void testEqualsObjectDifferentClasses() {
 IFXObject obj5 = new BaseType();
 assertFalse(obj1.equals(obj5));
 }

 @Test
 public void testToString() {
 String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = NULL\n}";
 String actual = obj1.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringArray() {
 String expected = "[org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = NULL\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = NULL\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = NULL\n}]";
 for (int i = 0; i < objArray1.length; i++) {
 objArray1[i] = new IFXObject();
 }
 String actual = objArray1.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringArrayWithValues() {
 BaseType baseType3 = new BaseType();
 BaseType baseType4 = new BaseType();
 objArray1[0] = new IFXObject();
 objArray1[0].setField1(baseType1);
 objArray1[1] = new IFXObject();
 objArray1[1].setField1(baseType2);
 objArray1[2] = new IFXObject();
 objArray1[2].setField1(baseType3);
 String expected = "[org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = BaseType = NULL\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = BaseType = NULL\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = BaseType = BaseType = NULL\n}]";
 String actual = objArray1.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithValues() {
 BaseType baseType5 = new BaseType();
 baseType5.setValue("Test Value");
 obj1.setField1(baseType1);
 obj1.setField2(baseType5);
 String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = BaseType = BaseType = NULL\ngetField2 = BaseType = Test Value\n}";
 String actual = obj1.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithNestedArrays() {
 IFXObject objArray2 = new IFXObject[3];
 for (int i = 0; i < objArray2.length; i++) {
 objArray2[i] = new IFXObject();
 }
 objArray1[0].setField1(objArray2);
 String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = [org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = [null]\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = [null]\n},\norg.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = [null]\n}]\n}";
 String actual = obj1.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testDoCheckEqualsSameInstances() {
 assertTrue(obj1.doCheckEquals(obj1, obj1));
 }

 @Test
 public void testDoCheckEqualsDifferentObjects() {
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsSameClassesDifferentValues() {
 IFXObject obj6 = new IFXObject();
 IFXObject obj7 = new IFXObject();
 obj6.setField1(baseType1);
 obj7.setField1(baseType2);
 assertFalse(obj1.doCheckEquals(obj6, obj7));
 }

 @Test
 public void testDoCheckEqualsSameClassesSameValues() {
 IFXObject obj8 = new IFXObject();
 IFXObject obj9 = new IFXObject();
 obj6.setField1(baseType1);
 obj8.setField1(baseType1);
 assertTrue(obj1.doCheckEquals(obj6, obj8));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj10 = new BaseType();
 IFXObject obj11 = new BaseType();
 assertFalse(obj1.doCheckEquals(obj10, obj11));
 }

 @Test
 public void testDoCheckEqualsWithArraysSameLength() throws Exception {
 IFXObject obj12 = new IFXObject();
 IFXObject obj13 = new IFXObject();
 IFXObject[] array1 = new IFXObject[2];
 IFXObject[] array2 = new IFXObject[2];
 array1[0] = new IFXObject();
 array1[1] = new IFXObject();
 array2[0] = new IFXObject();
 array2[1] = new IFXObject();
 Method method1 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 Method method2 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 for (int i = 0; i < array1.length; i++) {
 array1[i].setField1(baseType1);
 array2[i].setField1(baseType1);
 }
 method1.invoke(obj1, new Object[]{array1});
 method2.invoke(obj13, new Object[]{array2});
 assertTrue(obj1.doCheckEquals(obj1, obj13));
 }

 @Test
 public void testDoCheckEqualsWithArraysDifferentLengths() throws Exception {
 IFXObject obj14 = new IFXObject();
 IFXObject obj15 = new IFXObject();
 IFXObject[] array1 = new IFXObject[2];
 IFXObject[] array2 = new IFXObject[3];
 array1[0] = new IFXObject();
 array1[1] = new IFXObject();
 array2[0] = new IFXObject();
 array2[1] = new IFXObject();
 array2[2] = new IFXObject();
 Method method1 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 Method method2 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 for (int i = 0; i < array1.length; i++) {
 array1[i].setField1(baseType1);
 array2[i].setField1(baseType1);
 }
 method1.invoke(obj1, new Object[]{array1});
 method2.invoke(obj15, new Object[]{array2});
 assertFalse(obj1.doCheckEquals(obj1, obj15));
 }

 @Test
 public void testDoCheckEqualsWithArraysSameValues() throws Exception {
 IFXObject obj16 = new IFXObject();
 IFXObject obj17 = new IFXObject();
 IFXObject[] array1 = new IFXObject[2];
 IFXObject[] array2 = new IFXObject[2];
 array1[0] = new IFXObject();
 array1[1] = new IFXObject();
 array2[0] = new IFXObject();
 array2[1] = new IFXObject();
 Method method1 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 Method method2 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 for (int i = 0; i < array1.length; i++) {
 array1[i].setField1(new BaseType());
 array2[i].setField1(new BaseType());
 }
 method1.invoke(obj1, new Object[]{array1});
 method2.invoke(obj17, new Object[]{array2});
 assertTrue(obj1.doCheckEquals(obj1, obj17));
 }

 @Test
 public void testDoCheckEqualsWithArraysDifferentValues() throws Exception {
 IFXObject obj18 = new IFXObject();
 IFXObject obj19 = new IFXObject();
 IFXObject[] array1 = new IFXObject[2];
 IFXObject[] array2 = new IFXObject[2];
 array1[0] = new IFXObject();
 array1[1] = new IFXObject();
 array2[0] = new IFXObject();
 array2[1] = new IFXObject();
 Method method1 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 Method method2 = obj1.getClass().getDeclaredMethod("setField1", (Class[])null);
 for (int i = 0; i < array1.length; i++) {
 array1[i].setField1(new BaseType());
 array2[i].setField1(new BaseType());
 array1[i].setField2(new BaseType());
 array2[i].setField2(new BaseType());
 array1[i].setField3(new BaseType());
 array2[i].setField3(new BaseType());
 array1[i].setField4(new BaseType());
 array2[i].setField4(new BaseType());
 }
 method1.invoke(obj1, new Object[]{array1});
 method2.invoke(obj19, new Object[]{array2});
 assertFalse(obj1.doCheckEquals(obj1, obj19));
 }
}