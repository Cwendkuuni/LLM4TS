package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new DummyIFXObject("1");
        IFXObject obj2 = new DummyIFXObject("1");
        IFXObject obj3 = new DummyIFXObject("2");
        assertFalse(obj1.equals(null));
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testToString() {
        IFXObject obj = new DummyIFXObject("1");
        String expected = "DummyIFXObject = {\nClass = org.sourceforge.ifx.basetypes.DummyIFXObject\ngetString = 1\n}";
        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStringify_nullObject() {
        IFXObject obj = null;
        String expected = "Exception! null";
        String actual = IFXObject.stringify(obj, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringify_objectWithGetMethods() {
        IFXObject obj = new DummyIFXObject("1");
        String expected = "DummyIFXObject = {\nClass = org.sourceforge.ifx.basetypes.DummyIFXObject\ngetString = 1\n}";
        String actual = IFXObject.stringify(obj, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringify_objectWithArrayProperty() {
        IFXObject obj = new ArrayDummyIFXObject(new int[] { 1, 2, 3 });
        String expected = "ArrayDummyIFXObject = {\nClass = org.sourceforge.ifx.basetypes.ArrayDummyIFXObject\ngetArrayProperty = [1, 2, 3]\n}";
        String actual = IFXObject.stringify(obj, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringify_objectWithNestedArrayProperty() {
        IFXObject obj = new NestedArrayDummyIFXObject(new int[][] { { 1, 2 }, { 3, 4 } });
        String expected = "NestedArrayDummyIFXObject = {\nClass = org.sourceforge.ifx.basetypes.NestedArrayDummyIFXObject\ngetNestedArrayProperty = [\n[1, 2]\n[3, 4]\n]}\n}";
        String actual = IFXObject.stringify(obj, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoCheckEquals_equalObjects() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject("1");
        assertTrue(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_inequalObjects() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject("2");
        assertFalse(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_objectsWithArrayProperties() {
        ArrayDummyIFXObject obj1 = new ArrayDummyIFXObject(new int[] { 1, 2 });
        ArrayDummyIFXObject obj2 = new ArrayDummyIFXObject(new int[] { 1, 2 });
        assertTrue(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_objectsWithNestedArrayProperties() {
        NestedArrayDummyIFXObject obj1 = new NestedArrayDummyIFXObject(new int[][] { { 1, 2 }, { 3, 4 } });
        NestedArrayDummyIFXObject obj2 = new NestedArrayDummyIFXObject(new int[][] { { 1, 2 }, { 3, 4 } });
        assertTrue(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_objectsWithDifferentClasses() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        StringDummyIFXObject obj2 = new StringDummyIFXObject("1");
        assertFalse(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_objectsWithNullProperties() {
        DummyIFXObject obj1 = new DummyIFXObject(null);
        DummyIFXObject obj2 = new DummyIFXObject(null);
        assertTrue(IFXObject.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_objectsWithNullProperties_and_nonNullProperties() {
        DummyIFXObject obj1 = new DummyIFXObject("1");
        DummyIFXObject obj2 = new DummyIFXObject(null);
        assertFalse(IFXObject.doCheckEquals(obj1, obj2));
    }

    private static class DummyIFXObject implements IFXObject {
        private String stringProperty;

        public DummyIFXObject(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.doCheckEquals(this, obj);
        }

        @Override
        public String toString() {
            return IFXObject.stringify(this, 0);
        }

        @Override
        public String getString() {
            return stringProperty;
        }
    }

    private static class ArrayDummyIFXObject implements IFXObject {
        private int[] arrayProperty;

        public ArrayDummyIFXObject(int[] arrayProperty) {
            this.arrayProperty = arrayProperty;
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.doCheckEquals(this, obj);
        }

        @Override
        public String toString() {
            return IFXObject.stringify(this, 0);
        }

        @Override
        public int[] getArrayProperty() {
            return arrayProperty;
        }
    }

    private static class NestedArrayDummyIFXObject implements IFXObject {
        private int[][] nestedArrayProperty;

        public NestedArrayDummyIFXObject(int[][] nestedArrayProperty) {
            this.nestedArrayProperty = nestedArrayProperty;
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.doCheckEquals(this, obj);
        }

        @Override
        public String toString() {
            return IFXObject.stringify(this, 0);
        }

        @Override
        public int[][] getNestedArrayProperty() {
            return nestedArrayProperty;
        }
    }

    private static class StringDummyIFXObject implements IFXObject {
        private String stringProperty;

        public StringDummyIFXObject(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        @Override
        public boolean equals(Object obj) {
            return IFXObject.doCheckEquals(this, obj);
        }

        @Override
        public String toString() {
            return IFXObject.stringify(this, 0);
        }

        @Override
        public String getString() {
            return stringProperty;
        }
    }
}