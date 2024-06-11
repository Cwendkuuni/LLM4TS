package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Testing diff method with basic parameters
 @Test
 public void testDiffBasic() {
 String a = "HelloWorld";
 String b = "HelloThereWorld";
 RelativeString expectedResult = new RelativeString("3 Hello 5 There 1 World");
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expectedResult, result);
 }

 // Testing diff method with verbose parameter
 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HelloThereWorld";
 String verboseOutput = "Creating index... Searching links... @ 0% @ 33.33333333333333% Adding Link: StringLink(1, 3, 5) @ 66.66666666666667% Adding Link: StringLink(11, 11, 5) @ 100.0%";
 System.out.println("Expected verbose output: " + verboseOutput);
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String x) {
 assertEquals(verboseOutput, x);
 }
 });
 RelativeString result = StringIncrementor.diff(a, b, true);
 // Restore original System.out
 System.setOut(System.out);
 }

 // Testing diff method with empty strings
 @Test(expected = IllegalArgumentException.class)
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 StringIncrementor.diff(a, b);
 }

 // Testing diff method with null strings
 @Test(expected = NullPointerException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "HelloWorld";
 StringIncrementor.diff(a, b);
 }

 // Testing diff method with strings of different lengths
 @Test(expected = IllegalArgumentException.class)
 public void testDiffStringsDifferentLengths() {
 String a = "HelloWorld";
 String b = "Hello";
 StringIncrementor.diff(a, b);
 }

 // Testing patch method with basic parameters
 @Test
 public void testPatchBasic() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString("3 Hello 5 There 1 World");
 String expectedResult = "HelloThereWorld";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expectedResult, result);
 }

 // Testing patch method with verbose parameter
 @Test
 public void testPatchVerbose() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString("3 Hello 5 There 1 World");
 String verboseOutput = "Adding 5 absolute Bytes from pos#0 Adding Link: StringLink(1, 3, 5) Adding 5 absolute Bytes from pos#12 Adding Link: StringLink(11, 11, 5)";
 System.out.println("Expected verbose output: " + verboseOutput);
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String x) {
 assertEquals(verboseOutput, x);
 }
 });
 String result = StringIncrementor.patch(a, r, true);
 // Restore original System.out
 System.setOut(System.out);
 assertEquals(r.getAbsolute(), result.substring(r.getLength()));
 }

 // Testing patch method with empty strings
 @Test(expected = IllegalArgumentException.class)
 public void testPatchEmptyStrings() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString("");
 StringIncrementor.patch(a, r);
 }

 // Testing patch method with null RelativeString
 @Test(expected = NullPointerException.class)
 public void testPatchNullRelativeString() {
 String a = "HelloWorld";
 StringIncrementor.patch(a, null);
 }

 // Testing patch method with RelativeString with no links
 @Test
 public void testPatchNoLinks() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString("");
 String expectedResult = "HelloWorld";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expectedResult, result);
 }
}