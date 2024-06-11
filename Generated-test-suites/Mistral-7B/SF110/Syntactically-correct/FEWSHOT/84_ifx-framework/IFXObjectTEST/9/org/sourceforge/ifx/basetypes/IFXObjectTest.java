package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
        };
        IFXObject obj2 = new IFXObject() {
        };
        assertFalse(obj1.equals(null));
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(obj2));
        IFXObject obj3 = obj1;
        assertTrue(obj1.equals(obj3));
        IFXObject obj4 = new IFXObject() {
            @Override
            public String toString() {
                return "test";
            }
        };
        assertFalse(obj1.equals(obj4));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "test";
            }
        };
        String result = obj.toString();
        assertTrue(result.startsWith("test = {"));
        assertTrue(result.endsWith("}"));
        try {
            Method method = obj.getClass().getMethod("getSomething");
            assertNull(method);
        } catch (NoSuchMethodException e) {
            // Expected
        }

        try {
            Method getSomethingMethod = obj.getClass().getMethod("getSomething");
            Object result1 = getSomethingMethod.invoke(obj);
            assertNull(result1);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        IFXObject[] array = new IFXObject[2];
        array[0] = obj;
        array[1] = obj;
        String result2 = obj.stringify(array, 0);
        assertTrue(result2.contains("getSomething = NULL"));
        String result3 = obj.stringify(obj, 0);
        assertTrue(result3.contains("test = {"));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "test1";
            }

            public Object getSomething() {
                return new Object() {
                };
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "test2";
            }

            public Object getSomething() {
                return new Object() {
                };
            }
        };
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertFalse(obj1.doCheckEquals(obj1, obj2));
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "test3";
            }

            public Object getSomething() {
                return new Object[]{new Object(), new Object()};
            }
        };
        IFXObject obj4 = new IFXObject() {
            @Override
            public String toString() {
                return "test3";
            }

            public Object getSomething() {
                return new Object[]{new Object(), new Object()};
            }
        };
        Object[] array1 = new Object[]{new Object(), new Object()};
        Object[] array2 = new Object[]{new Object(), new Object()};
        assertTrue(obj3.doCheckEquals(obj3, obj3));
        assertTrue(obj3.doCheckEquals(obj3, obj4));
        assertTrue(Array.equals(array1, array2));
        IFXObject obj5 = new IFXObject() {
            @Override
            public String toString() {
                return "test4";
            }

            public Object getSomething() {
                return "test";
            }
        };
        IFXObject obj6 = new IFXObject() {
            @Override
            public String toString() {
                return "test4";
            }

            public Object getSomething() {
                return "test";
            }
        };
        assertTrue(obj5.doCheckEquals(obj5, obj5));
        assertTrue(obj5.doCheckEquals(obj5, obj6));
        String string1 = "test";
        String string2 = "test";
        IFXObject strObj1 = new IFXObject() {
            @Override
            public String toString() {
                return string1;
            }
        };
        IFXObject strObj2 = new IFXObject() {
            @Override
            public String toString() {
                return string2;
            }
        };
        assertTrue(strObj1.doCheckEquals(strObj1, strObj1));
        assertTrue(strObj1.doCheckEquals(strObj1, strObj2));
        assertTrue(string1.equals(string2));
    }
}