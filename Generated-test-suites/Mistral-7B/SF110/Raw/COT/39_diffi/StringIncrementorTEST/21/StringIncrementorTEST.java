package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Test for diff method with basic inputs
 @Test
 public void testDiffBasicInput() {
 String a = "HelloWorld";
 String b = "HelloWorldHello";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(6, 6, 6, "World"));
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Test for diff method with null inputs
 @Test(expected = NullPointerException.class)
 public void testDiffNullInputs() {
 String a = null;
 String b = "HelloWorld";
 RelativeString result = StringIncrementor.diff(a, b);
 }

 // Test for diff method with verbose flag
 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HelloWorldHello";
 boolean verbose = true;
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(6, 6, 6, "World"));
 RelativeString result = StringIncrementor.diff(a, b, verbose);
 assertEquals(expected, result);
 }

 // Test for patch method with basic inputs
 @Test
 public void testPatchBasicInput() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(6, 6, 6, "World"));
 String expected = "HelloWorldHello";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 // Test for patch method with empty inputs
 @Test
 public void testPatchEmptyInputs() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 // Test for patch method with null inputs
 @Test(expected = NullPointerException.class)
 public void testPatchNullInputs() {
 String a = null;
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 }

 // Test for patch method with verbose flag
 @Test
 public void testPatchVerbose() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(6, 6, 6, "World"));
 boolean verbose = true;
 String expected = "HelloWorldHello";
 String result = StringIncrementor.patch(a, r, verbose);
 assertEquals(expected, result);
 }

 // Test for diff method with edge cases (different length strings)
 @Test
 public void testDiffEdgeCases() {
 String a = "abcd";
 String b = "efghijkl";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(0, 0, 4, "abcd"));
 expected.addLink(new StringLink(5, 5, 2, "ef"));
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Test for diff method with exception handling (empty string as second argument)
 @Test(expected = IllegalArgumentException.class)
 public void testDiffEmptySecondArgument() {
 String a = "HelloWorld";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 }

 // Test for diff method with exception handling (null character in second argument)
 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testDiffNullCharacterSecondArgument() {
 String a = "HelloWorld";
 String b = "\u0000HelloWorld";
 RelativeString result = StringIncrementor.diff(a, b);
 }

 // Test for diff method with exception handling (negative index in second argument)
 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testDiffNegativeIndexSecondArgument() {
 String a = "HelloWorld";
 String b = "HelloWorldHello" + new String(new char[10000], 0, 10000);
 RelativeString result = StringIncrementor.diff(a, b);
 }
}