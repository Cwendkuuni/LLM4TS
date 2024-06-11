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

 // Test for diff method with simple differences
 @Test
 public void testDiffSimpleDifferences() {
 String a = "abcdef";
 String b = "abcdghi";
 RelativeString expected = new StringLink(5, 5, 2).addLink(new StringLink(7, 7, 1));
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 // Test for diff method with complex differences
 @Test
 public void testDiffComplexDifferences() {
 String a = "abcdabcdabcd";
 String b = "efghijklmnopqefghijklmnopq";
 RelativeString expected = new StringLink(10, 10, 4).addLink(new StringLink(15, 15, 5)).addLink(new StringLink(21, 21, 1));
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 // Test for diff method with verbose output
 @Test
 public void testDiffVerboseOutput() {
 String a = "abcdabcdabcd";
 String b = "efghijklmnopqefghijklmnopq";
 String expectedOutput = "Adding 4 absolute Bytes from pos#0\nAdding Link: StringLink@207d9a9d [5, 5, 4]\nAdding Link: StringLink@6c03d8b9 [15, 15, 5]\nAdding Link: StringLink@2319c871 [21, 21, 1]\n";
 System.out.println("Expected verbose output: " + expectedOutput);
 String resultOutput = "";
 System.out.println("Actual verbose output:");
 StringIncrementor.diff(a, b, true);
 assertEquals(expectedOutput, resultOutput + System.out.getBuffer().getBytes().toString());
 }

 // Test for patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with simple differences
 @Test
 public void testPatchSimpleDifferences() {
 String a = "abcdef";
 RelativeString r = new StringLink(5, 5, 2).addLink(new StringLink(7, 7, 1));
 String expected = "cdghi";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with complex differences
 @Test
 public void testPatchComplexDifferences() {
 String a = "abcdabcdabcd";
 RelativeString r = new StringLink(10, 10, 4).addLink(new StringLink(15, 15, 5)).addLink(new StringLink(21, 21, 1));
 String expected = "efghijklmnopqefghijklmnopq";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 // Test for patch method with verbose output
 @Test
 public void testPatchVerboseOutput() {
 String a = "abcdabcdabcd";
 RelativeString r = new StringLink(10, 10, 4).addLink(new StringLink(15, 15, 5)).addLink(new StringLink(21, 21, 1));
 String expectedOutput = "Adding 4 absolute Bytes from pos#0\nAdding Link: StringLink@207d9a9d [5, 5, 4]\nAdding Link: StringLink@6c03d8b9 [15, 15, 5]\nAdding Link: StringLink@2319c871 [21, 21, 1]\n";
 System.out.println("Expected verbose output: " + expectedOutput);
 String resultOutput = "";
 System.out.println("Actual verbose output:");
 StringIncrementor.patch(a, r, true);
 assertEquals(expectedOutput, resultOutput + System.out.getBuffer().getBytes().toString());
 }

}