package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 // Testing the diff method with basic functionality
 @Test
 public void testDiffBasicFunctionality() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy dog";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(7, 0, 3)); // red -> quick
 expected.addLink(new StringLink(11, 10, 1)); // fox -> jumps
 expected.setAbsolute("The quick brown fox jumps over the lazy dog");

 RelativeString actual = StringIncrementor.diff(a, b);
 assertEquals(expected, actual);
 }

 // Testing the diff method with verbose output
 @Test
 public void testDiffVerboseOutput() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy dog";

 RelativeString result = StringIncrementor.diff(a, b, true);
 List<StringLink> links = result.getLinks();
 Collections.reverse(links);
 for (StringLink link : links) {
 System.out.println("Link: " + link);
 }

 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(7, 0, 3)); // red -> quick
 expected.addLink(new StringLink(11, 10, 1)); // fox -> jumps
 expected.setAbsolute("The quick brown fox jumps over the lazy dog");

 assertEquals(expected, result);
 }

 // Testing the diff method with empty strings
 @Test(expected = IllegalArgumentException.class)
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 StringIncrementor.diff(a, b);
 }

 // Testing the diff method with null strings
 @Test(expected = NullPointerException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "The quick brown fox jumps over the lazy dog";
 StringIncrementor.diff(a, b);
 }

 // Testing the diff method with different string lengths
 @Test
 public void testDiffStringsWithDifferentLengths() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick brown fox jumps over the lazy dog and the cat";

 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(27, 24, 4)); // and the cat -> the
 expected.setAbsolute("The quick brown fox jumps over the lazy dog and the cat");

 RelativeString actual = StringIncrementor.diff(a, b);
 assertEquals(expected, actual);
 }

 // Testing the patch method with basic functionality
 @Test
 public void testPatchBasicFunctionality() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(7, 0, 3)); // red -> quick
 r.addLink(new StringLink(11, 10, 1)); // fox -> jumps
 r.setAbsolute("The quick brown fox jumps over the lazy dog");

 String expected = "The quick red fox jumps over the lazy dog";
 String actual = StringIncrementor.patch(a, r);
 assertEquals(expected, actual);
 }

 // Testing the patch method with verbose output
 @Test
 public void testPatchVerboseOutput() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(7, 0, 3)); // red -> quick
 r.addLink(new StringLink(11, 10, 1)); // fox -> jumps
 r.setAbsolute("The quick brown fox jumps over the lazy dog");

 StringBuilder output = new StringBuilder();
 String result = StringIncrementor.patch(a, r, true);
 System.out.println("Result: " + result);
 assertEquals(r.getAbsolute(), output.toString());
 }

 // Testing the patch method with null relative string
 @Test(expected = NullPointerException.class)
 public void testPatchNullRelativeString() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }

 // Testing the patch method with empty string as input
 @Test
 public void testPatchEmptyString() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 String expected = "The quick brown fox jumps over the lazy dog";
 String actual = StringIncrementor.patch(a, r);
 assertEquals(expected, actual);
 }
}