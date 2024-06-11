package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject arrayObj1 = new IFXObject[3];
 IFXObject arrayObj2 = new IFXObject[3];
 IBaseType baseTypeObj1 = new BaseType();
 IBaseType baseTypeObj2 = new BaseType();

 @Test
 public void testEquals() {
 // Test equal objects
 assertTrue(obj1.equals(obj1));
 // Test null objects
 assertFalse(obj1.equals(null));
 // Test different classes
 assertFalse(obj1.equals(new Object()));
 // Test same objects but different classes
 assertFalse(obj1.equals(new Object()));
 // Test equal objects with different class names
 IFXObject ifxObject1 = new IFXObject();
 IFXObject ifxObject2 = new IFXObject();
 assertFalse(ifxObject1.equals(ifxObject2));
 // Test equal objects with same class names
 obj1 = ifxObject1;
 obj2 = ifxObject2;
 assertTrue(obj1.equals(obj2));
 // Test equals method with different objects
 assertFalse(obj1.equals(baseTypeObj1));
 // Test equals method with null objects
 assertFalse(obj1.equals(null));
 // Test equals method with different classes
 assertFalse(obj1.equals(new Object()));
 // Test equals method with same class but different instances
 assertFalse(obj1.equals(new IFXObject()));
 // Test equals method with same class and same instances
 assertTrue(obj1.equals(obj1));
 // Test equals method with arrays
 arrayObj1[0] = new IFXObject();
 arrayObj1[1] = new IFXObject();
 arrayObj1[2] = new IFXObject();
 arrayObj2[0] = new IFXObject();
 arrayObj2[1] = new IFXObject();
 arrayObj2[2] = new IFXObject();
 assertTrue(Arrays.equals(arrayObj1, arrayObj2));
 // Test equals method with arrays of different lengths
 arrayObj1 = new IFXObject[3];
 arrayObj2 = new IFXObject[4];
 assertFalse(Arrays.equals(arrayObj1, arrayObj2));
 // Test equals method with arrays of different types
 arrayObj1 = new IFXObject[3];
 arrayObj2 = new String[3];
 assertFalse(Arrays.equals(arrayObj1, arrayObj2));
 // Test equals method with arrays of same type and same instances
 arrayObj1 = new IFXObject[3];
 arrayObj2 = new IFXObject[3];
 for (int i = 0; i < arrayObj1.length; ++i) {
 arrayObj1[i] = new IFXObject();
 arrayObj2[i] = arrayObj1[i];
 }
 assertTrue(Arrays.equals(arrayObj1, arrayObj2));
 // Test equals method with arrays of same type and different instances
 arrayObj1 = new IFXObject[3];
 arrayObj2 = new IFXObject[3];
 for (int i = 0; i < arrayObj1.length; ++i) {
 arrayObj1[i] = new IFXObject();
 arrayObj2[i] = new IFXObject();
 }
 assertFalse(Arrays.equals(arrayObj1, arrayObj2));
 // Test equals method with IBaseType objects
 baseTypeObj1.setValue("Test1");
 baseTypeObj2.setValue("Test2");
 assertFalse(obj1.equals(baseTypeObj1));
 assertFalse(obj1.equals(baseTypeObj2));
 // Test equals method with IBaseType objects of the same value
 baseTypeObj1.setValue("Test1");
 baseTypeObj2.setValue("Test1");
 assertTrue(obj1.equals(baseTypeObj2));
 }

 @Test
 public void testToString() {
 // Test toString method with simple object
 assertEquals("IFXObject = {getclass = class org.sourceforge.ifx.basetypes.IFXObject\ngetmember_0 = NULL\ngetmember_1 = NULL\n}", obj1.toString());
 // Test toString method with array object
 arrayObj1[0] = new IFXObject();
 arrayObj1[1] = new IFXObject();
 arrayObj1[2] = new IFXObject();
 assertEquals("IFXObject = {getclass = class org.sourceforge.ifx.basetypes.IFXObject\ngetmember_0 = IFXObject = {getclass = class org.sourceforge.ifx.basetypes.IFXObject\ngetmember_0 = NULL\ngetmember_1 = NULL\ngetmember_2 = NULL\n}", obj1.toString());
 // Test toString method with array object of different sizes
 arrayObj1 = new IFXObject[3];
 arrayObj2 = new IFXObject[4];
 assertFalse(obj1.toString().equals(obj2.toString()));
 // Test toString method with IBaseType object
 baseTypeObj1 = new BaseType();
 baseTypeObj1.setValue("Test1");
 assertEquals("IFXObject = {getclass = class org.sourceforge.ifx.basetypes.IFXObject\ngetmember_0 = BaseType = Test1\ngetmember_1 = NULL\ngetmember_2 = NULL\n}", obj1.toString());
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 // Test equals method with simple objects
 IFXObject obj3 = new IFXObject();
 Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class);
 assertTrue(equalsMethod.invoke(obj1, obj3).equals(Boolean.TRUE));
 // Test equals method with arrays
 obj1 = arrayObj1;
 obj3 = new IFXObject[3];
 for (int i = 0; i < obj1.length; ++i) {
 ((IFXObject)obj1[i]).setValue(String.valueOf(i));
 ((IFXObject)obj3[i]) = new IFXObject();
 ((IFXObject)obj3[i]).setValue(String.valueOf(i));
 }
 assertTrue(equalsMethod.invoke(obj1, obj3).equals(Boolean.TRUE));
 // Test equals method with IBaseType objects
 baseTypeObj1 = new BaseType();
 baseTypeObj1.setValue("Test1");
 obj3 = new BaseType();
 baseTypeObj2 = new BaseType();
 baseTypeObj2.setValue("Test2");
 Method getMemberMethod = IFXObject.class.getMethod("getMember0");
 Method getStringMethod = BaseType.class.getMethod("getString");
 equalsMethod.invoke(obj1, obj3).invoke(obj1, obj3, getMemberMethod, getMemberMethod, getStringMethod, getStringMethod);
 assertFalse(equalsMethod.invoke(obj1, obj3).equals(Boolean.TRUE));
 // Test equals method with IBaseType objects of the same value
 baseTypeObj1.setValue("Test1");
 baseTypeObj2.setValue("Test1");
 equalsMethod.invoke(obj1, obj3).invoke(obj1, obj3, getMemberMethod, getMemberMethod, getStringMethod, getStringMethod);
 assertTrue(equalsMethod.invoke(obj1, obj3).equals(Boolean.TRUE));
 }

 private void setValue(IFXObject obj, String value) throws InvocationTargetException, IllegalAccessException {
 Method setMember0Method = obj.getClass().getMethod("setMember0", BaseType.class);
 BaseType baseType = new BaseType();
 baseType.setValue(value);
 setMember0Method.invoke(obj, baseType);
 }
}