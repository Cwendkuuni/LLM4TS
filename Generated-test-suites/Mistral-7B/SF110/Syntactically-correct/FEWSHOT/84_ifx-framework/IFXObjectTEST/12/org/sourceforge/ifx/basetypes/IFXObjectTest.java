package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new DummyIFXObject("1");
        IFXObject obj2 = new DummyIFXObject("1");
        IFXObject obj3 = new DummyIFXObject("2");

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj = new DummyIFXObject("1");
        String expected = "DummyIFXObject = {" +
                          "getID = 1" +
                          "}";

        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify_SimpleObject() {
        IFXObject obj = new DummyIFXObject("1");
        String expected = "DummyIFXObject = {" +
                          "getID = 1" +
                          "}";

        String result = IFXObject.stringify(obj, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_ArrayOfObjects() {
        IFXObject[] objArray = { new DummyIFXObject("1"), new DummyIFXObject("2") };
        String expected = "DummyIFXObject[] = {" +
                          "[" +
                          "DummyIFXObject = {" +
                          "getID = 1" +
                          "}" +
                          "]," +
                          "[" +
                          "DummyIFXObject = {" +
                          "getID = 2" +
                          "}" +
                          "]" +
                          "}";

        String result = IFXObject.stringify(objArray, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testStringify_NestedObjects() {
        IFXObject obj1 = new NestedIFXObject("1");
        String expected = "NestedIFXObject = {" +
                          "getID = 1" +
                          "getNestedObject = NestedIFXObject = {" +
                          "getID = 1" +
                          "getNestedObject = NULL" +
                          "}";

        String result = IFXObject.stringify(obj1, 0);
        assertEquals(expected, result);
    }

    private static class DummyIFXObject extends IFXObject {
        private final String id;

        public DummyIFXObject(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private static class NestedIFXObject extends IFXObject {
        private final IFXObject nestedObject;

        public NestedIFXObject(String id) {
            this.nestedObject = new DummyIFXObject("1");
        }

        @Override
        public IFXObject getNestedObject() {
            return nestedObject;
        }
    }
}