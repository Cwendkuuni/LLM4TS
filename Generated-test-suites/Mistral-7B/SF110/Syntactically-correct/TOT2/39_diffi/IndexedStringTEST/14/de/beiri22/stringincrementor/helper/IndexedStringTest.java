import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString is = new IndexedString("abcdef");
        assertEquals(1, is.count('a'));
        assertEquals(1, is.count('e'));
        assertEquals(2, is.count('f'));
    }

    @Test
    public void testIndexOf() {
        IndexedString is = new IndexedString("abcabcabc");
        char[] target1 = {'a', 'b', 'c'};
        int expected1 = 0;
        int actual1 = is.indexOf(target1);
        assertEquals(expected1, actual1);

        char[] target2 = {'b', 'a', 'b'};
        int expected2 = 2;
        int actual2 = is.indexOf(target2);
        assertEquals(expected2, actual2);

        char[] target3 = {'x', 'y', 'z'};
        int expected3 = -1;
        int actual3 = is.indexOf(target3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testConstructor() {
        IndexedString is1 = new IndexedString("abc");
        IndexedString is2 = new IndexedString("ABC");
        IndexedString is3 = new IndexedString("123");

        assertEquals("abc", is1.toString());
        assertEquals("ABC", is2.toString());
        assertEquals("123", is3.toString());
    }
}