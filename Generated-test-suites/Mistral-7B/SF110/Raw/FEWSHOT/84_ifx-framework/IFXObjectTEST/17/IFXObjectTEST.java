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
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "ClassName = {";
            }
        };
        String expected = "ClassName = {\n" +
                "    getClass = ClassName = java.lang.Class\n" +
                "}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify_Empty() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "";
            }
        };
        String expected = "ClassName = {}";
        assertEquals(expected, obj.stringify(obj, 0));
    }

    @Test
    public void testStringify_SingleValue() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "ClassName = {value = 42}";
            }
        };
        String expected = "ClassName = {\n" +
                "    value = 42\n" +
                "}";
        assertEquals(expected, obj.stringify(obj, 0));
    }

    @Test
    public void testStringify_Array() {
        Integer[] arr = {1, 2, 3};
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "ClassName = {array = [4, 5, 6]}";
            }

            @Override
            public Object getValue(Method method, Object obj) {
                return arr;
            }
        };
        String expected = "ClassName = {\n" +
                "    array = [\n" +
                "        [0] = ClassName.java.lang.Integer = 1,\n" +
                "        [1] = ClassName.java.lang.Integer = 2,\n" +
                "        [2] = ClassName.java.lang.Integer = 3\n" +
                "    ]\n" +
                "}";
        assertEquals(expected, obj.stringify(obj, 0));
    }

    @Test
    public void testStringify_NestedArray() {
        Integer[][] arr2d = {{1, 2}, {3, 4}};
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "ClassName = {array2d = [[1, 2], [3, 4]]}";
            }

            @Override
            public Object getValue(Method method, Object obj) {
                return arr2d;
            }
        };
        String expected = "ClassName = {\n" +
                "    array2d = [\n" +
                "        [0] = [\n" +
                "            [0] = ClassName.java.lang.Integer = 1,\n" +
                "            [1] = ClassName.java.lang.Integer = 2\n" +
                "        ],\n" +
                "        [1] = [\n" +
                "            [0] = ClassName.java.lang.Integer = 3,\n" +
                "            [1] = ClassName.java.lang.Integer = 4\n" +
                "        ]\n" +
                "    ]\n" +
                "}";
        assertEquals(expected, obj.stringify(obj, 0));
    }

    @Test
    public void testEquals_SameInstance() {
        IFXObject obj1 = new IFXObject();
        assertTrue(obj1.equals(obj1));
    }

    @Test
    public void testEquals_DifferentInstance_SameClass() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_DifferentInstance_DifferentClass() {
        IFXObject obj1 = new IFXObject() {};
        IFXObject obj2 = new IFXObject() {};
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEquals_DifferentFieldValues() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Class1 = {field1 = 1, field2 = 2}";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "Class1 = {field1 = 3, field2 = 4}";
            }
        };
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEquals_DifferentClass_SameFields() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Class1 = {field1 = 1, field2 = 2}";
            }
        };
        IFXObject obj2 = new AnotherClass() {
            @Override
            public String toString() {
                return "AnotherClass = {field1 = 1, field2 = 2}";
            }
        };
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_DifferentClass_DifferentFields() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "Class1 = {field1 = 1, field2 = 2}";
            }
        };
        IFXObject obj2 = new AnotherClass() {
            @Override
            public String toString() {
                return "AnotherClass = {field1 = 3, field2 = 4}";
            }
        };
        assertFalse(obj1.equals(obj2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_NullArgument() {
        IFXObject obj = new IFXObject();
        obj.equals(null);
    }

    private static class AnotherClass extends IFXObject {
        // Empty
    }

    private Object getValue(Method method, Object obj) {
        try {
            return method.invoke(obj, (Object[])null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}