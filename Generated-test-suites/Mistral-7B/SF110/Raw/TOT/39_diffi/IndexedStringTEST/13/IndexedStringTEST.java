import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;

 @Test
 public void testIndexedStringConstruction() {
 is1 = new IndexedString("hello world");
 assertEquals(10, is1.values.length);
 assertArrayEquals(new int[]{}, is1.index['\u0000']);
 for (char c = 'h'; c <= 'z'; c++) {
 int expectedLength = (is1.values[0] == c) ? 1 : 0;
 assertArrayEquals(new int[] { 0 }, is1.index[c]);
 assertNotNull(is1.index[c]);
 }
 }

 @Test
 public void testIndexOfCharacter() {
 is1 = new IndexedString("hello world");
 char[] target1 = new char[]{'h', 'e', 'l', 'l', 'o'};
 int expectedIndex1 = 0;
 char[] target2 = new char[]{'o', 'l', 'd'};
 int expectedIndex2 = -1;
 int expectedIndex3 = -1;

 int actualIndex1 = is1.indexOf(target1);
 int actualIndex2 = is1.indexOf(target2);
 int actualIndex3 = is1.indexOf(new char[]{'x'});

 assertEquals(expectedIndex1, actualIndex1);
 assertEquals(expectedIndex2, actualIndex2);
 assertEquals(expectedIndex3, actualIndex3);
 }

 @Test
 public void testIndexOfSubstring() {
 is1 = new IndexedString("hello world");
 is2 = new IndexedString("world hello");
 char[] target = new char[]{'h', 'e', 'l', 'l', 'o'};
 int expectedIndex1 = 0;
 int expectedIndex2 = 5;

 int actualIndex1 = is1.indexOf(target);
 int actualIndex2 = is2.indexOf(target);

 assertEquals(expectedIndex1, actualIndex1);
 assertEquals(expectedIndex2, actualIndex2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructionWithNullString() {
 assertNull(new IndexedString(null));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringWithNegativeLength() {
 is1 = new IndexedString("hello world");
 char[] target = new char[11];
 Arrays.fill(target, 'h');
 int expectedIndex = -1;
 int actualIndex = is1.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringWithNegativeIndex() {
 is1 = new IndexedString("hello world");
 int index = -1;
 int expectedIndex = -1;
 int actualIndex = is1.indexOf(new char[5], index);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringWithSubstringLongerThanOriginalString() {
 is1 = new IndexedString("hello world");
 char[] target = new char[12];
 Arrays.fill(target, 'h');
 int expectedIndex = -1;
 int actualIndex = is1.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubstringWithSubstringLongerThanSingleChar() {
 is1 = new IndexedString("hello world");
 char[] target = new char[2];
 Arrays.fill(target, 'o');
 int expectedIndex = -1;
 int actualIndex = is1.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }
}