import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private char[] target1 = {'a', 'a', 'b', 'a'};
 private char[] target2 = {'c', 'd', 'e'};
 private char[] target3 = {};
 private char[] target4 = {'\u00E1', '\u00E2'};
 private String input;

 @Test
 public void testConstructor() {
 IndexedString is = new IndexedString("aaabbaab");
equality(is.values, new char[]{'a', 'a', 'b', 'a', 'b', 'a', 'a', 'b'});
 checkIndex(is, 'a', new int[]{-1, -1, 0, 1, 2, 3, 4, 5, 6});
 }

 @Test
 public void testCount() {
 IndexedString is = new IndexedString("aaabbaab");
 assertEquals(4, is.count('a'));
 assertEquals(2, is.count('b'));
 assertEquals(0, is.count('c'));
 }

 @Test
 public void testIndexOf() {
 IndexedString is = new IndexedString("aaabbaab");
 int index = is.indexOf(target1);
 assertEquals(0, index);
 index = is.indexOf(target2);
 assertEquals(-1, index);
 index = is.indexOf(target3);
 assertEquals(-1, index);
 index = is.indexOf(target4);
 assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfNullTarget() {
 IndexedString is = new IndexedString("aaabbaab");
 is.indexOf(null);
 }

 @Test
 public void setInput() {
 input = "TestString";
 IndexedString is = new IndexedString(input);
 assertEquals(input.length(), is.values.length);
 }

 private void equality(char[] a, char[] b) {
 for (int i = 0; i < a.length; i++) {
 assertEquals(a[i], b[i]);
 }
 }

 private void checkIndex(IndexedString is, char c, int[] expected) {
 int[] result = is.index[c];
 assertArrayEquals(expected, result);
 }

}