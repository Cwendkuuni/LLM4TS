package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "object1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            public String getString() {
                return "object1";
            }
        };
        assertTrue(obj1.equals(obj2));

        IFXObject obj3 = new IFXObject() {
            public String getString() {
                return "object3";
            }
        };
        assertFalse(obj1.equals(obj3));

        IFXObject obj4 = null;
        assertFalse(obj1.equals(obj4));

        IFXObject obj5 = new Object() {
            //Not an instance of IFXObject
        };
        assertFalse(obj1.equals(obj5));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            public String getString() {
                return "testObj";
            }
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetString = testObj\n}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testToString_Array() {
        IFXObject[] arr = new IFXObject[2];
        arr[0] = new IFXObject() {
            public String getString() {
                return "obj1";
            }
        };
        arr[1] = new IFXObject() {
            public String getString() {
                return "obj2";
            }
        };

        String expected = "class org.sourceforge.ifx.basetypes.IFXObject[] = {\n[0] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj1\n}\n[1] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj2\n}\n}";
        assertEquals(expected, Arrays.deepToString(arr));
    }

    @Test
    public void testToString_ArrayOfArrays() {
        IFXObject[][] arr2D = new IFXObject[2][2];
        arr2D[0][0] = new IFXObject() {
            public String getString() {
                return "obj1_1";
            }
        };
        arr2D[0][1] = new IFXObject() {
            public String getString() {
                return "obj1_2";
            }
        };
        arr2D[1][0] = new IFXObject() {
            public String getString() {
                return "obj2_1";
            }
        };
        arr2D[1][1] = new IFXObject() {
            public String getString() {
                return "obj2_2";
            }
        };

        String expected = "class org.sourceforge.ifx.basetypes.IFXObject[][] = {\n[0] = {\n[0] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj1_1\n}\n[1] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj1_2\n}\n}\n[1] = {\n[0] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj2_1\n}\n[1] = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj2_2\n}\n}";
        assertEquals(expected, Arrays.deepToString(arr2D));
    }

    @Test
    public void testToString_NestedObjects() {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "obj1";
            }

            public IFXObject getNestedObj() {
                return new IFXObject() {
                    public String getString() {
                        return "nestedObj";
                    }
                };
            }
        };

        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = obj1\ngetNestedObj = {\nclassName = org.sourceforge.ifx.basetypes.IFXObject\ngetString = nestedObj\n}";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testCheckEquals_SameObjects() {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "obj1";
            }
        };
        IFXObject obj2 = obj1;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testCheckEquals_DifferentObjects() {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "obj1";
            }
        };
        IFXObject obj2 = new IFXObject() {
            public String getString() {
                return "obj2";
            }
        };
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testCheckEquals_Null() {
        IFXObject obj = new IFXObject() {
            public String getString() {
                return "obj";
            }
        };
        assertFalse(obj.doCheckEquals(obj, null));
    }

    @Test
    public void testCheckEquals_DifferentClasses() {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "obj1";
            }
        };
        String obj2 = "obj2";
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testCheckEquals_Arrays() throws Exception {
        IFXObject arrayObj1 = new IFXObject() {
            public String getString() {
                return "array1";
            }

            public IFXObject[] getArray() {
                return new IFXObject[] {
                    new IFXObject() {
                        public String getString() {
                            return "element1";
                        }
                    },
                    new IFXObject() {
                        public String getString() {
                            return "element2";
                        }
                    }
                };
            }
        };

        IFXObject arrayObj2 = new IFXObject() {
            public String getString() {
                return "array2";
            }

            public IFXObject[] getArray() {
                return new IFXObject[] {
                    new IFXObject() {
                        public String getString() {
                            return "element1";
                        }
                    },
                    new IFXObject() {
                        public String getString() {
                            return "element3";
                        }
                    }
                };
            }
        };

        assertFalse(arrayObj1.doCheckEquals(arrayObj1, arrayObj2));
    }

    @Test
    public void testCheckEquals_BaseTypes() throws Exception {
        IFXObject obj1 = new IFXObject() {
            public String getString() {
                return "obj1";
            }

            public IBaseType getBaseType() {
                return new StringBaseTypeImpl() {
                    public String getString() {
                        return "baseType1";
                    }
                };
            }
        };

        IFXObject obj2 = new IFXObject() {
            public String getString() {
                return "obj2";
            }

            public IBaseType getBaseType() {
                return new StringBaseTypeImpl() {
                    public String getString() {
                        return "baseType2";
                    }
                };
            }
        };

        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
}