import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testConstructor() {
        String input = "abcabcabc";
        IndexedString is = new IndexedString(input);
        assertEquals(input.length(), is.values.length);
        for (int i = 0; i < input.length(); i++) {
            assertEquals(input.charAt(i), is.values[i]);
        }
    }

    @Test
    public void testIndexChar() {
        String input = "aaabbbccc";
        IndexedString is = new IndexedString(input);
        assertEquals(3, is.index['a'].length);
        assertEquals(1, is.index['a'][0]);
        assertEquals(2, is.index['a'][1]);
        assertEquals(4, is.index['a'][2]);
        assertEquals(-1, is.index['z'].length);
    }

    @Test
    public void testIndexSubString() {
        String input = "aaabbbccc";
        IndexedString is = new IndexedString(input);
        char[] target = {'a', 'a', 'b'};
        int expected = 1; // index of first occurrence of "aab"
        int result = is.indexOf(target);
        assertEquals(expected, result);
    }

    @Test
    public void testIndexEmptyString() {
        String input = "";
        IndexedString is = new IndexedString(input);
        char[] target = {};
        int result = is.indexOf(target);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexNullString() {
        String input = null;
        IndexedString is = new IndexedString(input);
        char[] target = {};
        is.indexOf(target);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexSubStringTooLong() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(input);
        char[] target = {'a', 'b', 'c', 'd', 'e', 'f'};
        is.indexOf(target);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexSubStringInvalidIndex() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        IndexedString is = new IndexedString(input);
        char[] target = {'z', 'z'};
        int index = is.indexOf(target);
        assertNotEquals(-1, index); // assert that index is not -1
        int expected = is.values.length - target.length;
        int result = index + target.length;
        assertEquals(expected, result);
    }
}