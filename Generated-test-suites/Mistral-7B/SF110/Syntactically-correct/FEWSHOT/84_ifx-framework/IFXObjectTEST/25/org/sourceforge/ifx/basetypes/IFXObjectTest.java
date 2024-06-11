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

public class IFXObjectTest {

    @Test
    public void testToString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        class TestObj {
            int i;
            String str;
        }

        TestObj obj = new TestObj();
        obj.i = 1;
        obj.str = "test";

        IFXObject ifxObj = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        Method toStringMethod = IFXObject.class.getMethod("stringify", IFXObject.class, int.class);
        String expected = "class TestObj = {\n" +
                "\ni = 1\n" +
                "\nstr = test\n" +
                "}";

        String result = (String) toStringMethod.invoke(ifxObj, obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        class TestObj1 {
            int i = 1;
        }

        class TestObj2 {
            int i = 2;
        }

        TestObj1 obj1 = new TestObj1();
        TestObj2 obj2 = new TestObj2();

        IFXObject ifxObj1 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        IFXObject ifxObj2 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        assertTrue(equalsMethod.invoke(ifxObj1, ifxObj1, null));
        assertFalse(equalsMethod.invoke(ifxObj1, ifxObj2, null));
    }

    @Test
    public void testEqualsArrays() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        class TestObj {
            int[] arr;
        }

        TestObj obj1 = new TestObj();
        obj1.arr = new int[] {1, 2, 3};

        TestObj obj2 = new TestObj();
        obj2.arr = new int[] {1, 2, 3};

        IFXObject ifxObj1 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        IFXObject ifxObj2 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        assertTrue(equalsMethod.invoke(ifxObj1, ifxObj1, null));
        assertTrue(equalsMethod.invoke(ifxObj1, ifxObj2, null));

        Object[] arr1 = obj1.arr;
        Object[] arr2 = obj2.arr;

        Method getClassMethod = IFXObject.class.getMethod("getClass");
        Method getMethod = IFXObject.class.getMethod("get", int.class);

        Method equalsArrayElementMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        IFXObject ifxArr1 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        IFXObject ifxArr2 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        for (int i = 0; i < arr1.length; i++) {
            IFXObject ifxElement1 = (IFXObject) getMethod.invoke(obj1, i);
            IFXObject ifxElement2 = (IFXObject) getMethod.invoke(obj2, i);
            ifxArr1 = (IFXObject) equalsArrayElementMethod.invoke(ifxObj1, ifxArr2, null);
            ifxArr2 = (IFXObject) equalsArrayElementMethod.invoke(ifxObj2, ifxArr1, null);
        }

        assertTrue(equalsMethod.invoke(ifxObj1, ifxObj2, null));
    }

    @Test
    public void testEqualsWithNull() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        class TestObj {
            int i;
        }

        TestObj obj = new TestObj();

        IFXObject ifxObj1 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        IFXObject ifxObj2 = null;

        Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        assertFalse(equalsMethod.invoke(ifxObj1, ifxObj2, null));
        assertNull(ifxObj2);
    }

    @Test
    public void testEqualsDifferentClasses() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        class TestObj1 {
            int i = 1;
        }

        class TestObj2 {
            String s = "test";
        }

        TestObj1 obj1 = new TestObj1();
        TestObj2 obj2 = new TestObj2();

        IFXObject ifxObj1 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        IFXObject ifxObj2 = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        assertFalse(equalsMethod.invoke(ifxObj1, ifxObj2, null));
    }

    @Test
    public void testEqualsWithSameInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        class TestObj {
            int i = 1;
        }

        TestObj obj = new TestObj();

        IFXObject ifxObj = new IFXObject() {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        Method equalsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);

        assertTrue(equalsMethod.invoke(ifxObj, obj, null));
        assertSame(obj, ifxObj);
    }
}