package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 1";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 2";
            }
        };

        assertEquals(obj1, obj1);
        assertEquals(obj1, obj2);
        assertFalse(obj1.equals(obj3));
        assertFalse(obj1.equals(null));

        Object[] arr1 = new Object[] { obj1, obj2 };
        Object[] arr2 = new Object[] { obj1, obj3 };
        Object[] arr3 = new Object[] { obj2, obj1 };
        Object[] arr4 = new Object[0];

        IFXObject arrObj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Array 1";
            }
        };
        IFXObject arrObj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Array 2";
            }
        };

        IFXObject[] arrIFXObj1 = { arrObj1, arrObj2 };
        IFXObject[] arrIFXObj2 = { arrObj2, arrObj1 };

        assertEquals(arr1, arr1);
        assertEquals(arr1, arrIFXObj1);
        assertFalse(arr1.equals(arr2));
        assertFalse(arr1.equals(arr3));
        assertFalse(arr1.equals(arr4));
        assertFalse(arr1.equals(null));

        assertEquals(arrObj1, arrObj1);
        assertEquals(arrObj1, arrIFXObj1[0]);
        assertFalse(arrObj1.equals(arrObj2));
        assertFalse(arrObj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "Test Object";
            }
        };
        assertEquals("Test Object", obj.toString());
    }

    @Test
    public void testStringify() throws Exception {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "Test Object";
            }

            @Override
            public String getString() {
                return "Test String";
            }
        };

        String expected = "TestObject = {\n\nTest String\n}";
        String result = obj.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 1";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "Object 2";
            }
        };

        Object[] arr1 = new Object[] { obj1, obj2 };
        Object[] arr2 = new Object[] { obj1, obj3 };
        Object[] arr3 = new Object[] { obj2, obj1 };
        Object[] arr4 = new Object[0];

        IFXObject arrObj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Array 1";
            }
        };
        IFXObject arrObj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Array 2";
            }
        };

        IFXObject[] arrIFXObj1 = { arrObj1, arrObj2 };
        IFXObject[] arrIFXObj2 = { arrObj2, arrObj1 };

        Class<?> objClass = IFXObject.class;
        Method getStringMethod = objClass.getMethod("getString");

        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, obj2));

        assertFalse(obj1.doCheckEquals(obj1, null));
        assertFalse(obj1.doCheckEquals(obj2, null));
        assertFalse(obj1.doCheckEquals(null, obj1));
        assertFalse(obj1.doCheckEquals(null, obj2));

        assertFalse(obj1.doCheckEquals(obj1, obj3));
        assertFalse(obj1.doCheckEquals(obj2, obj3));
        assertFalse(obj1.doCheckEquals(obj3, obj1));
        assertFalse(obj1.doCheckEquals(obj3, obj2));

        assertFalse(obj1.doCheckEquals(arr1, null));
        assertFalse(arr1.doCheckEquals(null, obj1));
        assertFalse(arr1.doCheckEquals(null, arr2));
        assertFalse(arr1.doCheckEquals(null, arr3));
        assertFalse(arr1.doCheckEquals(null, arr4));

        assertTrue(obj1.doCheckEquals(arr1, arr1));
        assertTrue(obj1.doCheckEquals(arr1, arrIFXObj1));
        assertTrue(arrIFXObj1[0].doCheckEquals(arrIFXObj1[0], obj1));
        assertTrue(arrIFXObj1[1].doCheckEquals(arrIFXObj1[1], obj2));

        assertFalse(obj1.doCheckEquals(arr2, null));
        assertFalse(arr2.doCheckEquals(null, obj1));
        assertFalse(arr2.doCheckEquals(null, arr3));
        assertFalse(arr2.doCheckEquals(null, arr4));

        assertFalse(obj1.doCheckEquals(arr2, arr1));
        assertFalse(arr2.doCheckEquals(arr1, obj1));
        assertFalse(obj1.doCheckEquals(arr2, arrIFXObj1));
        assertFalse(arrIFXObj1[0].doCheckEquals(arrIFXObj1[0], arr2));
        assertFalse(arrIFXObj1[1].doCheckEquals(arrIFXObj1[1], arr2));

        assertFalse(obj1.doCheckEquals(arr3, null));
        assertFalse(arr3.doCheckEquals(null, obj1));
        assertFalse(arr3.doCheckEquals(null, arr1));
        assertFalse(arr3.doCheckEquals(null, arr2));

        assertTrue(obj1.doCheckEquals(arr3, arr3));
        assertTrue(obj1.doCheckEquals(arr3, arrIFXObj2));
        assertTrue(arrIFXObj2[0].doCheckEquals(arrIFXObj2[0], obj2));
        assertTrue(arrIFXObj2[1].doCheckEquals(arrIFXObj2[1], obj1));

        assertFalse(obj1.doCheckEquals(arr4, null));
        assertFalse(arr4.doCheckEquals(null, obj1));
        assertFalse(arr4.doCheckEquals(null, arr1));
        assertFalse(arr4.doCheckEquals(null, arr2));
        assertFalse(arr4.doCheckEquals(null, arr3));

        Object[] arr5 = new Object[] { obj1, obj2, obj3 };
        IFXObject arrObj5 = new IFXObject() {
            @Override
            public String toString() {
                return "Array 5";
            }
        };
        IFXObject[] arrIFXObj5 = { arrObj1, arrObj2, arrObj3 };
        IFXObject[] arrIFXObj6 = { arrObj3, arrObj1, arrObj2 };

        assertTrue(obj1.doCheckEquals(arr5, arr5));
        assertTrue(obj1.doCheckEquals(arr5, arrIFXObj5));
        assertFalse(arrIFXObj5[0].doCheckEquals(arrIFXObj5[0], arrObj3));
        assertFalse(arrIFXObj5[1].doCheckEquals(arrIFXObj5[1], arrObj2));
        assertFalse(arrIFXObj5[2].doCheckEquals(arrIFXObj5[2], arrObj1));

        assertFalse(obj1.doCheckEquals(arrIFXObj6, null));
        assertFalse(arrIFXObj6.doCheckEquals(null, obj1));
        assertFalse(arrIFXObj6[0].doCheckEquals(arrIFXObj6[0], arrObj3));
        assertFalse(arrIFXObj6[1].doCheckEquals(arrIFXObj6[1], arrObj2));
        assertFalse(arrIFXObj6[2].doCheckEquals(arrIFXObj6[2], arrObj1));

        assertFalse(obj1.doCheckEquals(arrIFXObj5, arrIFXObj6));
        assertFalse(arrIFXObj6.doCheckEquals(arrIFXObj5, obj1));
        assertFalse(obj1.doCheckEquals(arrIFXObj5, arrIFXObj6));
        assertFalse(arrIFXObj5[0].doCheckEquals(arrIFXObj5[0], arrIFXObj6[2]));
        assertFalse(arrIFXObj5[1].doCheckEquals(arrIFXObj5[1], arrIFXObj6[1]));
        assertFalse(arrIFXObj5[2].doCheckEquals(arrIFXObj5[2], arrIFXObj6[0]));

        assertTrue(obj1.doCheckEquals(arrIFXObj6, arrIFXObj6));
        assertTrue(obj1.doCheckEquals(arrIFXObj6, arrIFXObj5));
        assertFalse(arrIFXObj5[0].doCheckEquals(arrIFXObj6[2], obj1));
        assertFalse(arrIFXObj5[1].doCheckEquals(arrIFXObj6[1], obj1));
        assertFalse(arrIFXObj5[2].doCheckEquals(arrIFXObj6[0], obj1));

        Object[] arr6 = new Object[0];

        assertTrue(obj1.doCheckEquals(arr6, arr6));
        assertTrue(obj1.doCheckEquals(arr6, arrIFXObj4));
        assertFalse(arrIFXObj4[0].doCheckEquals(arrIFXObj4[0], null));
        assertFalse(arrIFXObj4[0].doCheckEquals(null, obj1));
        assertFalse(arrIFXObj4[0].doCheckEquals(null, arr6));
    }
}