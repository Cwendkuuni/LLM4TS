package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("", result.getAbsolute());
 }

 // Test for diff method with same strings
 @Test
 public void testDiffSameStrings() {
 String a = "HelloWorld";
 String b = "HelloWorld";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 // Test for diff method with different strings
 @Test
 public void testDiffDifferentStrings() {
 String a = "HelloWorld";
 String b = "HelloHelloWorld";
 RelativeString result = StringIncrementor.diff(a, b, false);
 String expected = new StringLink(7, 7, 5).toString() + " @ 50%";
 System.out.println(result);
 assertEquals(expected, result.toString());
 }

 // Test for diff method with verbose output
 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HelloHelloWorld";
 String output = "";
 try (PrintStream out = new PrintStream(new ByteArrayOutputStream())) {
 RelativeString result = StringIncrementor.diff(a, b, true);
 out.close();
 output = out.getBuffer().toString();
 }
 String expected = "Searching links...\nAdding Link: StringLink@[7, 7, 5]\n@ 50%";
 assertEquals(expected, output);
 }

 // Test for patch method with empty string and empty relativestring
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("", result);
 }

 // Test for patch method with empty string and non-empty relativestring
 @Test
 public void testPatchEmptyStringNonEmptyRelativeString() {
 String a = "";
 RelativeString r = new RelativeString(new StringLink(1, 1, 5).toString());
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("", result);
 }

 // Test for patch method with non-empty string and empty relativestring
 @Test
 public void testPatchNonEmptyStringEmptyRelativeString() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(a, result);
 }

 // Test for patch method with non-empty string and non-empty relativestring
 @Test
 public void testPatchNonEmptyStrings() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString(new StringLink(7, 7, 5).toString());
 String result = StringIncrementor.patch(a, r, false);
 String expected = "HelloWorldHelloWorld";
 assertEquals(expected, result);
 }

 // Test for patch method with verbose output
 @Test
 public void testPatchVerbose() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString(new StringLink(7, 7, 5).toString());
 String output = "";
 try (PrintStream out = new PrintStream(new ByteArrayOutputStream())) {
 String result = StringIncrementor.patch(a, r, true);
 out.close();
 output = out.getBuffer().toString();
 }
 String expected = "Adding 5 absolute Bytes from pos#0\nAdding Link: StringLink@[7, 7, 5]\n";
 assertEquals(expected, output);
 }
}