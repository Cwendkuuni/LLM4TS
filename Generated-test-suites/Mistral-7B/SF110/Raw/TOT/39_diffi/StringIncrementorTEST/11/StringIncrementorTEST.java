import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_SameStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString expectedResult = new RelativeString();
 StringIncrementor.diff(a, b, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 StringIncrementor.diff(a, b, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_EmptyStringAndNonEmptyString() {
 String a = "";
 String b = "hello";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(0, 0, 5));
 StringIncrementor.diff(a, b, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_NonEmptyStringsWithDifferentLengths() {
 String a = "hello";
 String b = "helloworld";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(5, 5, 6));
 StringIncrementor.diff(a, b, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_StringsWithIdenticalSubstrings() {
 String a = "hellohello";
 String b = "helloworldhello";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(6, 6, 6));
 StringIncrementor.diff(a, b, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullString() {
 String a = null;
 String b = "hello";
 StringIncrementor.diff(a, b);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullRelativeString() {
 String a = "hello";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }

 @Test
 public void testPatch_EmptyStringAndEmptyRelativeString() {
 String a = "";
 RelativeString r = new RelativeString();
 String expectedResult = "";
 StringIncrementor.patch(a, r, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 @Test
 public void testPatch_EmptyStringAndNonEmptyRelativeString() {
 String a = "";
 RelativeString r = new RelativeString().addLink(new StringLink(0, 0, 5));
 String expectedResult = "-----";
 StringIncrementor.patch(a, r, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 @Test
 public void testPatch_NonEmptyStringAndEmptyRelativeString() {
 String a = "hello";
 RelativeString r = new RelativeString();
 String expectedResult = "hello";
 StringIncrementor.patch(a, r, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 @Test
 public void testPatch_StringsWithIdenticalSubstrings() {
 String a = "hellohello";
 RelativeString r = new RelativeString().addLink(new StringLink(6, 6, 6));
 String expectedResult = "helloworld";
 StringIncrementor.patch(a, r, true); // Verbose mode to check if any output is printed
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_NullString() {
 String a = null;
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_NullStringWithRelativeString() {
 String a = "hello";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }
}