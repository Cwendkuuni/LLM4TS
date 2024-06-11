import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;
 private IndexedString is3;
 private char[] target1;
 private char[] target2;
 private char[] target3;

 @Test
 public void testConstructor() {
 is1 = new IndexedString("abcabc");
 Assert.assertEquals("Size mismatch", 6, is1.values.length);
 Assert.assertEquals("Index table size mismatch", 256, is1.index.length);
 for (int i = 0; i < 256; ++i) {
 Assert.assertArrayEquals("Index table element size mismatch", new int[0], is1.index[i]);
 }
 }

 @Test
 public void testIndexOf() {
 is2 = new IndexedString("ababab");
 target1 = new char[]{'a', 'b', 'a'};
 target2 = new char[]{'b', 'b', 'a'};
 target3 = new char[]{'a', 'b'};

 // Positive cases
 Assert.assertEquals("Index of substring 'ab' should be 1", 1, is2.indexOf(target1));
 Assert.assertEquals("Index of substring 'bba' should be 3", 3, is2.indexOf(target2));
 Assert.assertEquals("Index of substring 'ab' should be 0", 0, is2.indexOf(target3));

 // Empty string case
 Assert.assertEquals("Index of empty string should be 0", 0, is2.indexOf(new char[0][]));

 // Null string case
 Assert.assertNull("Index of null string should return null", is2.indexOf(null));

 // Empty target array case
 Assert.assertEquals("Index of empty target array should be -1", -1, is2.indexOf(new char[0][]));

 // Target array length exceeds string length case
 target3 = new char[is2.values.length + 1];
 Assert.assertEquals("Index of target array with length exceeding string length should be -1", -1, is2.indexOf(target3));

 // Target array contains non-matching character case
 char nonMatchingChar = '\u0100';
 target3 = new char[target2.length + 1];
 System.arraycopy(target2, 0, target3, 0, target2.length);
 target3[target3.length - 1] = nonMatchingChar;
 Assert.assertEquals("Index of target array with non-matching character should be -1", -1, is2.indexOf(target3));
 }

 @Test
 public void testCount() {
 is3 = new IndexedString("0110110110110110");

 // Positive cases
 Assert.assertEquals("Count of '0' should be 10", 10, is3.count('0'));
 Assert.assertEquals("Count of '1' should be 10", 10, is3.count('1'));

 // Character not present in string case
 Assert.assertEquals("Count of 'x' should be 0", 0, is3.count('x'));

 // Negative character case
 Assert.assertEquals("Count of negative character should throw an Exception", 0, is3.count((char) -1));
 }
}