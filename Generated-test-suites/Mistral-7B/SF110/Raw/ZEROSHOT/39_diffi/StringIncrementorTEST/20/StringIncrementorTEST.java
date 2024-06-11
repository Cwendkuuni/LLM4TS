package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 private static final String TEST_STRING_1 = "The quick brown fox jumps over the lazy dog.";
 private static final String TEST_STRING_2 = "The quick brown fox jumps over the slow dog.";

 @Test
 public void testDiff() {
 StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2); // Test without verbose
 StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2, true); // Test with verbose

 RelativeString relativeString = StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2);
 String expectedAbsoluteDifference = " dog. is slow.";
 String expectedRelativeDifference = " dog. => is slow.";
 assertEquals(expectedAbsoluteDifference, relativeString.getAbsolute());
 assertEquals(expectedRelativeDifference, relativeString.toString());
 }

 @Test
 public void testPatch() {
 String patchedString = StringIncrementor.patch(TEST_STRING_1, StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2));
 assertEquals(TEST_STRING_2, patchedString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDiffArgument() {
 StringIncrementor.diff(null, TEST_STRING_2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPatchArgument() {
 StringIncrementor.patch(TEST_STRING_1, null);
 }

 @Test
 public void testDiffVerbose() throws IOException {
 System.setOut(new PrintStream(new ByteArrayOutputStream())); // Reset System.out to avoid printing to console
 StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2, true);
 // Replace the following line with your expected verbose output or a way to capture the output and assert it
 // fail("Verbose output not tested yet.");
 }

 @Test
 public void testPatchVerbose() throws IOException {
 System.setOut(new PrintStream(new ByteArrayOutputStream())); // Reset System.out to avoid printing to console
 StringIncrementor.patch(TEST_STRING_1, StringIncrementor.diff(TEST_STRING_1, TEST_STRING_2), true);
 // Replace the following line with your expected verbose output or a way to capture the output and assert it
 // fail("Verbose output not tested yet.");
 }

}