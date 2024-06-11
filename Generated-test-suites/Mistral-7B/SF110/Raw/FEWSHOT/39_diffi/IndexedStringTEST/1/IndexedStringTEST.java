package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstruction() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        Assert.assertEquals(input.length(), is.values.length);
        for (int i = 0; i < input.length(); ++i) {
            Assert.assertEquals(input.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexOfSimpleCharacter() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char target = 'a';
        int index = is.indexOf(new char[] {target});
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexOfSingleCharacterNotFound() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char target = 'z';
        int index = is.indexOf(new char[] {target});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfMultipleCharactersSameString() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char[] target = {'b', 'c', 'd'};
        int index = is.indexOf(target);
        Assert.assertEquals(3, index);
    }

    @Test
    public void testIndexOfMultipleCharactersDifferentOrder() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char[] target = {'d', 'c', 'b'};
        int index = is.indexOf(target);
        Assert.assertEquals(5, index);
    }

    @Test
    public void testIndexOfMultipleCharactersSubstring() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char[] target = {'c', 'c', 'd'};
        int index = is.indexOf(target);
        Assert.assertEquals(4, index);
    }

    @Test
    public void testIndexOfMultipleCharactersSubstringPartiallyFound() {
        String input = "abcabccdeede";
        IndexedString is = new IndexedString(input);
        char[] target = {'c', 'z'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }
}