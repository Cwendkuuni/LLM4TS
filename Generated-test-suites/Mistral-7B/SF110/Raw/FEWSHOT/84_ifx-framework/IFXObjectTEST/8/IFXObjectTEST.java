package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject2";
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject1";
            }
        };

        assertTrue(obj1.equals(obj3));
        assertFalse(obj1.equals(obj2));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(""));
        assertFalse(obj1.equals(new Object()));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject";
            }
        };

        String expected = "IFXObject = {\n" +
                "\tClassName = java.lang.Object = {NULL}\n" +
                "\tgetClass = class java.lang.Class = {Class [Ljava.lang.Class;@1b}\n" +
                "}";

        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify() throws Exception {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject";
            }

            @SuppressWarnings("unused")
            Object getField1() {
                return "field1";
            }

            @SuppressWarnings("unused")
            Object[] getField2() {
                return new Object[]{"field2", new IFXObject() {
                    @Override
                    public String toString() {
                        return "IFXObject2";
                    }
                }};
            }
        };

        String expected = "IFXObject = {\n" +
                "\tClassName = java.lang.Object = {NULL}\n" +
                "\tgetField1 = java.lang.String = {field1}\n" +
                "\tgetField2 = [Ljava.lang.Object;@272b97\n" +
                "\t[\n" +
                "\t\t0 = field2 = java.lang.String = {field2}\n" +
                "\t\t1 = IFXObject2 = {\n" +
                "\t\t\tClassName = org.sourceforge.ifx.basetypes.IFXObject = {IFXObject@1b}\n" +
                "\t\t}\n" +
                "]\n" +
                "}";

        assertEquals(expected, obj.stringify(obj, 0));
    }

    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject1";
            }

            @SuppressWarnings("unused")
            Object getField1() {
                return "field1";
            }

            @SuppressWarnings("unused")
            Object[] getField2() {
                return new Object[]{"field2", new IFXObject() {
                    @Override
                    public String toString() {
                        return "IFXObject2";
                    }
                }};
            }
        };

        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject2";
            }

            @SuppressWarnings("unused")
            Object getField1() {
                return "field1";
            }

            @SuppressWarnings("unused")
            Object[] getField2() {
                return new Object[]{"field2", new IFXObject() {
                    @Override
                    public String toString() {
                        return "IFXObject3";
                    }
                }};
            }
        };

        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "IFXObject1";
            }

            @SuppressWarnings("unused")
            Object getField1() {
                return "field1";
            }

            @SuppressWarnings("unused")
            Object[] getField2() {
                return new Object[]{"field2", new IFXObject() {
                    @Override
                    public String toString() {
                        return "IFXObject1";
                    }
                }};
            }
        };

        assertTrue(obj1.doCheckEquals(obj1, obj3));
        assertFalse(obj1.doCheckEquals(obj1, obj2));

        Method getField1Method = obj1.getClass().getMethod("getField1");
        Method getField1Method2 = obj3.getClass().getMethod("getField1");

        Object field1Obj1 = getField1Method.invoke(obj1);
        Object field1Obj2 = getField1Method2.invoke(obj3);

        assertTrue(obj1.doCheckEquals(obj1, obj3, getField1Method, getField1Method2, field1Obj1, field1Obj2));

        Object field2Obj1 = obj1.getField2()[0];
        Object field2Obj2 = obj3.getField2()[0];

        IFXObject field2Obj3 = (IFXObject)obj1.getField2()[1];
        IFXObject field2Obj4 = (IFXObject)obj3.getField2()[1];

        assertTrue(obj1.doCheckEquals(obj1, obj3, getField1Method, getField1Method2, field1Obj1, field1Obj2,
                getField2Method, getField2Method, field2Obj1, field2Obj2, field2Obj3, field2Obj4));

        assertFalse(obj1.doCheckEquals(obj1, obj2, getField1Method, getField1Method2, field1Obj1, field1Obj2,
                getField2Method, getField2Method, field2Obj1, field2Obj2, field2Obj3, field2Obj4));
    }

    @Test
    public void testTab() {
        assertEquals("  ", IFXObject.tab(0));
        assertEquals("   ", IFXObject.tab(1));
        assertEquals("    ", IFXObject.tab(2));
        assertEquals("     ", IFXObject.tab(3));
        assertEquals("      ", IFXObject.tab(4));
    }

    private Method getField2Method;

    @Test(expected = Exception.class)
    public void testStringifyWithException() throws Exception {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                throw new Exception("Test Exception");
            }
        };

        getField2Method = obj.getClass().getMethod("getField2");

        obj.stringify(obj, 0);
    }

    @Test
    public void testEqualsWithNull() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;

        assertFalse(obj1.equals(obj2));
    }
}