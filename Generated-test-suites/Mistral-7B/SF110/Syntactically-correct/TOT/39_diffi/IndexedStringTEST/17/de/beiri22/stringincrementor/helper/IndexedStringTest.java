import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNegative;

public class IndexedStringTest {

 private char[] testString1 = {'a', 'b', 'a', 'a', 'a'};
 private char[] testString2 = {'a', 'b', 'c'};
 private char[] testString3 = {};
 private char[] testString4 = {'\u00E9', '\u00E9', '\u00E9'};
 private char[] testString5 = {'\u0100'};
 private char[] testString6 = {'\u0100', '\u0100', '\u0100'};
 private char[] target1 = {'a', 'a'};
 private char[] target2 = {'a', 'b'};
 private char[] target3 = {'b'};
 private char[] target4 = {};
 private IndexedString is1;
 private IndexedString is2;
 private IndexedString is3;
 private IndexedString is4;
 private IndexedString is5;
 private IndexedString is6;

 @Test
 public void testCount() {
 is1 = new IndexedString(new String(testString1));
 assertEquals(5, is1.count('a'));
 }

 @Test
 public void testConstructor() {
 is2 = new IndexedString(new String(testString2));
 assertArrayEquals(testString2, is2.values);
 int[][] expIndex2 = new int[256][];
 for (char c = '\0'; c < '\u0100'; ++c) {
 expIndex2[c] = new int[1];
 expIndex2['a'][0] = 0;
 expIndex2['b'][0] = 1;
 }
 assertArrayEquals(expIndex2, is2.index);
 }

 @Test
 public void testEmptyString() {
 is3 = new IndexedString(new String(testString3));
 assertArrayEquals(new char[0], is3.values);
 int[][] expIndex3 = new int[256][];
 assertArrayEquals(expIndex3, is3.index);
 }

 @Test
 public void testNonASCIIString() {
 is4 = new IndexedString(new String(testString4));
 assertArrayEquals(testString4, is4.values);
 int[][] expIndex4 = new int[256][];
 for (char c = '\0'; c < '\u0100'; ++c) {
 expIndex4[c] = new int[1];
 expIndex4['\u00E9'][0] = 0;
 }
 assertArrayEquals(expIndex4, is4.index);
 }

 @Test
 public void testOneCharacterString() {
 is5 = new IndexedString(new String(testString5));
 assertArrayEquals(testString5, is5.values);
 int[][] expIndex5 = new int[256][];
 for (char c = '\0'; c < '\u0100'; ++c) {
 expIndex5[c] = new int[1];
 expIndex5['\u0100'][0] = 0;
 }
 assertArrayEquals(expIndex5, is5.index);
 }

 @Test
 public void testIndexOf() {
 is6 = new IndexedString(new String(testString6));
 assertEquals(0, is6.indexOf(target1));
 assertEquals(3, is6.indexOf(target2));
 assertNegative(is6.indexOf(target3));
 }
}