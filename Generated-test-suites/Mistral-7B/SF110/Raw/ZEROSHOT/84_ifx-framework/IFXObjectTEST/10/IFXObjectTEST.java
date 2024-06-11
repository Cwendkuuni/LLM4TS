package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest
{
    @Test
    public void testEquals() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IBaseType baseType1 = new BaseTypeInt(1);
        IBaseType baseType2 = new BaseTypeInt(2);

        // Test equals with null object
        assertFalse(obj1.equals(null));

        // Test equals with same object
        assertTrue(obj1.equals(obj1));

        // Test equals with different classes
        assertFalse(obj1.equals(new Object()));

        // Test equals with different objects of the same class
        assertFalse(obj1.equals(obj2));

        // Test equals with same objects of the same class
        obj1.getClass().getMethod("setIntValue", int.class).invoke(obj1, 1);
        obj2.getClass().getMethod("setIntValue", int.class).invoke(obj2, 1);
        assertTrue(obj1.equals(obj2));

        // Test equals with different objects of the same class with different values
        obj1.getClass().getMethod("setIntValue", int.class).invoke(obj1, 1);
        obj2.getClass().getMethod("setIntValue", int.class).invoke(obj2, 2);
        assertFalse(obj1.equals(obj2));

        // Test equals with different objects of different classes
        assertFalse(obj1.equals(baseType1));

        // Test equals with the same object but different method invocation order
        obj3.getClass().getMethod("setIntValue", int.class).invoke(obj3, 1);
        obj1.getClass().getMethod("setIntValue", int.class).invoke(obj1, 2);
        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));

        // Test equals with array objects
        Object[] arrObj1 = new Object[3];
        arrObj1[0] = baseType1;
        arrObj1[1] = baseType2;
        arrObj1[2] = obj1;
        Object[] arrObj2 = new Object[3];
        arrObj2[0] = baseType2;
        arrObj2[1] = baseType1;
        arrObj2[2] = obj2;
        assertFalse(Arrays.deepEquals(arrObj1, arrObj2));
        assertFalse(obj1.equals(arrObj2));
        assertFalse(arrObj2[2].equals(obj1));
        assertFalse(obj2.equals(arrObj1));
        assertFalse(arrObj1[2].equals(obj2));

        // Test equals with nested array objects
        Object[] arrObj3 = new Object[3];
        arrObj3[0] = new Object[3][3];
        arrObj3[0][0] = baseType1;
        arrObj3[0][1] = baseType2;
        arrObj3[0][2] = obj1;
        arrObj3[1] = new Object[3][3];
        arrObj3[1][0] = baseType2;
        arrObj3[1][1] = baseType1;
        arrObj3[1][2] = obj2;
        arrObj3[2] = new Object[3][3];
        arrObj3[2][0] = obj1;
        arrObj3[2][1] = obj2;
        arrObj3[2][2] = obj3;
        Object[] arrObj4 = new Object[3];
        arrObj4[0] = new Object[3][3];
        arrObj4[0][0] = baseType2;
        arrObj4[0][1] = baseType1;
        arrObj4[0][2] = obj2;
        arrObj4[1] = new Object[3][3];
        arrObj4[1][0] = baseType1;
        arrObj4[1][1] = baseType2;
        arrObj4[1][2] = obj3;
        arrObj4[2] = new Object[3][3];
        arrObj4[2][0] = obj2;
        arrObj4[2][1] = obj1;
        arrObj4[2][2] = obj3;
        assertFalse(Arrays.deepEquals(arrObj3, arrObj4));
        assertFalse(obj1.equals(arrObj4));
        assertFalse(arrObj4[2][1].equals(obj1));
        assertFalse(arrObj4[2][1].equals(obj1));
        assertFalse(obj1.equals(arrObj4[2][1]));
    }

    @Test
    public void testToString() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IBaseType baseType1 = new BaseTypeInt(1);
        IBaseType baseType2 = new BaseTypeInt(2);

        obj1.getClass().getMethod("setIntValue", int.class).invoke(obj1, 1);
        obj2.getClass().getMethod("setIntValue", int.class).invoke(obj2, 2);

        assertEquals("IFXObject = {\nintValue = 1\n}", obj1.toString());
        assertEquals("IFXObject = {\nintValue = 2\n}", obj2.toString());
    }
}