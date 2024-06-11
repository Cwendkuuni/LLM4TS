package com.example.junit.tests.google.common.base;

import static org.junit.Assert.*;

import com.google.common.base.Objects;

import org.junit.Test;

public class ObjectsTest {

    // equal method tests
    @Test
    public void testEqualNullNull() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullObject() {
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqualSameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqualDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqualNullAndNonnullObject() {
        Object obj = new Object();
        assertFalse(Objects.equal(null, obj));
    }

    // hashCode method tests
    @Test
    public void testHashCodeNullArray() {
        int result = Objects.hashCode(null);
        assertEquals(-1, result);
    }

    @Test
    public void testHashCodeEmptyArray() {
        int result = Objects.hashCode(new Object[0]);
        // hash code of empty array is specified in Java docs: 0
        assertEquals(0, result);
    }

    @Test
    public void testHashCodeSingleObject() {
        Object obj = new Object();
        int result = Objects.hashCode(obj);
        // hash code of an object is specified in Java docs: the internal hash code of the object
        assertNotSame(0, result);
    }

    @Test
    public void testHashCodeMultipleObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = Objects.hashCode(obj1, obj2);
        // hash codes of different objects should be different
        assertNotSame(Objects.hashCode(obj1), Objects.hashCode(obj2));
        // hash code of multiple objects is the XOR of the hash codes of the individual objects
        int hashCodeObj1 = obj1.hashCode();
        int hashCodeObj2 = obj2.hashCode();
        int expectedResult = hashCodeObj1 ^ hashCodeObj2;
        assertEquals(expectedResult, result);
    }

    // toStringHelper methods tests
    @Test(expected = IllegalArgumentException.class)
    public void testToStringHelperNullSelf() {
        Objects.toStringHelper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringHelperNullClassName() {
        Objects.toStringHelper(null);
    }

    @Test
    public void testToStringHelperStringClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        String result = helper.toString();
        String expectedResult = "ClassName{ }";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToStringHelperClassClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        String result = helper.toString();
        String expectedResult = "class String{ }";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToStringHelperWithValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").add("name", "value");
        String result = helper.toString();
        String expectedResult = "ClassName{ name=value }";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToStringHelperWithOmittedNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").add("name1", null).add("name2", "value2");
        String result = helper.toString();
        String expectedResult = "ClassName{ name2=value2 }";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToStringHelperWithNestedCalls() {
        Objects.ToStringHelper outerHelper = Objects.toStringHelper("OuterClassName").add("outerName", "outerValue");
        Objects.ToStringHelper innerHelper = outerHelper.add("inner", Objects.toStringHelper("InnerClassName")
                .add("innerName1", "innerValue1").add("innerName2", "innerValue2"));
        String result = outerHelper.toString();
        String expectedResult = "OuterClassName{ outerName=outerValue, inner=InnerClassName{ innerName1=innerValue1, innerName2=innerValue2 } }";
        assertEquals(expectedResult, result);
    }

}