package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

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
        IFXObject obj3 = obj1;

        assertEquals(obj1, obj3);
        assertFalse(obj1.equals(new Object()));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Integer(0)));

        IFXObject obj4 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };
        assertFalse(obj1.equals(obj4));

        obj4.getClass().getMethods()[0].invoke(obj4); // invoke a getter method to ensure objects have the same class
        assertEquals(obj1, obj4);

        IFXObject[] objArray1 = new IFXObject[0];
        IFXObject[] objArray2 = new IFXObject[0];
        assertEquals(objArray1, objArray2);

        IFXObject[] objArray3 = {obj1};
        IFXObject[] objArray4 = {obj2};
        assertFalse(objArray3.equals(objArray4));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "obj";
            }
        };
        String expected = obj.getClass().getName() + " = {" +
                "\n" + this.tab(1) + "toString = obj" +
                "\n" + "}";
        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphens() {
        testStripLeadingHyphens("", null);
        testStripLeadingHyphens("-", null);
        testStripLeadingHyphens("--foo", "foo");
        testStripLeadingHyphens("---foo---", "foo");
        testStripLeadingHyphens("--foo--", "foo");
        testStripLeadingHyphens("--foo--bar--", "foo bar");
        testStripLeadingHyphens("--foo--bar--baz--", "foo bar baz");
        testStripLeadingHyphens("--foo--bar--baz--qux--", "foo bar baz qux");
        testStripLeadingHyphens("--foo--bar--baz--qux--quux--", "foo bar baz qux quux");
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "obj";
            }
        };
        Object[] fields = new Object[1];
        fields[0] = new Integer(42);

        try {
            String expected = obj.getClass().getName() + " = {" +
                    "\n" + this.tab(1) + "toString = obj" +
                    "\n" + this.tab(2) + "getFields = [42]" +
                    "\n" + this.tab(1) + "}";
            String actual = obj.stringify(obj, 0);
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }

            @Override
            public Object getFields() {
                return new Integer(42);
            }
        };
        IFXObject obj2 = new IFXObject() {
            @Override
            public String toString() {
                return "obj2";
            }

            @Override
            public Object getFields() {
                return new Integer(42);
            }
        };
        IFXObject obj3 = new IFXObject() {
            @Override
            public String toString() {
                return "obj3";
            }

            @Override
            public Object getFields() {
                return new Integer(43);
            }
        };

        try {
            assertTrue(obj1.doCheckEquals(obj1, obj1));
            assertTrue(obj1.doCheckEquals(obj1, obj2));
            assertFalse(obj1.doCheckEquals(obj1, obj3));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTab() {
        String expected = "  ";
        String actual = IFXObject.this.tab(0);
        assertEquals(expected, actual);

        expected = "   ";
        actual = IFXObject.this.tab(1);
        assertEquals(expected, actual);

        expected = "  ** ";
        actual = IFXObject.this.tab(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethod() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "obj";
            }
        };

        Method getFieldsMethod = null;
        try {
            getFieldsMethod = obj.getClass().getMethod("getFields");
        } catch (NoSuchMethodException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNotNull(getFieldsMethod);
    }

    @Test
    public void testInvokeGetMethod() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "obj";
            }

            public Object getFields() {
                return new Integer(42);
            }
        };

        try {
            Object fields = getFieldsMethod.invoke(obj);
            assertEquals(new Integer(42), fields);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testInvokeGetMethodArray() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                return "obj";
            }

            public Object[] getFields() {
                return new Object[] {new Integer(42), new String("foo")};
            }
        };

        try {
            Object[] fields = (Object[]) getFieldsMethod.invoke(obj);
            assertEquals(new Integer(42), fields[0]);
            assertEquals("foo", fields[1]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckEqualsArrays() {
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        assertTrue(IFXObject.this.doCheckEquals(array1, array2));

        Object[] array3 = {new Integer(42)};
        Object[] array4 = {new Integer(42)};
        assertTrue(IFXObject.this.doCheckEquals(array3, array4));

        Object[] array5 = {new Integer(42)};
        Object[] array6 = {new Integer(43)};
        assertFalse(IFXObject.this.doCheckEquals(array5, array6));

        Object[] array7 = {new Integer(42), new String("foo")};
        Object[] array8 = {new Integer(42), new String("bar")};
        assertFalse(IFXObject.this.doCheckEquals(array7, array8));
    }

    @Test
    public void testCheckEqualsNull() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };

        assertFalse(IFXObject.this.doCheckEquals(obj1, null));
    }

    @Test
    public void testCheckEqualsException() {
        IFXObject obj1 = new IFXObject() {
            @Override
            public String toString() {
                return "obj1";
            }
        };

        try {
            IFXObject obj2 = new IFXObject() {
                @Override
                public String toString() {
                    throw new RuntimeException();
                }
            };
            assertFalse(IFXObject.this.doCheckEquals(obj1, obj2));
        } catch (Exception e) {
            // expected
        }
    }

    private Method getFieldsMethod;

    private void testStripLeadingHyphens(String input, String expectedOutput) {
        String output = "";
        try {
            output = (String) getFieldsMethod.invoke(new IFXObject() {
                @Override
                public String toString() {
                    return "stripLeadingHyphens";
                }

                public String stripLeadingHyphens(String str) {
                    return IFXObject.Util.stripLeadingHyphens(str);
                }
            }, 0);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(expectedOutput, output);
        assertEquals(expectedOutput, IFXObject.Util.stripLeadingHyphens(input));
    }

    private void tab(int level) {
        // not used in this test
    }
}