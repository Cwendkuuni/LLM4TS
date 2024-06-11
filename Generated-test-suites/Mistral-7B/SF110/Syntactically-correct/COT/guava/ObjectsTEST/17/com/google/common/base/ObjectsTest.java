package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object obj1 = new Object(); // replace with actual object type
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        Object obj3 = new Object(); // replace with actual object type
        assertFalse(Objects.equal(obj1, obj3));

        Object obj4 = null;
        assertFalse(Objects.equal(obj1, obj4));

        Object obj5 = new Object(); // replace with actual object type
        assertFalse(Objects.equal(obj4, obj5));

        Object obj6 = new Object(); // replace with actual object type
        assertTrue(Objects.equal(obj6, obj6));

        Object obj7 = new String("test");
        String obj8 = new String("test");
        assertTrue(Objects.equal(obj7, obj8));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullPointerExceptionScenario() {
        Object obj1 = null;
        Object obj2 = new Object(); // replace with actual object type
        Objects.equal(obj1, obj2);
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object(); // replace with actual object type
        Object obj2 = new Object(); // replace with actual object type
        int hashCode1 = Objects.hashCode(obj1);
        int hashCode2 = Objects.hashCode(obj2);
        assertNotSame(hashCode1, hashCode2);

        Object obj3 = obj1;
        int hashCode3 = Objects.hashCode(obj3);
        assertEquals(hashCode1, hashCode3);

        Object[] objects = new Object[5];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object(); // replace with actual object type
        }
        int hashCode4 = Objects.hashCode(objects);
        int expectedHashCode = 16777619; // Java hashCode for "com.google.common.base.Objects"
        assertEquals(expectedHashCode, hashCode4);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNullPointerExceptionScenario() {
        Object[] objects = null;
        Objects.hashCode(objects);
    }

    @Test
    public void testToStringHelperAddNullValues() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", null)
                .add("name2", null);
        String output = toStringHelper.toString();
        assertTrue(output.contains("ClassName{name1=, name2=}"));
    }

    @Test
    public void testToStringHelperAddValues() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", "value2");
        String output = toStringHelper.toString();
        String expectedOutput = "ClassName{name1=value1, name2=value2}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", null)
                .add("name2", "value2")
                .omitNullValues();
        String output = toStringHelper.toString();
        String expectedOutput = "ClassName{name2=value2}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testToStringHelperAddDifferentTypes() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", 1)
                .add("name2", "value2")
                .add("name3", 3.14);
        String output = toStringHelper.toString();
        String expectedOutput = "ClassName{name1=1, name2=value2, name3=3.14}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testToStringHelperAddEmptyString() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name", "");
        String output = toStringHelper.toString();
        String expectedOutput = "ClassName{name=}";
        assertEquals(expectedOutput, output);
    }
}