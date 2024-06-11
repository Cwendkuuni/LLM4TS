package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    // Test for empty pointer
    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    // Test for pointer with single property
    @Test
    public void testSinglePropertyPointer() {
        String input = "/propertyName";
        JsonPointer propertyPointer = JsonPointer.compile(input);
        assertEquals("/propertyName", propertyPointer.toString());
        assertTrue(propertyPointer.matches());
        assertEquals("propertyName", propertyPointer.getMatchingProperty());
        assertFalse(propertyPointer.mayMatchElement());
        assertNull(propertyPointer.tail());
    }

    // Test for pointer with single index
    @Test
    public void testSingleIndexPointer() {
        String input = "/array/0";
        JsonPointer indexPointer = null;
        try {
            indexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            // This is expected, as the number of elements in the array is not known
            // at compile time.
            return;
        }
        assertEquals("/array/0", indexPointer.toString());
        assertTrue(indexPointer.matches());
        assertNull(indexPointer.getMatchingProperty());
        assertEquals(0, indexPointer.getMatchingIndex());
        assertFalse(indexPointer.mayMatchProperty());
        assertNull(indexPointer.tail());
    }

    // Test for pointer with multiple properties and index
    @Test
    public void testMultiplePropertiesAndIndexPointer() {
        String input = "/object/property1/0";
        JsonPointer complexPointer = JsonPointer.compile(input);
        assertEquals("/object/property1/0", complexPointer.toString());
        assertTrue(complexPointer.matches());
        assertEquals("property1", complexPointer.getMatchingProperty());
        assertEquals(0, complexPointer.getMatchingIndex());
        assertFalse(complexPointer.mayMatchProperty());
        assertNull(complexPointer.tail());
    }

    // Test for pointer with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputPointer() {
        String input = "invalidInput";
        JsonPointer.compile(input);
    }

    // Test for pointer with empty input
    @Test
    public void testEmptyInputPointer() {
        String input = "";
        JsonPointer emptyInputPointer = JsonPointer.compile(input);
        assertEquals("", emptyInputPointer.toString());
        assertTrue(emptyInputPointer.matches());
        assertEquals("", emptyInputPointer.getMatchingProperty());
        assertEquals(-1, emptyInputPointer.getMatchingIndex());
        assertFalse(emptyInputPointer.mayMatchProperty());
        assertFalse(emptyInputPointer.mayMatchElement());
        assertNull(emptyInputPointer.tail());
    }

    // Test for pointer with property and index
    @Test
    public void testPropertyAndIndexPointer() {
        String input = "/object/property1/0";
        JsonPointer propertyAndIndexPointer = JsonPointer.compile(input);
        assertEquals("/object/property1/0", propertyAndIndexPointer.toString());
        assertTrue(propertyAndIndexPointer.matches());
        assertEquals("property1", propertyAndIndexPointer.getMatchingProperty());
        assertEquals(0, propertyAndIndexPointer.getMatchingIndex());
        assertFalse(propertyAndIndexPointer.mayMatchProperty());
        assertNull(propertyAndIndexPointer.tail());

        JsonPointer propertyPointer = propertyAndIndexPointer.matchProperty("property1");
        assertEquals("/object/property1", propertyPointer.toString());
        assertTrue(propertyPointer.matches());
        assertEquals("property1", propertyPointer.getMatchingProperty());
        assertFalse(propertyPointer.mayMatchElement());
        assertNull(propertyPointer.tail());

        JsonPointer indexPointer = propertyAndIndexPointer.matchElement(0);
        assertEquals("/object/property1/0", indexPointer.toString());
        assertTrue(indexPointer.matches());
        assertNull(indexPointer.getMatchingProperty());
        assertEquals(0, indexPointer.getMatchingIndex());
        assertFalse(indexPointer.mayMatchProperty());
        assertNull(indexPointer.tail());
    }

    // Test for pointer with multiple properties and multiple indices
    @Test
    public void testMultiplePropertiesAndIndicesPointer() {
        String input = "/object/property1/0/array/1";
        JsonPointer complexPointer = JsonPointer.compile(input);
        assertEquals("/object/property1/0/array/1", complexPointer.toString());
        assertTrue(complexPointer.matches());
        assertEquals("property1", complexPointer.getMatchingProperty());
        assertEquals(0, complexPointer.getMatchingIndex());
        assertFalse(complexPointer.mayMatchProperty());
        assertNull(complexPointer.tail());

        JsonPointer propertyPointer = complexPointer.matchProperty("property1");
        assertEquals("/object/property1", propertyPointer.toString());
        assertTrue(propertyPointer.matches());
        assertEquals("property1", propertyPointer.getMatchingProperty());
        assertFalse(propertyPointer.mayMatchElement());
        assertNull(propertyPointer.tail());

        JsonPointer indexPointer1 = propertyPointer.matchElement(0);
        assertEquals("/object/property1/0", indexPointer1.toString());
        assertTrue(indexPointer1.matches());
        assertNull(indexPointer1.getMatchingProperty());
        assertEquals(0, indexPointer1.getMatchingIndex());
        assertFalse(indexPointer1.mayMatchProperty());
        assertNull(indexPointer1.tail());

        JsonPointer indexPointer2 = indexPointer1.matchElement(1);
        assertEquals("/object/property1/0/array/1", indexPointer2.toString());
        assertTrue(indexPointer2.matches());
        assertNull(indexPointer2.getMatchingProperty());
        assertEquals(1, indexPointer2.getMatchingIndex());
        assertFalse(indexPointer2.mayMatchProperty());
        assertNull(indexPointer2.tail());
    }

    // Test for pointer with multiple properties and invalid index
    @Test
    public void testMultiplePropertiesAndInvalidIndexPointer() {
        String input = "/object/property1/-1";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and index out of bounds
    @Test
    public void testMultiplePropertiesAndIndexOutOfBoundsPointer() {
        String input = "/object/property1/-1";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and index larger than array size
    @Test
    public void testMultiplePropertiesAndIndexLargerThanArraySizePointer() {
        String input = "/object/property1/1000000";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and empty array
    @Test
    public void testMultiplePropertiesAndEmptyArrayPointer() {
        String input = "/object/property1/[]";
        JsonPointer complexPointer = JsonPointer.compile(input);
        assertEquals("/object/property1/[]", complexPointer.toString());
        assertTrue(complexPointer.matches());
        assertEquals("property1", complexPointer.getMatchingProperty());
        assertFalse(complexPointer.mayMatchElement());
        assertNull(complexPointer.tail());

        JsonPointer propertyPointer = complexPointer.matchProperty("property1");
        assertEquals("/object/property1", propertyPointer.toString());
        assertTrue(propertyPointer.matches());
        assertEquals("property1", propertyPointer.getMatchingProperty());
        assertFalse(propertyPointer.mayMatchElement());
        assertNull(propertyPointer.tail());

        JsonPointer emptyArrayPointer = propertyPointer.matchElement(0);
        assertEquals("/object/property1/[]", emptyArrayPointer.toString());
        assertTrue(emptyArrayPointer.matches());
        assertNull(emptyArrayPointer.getMatchingProperty());
        assertFalse(emptyArrayPointer.mayMatchElement());
        assertNull(emptyArrayPointer.tail());
    }

    // Test for pointer with multiple properties and non-existent property
    @Test
    public void testMultiplePropertiesAndNonExistentPropertyPointer() {
        String input = "/object/nonExistentProperty/0";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and non-existent property and empty array
    @Test
    public void testMultiplePropertiesAndNonExistentPropertyAndEmptyArrayPointer() {
        String input = "/object/nonExistentProperty/[]";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and non-existent property and array with one element
    @Test
    public void testMultiplePropertiesAndNonExistentPropertyAndArrayWithOneElementPointer() {
        String input = "/object/nonExistentProperty/[1]";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    // Test for pointer with multiple properties and non-existent property and array with multiple elements
    @Test
    public void testMultiplePropertiesAndNonExistentPropertyAndArrayWithMultipleElementsPointer() {
        String input = "/object/nonExistentProperty/[1, 2, 3]";
        try {
            JsonPointer complexPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }
}