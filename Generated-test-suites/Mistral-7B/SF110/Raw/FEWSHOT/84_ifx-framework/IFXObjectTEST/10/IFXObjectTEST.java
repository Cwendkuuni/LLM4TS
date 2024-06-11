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
    public void testEquals_NullObjects() {
        IFXObject obj1 = null;
        IFXObject obj2 = null;
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_NullAndNonNullObject() {
        IFXObject obj1 = null;
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEquals_SameClass() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_DifferentClasses() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify_NullObject() {
        IFXObject obj = null;
        String expected = "Exception! java.lang.NullPointerException";
        String result = obj.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_EmptyObject() {
        IFXObject obj = new IFXObject();
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {}";
        String result = obj.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_ObjectWithMethods() {
        IFXObject obj = new IFXObject() {
            private int value = 42;

            public int getValue() {
                return value;
            }
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetValue = 42}";
        String result = obj.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_ObjectWithArrayProperty() {
        IFXObject obj = new IFXObject() {
            private int[] array = {1, 2, 3};

            public int[] getArray() {
                return array;
            }
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\narray = [\n         [0]:1,\n         [1]:2,\n         [2]:3\n]}";
        String result = obj.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_ObjectWithNestedObjects() {
        IFXObject outerObj = new IFXObject() {
            private IFXObject innerObj = new IFXObject() {
                private int value = 42;

                public int getValue() {
                    return value;
                }
            };

            public IFXObject getInnerObj() {
                return innerObj;
            }
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\ninnerObj = {\ngetValue = 42}\n}";
        String result = outerObj.stringify(outerObj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testDoCheckEquals_NullObjects() {
        IFXObject obj1 = null;
        IFXObject obj2 = null;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_NullAndNonNullObject() {
        IFXObject obj1 = null;
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_SameObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = obj1;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentClasses() {
        IFXObject obj1 = new IFXObject();
        String obj2 = "Hello World!";
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_Arrays() {
        IFXObject obj1 = new IFXObject() {
            private int[] array1 = {1, 2, 3};

            public int[] getArray1() {
                return array1;
            }
        };
        IFXObject obj2 = new IFXObject() {
            private int[] array2 = {1, 2, 3};

            public int[] getArray2() {
                return array2;
            }
        };
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_ArraysDifferentLengths() {
        IFXObject obj1 = new IFXObject() {
            private int[] array1 = {1, 2, 3};

            public int[] getArray1() {
                return array1;
            }
        };
        IFXObject obj2 = new IFXObject() {
            private int[] array2 = {1, 2};

            public int[] getArray2() {
                return array2;
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_ArraysDifferentValues() {
        IFXObject obj1 = new IFXObject() {
            private int[] array1 = {1, 2, 3};

            public int[] getArray1() {
                return array1;
            }
        };
        IFXObject obj2 = new IFXObject() {
            private int[] array2 = {1, 2, 4};

            public int[] getArray2() {
                return array2;
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_ObjectsWithSameMethods() {
        IFXObject obj1 = new IFXObject() {
            private int value1 = 42;

            public int getValue1() {
                return value1;
            }
        };
        IFXObject obj2 = new IFXObject() {
            private int value2 = 42;

            public int getValue2() {
                return value2;
            }
        };
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_ObjectsWithDifferentMethods() {
        IFXObject obj1 = new IFXObject() {
            private int value1 = 42;

            public int getValue1() {
                return value1;
            }

            public String getString() {
                return "Hello World!";
            }
        };
        IFXObject obj2 = new IFXObject() {
            private int value2 = 43;

            public int getValue2() {
                return value2;
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_ObjectsWithNestedObjects() {
        IFXObject outerObj1 = new IFXObject() {
            private IFXObject innerObj1 = new IFXObject() {
                private int value = 42;

                public int getValue() {
                    return value;
                }
            };

            public IFXObject getInnerObj1() {
                return innerObj1;
            }
        };
        IFXObject outerObj2 = new IFXObject() {
            private IFXObject innerObj2 = new IFXObject() {
                private int value = 43;

                public int getValue() {
                    return value;
                }
            };

            public IFXObject getInnerObj2() {
                return innerObj2;
            }
        };
        assertFalse(outerObj1.doCheckEquals(outerObj1, outerObj2));
    }

    @Test
    public void testDoCheckEquals_ObjectsWithNestedObjectsEqual() {
        IFXObject outerObj1 = new IFXObject() {
            private IFXObject innerObj1 = new IFXObject() {
                private int value = 42;

                public int getValue() {
                    return value;
                }
            };

            public IFXObject getInnerObj1() {
                return innerObj1;
            }
        };
        IFXObject outerObj2 = new IFXObject() {
            private IFXObject innerObj2 = new IFXObject() {
                private int value = 42;

                public int getValue() {
                    return value;
                }
            };

            public IFXObject getInnerObj2() {
                return innerObj2;
            }
        };
        assertTrue(outerObj1.doCheckEquals(outerObj1, outerObj2));
    }

    @Test
    public void testTab_ZeroLevel() {
        IFXObject obj = new IFXObject();
        String expected = "";
        String result = obj.tab(0);
        assertEquals(expected, result);
    }

    @Test
    public void testTab_NonZeroLevel() {
        IFXObject obj = new IFXObject();
        String expected = "  ";
        String result = obj.tab(1);
        assertEquals(expected, result);
    }

    @Test
    public void testTab_MultipleLevels() {
        IFXObject obj = new IFXObject();
        String expected = "   ";
        String result = obj.tab(2);
        assertEquals(expected, result);
    }
}