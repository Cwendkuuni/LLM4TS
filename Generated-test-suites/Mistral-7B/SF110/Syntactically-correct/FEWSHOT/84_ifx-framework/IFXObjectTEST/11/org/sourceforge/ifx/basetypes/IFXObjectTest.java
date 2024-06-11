package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String getString() {
                return "bar";
            }
        };
        assertSame(obj1, obj1);
        assertSame(obj1, obj1);
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(obj3));
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        String expectedOutput = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\n" +
                "getString = foo" +
                "\n}";
        assertEquals(expectedOutput, obj.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        String expectedOutput = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\ngetString = foo" +
                "\n}";
        assertEquals(expectedOutput, obj.stringify(obj, 0));

        IFXObject objArray = new IFXObject[2];
        objArray[0] = obj;
        objArray[1] = obj;
        String expectedOutputArray = "org.sourceforge.ifx.basetypes.IFXObject[] = {" +
                "\n[0] = org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\ngetString = foo" +
                "\n}" +
                "\n[1] = org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\ngetString = foo" +
                "\n}" +
                "\n}";
        assertEquals(expectedOutputArray, obj.stringify(objArray, 0));

        IFXObject objBaseType = new IFXObject() {
            @Override
            public String getString() {
                return "bar";
            }
        };
        String expectedOutputBaseType = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\ngetString = bar" +
                "\n}";
        assertEquals(expectedOutputBaseType, obj.stringify(objBaseType, 0));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String getString() {
                return "foo";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String getString() {
                return "bar";
            }
        };
        IFXObject obj4 = new IFXObject() {
            @Override
            public String getString() {
                return "bar";
            }
        };

        Method getStringMethod = null;
        try {
            getStringMethod = obj1.getClass().getMethod("getString");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        assertFalse(obj1.doCheckEquals(obj1, obj3));
        assertFalse(obj1.doCheckEquals(obj1, null));
        assertFalse(obj1.doCheckEquals(obj1, new Object()));
        assertTrue(obj1.doCheckEquals(obj2, obj2));
        assertFalse(obj1.doCheckEquals(obj2, obj3));
        assertTrue(obj1.doCheckEquals(obj4, obj4));
        assertFalse(obj1.doCheckEquals(obj1, new Object()[0]));
        assertFalse(obj1.doCheckEquals(new Object(), obj1));
    }

    @Test
    public void testTab() {
        String expectedOutput = "  ";
        String tab = new IFXObject().tab(0);
        assertEquals(expectedOutput, tab);
    }

    @Test(expected = AssertionError.class)
    public void testStringifyException() {
        IFXObject obj = new IFXObject() {
            @Override
            public String getString() {
                throw new RuntimeException("Test exception");
            }
        };
        obj.stringify(obj, 0);
    }
}