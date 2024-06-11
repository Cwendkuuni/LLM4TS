package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertTrue(expectedResult.equals(result));
 }

 // Test for diff method with identical strings
 @Test
 public void testDiffIdenticalStrings() {
 String a = "foo";
 String b = "foo";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertTrue(expectedResult.equals(result));
 }

 // Test for diff method with single character difference
 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "foo";
 String b = "bar";
 RelativeString expectedResult = new StringLink(3, 0, 1);
 RelativeString result = StringIncrementor.diff(a, b);
 assertTrue(expectedResult.equals(result));
 }

 // Test for diff method with multiple character differences
 @Test
 public void testDiffMultipleCharacterDifferences() {
 String a = "abcd";
 String b = "efgh";
 RelativeString expectedResult = new StringLink(3, 2, 2)
 + new StringLink(7, 0, 2)
 + new StringLink(8, 4, 1)
 + new StringLink(11, 1, 1);
 RelativeString result = StringIncrementor.diff(a, b);
 assertTrue(expectedResult.equals(result));
 }

 // Test for diff method with verbose output
 @Test
 public void testDiffVerbose() {
 String a = "abcd";
 String b = "efgh";
 System.setOut(new PrintStream(new ByteArrayOutputStream()));
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expectedLog = "Creating index...\nLink: [0, 2, 2] @ 0%\nAdding 2 absolute Bytes from pos#0\nAdding Link: [3, 0, 2]\nLink: [7, 0, 2] @ 50%\nAdding 2 absolute Bytes from pos#3\nAdding Link: [8, 4, 1]\nLink: [11, 1, 1] @ 100%\nAdding 1 absolute Byte from pos#8\n";
 assertEquals(expectedLog, System.out.getOutputStream().toString());
 assertTrue(expectedResult.equals(result));
 }

 // Test for patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expectedResult = "";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with single character difference
 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "foo";
 RelativeString r = new StringLink(3, 0, 1);
 String expectedResult = "foob";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with multiple character differences
 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "abcd";
 RelativeString r = new StringLink(3, 2, 2)
 + new StringLink(7, 0, 2)
 + new StringLink(8, 4, 1)
 + new StringLink(11, 1, 1);
 String expectedResult = "efghabcd";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with verbose output
 @Test
 public void testPatchVerbose() {
 String a = "abcd";
 RelativeString r = new StringLink(3, 2, 2)
 + new StringLink(7, 0, 2)
 + new StringLink(8, 4, 1)
 + new StringLink(11, 1, 1);
 System.setOut(new PrintStream(new ByteArrayOutputStream()));
 String result = StringIncrementor.patch(a, r, true);
 String expectedLog = "Adding 2 absolute Bytes from pos#0\nAdding Link: [3, 2, 2]\nAdding 2 absolute Bytes from pos#3\nAdding Link: [7, 0, 2]\nAdding 2 absolute Bytes from pos#7\nAdding Link: [8, 4, 1]\nAdding 1 absolute Byte from pos#9\nAdding Link: [11, 1, 1]\n";
 assertEquals(expectedLog, System.out.getOutputStream().toString());
 assertEquals("efghabcd", result);
 }
}