package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEqualsNull() {
        IFXObject obj1 = new IFXObject() {};
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        IFXObject obj1 = new IFXObject() {};
        assertEquals(obj1, obj1);
    }

    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObject() {};
        Object obj2 = new Object() {};
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToStringEmptyClass() {
        assertEquals("class org.sourceforge.ifx.basetypes.IFXObject = {}", new IFXObject() {}.toString());
    }

    @Test
    public void testToStringSimpleClass() {
        class TestClass {
            public String getStringValue() {
                return "test";
            }
        }

        TestClass testObj = new TestClass();
        assertEquals("class org.sourceforge.ifx.basetypes.TestClass = {getStringValue = test}", testObj.toString());
    }

    @Test
    public void testToStringArray() {
        class TestClass {
            public String[] getStringArray() {
                return new String[] {"foo", "bar"};
            }
        }

        TestClass testObj = new TestClass();
        String expected = "class org.sourceforge.ifx.basetypes.TestClass = {getStringArray = [foo, bar]}";
        assertEquals(expected, testObj.toString());
    }

    @Test
    public void testToStringNestedArray() {
        class OuterClass {
            class InnerClass {
                public String getStringValue() {
                    return "inner";
                }
            }

            public InnerClass[] getInnerArray() {
                return new InnerClass[] {new InnerClass()};
            }
        }

        OuterClass outerObj = new OuterClass();
        String expected = "class org.sourceforge.ifx.basetypes.OuterClass = {getInnerArray = [class org.sourceforge.ifx.basetypes.OuterClass$InnerClass = {getStringValue = inner}]}";
        assertEquals(expected, outerObj.toString());
    }

    @Test
    public void testToStringNestedClasses() {
        class OuterClass {
            class InnerClass1 {
                public String getStringValue1() {
                    return "inner1";
                }
            }

            class InnerClass2 {
                public InnerClass1 getInnerClass1() {
                    return new InnerClass1();
                }

                public String getStringValue2() {
                    return "inner2";
                }
            }

            public InnerClass2 getInnerClass2() {
                return new InnerClass2();
            }
        }

        OuterClass outerObj = new OuterClass();
        String expected = "class org.sourceforge.ifx.basetypes.OuterClass = {getInnerClass2 = {getClassName = class org.sourceforge.ifx.basetypes.OuterClass$InnerClass2, getInnerClass1 = {getClassName = class org.sourceforge.ifx.basetypes.OuterClass$InnerClass1, getStringValue1 = inner1}}}";
        assertEquals(expected, outerObj.toString());
    }

    @Test
    public void testToStringNullValue() throws IllegalAccessException, InvocationTargetException {
        class TestClass {
            public IBaseType getNullValue() {
                return null;
            }
        }

        TestClass testObj = new TestClass();
        assertEquals("class org.sourceforge.ifx.basetypes.TestClass = {getNullValue = NULL}", testObj.toString());
    }

    @Test
    public void testEqualsNullValues() {
        IFXObject obj1 = new IFXObject() {};
        IFXObject obj2 = new IFXObject() {};
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        IFXObject obj1 = new IFXObject() {};
        IFXObject obj2 = new IFXObject() {};
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsSameFields() throws IllegalAccessException, InvocationTargetException {
        class TestClass {
            private int field1;

            public int getField1() {
                return field1;
            }

            public void setField1(int value) {
                this.field1 = value;
            }
        }

        TestClass obj1 = new TestClass();
        obj1.setField1(5);
        TestClass obj2 = new TestClass();
        obj2.setField1(5);
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentFields() throws IllegalAccessException, InvocationTargetException {
        class TestClass {
            private int field1;

            public int getField1() {
                return field1;
            }

            public void setField1(int value) {
                this.field1 = value;
            }
        }

        TestClass obj1 = new TestClass();
        obj1.setField1(5);
        TestClass obj2 = new TestClass();
        obj2.setField1(6);
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToStringNestedClassesWithArrays() {
        class OuterClass {
            class InnerClass {
                public String getStringValue1() {
                    return "inner1";
                }

                public String[] getStringArray() {
                    return new String[] {"foo", "bar"};
                }
            }

            public InnerClass getInnerClass() {
                return new InnerClass();
            }
        }

        OuterClass outerObj = new OuterClass();
        String expected = "class org.sourceforge.ifx.basetypes.OuterClass = {getInnerClass = {getClassName = class org.sourceforge.ifx.basetypes.OuterClass$InnerClass, getStringValue1 = inner1, getStringArray = [foo, bar]}}";
        assertEquals(expected, outerObj.toString());
    }
}