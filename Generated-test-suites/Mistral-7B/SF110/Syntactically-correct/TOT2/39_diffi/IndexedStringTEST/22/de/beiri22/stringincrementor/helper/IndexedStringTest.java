package de.beiri22.stringincrementor.helper;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class IndexedStringTest {

    private IndexedString testString;

    @BeforeEach
    public void setup() {
        testString = new IndexedString("TestString");
    }

    @Test
    public void testConstructor() {
        assertEquals(10, testString.values.length);
        // Add more assertions to test the index array
    }

    @Test
    public void testCountCharacter() {
        assertEquals(2, testString.count('t'));
        assertEquals(1, testString.count('s'));
        assertEquals(0, testString.count('z'));
    }

    @Test
    public void testIndexOfValidSequence() {
        char[] target = {'t', 'e', 's', 't'};
        int result = testString.indexOf(target);
        assertEquals(0, result);
    }

    @Test
    public void testIndexOfEmptyArray() {
        int result = testString.indexOf(new char[0]);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOfInvalidSequence() {
        char[] target = {'z', 'e', 's', 't'};
        int result = testString.indexOf(target);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOfNullArray() {
        assertThrows(NullPointerException.class, () -> testString.indexOf(null));
    }

    @Test
    public void testEdgeCaseEmptyString() {
        IndexedString emptyString = new IndexedString("");
        // Add more assertions to test the index array
    }

    @Test
    public void testEdgeCaseInvalidUnicode() {
        // Add code to create an IndexedString from a string with invalid Unicode characters
        // Add assertions to test the index array
    }

    @Test
    public void testEdgeCaseMaximumLength() {
        char[] target = new char[testString.values.length];
        int result = testString.indexOf(target);
        assertEquals(-1, result);
    }
}