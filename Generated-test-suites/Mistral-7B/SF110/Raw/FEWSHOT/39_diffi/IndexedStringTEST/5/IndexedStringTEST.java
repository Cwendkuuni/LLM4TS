package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/? ";
        IndexedString indexedString = new IndexedString(testString);
        assertEquals(testString.length(), indexedString.values.length);
    }

    @Test
    public void testIndexOfCharacter() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        char testChar = 't';
        int result = indexedString.indexOf(new char[] {testChar});
        assertEquals(0, result);
    }

    @Test
    public void testIndexOfEmptyArray() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        int result = indexedString.indexOf(new char[0]);
        assertNull(result);
    }

    @Test
    public void testIndexOfNonExistentChar() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        char testChar = 'z';
        int result = indexedString.indexOf(new char[] {testChar});
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSubstring() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        String testSubstring = "special characters: ";
        int result = indexedString.indexOf(testSubstring.toCharArray());
        int expectedIndex = 15;
        assertEquals(expectedIndex, result);
    }

    @Test
    public void testIndexOfSubstringEmpty() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        int result = indexedString.indexOf(new char[0]);
        assertNull(result);
    }

    @Test
    public void testIndexOfSubstringNull() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        int result = indexedString.indexOf(null);
        assertNull(result);
    }

    @Test
    public void testIndexOfSubstringDifferentLength() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        String testSubstring = "different characters: ";
        int result = indexedString.indexOf(testSubstring.toCharArray());
        assertNull(result);
    }

    @Test
    public void testCountCharacter() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        char testChar = 't';
        int result = indexedString.count(testChar);
        int expectedCount = 10;
        assertEquals(expectedCount, result);
    }

    @Test
    public void testCountCharacterNonExistent() {
        String testString = "testString with special characters: !@#$%&*()_+-=[]{}|;:,.<>/?";
        IndexedString indexedString = new IndexedString(testString);
        char testChar = 'z';
        int result = indexedString.count(testChar);
        assertEquals(0, result);
    }
}