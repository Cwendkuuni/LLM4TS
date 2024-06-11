import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 private static final String TEST_STRING_1 = "abcdeabcde";
 private static final String TEST_STRING_2 = "abc";
 private static final String TEST_STRING_3 = "";
 private static final String TEST_STRING_4 = "abcdefghijklmnopqrstuvwxyz";

 @Test
 public void testIndexCount() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 int expectedCount = TEST_STRING_1.length();
 int actualCount = indexedString.count('a');
 Assert.assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testIndexOfSimple() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'b', 'c', 'e'};
 int expectedIndex = 0; // index of the first 'a'
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMultipleConsecutive() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'b', 'c', 'e', 'a'};
 int expectedIndex = 1; // index of the second 'a'
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfEmptyArray() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 int actualIndex = indexedString.indexOf(new char[0][]);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfNull() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 int actualIndex = indexedString.indexOf(null);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfSingleCharacterNotFound() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char target = 'x';
 int actualIndex = indexedString.indexOf(new char[] {target});
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceNotPresent() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'d', 'g'};
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequencePartialMatch() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'b', 'c', 'f'};
 int expectedIndex = 4; // index of the first 'f'
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresent() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'b', 'c', 'e', 'a'};
 int expectedIndex = 0; // index of the first 'a'
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentEndOfString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'e', 'a'};
 int expectedIndex = TEST_STRING_1.length() - target.length;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentBeginningOfString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'b', 'c'};
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentMiddleOfString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'c', 'e', 'a'};
 int expectedIndex = TEST_STRING_1.indexOf("cea") + 1;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentBackward() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'e', 'c'};
 int expectedIndex = TEST_STRING_1.lastIndexOf("ace") - 2;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentBackwardBeginning() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'a', 'a', 'e'};
 int expectedIndex = TEST_STRING_1.lastIndexOf("aae") - 2;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentBackwardMiddle() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'e', 'a', 'c'};
 int expectedIndex = TEST_STRING_1.lastIndexOf("ace") - 2;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceAllPresentBackwardEnd() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = {'e', 'a'};
 int expectedIndex = TEST_STRING_1.length() - target.length;
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOfMulticharacterSubsequenceEmptyString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 int actualIndex = indexedString.indexOf(new char[0][]);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfNullTarget() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 int actualIndex = indexedString.indexOf(null);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfTargetLongerThanOriginalString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = new char[TEST_STRING_1.length() + 1];
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfTargetShorterThanEmptyString() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = new char[0];
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfTargetLongerThanOriginalStringWithRandomChars() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = new char[TEST_STRING_1.length() + 1];
 for (int i = 0; i < target.length; ++i) {
 target[i] = (char) (Math.random() * 256);
 }
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(-1, actualIndex);
 }

 @Test
 public void testIndexOfTargetShorterThanEmptyStringWithRandomChars() {
 IndexedString indexedString = new IndexedString(TEST_STRING_1);
 char[] target = new char[0];
 for (int i = 0; i < target.length; ++i) {
 target[i] = (char) (Math.random() * 256);
 }
 int actualIndex = indexedString.indexOf(target);
 Assert.assertEquals(-1, actualIndex);
 }
}