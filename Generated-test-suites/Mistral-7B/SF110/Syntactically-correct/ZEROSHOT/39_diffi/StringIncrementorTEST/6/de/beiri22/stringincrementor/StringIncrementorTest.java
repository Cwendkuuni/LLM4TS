package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiff() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick brown fox jumps over the quick brown dog.";
 RelativeString result = StringIncrementor.diff(a, b, false);
 String expected = "The quick brown fox jumps over [+11] quick brown dog.";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testDiff_verbose() {
 String a = "The quick brown fox jumps over the quick brown dog.";
 String b = "The quick brown fox jumps over the lazy dog.";
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expected = "Creating index...\nSearching links...\n[+11] @ 50.0%\nAdding Link: StringLink@15-18[+3]: The quick brown fox jumps over [+11] quick brown dog.\n";
 printStreamToConsole(System.out, result.getVerboseOutput());
 assertEquals(expected, result.getVerboseOutput());
 }

 @Test
 public void testDiff_emptyString() {
 String a = "";
 String b = "abcdef";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.toString());
 }

 @Test
 public void testDiff_nullString() {
 String a = null;
 String b = "abcdef";
 try {
 RelativeString result = StringIncrementor.diff(a, b, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testDiff_nullStringB() {
 String a = "abcdef";
 String b = null;
 try {
 RelativeString result = StringIncrementor.diff(a, b, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testPatch() {
 String a = "The quick brown fox jumps over the quick brown dog.";
 String b = "The quick brown fox jumps over [+11] quick brown dog.";
 RelativeString r = StringIncrementor.diff(b, a, false);
 String patched = StringIncrementor.patch(a, r, false);
 String expected = "The quick brown fox jumps over the quick brown fox jumps over the quick brown dog.";
 assertEquals(expected, patched);
 }

 @Test
 public void testPatch_verbose() {
 String a = "The quick brown fox jumps over the quick brown dog.";
 String b = "The quick brown fox jumps over [+11] quick brown dog.";
 RelativeString r = StringIncrementor.diff(b, a, false);
 String patched = StringIncrementor.patch(a, r, true);
 String expected = "The quick brown fox jumps over the quick brown fox jumps over the quick brown dog.\nAdding [+11] quick brown dog. to patchString";
 printStreamToConsole(System.out, patched.getVerboseOutput());
 assertEquals(expected, patched.getVerboseOutput());
 }

 @Test
 public void testPatch_emptyString() {
 String a = "The quick brown fox jumps over the quick brown dog.";
 RelativeString r = new RelativeString();
 String patched = StringIncrementor.patch(a, r, false);
 assertEquals("The quick brown fox jumps over the quick brown dog.", patched);
 }

 @Test
 public void testPatch_nullString() {
 String a = "The quick brown fox jumps over the quick brown dog.";
 RelativeString r = null;
 try {
 String patched = StringIncrementor.patch(a, r, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

}