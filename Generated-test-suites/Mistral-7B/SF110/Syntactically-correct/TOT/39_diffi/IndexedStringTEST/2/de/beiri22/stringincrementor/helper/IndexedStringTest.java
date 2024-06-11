import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;

 @Test
 public void testConstructor() {
 is1 = new IndexedString("hello world");
 assertNotNull(is1);
 }

 @Test
 public void testCountChar() {
 char c = 'l';
 int expected = 3;
 int actual = is1.count(c);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfEmptyArray() {
 char[] target = new char[0];
 int expected = -1;
 int actual = is1.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSingleChar() {
 char target = 'l';
 int expected = 2;
 int actual = is1.indexOf(new char[]{target});
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfMultipleChars() {
 char[] target = new char[]{'l', 'e'};
 int expected = 0;
 int actual = is1.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubString() {
 char[] target = new char[]{'h', 'e', 'l'};
 int expected = -1;
 int actual = is1.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubStringStartingMiddle() {
 char[] target = new char[]{'w', 'o'};
 int expected = 7;
 int actual = is1.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubStringStartingEnd() {
 char[] target = new char[]{'d', 'o'};
 int expected = 11;
 int actual = is1.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOfNull() {
 is2 = new IndexedString("hello world");
 int expected = -1;
 try {
 int actual = is2.indexOf(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Target array cannot be null", e.getMessage());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfBeyondEnd() {
 char[] target = new char[]{'z'};
 is2 = new IndexedString("hello world");
 try {
 int actual = is2.indexOf(target);
 } catch (ArrayIndexOutOfBoundsException e) {
 assertEquals(-1, actual);
 }
 }

 @Test
 public void testToString() {
 IndexedString is3 = new IndexedString("abcdefg");
 String expected = "IndexedString{values='abcdefg', index={0=[0, 4, 5], 1=[1, 2], 2=[3], 3=[6], 4=[7], 5=[1, 3], 6=[], 7=[], 8=[], 9=[], 10=[], 11=[], 12=[], 13=[], 14=[], 15=[], 16=[], 17=[], 18=[], 19=[], 255=[]}}";
 String actual = is3.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode() {
 IndexedString is4 = new IndexedString("abcdefg");
 IndexedString is5 = new IndexedString("abcdefg");
 int expected = is4.hashCode();
 int actual = is5.hashCode();
 assertEquals(expected, actual);
 }
}