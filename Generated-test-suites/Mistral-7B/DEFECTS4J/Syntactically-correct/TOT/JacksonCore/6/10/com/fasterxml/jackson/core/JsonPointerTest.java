package com.fasterxml.jackson.core; // for importing JsonPointer class

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertTrue(pointer.matches());
        Assert.assertNull(pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertFalse(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
        Assert.assertNull(pointer.tail());
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertTrue(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
        JsonPointer next = pointer.tail();
        Assert.assertEquals("/bar", next.toString());
    }

    @Test
    public void testIndexPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(0, pointer.getMatchingIndex());
        Assert.assertTrue(pointer.mayMatchProperty());
        Assert.assertTrue(pointer.mayMatchElement());
        JsonPointer next = pointer.tail();
        Assert.assertNull(next);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~foo";
        JsonPointer.compile(input); // this should throw an exception
    }
}