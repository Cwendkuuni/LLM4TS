import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;

 @Test
 public void testIndexedStringConstructor() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 IndexedString expectedIs = new IndexedString(new String(values));
 is1 = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, is1.values);
 }

 @Test
 public void testIndexedStringEmptyConstructor() {
 IndexedString is = new IndexedString("");
 Assert.assertArrayEquals(new char[0], is.values);
 Assert.assertNull(is.index);
 }

 @Test
 public void testIndexOfChar() {
 char[] target = new char[]{'h', 'e'};
 int index = is1.indexOf(target);
 Assert.assertEquals(1, index);
 }

 @Test
 public void testIndexOfEmptyArray() {
 char[] target = new char[0];
 int index = is1.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSingleChar() {
 char target = 'l';
 int index = is1.indexOf(new char[]{target});
 Assert.assertEquals(5, index);
 }

 @Test
 public void testIndexOfCharOutOfRange() {
 char target = 'z';
 int index = is1.indexOf(new char[]{target});
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubString() {
 char[] target = new char[]{'h', 'e', 'l', 'l'};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringOutOfRange() {
 char[] target = new char[]{'m', 'u', 'n', 'd'};
 int index = is1.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubStringWithSpaces() {
 char[] target = new char[]{'h', 'e', 'l', 'l', 'o', ' '};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringMiddleOfString() {
 char[] target = new char[]{'o', 'r', 'l', 'd'};
 int index = is1.indexOf(target);
 Assert.assertEquals(10, index);
 }

 @Test
 public void testIndexOfSubStringMultipleOccurrences() {
 char[] target = new char[]{'l', 'o', 'r', 'd'};
 int index = is1.indexOf(target);
 Assert.assertEquals(10, index);
 int index2 = is1.indexOf(target);
 Assert.assertEquals(5, index2);
 }

 @Test
 public void testIndexOfSubStringWithRepeatedCharacters() {
 char[] target = new char[]{'l', 'l'};
 int index = is1.indexOf(target);
 Assert.assertEquals(5, index);
 int index2 = is1.indexOf(target);
 Assert.assertEquals(6, index2);
 }

 @Test
 public void testIndexOfSubStringWithMixedCharacters() {
 char[] target = new char[]{'h', 'e', 'l', 'l', 'o'};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringWithLeadingSpaces() {
 char[] target = new char[]{' ', 'h', 'e', 'l', 'l', 'o'};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringWithTrailingSpaces() {
 char[] target = new char[]{'h', 'e', 'l', 'l', 'o', ' '};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringWithLeadingAndTrailingSpaces() {
 char[] target = new char[]{' ', 'h', 'e', 'l', 'l', 'o', ' '};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringWithDifferentCases() {
 char[] target = new char[]{'H', 'e', 'l', 'l', 'o'};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringWithMixedCases() {
 char[] target = new char[]{'h', 'E', 'l', 'l', 'o'};
 int index = is1.indexOf(target);
 Assert.assertEquals(0, index);
 }

 @Test(expected = NullPointerException.class)
 public void testNullIndexedString() {
 int index = is1.indexOf(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTargetArray() {
 int index = is1.indexOf(null);
 }

 @Test
 public void testIndexOfSubStringWithEmptyTargetArray() {
 int index = is1.indexOf(new char[0][]);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfWithArraysOfDifferentSizes() {
 char[] target1 = new char[]{'h', 'e'};
 char[] target2 = new char[]{'h', 'e', 'l'};
 int index1 = is1.indexOf(target1);
 int index2 = is1.indexOf(target2);
 Assert.assertEquals(1, index1);
 Assert.assertEquals(-1, index2);
 }

 @Test
 public void testIndexOfSubStringWithLargerTargetArray() {
 char[] target = new char[12];
 int index = is1.indexOf(target);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexOfSubStringWithLargerTargetArrayThanStringLength() {
 char[] target = new char[13];
 int index = is1.indexOf(target);
 Assert.assertEquals(-1, index);
 }
}