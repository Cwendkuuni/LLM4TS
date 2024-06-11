package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringLink;
import de.beiri22.stringincrementor.relativestring.RelativeString;

public class StringIncrementorTest {

 @Test
 public void testDiff() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";
 RelativeString result = StringIncrementor.diff(a, b);

 // Test links
 String expectedLink1 = new StringLink(12, 14, 3);
 assertEquals(expectedLink1, result.getLink(0));
 String expectedLink2 = new StringLink(34, 35, 1);
 assertEquals(expectedLink2, result.getLink(1));

 // Test absolute string
 String expectedAbsolute = " red ";
 assertEquals(expectedAbsolute, result.getAbsolute());
 }

 @Test
 public void testDiff_verbose() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";
 RelativeString result = StringIncrementor.diff(a, b, true);

 // Test links
 String expectedLink1 = new StringLink(12, 14, 3);
 assertEquals(expectedLink1, result.getLink(0));
 String expectedLink2 = new StringLink(34, 35, 1);
 assertEquals(expectedLink2, result.getLink(1));

 // Test absolute string
 String expectedAbsolute = " red ";
 assertEquals(expectedAbsolute, result.getAbsolute());

 // Test output
 String expectedOutput = "Creating index...\nSearching links...\n@ 8.16666666666666667%\n@ 40.83333333333333333%\nAdding 3 absolute Bytes from pos#0\nAdding Link: The quick brown fox jumps over the lazy dog.@ 8.16666666666666667%\nAdding Link: red @ 40.83333333333333333%";
 String actualOutput = System.out.getOutputStream().toString(StandardCharsets.UTF_8);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDiff_emptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);

 // Test links
 assertEquals(0, result.linksCount());

 // Test absolute string
 assertEquals("", result.getAbsolute());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_invalidStringLength() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick brown fox jumps over the lazy dog.";
 String c = "The quick brown fox jumps over the lazy dog.";

 // Test exception
 StringIncrementor.diff(a, c, true);
 }

 @Test
 public void testPatch() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";
 RelativeString r = StringIncrementor.diff(a, b);
 String patched = StringIncrementor.patch(a, r);

 // Test result
 String expectedPatched = "The quick red brown fox jumps over the lazy dog.";
 assertEquals(expectedPatched, patched);
 }

 @Test
 public void testPatch_verbose() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";
 RelativeString r = StringIncrementor.diff(a, b);
 String patched = StringIncrementor.patch(a, r, true);

 // Test result
 String expectedPatched = "The quick red brown fox jumps over the lazy dog.";
 assertEquals(expectedPatched, patched);

 // Test output
 String expectedOutput = "Adding 3 absolute Bytes from pos#0\nAdding Link: The quick brown fox jumps over the lazy dog.@ 8.16666666666666667%\nAdding Link: red @ 40.83333333333333333%";
 String actualOutput = System.out.getOutputStream().toString(StandardCharsets.UTF_8);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testPatch_emptyStrings() {
 String a = "";
 String b = "";
 RelativeString r = StringIncrementor.diff(a, b);
 String patched = StringIncrementor.patch(a, r);

 // Test result
 assertEquals("", patched);
 }
}