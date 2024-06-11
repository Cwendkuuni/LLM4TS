package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import java.util.Arrays;

import org.junit.Test;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString(), result);
 }

 // Test for diff method with identical strings
 @Test
 public void testDiffIdenticalStrings() {
 String a = "foo";
 String b = "foo";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString(), result);
 }

 // Test for diff method with simple differences
 @Test
 public void testDiffSimpleDifferences() {
 String a = "abcd";
 String b = "abce";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new RelativeString();
 expected.addLink(new StringLink(3, 3, 1));
 assertEquals(expected, result);
 }

 // Test for diff method with complex differences
 @Test
 public void testDiffComplexDifferences() {
 String a = "abcd1234efghij5678klmnopqrs";
 String b = "abxyz1234efghij5678klmnpqrs";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new RelativeString();
 expected.addLink(new StringLink(2, 2, 3));
 expected.addLink(new StringLink(13, 13, 7));
 expected.addLink(new StringLink(24, 24, 10));
 expected.addLink(new StringLink(36, 36, 4));
 assertEquals(expected, result);
 }

 // Test for diff method with verbose flag
 @Test
 public void testDiffVerbose() {
 String a = "abcd";
 String b = "abce";
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expected = new RelativeString();
 expected.addLink(new StringLink(3, 3, 1));
 System.out.println("Expected Output:");
 System.out.println(expected);
 String actualOutput = "";
 try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 PrintStream originalOut = System.out) {
 System.setOut(new PrintStream(outputStream));
 ResultSet rs = StringIncrementor.diff(a, b, true);
 RelativeString actualResult = (RelativeString) rs.getObject();
 System.out.flush();
 System.setOut(originalOut);
 actualOutput = outputStream.toString();
 }
 assertEquals(expected.toString() + actualOutput, result.toString());
 }

 // Test for patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("", result);
 }

 // Test for patch method with no links
 @Test
 public void testPatchNoLinks() {
 String a = "abcd";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("abcd", result);
 }

 // Test for patch method with simple differences
 @Test
 public void testPatchSimpleDifferences() {
 String a = "abcd";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 1));
 String result = StringIncrementor.patch(a, r);
 assertEquals("abce", result);
 }

 // Test for patch method with complex differences
 @Test
 public void testPatchComplexDifferences() {
 String a = "abcd1234efghij5678klmnopqrs";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(2, 2, 3));
 r.addLink(new StringLink(13, 13, 7));
 r.addLink(new StringLink(24, 24, 10));
 r.addLink(new StringLink(36, 36, 4));
 String result = StringIncrementor.patch(a, r);
 assertEquals("abxyz1234efghij5678klmnpqrs", result);
 }

 // Test for patch method with verbose flag
 @Test
 public void testPatchVerbose() {
 String a = "abcd";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 1));
 String result = StringIncrementor.patch(a, r, true);
 String expected = "abce";
 System.out.println("Expected Output:");
 System.out.println(expected);
 String actualOutput = "";
 try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 PrintStream originalOut = System.out) {
 System.setOut(new PrintStream(outputStream));
 String actualResult = StringIncrementor.patch(a, r, true).toString();
 System.out.flush();
 System.setOut(originalOut);
 actualOutput = outputStream.toString();
 }
 assertEquals(expected + actualOutput, result);
 }
}