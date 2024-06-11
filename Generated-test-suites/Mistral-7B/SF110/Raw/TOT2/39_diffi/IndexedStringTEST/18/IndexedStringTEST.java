import org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(org.junit.experimental.categories.Integration.class)
public class IndexedStringTest {

    @Test
    public void testCount() {
        char[] testChars = {'a', 'b', 'c', 'a', 'a'};
        IndexedString string = new IndexedString(new String(testChars));
        int expectedCount = 3;
        int actualCount = string.count('a');
        assertEquals(expectedCount, actualCount);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        char[] target = {'a', 'b', 'a', 'b'};
        IndexedString indexedString = new IndexedString("abcab");
        indexedString.indexOf(new char[4]); // array length is too long
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacter() {
        char[] target = {'\u0101', 'a'};
        IndexedString indexedString = new IndexedString("abc");
        indexedString.indexOf(target); // character outside UTF-8 range
    }

    @Test
    public void testEmptyTargetArray() {
        char[] target = {};
        IndexedString indexedString = new IndexedString("abc");
        int result = indexedString.indexOf(target);
        assertEquals(-1, result);
    }

    @Test
    public void testDuplicateCharacters() {
        char[] target = {'a', 'a'};
        IndexedString indexedString = new IndexedString("aaaaaa");
        int result = indexedString.indexOf(target);
        assertEquals(0, result);
    }

    @Test
    public void testNoMatchingCharacters() {
        char[] target = {'b'};
        IndexedString indexedString = new IndexedString("aaa");
        int result = indexedString.indexOf(target);
        assertEquals(-1, result);
    }
}