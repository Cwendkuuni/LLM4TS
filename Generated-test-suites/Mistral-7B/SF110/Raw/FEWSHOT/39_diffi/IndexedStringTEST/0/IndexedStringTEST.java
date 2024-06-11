package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringCharacterCount() {
        String s = "abcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(s);

        for (char c = '\0'; c < '\u0100'; ++c) {
            int count = is.count(c);
            int expectedCount = (c == 'a') ? 10 : (c == 'e' || c == 'i' || c == 'o' || c == 'u') ? 4 : 26;
            Assert.assertEquals("Unexpected count for character: " + c, expectedCount, count);
        }
    }

    @Test
    public void testIndexedStringConstructorNullString() {
        Assert.assertThrows("Expected NullPointerException", () -> new IndexedString(null));
    }

    @Test
    public void testIndexedStringConstructorEmptyString() {
        IndexedString is = new IndexedString("");
        Assert.assertEquals("Unexpected length of values array", 0, is.values.length);
        Assert.assertArrayEquals("Unexpected initialisation of index array", new int[0][], is.index);
    }

    @Test
    public void testIndexedStringConstructorStringWithLengthZero() {
        IndexedString is = new IndexedString("");
        Assert.assertEquals("Unexpected length of values array", 0, is.values.length);
        Assert.assertArrayEquals("Unexpected initialisation of index array", new int[0][], is.index);
    }

    @Test
    public void testIndexedStringConstructorStringWithSingleCharacter() {
        IndexedString is = new IndexedString("a");
        Assert.assertEquals("Unexpected length of values array", 1, is.values.length);
        Assert.assertEquals("Unexpected index position for character 'a'", 0, is.index['a'][0]);
    }

    @Test
    public void testIndexedStringConstructorStringWithMultipleSameCharacters() {
        IndexedString is = new IndexedString("aaa");
        Assert.assertEquals("Unexpected length of values array", 3, is.values.length);
        Assert.assertEquals("Unexpected index position for character 'a'", 0, is.index['a'][0]);
        Assert.assertEquals("Unexpected index position for character 'a' (second occurrence)", 1, is.index['a'][1]);
        Assert.assertEquals("Unexpected index position for character 'a' (third occurrence)", 2, is.index['a'][2]);
    }

    @Test
    public void testIndexedStringConstructorStringWithMultipleDifferentCharacters() {
        IndexedString is = new IndexedString("abc");
        Assert.assertEquals("Unexpected length of values array", 3, is.values.length);
        Assert.assertEquals("Unexpected index position for character 'a'", 0, is.index['a'][0]);
        Assert.assertEquals("Unexpected index position for character 'b'", 1, is.index['b'][0]);
        Assert.assertEquals("Unexpected index position for character 'c'", 2, is.index['c'][0]);
    }

    @Test
    public void testIndexedStringIndexOfSingleCharacter() {
        IndexedString is = new IndexedString("abcdefghijklmnopqrstuvwxyz");
        int index = is.indexOf(new char[]{'a'});
        Assert.assertEquals("Unexpected index position for character 'a'", 0, index);
    }

    @Test
    public void testIndexedStringIndexOfMultipleCharacters() {
        IndexedString is = new IndexedString("abcdefghijklmnopqrstuvwxyz");
        int index = is.indexOf(new char[]{'b', 'c'});
        Assert.assertEquals("Unexpected index position for characters 'b' and 'c'", 1, index);
    }

    @Test
    public void testIndexedStringIndexOfCharacterNotPresentInString() {
        IndexedString is = new IndexedString("abcdefghijklmnopqrstuvwxyz");
        int index = is.indexOf(new char[]{'x'});
        Assert.assertEquals("Unexpected index position for character 'x'", -1, index);
    }

    @Test
    public void testIndexedStringIndexOfMismatchedArrayLength() {
        IndexedString is = new IndexedString("abcdefghijklmnopqrstuvwxyz");
        char[] target = new char[27];
        Assert.assertThrows("Expected ArrayIndexOutOfBoundsException", () -> is.indexOf(target));
    }
}