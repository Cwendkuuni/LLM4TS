package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

 // Testing the diff method with basic functionality
 @Test
 public void testDiffBasicFunctionality() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy cat";

 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(11, 11, 2)); // 'b' becomes 'cat' in 'b'
 expectedResult.setAbsolute("dog");

 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expectedResult, result);
 }

 // Testing the diff method with edge cases and exceptions
 @Test(expected = IndexOutOfBoundsException.class)
 public void testDiffIndexOutOfBoundsException() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "";

 RelativeString result = StringIncrementor.diff(a, b);
 fail("Expected IndexOutOfBoundsException but got: " + result);
 }

 @Test(expected = NullPointerException.class)
 public void testDiffNullPointerException() {
 String a = null;
 String b = "The quick brown fox jumps over the lazy dog";

 RelativeString result = StringIncrementor.diff(a, b);
 fail("Expected NullPointerException but got: " + result);
 }

 // Testing the diff method with large strings
 @Test
 public void testDiffLargeStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";

 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(190, 190, 22)); // 'b' becomes 'commodo consequat. Ut enim ad minim veniam,' in 'b'
 expectedResult.setAbsolute("commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");

 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals(expectedResult, result);
 }

 // Testing the patch method with basic functionality
 @Test
 public void testPatchBasicFunctionality() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patchString = "11 13 delete 2 cat insert";

 String expectedResult = "The quick brown fox jumps over the lazy cat";
 String result = StringIncrementor.patch(a, StringIncrementor.parsePatch(patchString));
 assertEquals(expectedResult, result);
 }

 // Testing the patch method with edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testPatchIllegalArgumentException() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patchString = "11 13 delete 2 cat insert dog";

 try {
 String result = StringIncrementor.patch(a, StringIncrementor.parsePatch(patchString));
 fail("Expected IllegalArgumentException but got: " + result);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPatchNullPointerException() {
 String a = null;
 String patchString = "11 13 delete 2 cat insert";

 try {
 String result = StringIncrementor.patch(a, StringIncrementor.parsePatch(patchString));
 fail("Expected NullPointerException but got: " + result);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Testing the patch method with large strings
 @Test
 public void testPatchLargeStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String patchString = "190 190 22 delete commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";

 String expectedResult = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
 String result = StringIncrementor.patch(a, StringIncrementor.parsePatch(patchString));
 assertEquals(expectedResult, result);
 }
}

class StringIncrementorTestHelper {
 RelativeString parsePatch(String patchString) {
 final String[] parsedPatch = patchString.split(" ");
 final RelativeString result = new RelativeString();
 for (String command : parsedPatch) {
 final String[] parts = command.split(" ");
 switch (parts[0]) {
 case "delete":
 result.delete(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
 break;
 case "insert":
 result.insert(Integer.parseInt(parts[1]), parts[2]);
 break;
 }
 }
 return result;
 }
}