import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNegative;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;
 private IndexedString is3;
 private char[] target1;
 private char[] target2;
 private char[] target3;

 @Test
 public void testIndexedStringConstructor() {
 is1 = new IndexedString("hello world");
 assertEquals(10, is1.values.length);
 }

 @Test
 public void testIndexedStringCountMethod() {
 is1 = new IndexedString("hello world");
 assertEquals(3, is1.index['h'].length);
 assertEquals(5, is1.index['o'].length);
 assertEquals(5, is1.index['l'].length);
 assertEquals(5, is1.index['e'].length);
 assertEquals(5, is1.index[' '].length);
 assertEquals(12, is1.index['w'].length);
 assertEquals(1, is1.index['r'].length);
 assertEquals(6, is1.index['d'].length);
 }

 @Test
 public void testIndexedStringIndexOfMethodWithValidInput() {
 is1 = new IndexedString("hello world");
 target1 = new char[]{'h', 'e', 'l', 'l', 'o'};
 assertEquals(0, is1.indexOf(target1));
 }

 @Test
 public void testIndexedStringIndexOfMethodWithEmptyInput() {
 is1 = new IndexedString("hello world");
 assertNegative(is1.indexOf(new char[0]));
 }

 @Test
 public void testIndexedStringIndexOfMethodWithLongerInput() {
 is1 = new IndexedString("hello world");
 target2 = new char[]{'h', 'e', 'l', 'l', 'o', 'w'};
 assertNegative(is1.indexOf(target2));
 }

 @Test
 public void testIndexedStringIndexOfMethodWithMismatchedInput() {
 is1 = new IndexedString("hello world");
 target3 = new char[]{'h', 'e', 'l', 'l', 'o', 'd'};
 assertNegative(is1.indexOf(target3));
 }

 @Test
 public void testIndexedStringIndexOfMethodWithMultipleMatches() {
 is2 = new IndexedString("hello hello world");
 target1 = new char[]{'h', 'e', 'l', 'l'};
 assertEquals(0, is2.indexOf(target1));
 assertEquals(4, is2.indexOf(target1) + target1.length);
 }

 @Test
 public void testIndexedStringIndexOfMethodWithNoMatches() {
 is3 = new IndexedString("hello world");
 target1 = new char[]{'a', 'b', 'c', 'd'};
 assertNegative(is3.indexOf(target1));
 }
}