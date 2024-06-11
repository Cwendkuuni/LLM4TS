import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNegative;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;
 private char[] target1;
 private char[] target2;
 private char[] target3;
 private char[] target4;

 @Test
 public void testConstructor() {
 char[] input = "abcdeabcde".toCharArray();
 is1 = new IndexedString(new String(input));
 }

 @Test
 public void testCount() {
 assertEquals(2, is1.count('a'));
 assertEquals(1, is1.count('e'));
 assertEquals(0, is1.count('z'));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 target1 = new char[]{'a', 'a'};
 assertEquals(0, is1.indexOf(target1));
 }

 @Test
 public void testIndexOfMultipleCharacters() {
 target2 = new char[]{'a', 'b', 'a'};
 assertEquals(1, is1.indexOf(target2));
 }

 @Test
 public void testIndexOfEmptyArray() {
 assertEquals(-1, is1.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfNull() {
 assertNull(is1.indexOf(null));
 }

 @Test
 public void testIndexOfDifferentString() {
 is2 = new IndexedString("abcdabcd");
 target3 = new char[]{'b', 'c', 'd'};
 assertNegative(is1.indexOf(target3));
 }

 @Test
 public void testIndexOfSubString() {
 target4 = new char[]{'a', 'b', 'c', 'a'};
 assertEquals(1, is1.indexOf(target4));
 assertEquals(4, is1.indexOf(target4, 2));
 }

 @Test
 public void testIndexOfSubStringWithShift() {
 target4 = new char[]{'b', 'c', 'a'};
 assertEquals(1, is1.indexOf(target4, 2));
 }
}