import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 // Test for diff method with same strings
 @Test
 public void testDiffSameStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 // Test for diff method with different strings
 @Test
 public void testDiffDifferentStrings() {
 String a = "hello world";
 String b = "hello there world";
 RelativeString expected = new StringLink(5, 5, 6).addLink(new StringLink(12, 12, 3)).addLink(new StringLink(19, 19, 3));
 expected.setAbsolute(" there world");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 // Test for diff method with verbose flag
 @Test
 public void testDiffVerboseFlag() {
 String a = "hello world";
 String b = "hello there world";
 String expectedOutput = "Link: @ 5 % (5,5): length=6\nLink: @ 12 % (12,12): length=3\nLink: @ 19 % (19,19): length=3\n";
 System.out.println("Expected Output: " + expectedOutput);
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String message) {
 String savedOutput = super.getOutputStream().getBuffer().getBytes().toString();
 assertEquals(expectedOutput, savedOutput);
 super.println(message);
 }
 });
 StringIncrementor.diff(a, b, true);
 }

 // Test for patch method with empty string and empty relative string
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with absolute string and empty relative string
 @Test
 public void testPatchAbsStringEmptyRelString() {
 String a = "hello world";
 RelativeString r = new RelativeString();
 String expected = "hello world";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with absolute string and non-empty relative string
 @Test
 public void testPatchAbsStringNonEmptyRelString() {
 String a = "hello world";
 RelativeString r = new StringLink(5, 5, 6).addLink(new StringLink(12, 12, 3)).addLink(new StringLink(19, 19, 3));
 r.setAbsolute(" there world");
 String expected = "hellothere world";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with verbose flag
 @Test
 public void testPatchVerboseFlag() {
 String a = "hello world";
 RelativeString r = new StringLink(5, 5, 6).addLink(new StringLink(12, 12, 3)).addLink(new StringLink(19, 19, 3));
 r.setAbsolute(" there world");
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("Adding 6 absolute Bytes from pos#0\n");
 expectedOutput.append("Adding Link: @ 5 % (5,5): length=6\n");
 expectedOutput.append("Adding 3 absolute Bytes from pos#12\n");
 expectedOutput.append("Adding Link: @ 12 % (12,12): length=3\n");
 expectedOutput.append("Adding 3 absolute Bytes from pos#19\n");
 expectedOutput.append("Adding Link: @ 19 % (19,19): length=3\n");
 System.out.println("Expected Output: " + expectedOutput.toString());
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String message) {
 String savedOutput = super.getOutputStream().getBuffer().getBytes().toString();
 StringBuilder actualOutput = new StringBuilder(savedOutput);
 actualOutput.setLength(actualOutput.length() - 1); // Remove newline character
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 super.println(message);
 }
 });
 StringIncrementor.patch(a, r, true);
 }
}