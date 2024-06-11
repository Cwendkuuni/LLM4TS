package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

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
        assertTrue(obj1.equals(obj2));

        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "Object2";
            }
        };
        assertFalse(obj1.equals(obj3));

        IFXObject obj4 = null;
        assertFalse(obj1.equals(obj4));

        IFXObject obj5 = new Object() {
            // Not an instance of IFXObject
        };
        assertFalse(obj1.equals(obj5));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "TestObject";
            }
        };
        assertEquals("TestObject = {", obj.toString());

        int[] intArray = { 1, 2, 3 };
        IFXObject arrayObj = new Object() {
            @Override
            public String toString() {
                return "ArrayObject";
            }

            @Override
            public Object[] getLength() {
                return new Object[] { 3 };
            }

            @Override
            public IFXObject get(int index) {
                return new Object() {
                    @Override
                    public String toString() {
                        return Integer.toString(index + 1);
                    }
                };
            }
        };
        IFXObject objWithArray = new IFXObject() {
            @Override
            public String toString() {
                return "TestObjectWithArray";
            }

            @Override
            public Object get(String name) {
                if (name.equals("array")) {
                    return arrayObj;
                }
                return null;
            }
        };

        String expected = "TestObjectWithArray = {\n"
                + this.tab(1) + "array = [\n"
                + this.tab(2) + "1:\n"
                + this.tab(2) + "2:\n"
                + this.tab(2) + "3:\n"
                + this.tab(1) + "}\n";
        assertEquals(expected, objWithArray.toString());
    }

    @Test(expected = Exception.class)
    public void testToStringWithException() {
        IFXObject obj = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException();
            }
        };
        obj.toString();
    }

    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject obj1 = new Object() {
            @Override
            public String toString() {
                return "Object1";
            }

            @Override
            public int getSomeInt() {
                return 1;
            }
        };
        IFXObject obj2 = new Object() {
            @Override
            public String toString() {
                return "Object1";
            }

            @Override
            public int getSomeInt() {
                return 1;
            }
        };
        assertTrue(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj2));

        IFXObject obj3 = new Object() {
            @Override
            public String toString() {
                return "Object1";
            }

            @Override
            public int getSomeInt() {
                return 2;
            }
        };
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj3));

        IFXObject obj4 = null;
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj4));

        IFXObject obj5 = new Object() {
            // Not an instance of IFXObject
        };
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj5));

        IFXObject obj6 = new Object() {
            @Override
            public String toString() {
                return "Object2";
            }
        };
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj6));

        IFXObject obj7 = new Object()[1]; // An array of IFXObject
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj7));

        IFXObject obj8 = new Object() {
            @Override
            public String toString() {
                return "Object1";
            }

            @Override
            public String getSomeString() {
                return "Test";
            }
        };
        Method getSomeStringMethod = IFXObject.class.getMethod("getSomeString", IFXObject.class);
        Object[] args = { obj8 };
        assertFalse(IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj8));
        assertFalse(getSomeStringMethod.invoke(obj8));
    }

    private String tab(int level) {
        IFXObject obj = new Object() {
            @Override
            public String toString() {
                return "TabObject";
            }
        };
        return obj.class.getMethod("tab", int.class).invoke(obj, level).toString();
    }
}