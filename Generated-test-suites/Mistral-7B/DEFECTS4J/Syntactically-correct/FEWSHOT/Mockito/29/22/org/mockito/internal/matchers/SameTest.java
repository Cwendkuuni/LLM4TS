package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;

import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object expected = new Object();
        assertThat(expected, is(same(expected)));
    }

    @Test
    public void testSameString() {
        String expected = "expected";
        assertThat(expected, is(same(expected)));
    }

    @Test
    public void testSameCharacter() {
        char expected = 'e';
        assertThat(expected, is(same(expected)));
    }

    @Test
    public void testDifferentInstances() {
        Object expected = new Object();
        Object actual = new Object();
        assertFalse(same(expected, actual).matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String expected = "expected";
        String actual = "actual";
        assertFalse(same(expected, actual).matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char expected = 'e';
        char actual = 'a';
        assertFalse(same(expected, actual).matches(actual));
    }

    @Test
    public void testSameArrayList() {
        Object[] expected = new Object[0];
        Object[] actual = new Object[0];
        assertThat(expected, is(same(actual)));
    }

    @Test
    public void testDifferentArrayLists() {
        Object[] expected = new Object[0];
        Object[] actual = new Object[1];
        assertFalse(same(expected, actual).matches(actual));
    }

    @Test
    public void testSameNull() {
        Object expected = null;
        assertThat(expected, is(same(expected)));
    }

    @Test
    public void testSamePrimitive() {
        int expected = 42;
        int actual = 42;
        assertThat(expected, is(same(actual)));
    }

    @Test
    public void testDifferentPrimatives() {
        int expected = 42;
        long actual = 42L;
        assertFalse(same(expected, actual).matches(actual));
    }
}