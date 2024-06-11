import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_emptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDiff_sameStrings() {
 String a = "abcdefghijklmnopqrst";
 String b = "abcdefghijklmnopqrst";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDiff_identicalSubstrings() {
 String a = "abcabcabc";
 String b = "abcabcdef";
 RelativeString expectedResult = new StringLink(2, 3, 3);
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDiff_noMatches() {
 String a = "abcdefghijklmnopqrst";
 String b = "uvwxyz";
 RelativeString expectedResult = new RelativeString("uvwxyz");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullStrings() {
 String a = null;
 String b = "abcdefghijklmnopqrst";
 StringIncrementor.diff(a, b, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullSecondString() {
 String a = "abcdefghijklmnopqrst";
 String b = null;
 StringIncrementor.diff(a, b, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testDiff_outOfBoundsIndex() {
 String a = "abcdefghijklmnopqrst";
 String b = "abcdefghijklmnopqrstuvw";
 StringIncrementor.diff(a, b, false);
 }

 @Test
 public void testPatch_emptyPatch() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new RelativeString();
 String expectedResult = "abcdefghijklmnopqrst";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatch_singleCharacterPatch() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new StringLink(4, 5, 1);
 String expectedResult = "abcxefghijklmnopqrst";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatch_multipleCharacterPatch() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new StringLink(4, 5, 3);
 String expectedResult = "abcdghijklmnopqrst";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatch_patchOutOfBounds() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new StringLink(14, 5, 3);
 String expectedResult = "abcddefghijklmnoprst";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatch_nullString() {
 String a = null;
 RelativeString r = new StringLink(0, 0, 3);
 String expectedResult = "";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatch_nullRelativeString() {
 String a = "abcdefghijklmnopqrst";
 String result = StringIncrementor.patch(a, null, false);
 assertEquals("abcdefghijklmnopqrst", result);
 }
}