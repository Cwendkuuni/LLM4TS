package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Object1";
            }
        };

        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Object1";
            }
        };

        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "Object2";
            }
        };

        IFXObject[] array1 = new IFXObject[0];
        IFXObject[] array2 = { obj1, obj2 };
        IFXObject[] array3 = { obj1, obj3 };

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));

        assertTrue(obj2.equals(obj1));
        assertTrue(obj2.equals(obj2));
        assertFalse(obj2.equals(obj3));

        assertFalse(obj3.equals(obj1));
        assertFalse(obj3.equals(obj2));
        assertFalse(obj3.equals(obj3));

        assertFalse(obj1.equals(null));
        assertFalse(null.equals(obj1));

        assertTrue(array1[0].equals(array1[0]));
        assertFalse(array1[0].equals(obj1));

        assertTrue(array2[0].equals(array2[0]));
        assertTrue(array2[0].equals(array2[1]));
        assertFalse(array2[0].equals(obj1));
        assertFalse(array2[1].equals(obj1));

        assertTrue(array3[0].equals(array3[0]));
        assertTrue(array3[0].equals(array3[1]));
        assertFalse(array3[0].equals(obj1));
        assertFalse(array3[1].equals(obj1));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "Object";
            }
        };

        assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = NULL}", obj.toString());
    }

    @Test
    public void testStringify() throws Exception {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "Object";
            }
        };

        Class<?> objClass = obj.getClass();
        Method getX = objClass.getMethod("getX");
        Object x = getX.invoke(obj, (Object[])null);

        assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = NULL}", obj.stringify(obj, 0));
        assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = NULL}", obj.stringify(obj, 1));

        assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = NULL}", obj.stringify(obj, 2));

        if (x != null) {
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object]}", obj.stringify(obj, 0));
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object, Object]}", obj.stringify(obj, 1));
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object, Object, NULL]}", obj.stringify(obj, 2));

            IFXObject xObj = (IFXObject)x;
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object, Object, IFXObject]}", obj.stringify(obj, 0));
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object, Object, IFXObject, NULL]}", obj.stringify(obj, 1));
            assertEquals("IFXObject = {Classname = org.sourceforge.ifx.basetypes.IFXObject, getX = [Object, Object, IFXObject, IFXObject]}", obj.stringify(obj, 2));
        }
    }

    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Object1";
            }
        };

        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Object1";
            }
        };

        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "Object2";
            }
        };

        IFXObject obj4 = new IFXObject() {
            @Override
            public String toString() {
                return "Object2";
            }
        };

        IFXObject[] array1 = new IFXObject[0];
        IFXObject[] array2 = { obj1, obj2 };
        IFXObject[] array3 = { obj1, obj3 };

        Object x = null;
        Object y = null;

        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        assertFalse(obj1.doCheckEquals(obj1, obj3));

        assertTrue(obj2.doCheckEquals(obj1, obj2));
        assertTrue(obj2.doCheckEquals(obj2, obj2));
        assertFalse(obj2.doCheckEquals(obj2, obj3));

        assertFalse(obj3.doCheckEquals(obj1, obj3));
        assertFalse(obj3.doCheckEquals(obj2, obj3));
        assertFalse(obj3.doCheckEquals(obj3, obj3));

        assertFalse(obj1.doCheckEquals(null, obj1));
        assertFalse(null.doCheckEquals(obj1, null));

        assertFalse(obj1.doCheckEquals(array1, obj1));
        assertFalse(obj1.doCheckEquals(obj1, array1));

        assertFalse(obj1.doCheckEquals(array2, obj1));
        assertFalse(obj1.doCheckEquals(obj1, array2));

        assertFalse(obj1.doCheckEquals(array3, obj1));
        assertFalse(obj1.doCheckEquals(obj1, array3));

        if (x != null) {
            Object[] xArray = { x, x };
            Object[] yArray = { x, y };

            assertTrue(obj1.doCheckEquals(x, x));
            assertTrue(obj1.doCheckEquals(x, x));
            assertFalse(obj1.doCheckEquals(x, y));

            assertTrue(obj1.doCheckEquals(xArray[0], xArray[0]));
            assertTrue(obj1.doCheckEquals(xArray[0], xArray[1]));
            assertFalse(obj1.doCheckEquals(xArray[0], y));
            assertFalse(obj1.doCheckEquals(xArray[1], y));

            assertTrue(obj1.doCheckEquals(yArray[0], xArray[0]));
            assertTrue(obj1.doCheckEquals(yArray[0], xArray[1]));
            assertFalse(obj1.doCheckEquals(yArray[0], y));
            assertFalse(obj1.doCheckEquals(xArray[0], y));

            IFXObject xObj = new IFXObject() {
                @Override
                public String toString() {
                    return "ObjectX";
                }
            };

            assertFalse(obj1.doCheckEquals(xObj, xObj));
            assertFalse(xObj.doCheckEquals(obj1, xObj));
        }
    }
}