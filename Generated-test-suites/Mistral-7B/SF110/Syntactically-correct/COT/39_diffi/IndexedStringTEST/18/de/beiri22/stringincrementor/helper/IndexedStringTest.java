package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        IndexedString obj = new IndexedString(input);
        assertEquals(input.length(), obj.values.length);
    }

    @Test
    public void testCountCharacter() {
        String input = "aaabbbccc";
        IndexedString obj = new IndexedString(input);
        assertEquals(3, obj.count('a'));
        assertEquals(3, obj.count('b'));
        assertEquals(1, obj.count('c'));
    }

    @Test
    public void testIndexOfCharacter() {
        String input = "abcabcabc";
        IndexedString obj = new IndexedString(input);
        assertEquals(0, obj.indexOf("a".toCharArray()));
        assertEquals(6, obj.indexOf("c".toCharArray()));
        assertEquals(-1, obj.indexOf("d".toCharArray()));
    }

    @Test
    public void testIndexOfEmptyCharArray() {
        String input = "abc";
        IndexedString obj = new IndexedString(input);
        assertEquals(-1, obj.indexOf(new char[0]));
    }

    @Test(expected = NullPointerException.class)
    public void testIndexOfNullCharArray() {
        String input = "abc";
        IndexedString obj = new IndexedString(input);
        obj.indexOf(null);
    }

    @Test
    public void testIndexOfSubArray() {
        String input = "abcabcabc";
        IndexedString obj = new IndexedString(input);
        char[] subArray = new char[]{'a', 'b', 'c'};
        assertEquals(0, obj.indexOf(subArray));
        char[] subArrayDifferentLength = new char[]{'a', 'b', 'd'};
        assertEquals(-1, obj.indexOf(subArrayDifferentLength));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOfCharArrayBeyondStringLength() {
        String input = "abc";
        IndexedString obj = new IndexedString(input);
        char[] target = new char[input.length() + 1];
        obj.indexOf(target);
    }

    @Test
    public void testIndexOfCharArrayWithMismatchedLength() {
        String input = "abc";
        IndexedString obj = new IndexedString(input);
        char[] target = new char[input.length() + 1];
        assertEquals(-1, obj.indexOf(target));
    }
}