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
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        assertFalse(obj1.equals(new Object()));
        assertTrue(obj1.equals(obj3));
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject";
            }
        };
        assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}}", obj.toString());
    }

    @Test
    public void testStringify_Null() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject";
            }
        };
        String stringified = obj.stringify(null, 0);
        assertNull(stringified);
    }

    @Test
    public void testStringify_Object() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "obj3";
            }
        };
        IFXObject[] array = new IFXObject[] { obj1, obj2, obj3 };
        Object[] intArray = new Object[] { 1, 2, 3 };
        Object iBaseType = new Object() {
            @Override
            public String getString() {
                return "iBaseType";
            }
        };
        try {
            Method getStringMethod = IFXObject.class.getMethod("getString");
            IFXObject ifxObject = new IFXObject() {
                @Override
                public String getString() {
                    return "getString";
                }
            };
            String stringified = obj1.stringify(obj1, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}}", stringified);
            stringified = obj1.stringify(obj2, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}}", stringified);
            stringified = obj1.stringify(obj3, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}}", stringified);
            stringified = obj1.stringify(array, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = [Ljava.lang.Object;}} = {Class = java.lang.Object} = {Class = org.sourceforge.ifx.basetypes.IFXObject} = {Class = org.sourceforge.ifx.basetypes.IFXObject} = {Class = org.sourceforge.ifx.basetypes.IFXObject}", stringified);
            stringified = obj1.stringify(intArray, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = [I}} = {Class = java.lang.Object} = {Class = [I} = {Class = java.lang.Integer} = {Class = java.lang.Integer} = {Class = java.lang.Integer}", stringified);
            stringified = obj1.stringify(iBaseType, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}} = {Class = java.lang.Object} = {Class = org.sourceforge.ifx.basetypes.IBaseType} = iBaseType", stringified);
            stringified = obj1.stringify(ifxObject, 0);
            assertEquals("IFXObject = {Class = org.sourceforge.ifx.basetypes.IFXObject = {Class = java.lang.Object}} = {Class = java.lang.Object} = {Class = org.sourceforge.ifx.basetypes.IFXObject} = getString", stringified);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoCheckEquals_Null() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = null;
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentClasses() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        String obj2 = "obj2";
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_SameObjects() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = obj1;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentObjects_SameClass() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_Array_SameLength_SameElements() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        IFXObject[] array1 = new IFXObject[] { obj1, obj1 };
        IFXObject[] array2 = new IFXObject[] { obj2, obj2 };
        assertTrue(obj1.doCheckEquals(obj1, array1));
        assertTrue(obj1.doCheckEquals(array1, array2));
        assertTrue(obj1.doCheckEquals(array2, obj1));
        assertTrue(obj1.doCheckEquals(array1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, array2));
        assertTrue(obj1.doCheckEquals(array2, obj1));
    }

    @Test
    public void testDoCheckEquals_Array_SameLength_DifferentElements() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        IFXObject[] array1 = new IFXObject[] { obj1, obj1 };
        IFXObject[] array2 = new IFXObject[] { obj2, obj1 };
        assertFalse(obj1.doCheckEquals(obj1, array2));
    }

    @Test
    public void testDoCheckEquals_Array_DifferentLength() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }
        };
        IFXObject[] array1 = new IFXObject[] { obj1, obj1 };
        IFXObject[] array2 = new IFXObject[] { obj1, obj1, obj2 };
        assertFalse(obj1.doCheckEquals(obj1, array2));
    }

    @Test
    public void testDoCheckEquals_SimpleDataType() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        String string = "string";
        assertFalse(obj1.doCheckEquals(obj1, string));
    }

    @Test
    public void testDoCheckEquals_SimpleDataType_SameValue() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        String string = "obj1";
        assertTrue(obj1.doCheckEquals(obj1, string));
    }

    @Test
    public void testDoCheckEquals_Recursive_SameObjects() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }

            @Override
            public Object getValue() {
                return obj1;
            }
        };
        IFXObject obj2 = obj1;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_Recursive_DifferentObjects() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }

            @Override
            public Object getValue() {
                return obj1;
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }

            @Override
            public Object getValue() {
                return obj2;
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
}