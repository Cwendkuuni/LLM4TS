import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class IndexedStringTest {

 private IndexedString is;

 @BeforeEach
 public void setUp() {
 is = new IndexedString("abcabcabcdefgdefg");
 }

 @Test
 public void testCountCharacter() {
 char c = 'a';
 int expected = 9;
 int actual = is.count(c);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyString() {
 is = new IndexedString("");
 int expected = 0;
 int actual = is.count('x');
 assertEquals(expected, actual);
 }

 @Test
 public void testNullInput() {
 assertThrows(NullPointerException.class, () -> new IndexedString(null));
 }

 @Test
 public void testIndexOfSingleCharacter() {
 char target = 'a';
 int expected = 0;
 int actual = is.indexOf(new char[]{target});
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfEmptyArray() {
 int expected = -1;
 int actual = is.indexOf(new char[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubstring() {
 char[] target = {'a', 'b', 'c'};
 int expected = 0;
 int actual = is.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubstringWithOffset() {
 char[] target = {'b', 'c', 'a'};
 int expected = 4;
 int actual = is.indexOf(target, 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 char[] target = {'d', 'e', 'f'};
 int expected = -1;
 int actual = is.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfSubstringWithNegativeOffset() {
 char[] target = {'a', 'b', 'c'};
 int expected = -1;
 int actual = is.indexOf(target, -1);
 assertEquals(expected, actual);
 }
}