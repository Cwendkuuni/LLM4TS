import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString is = new IndexedString("hello");
        int result = is.count('l');
        assertEquals(3, result);
    }

    @Test
    public void testIndexedString() {
        IndexedString is = new IndexedString("hello");
        char[] target = new char[]{'l', 'l', 'l'};
        int result = is.indexOf(target);
        assertEquals(1, result);
    }

    @Test
    public void testIndexOf() {
        IndexedString is = new IndexedString("hello");
        char[] target = new char[]{'h', 'e', 'l'};
        int result = is.indexOf(target);
        assertEquals(-1, result);
    }

    @Test
    public void testEmptyString() {
        IndexedString is = new IndexedString("");
        char[] emptyTarget = new char[0];
        int result = is.indexOf(emptyTarget);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullString() {
        IndexedString is = new IndexedString(null);
    }

    @Test
    public void testTargetLengthGreaterThanIndexedStringLength() {
        IndexedString is = new IndexedString("hello");
        char[] target = new char[6];
        int result = is.indexOf(target);
        assertEquals(-1, result);
    }

    @Test
    public void testNonPrintableCharacters() {
        IndexedString is = new IndexedString("hello");
        char[] nonPrintableTarget = new char[]{'\u0000'};
        int result = is.indexOf(nonPrintableTarget);
        assertEquals(-1, result);
    }

    @Test
    public void testTargetCharactersNotPresentInIndexedString() {
        IndexedString is = new IndexedString("hello");
        char[] target = new char[]{'m', 'i'};
        int result = is.indexOf(target);
        assertEquals(-1, result);
    }
}