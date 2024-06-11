package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Test case for diff method with basic functionality
 @Test
 public void testDiffBasicFunctionality() {
 String a = "HelloWorld";
 String b = "HelloWorldHiThere";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(6, 6, 6));
 String result = StringIncrementor.diff(a, b);
 assertEquals(expectedResult, result);
 }

 // Test case for diff method with edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullString() {
 StringIncrementor.diff(null, "HelloWorld");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffEmptyString() {
 String a = "";
 String b = "HelloWorld";
 StringIncrementor.diff(a, b);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffDifferentLengthStrings() {
 String a = "HelloWorld";
 String b = "HelloWorldHelloWorld";
 StringIncrementor.diff(a, b);
 }

 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HelloWorldHiThere";
 StringIncrementor.diff(a, b, true);
 }

 // Test case for patch method with basic functionality
 @Test
 public void testPatchBasicFunctionality() {
 String a = "HelloWorld";
 String b = "HelloWorldHiThere";
 String expectedResult = "HelloWorldHiThere";
 String result = StringIncrementor.patch(a, StringIncrementor.diff(a, b));
 assertEquals(expectedResult, result);
 }

 // Test case for patch method with edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullString() {
 String a = null;
 String b = "HelloWorld";
 String result = StringIncrementor.patch(a, StringIncrementor.diff(b, a));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchEmptyString() {
 String a = "HelloWorld";
 String b = "";
 String result = StringIncrementor.patch(a, StringIncrementor.diff(b, a));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchRelativeStringIsEmpty() {
 String a = "HelloWorld";
 RelativeString rs = new RelativeString();
 String result = StringIncrementor.patch(a, rs);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPatchIndexOutOfBounds() {
 String a = "HelloWorld";
 String b = "HelloWorldHiThere";
 RelativeString rs = StringIncrementor.diff(a, b);
 String result = StringIncrementor.patch(a, rs);
 String patchResult = StringIncrementor.patch(result, rs);
 String expectedResult = "HelloWorldHithere"; // Different length than the original strings
 result = result.substring(0, rs.getLink(rs.linksCount() - 1).getPosNew());
 String patchExpectedResult = "iThere";
 int index = patchResult.indexOf(patchExpectedResult);
 patchResult = patchResult.substring(0, index);
 assertEquals(patchExpectedResult, patchResult);
 String finalResult = StringIncrementor.patch(result, rs.subtractLastLink());
 assertEquals(expectedResult, finalResult);
 // Index is out of bounds when trying to get the last link
 rs = new RelativeString();
 rs.addLink(new StringLink(result.length(), result.length(), result.length()));
 result = StringIncrementor.patch(a, rs);
 }

}