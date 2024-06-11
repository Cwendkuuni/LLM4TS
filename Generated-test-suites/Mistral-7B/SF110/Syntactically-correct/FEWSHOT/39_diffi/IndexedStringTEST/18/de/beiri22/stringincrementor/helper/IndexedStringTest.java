package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testIndexedStringConstructor() {
        String input = "abcabcabc";
        IndexedString is = new IndexedString(input);
        Assert.assertEquals(input.length(), is.values.length);
        for (char c : input.toCharArray()) {
            int count = is.count(c);
            int[] expectedIndexes = new int[count];
            for (int i = 0; i < count; i++) {
                expectedIndexes[i] = i;
            }
            Assert.assertArrayEquals(expectedIndexes, is.index[c]);
        }
    }

    @Test
    public void testIndexOfEmptyTarget() {
        IndexedString is = new IndexedString("abcabcabc");
        Assert.assertNull(is.indexOf(new char[0][0]));
    }

    @Test
    public void testIndexOfSingleCharacterTarget() {
        IndexedString is = new IndexedString("aaabbbccc");
        char target = 'a';
        int index = is.indexOf(new char[]{target});
        Assert.assertEquals(-1, index);

        index = is.indexOf(new char[]{'a'});
        Assert.assertEquals(0, index);

        char target2 = 'b';
        index = is.indexOf(new char[]{target2});
        Assert.assertEquals(3, index);

        index = is.indexOf(new char[]{'b'});
        Assert.assertEquals(3, index);
    }

    @Test
    public void testIndexOfMultipleCharacterTarget() {
        IndexedString is = new IndexedString("aaabbbccc");
        char[] target = {'a', 'b'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);

        index = is.indexOf(new char[]{'a', 'b'});
        Assert.assertEquals(1, index);

        char[] target2 = {'b', 'b'};
        index = is.indexOf(target2);
        Assert.assertEquals(3, index);

        index = is.indexOf(new char[]{'b', 'b'});
        Assert.assertEquals(3, index);

        char[] target3 = {'c', 'c'};
        index = is.indexOf(target3);
        Assert.assertEquals(6, index);

        index = is.indexOf(new char[]{'c', 'c'});
        Assert.assertEquals(6, index);
    }

    @Test
    public void testIndexOfTargetWithMoreCharactersThanInput() {
        IndexedString is = new IndexedString("abcabcabc");
        char[] target = {'x', 'y', 'z'};
        int index = is.indexOf(target);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexOfTargetWithLessCharactersThanInput() {
        IndexedString is = new IndexedString("abcabcabc");
        char[] target = {'a', 'b'};
        int index = is.indexOf(target);
        Assert.assertEquals(1, index);

        index = is.indexOf(new char[]{'b', 'a'});
        Assert.assertEquals(4, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOfNullTarget() {
        IndexedString is = new IndexedString("abcabcabc");
        is.indexOf(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOfTargetOutOfBounds() {
        IndexedString is = new IndexedString("abcabcabc");
        char[] target = {'z', 'z'};
        is.indexOf(target);
    }
}