import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;
 private char[] target1;
 private char[] target2;
 private char[] target3;
 private char[] targetEmpty;

 @Test
 public void testConstructor() {
 is1 = new IndexedString("hello");
 }

 @Test
 public void testCountCharacter() {
 assertEquals(5, is1.count('l'));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 assertEquals(4, is1.indexOf("ll".toCharArray()));
 }

 @Test
 public void testIndexOfMultipleCharacter() {
 target1 = "hellohello".toCharArray();
 is2 = new IndexedString("hello");
 assertEquals(0, is2.indexOf(target1));
 }

 @Test
 public void testIndexOfSubstringAtBeginning() {
 target1 = "hellohello".toCharArray();
 is2 = new IndexedString("hello");
 assertEquals(0, is2.indexOf(target1));
 }

 @Test
 public void testIndexOfSubstringAtEnd() {
 target1 = "hellohello".toCharArray();
 is2 = new IndexedString("hello");
 assertEquals(5, is2.indexOf(target1.substring(1)));
 }

 @Test
 public void testIndexOfSubstringInMiddle() {
 target1 = "hellohello".toCharArray();
 is2 = new IndexedString("hello");
 assertEquals(3, is2.indexOf(target1.substring(1, 4)));
 }

 @Test
 public void testIndexOfEmptyString() {
 targetEmpty = new char[0];
 is1 = new IndexedString("hello");
 assertEquals(-1, is1.indexOf(targetEmpty));
 }

 @Test
 public void testIndexOfNullString() {
 is1 = new IndexedString("hello");
 try {
 is1.indexOf(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIndexOfSubstringNotInString() {
 target2 = "helloworld".toCharArray();
 is2 = new IndexedString("hello");
 assertEquals(-1, is2.indexOf(target2));
 }

 @Test
 public void testIndexOfSubstringWithLengthGreaterThanString() {
 target3 = new char[10];
 is1 = new IndexedString("hello".toCharArray());
 try {
 is1.indexOf(target3);
 } catch (ArrayIndexOutOfBoundsException e) {
 // Expected
 }
 }
}