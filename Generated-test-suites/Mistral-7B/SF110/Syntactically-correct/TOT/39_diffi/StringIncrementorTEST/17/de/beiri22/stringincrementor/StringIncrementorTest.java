import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 // Test for diff method with identical strings
 @Test
 public void testDiffIdenticalStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 // Test for diff method with different strings with no common substrings
 @Test
 public void testDiffDifferentStrings() {
 String a = "hello";
 String b = "world";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("world", result.getAbsolute());
 }

 // Test for diff method with different strings with common substrings
 @Test
 public void testDiffStringsWithCommonSubstrings() {
 String a = "hello world";
 String b = "world hello";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 // Test for diff method with verbose mode
 @Test
 public void testDiffVerbose() {
 String a = "abcdefghijk";
 String b = "abcdefghijklm";
 RelativeString result = StringIncrementor.diff(a, b, true);
 // Expected output:
 // Creating index...
 // Searching links...
 // @ 0%
 // @ 10%
 // @ 20%
 // @ 30%
 // @ 40%
 // @ 50%
 // @ 60%
 // @ 70%
 // @ 80%
 // @ 90%
 // Link: StringLink(4, 5, 6) @ 50%
 // @ 100%
 // Setting absolute part: lm
 }

 // Test for patch method with empty string and empty RelativeString
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("", result);
 }

 // Test for patch method with empty string and non-empty RelativeString
 @Test
 public void testPatchEmptyStringNonEmptyRelativeString() {
 String a = "";
 RelativeString r = new RelativeString("test");
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("test", result);
 }

 // Test for patch method with non-empty string and empty RelativeString
 @Test
 public void testPatchNonEmptyStringEmptyRelativeString() {
 String a = "test";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("test", result);
 }

 // Test for patch method with non-empty string and non-empty RelativeString
 @Test
 public void testPatchNonEmptyStrings() {
 String a = "hello world";
 RelativeString r = new RelativeString("world hello");
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("helloworld", result);
 }

 // Test for patch method with verbose mode
 @Test
 public void testPatchVerbose() {
 String a = "abcdefghijk";
 RelativeString r = new RelativeString("ijklm");
 String result = StringIncrementor.patch(a, r, true);
 // Expected output:
 // Adding 4 absolute Bytes from pos#0
 // Adding Link: StringLink(4, 8, 4)
 // Adding 3 absolute Bytes from pos#4
 // Adding Link: StringLink(8, 11, 3)
 // Adding 2 absolute Bytes from pos#11
 }
}