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

    private static final Class IFXObjectClass = IFXObject.class;
    private static final String TAB_STOP = IFXObject.TABSTOP;

    @Test
    public void testEquals() {
        IFXObject obj1 = new DummyIFXObject("1");
        IFXObject obj2 = new DummyIFXObject("1");
        IFXObject obj3 = new DummyIFXObject("2");

        assertEquals(obj1, obj1);
        assertEquals(obj1, obj2);
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Object()));
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testToString_EmptyClass() {
        assertNull(new IFXObject().toString());
    }

    @Test
    public void testToString() {
        DummyIFXObject obj = new DummyIFXObject("1");
        String expected = "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                this.tab(1) + "getField1 = 1\n" +
                "}";

        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_Array() {
        DummyIFXObject[] array = new DummyIFXObject[2];
        array[0] = new DummyIFXObject("1");
        array[1] = new DummyIFXObject("2");

        String expected = "org.sourceforge.ifx.basetypes.DummyIFXObject[] = {\n" +
                this.tab(1) + "[0]:\n" +
                this.tab(2) + "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                this.tab(3) + "getField1 = 1\n" +
                this.tab(3) + "}\n" +
                this.tab(1) + "[1]:\n" +
                this.tab(2) + "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                this.tab(3) + "getField1 = 2\n" +
                this.tab(3) + "}\n" +
                this.tab(1) + "}";

        String actual = Array.toString(array);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_BaseType() {
        DummyIFXObject obj = new DummyIFXObject("1");
        IFXBaseType baseType = new DummyIFXBaseType("Base");

        String expected = "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                "getField1 = org.sourceforge.ifx.basetypes.DummyIFXBaseType = Base\n" +
                "}";

        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_NestedBaseTypes() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXBaseType baseType1 = new DummyIFXBaseType("Base1");
        DummyIFXObject obj2 = new DummyIFXObject("2");
        DummyIFXBaseType baseType2 = new DummyIFXBaseType("Base2");

        Object nestedObject = new DummyNestedObject(baseType1, baseType2);

        String expected = "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                "getField1 = org.sourceforge.ifx.basetypes.DummyIFXBaseType = Base1\n" +
                "getField2 = org.sourceforge.ifx.basetypes.DummyIFXBaseType = Base2\n" +
                "}";

        String actual = obj1.toString();
        assertEquals(expected, actual);

        expected = "org.sourceforge.ifx.basetypes.DummyIFXObject = {\n" +
                "getField1 = org.sourceforge.ifx.basetypes.DummyIFXBaseType = Base1\n" +
                "getField2 = {\n" +
                this.tab(1) + "getClass = class org.sourceforge.ifx.basetypes.DummyIFXBaseType\n" +
                this.tab(1) + "getString = Base2\n" +
                "}\n" +
                "}";

        actual = obj2.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoCheckEquals_EmptyClasses() {
        IFXObject obj1 = new DummyIFXObject("");
        IFXObject obj2 = new DummyIFXObject("");
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentClasses() {
        IFXObject obj1 = new DummyIFXObject("1");
        IFXObject obj2 = new DummyIFXBaseType("Base");
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_DifferentFields() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject("2");
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_SameFieldsButDifferentValues() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject("2");
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_SameFieldsWithSameValues() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject("1");
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_NestedObjects_SameObjects() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXBaseType baseType1 = new DummyIFXBaseType("Base1");
        DummyIFXObject obj2 = new DummyIFXObject("1");
        DummyIFXBaseType baseType2 = new DummyIFXBaseType("Base2");

        DummyNestedObject nestedObject1 = new DummyNestedObject(baseType1, baseType2);
        DummyNestedObject nestedObject2 = new DummyNestedObject(baseType1, baseType2);

        assertTrue(obj1.doCheckEquals(obj1, obj2));
        assertTrue(obj1.doCheckEquals(obj1, obj2, nestedObject1, nestedObject2));
    }

    @Test
    public void testDoCheckEquals_NestedObjects_DifferentObjects() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXBaseType baseType1 = new DummyIFXBaseType("Base1");
        DummyIFXObject obj2 = new DummyIFXObject("1");
        DummyIFXBaseType baseType2 = new DummyIFXBaseType("Base2");

        DummyNestedObject nestedObject1 = new DummyNestedObject(baseType1, baseType2);
        DummyNestedObject nestedObject2 = new DummyNestedObject(new DummyIFXBaseType("Base3"), new DummyIFXBaseType("Base4"));

        assertFalse(obj1.doCheckEquals(obj1, obj2, nestedObject1, nestedObject2));
    }

    private static class DummyIFXObject implements IFXObject {
        private String field1;

        public DummyIFXObject(String field1) {
            this.field1 = field1;
        }

        @Override
        public String getField1() {
            return field1;
        }

        @Override
        public String toString() {
            return IFXObject.this.stringify(this, 0);
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.this.doCheckEquals(this, obj);
        }

        private String getClassString() {
            return IFXObjectClass.getName();
        }

        private String tab(int level) {
            return IFXObject.this.tab(level);
        }
    }

    private static class DummyIFXBaseType implements IBaseType {
        private String baseType;

        public DummyIFXBaseType(String baseType) {
            this.baseType = baseType;
        }

        @Override
        public String getString() {
            return baseType;
        }

        public String toString() {
            return baseType;
        }
    }

    private static class DummyNestedObject implements IFXObject {
        private DummyIFXBaseType baseType1;
        private DummyIFXBaseType baseType2;

        public DummyNestedObject(DummyIFXBaseType baseType1, DummyIFXBaseType baseType2) {
            this.baseType1 = baseType1;
            this.baseType2 = baseType2;
        }

        @Override
        public String getField1() {
            return null;
        }

        @Override
        public String toString() {
            return IFXObject.this.stringify(this, 0);
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.this.doCheckEquals(this, obj);
        }

        private String getClassString() {
            return IFXObjectClass.getName();
        }

        private String tab(int level) {
            return IFXObject.this.tab(level);
        }
    }
}