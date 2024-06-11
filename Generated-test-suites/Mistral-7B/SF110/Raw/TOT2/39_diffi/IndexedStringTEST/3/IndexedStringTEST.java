
package de.beiri22.stringincrementor.helper.test;

import org.junit.Test;
import org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCountMethod() {
        String input = "abcccdeef";
        IndexedString indexedString = new IndexedString(input);
        assertEquals(3, indexedString.count('c'));
    }

    @Test
    public void testCountEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));
    }

    @Test
    public void testCountSingleCharacter() {
        String input = "a";
        IndexedString indexedString = new IndexedString(input);
        assertEquals(1, indexedString.count('a'));
    }

    @Test
    public void testCountMultipleCharacters() {
        String input = "cccc";
        IndexedString indexedString = new IndexedString(input);
        assertEquals(3, indexedString.count('c'));
    }

    @Test
    public void testCountNoOccurrences() {
        String input = "abcd";
        IndexedString indexedString = new IndexedString(input);
        assertEquals(0, indexedString.count('x'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountNegativeCharacter() {
        String input = "ab";
        IndexedString indexedString = new IndexedString(input);
        indexedString.count((char) -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountCharacterOutsideUtf16Range() {
        String input = "ab";
        IndexedString indexedString = new IndexedString(input);
        indexedString.count((char) 0xFFFD);
    }

    @Test
    public void testIndexOfMethod() {
        String input = "abcccdeef";
        IndexedString indexedString = new IndexedString(input);
        char[] target = {'c', 'c', 'c'};
        assertEquals(0, indexedString.indexOf(target));

        char[] target2 = {'a', 'b'};
        assertEquals(-1, indexedString.indexOf(target2));

        char[] target3 = {'x', 'x', 'x'};
        assertEquals(-1, indexedString.indexOf(target3));
    }

    // Add test cases for other methods in the IndexedString class here
}
