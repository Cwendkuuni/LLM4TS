package de.beiri22.stringincrementor.helper.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testIndexOf() {
        IndexedString is = new IndexedString("testtesttesta test test a");
        char[] target = {'t', 'e', 's', 't'};
        int expectedIndex = 10; // index of the first occurrence of "test" in the string
        int actualIndex = is.indexOf(target);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexOfEmptyString() {
        IndexedString is = new IndexedString("testtesttest");
        char[] target = {};
        int expectedIndex = -1;
        int actualIndex = is.indexOf(target);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test(expected = NullPointerException.class)
    public void testIndexOfNullInput() {
        IndexedString is = new IndexedString("testtesttest");
        char[] target = null;
        is.indexOf(target);
    }

    @Test
    public void testCount() {
        IndexedString is = new IndexedString("testtesttesta test test a");
        char c = 't';
        int expectedCount = 5;
        int actualCount = is.count(c);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testCountEdgeCase() {
        IndexedString is = new IndexedString("testtesttesta test test a");
        char c = 'z';
        int expectedCount = 0;
        int actualCount = is.count(c);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testIndexedStringEmptyString() {
        IndexedString is = new IndexedString("");
        int expectedLength = 0;
        char[] actualValues = is.values;
        assertEquals(expectedLength, actualValues.length);
    }

    @Test
    public void testIndexedStringConstructor() {
        IndexedString is = new IndexedString("testtesttesta test test a");
        int expectedLength = 15;
        char[] actualValues = is.values;
        int[][] actualIndex = is.index;
        assertEquals(expectedLength, actualValues.length);
        // Checking if index array is populated correctly for the first character 't'
        int expectedNumberOfOccurrences = 5;
        int[] actualIndexOccurrences = actualIndex['t'];
        assertEquals(expectedNumberOfOccurrences, actualIndexOccurrences.length);
        int expectedFirstIndex = 0;
        int actualFirstIndex = actualIndexOccurrences[0];
        assertEquals(expectedFirstIndex, actualFirstIndex);
    }

    @Test
    public void testIndexOfEdgeCase() {
        IndexedString is = new IndexedString("testtesttesta test test a");
        char[] target = {'a', 'b', 'c'};
        int expectedIndex = -1;
        int actualIndex = is.indexOf(target);
        assertEquals(expectedIndex, actualIndex);
    }

}