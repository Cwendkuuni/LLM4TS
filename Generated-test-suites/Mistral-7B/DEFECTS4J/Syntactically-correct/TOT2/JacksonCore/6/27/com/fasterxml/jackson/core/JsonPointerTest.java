package com.fasterxml.jackson.core.test;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonPointerTest {
    @Test
    public void testCompile_ValidJsonPointer() {
        String input = "/foo/bar/baz";
        JsonPointer expected = new JsonPointer("/foo/bar/baz");
        JsonPointer actual = JsonPointer.compile(input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidJsonPointer() {
        String input = "invalid";
        JsonPointer actual = null;
        try {
            actual = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        Assert.assertNull(actual);
    }

    @Test
    public void testMatchProperty_MatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        String property = "bar";
        JsonPointer result = pointer.matchProperty(property);
        Assert.assertEquals(pointer.tail(), result);
    }

    @Test
    public void testMatchProperty_NonMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        String property = "baz";
        JsonPointer result = pointer.matchProperty(property);
        Assert.assertNull(result);
    }

    @Test
    public void testMatchElement_MatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo/0");
        int index = 0;
        JsonPointer result = pointer.matchElement(index);
        Assert.assertEquals(pointer.tail(), result);
    }

    @Test
    public void testMatchElement_NonMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo/0");
        int index = 1;
        JsonPointer result = pointer.matchElement(index);
        Assert.assertNull(result);
    }

    @Test
    public void testMatches_EmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testMatches_NonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        Assert.assertFalse(pointer.matches());
    }

    @Test
    public void testToString_EmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode_SameObjects() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals_NullObject() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        Assert.assertFalse(pointer.equals(null));
    }

    @Test
    public void testEquals_DifferentObjectClasses() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        Object obj = new Object();
        Assert.assertFalse(pointer.equals(obj));
    }

    @Test
    public void testTail_NullPointer() {
        JsonPointer pointer = null;
        Assert.assertNull(pointer.tail());
    }

    @Test
    public void testFromSegment_EmptyArray() {
        String[] segments = {};
        JsonPointer expected = JsonPointer.EMPTY;
        JsonPointer actual = JsonPointer.fromSegment(segments);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromSegment_NullArrayElements() {
        String[] segments = { null };
        JsonPointer actual = null;
        try {
            actual = JsonPointer.fromSegment(segments);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        Assert.assertNull(actual);
    }

    // Test cases for other edge cases and exceptions
}