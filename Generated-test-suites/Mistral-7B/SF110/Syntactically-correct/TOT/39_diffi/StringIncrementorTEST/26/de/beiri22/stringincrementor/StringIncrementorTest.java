package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiff_emptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 assertEquals(expected, StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_identicalStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString expected = new RelativeString();
 assertEquals(expected, StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_singleCharacterDifference() {
 String a = "hello";
 String b = "helo";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(4, 3, 1));
 assertEquals(expected, StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_multipleCharacterDifferences() {
 String a = "hello";
 String b = "helloworld";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(4, 5, 1));
 expected.addLink(new StringLink(6, 6, 1));
 expected.addLink(new StringLink(10, 10, 6));
 assertEquals(expected, StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_verboseMode() {
 String a = "abcdefghijk";
 String b = "abcxyzdefghijk";
 String expectedVerbose = "Link: [4, 4, 5] @ 25.0%\nLink: [10, 10, 5] @ 50.0%\nLink: [13, 13, 5] @ 75.0%\nLink: [15, 15, 1] @ 91.66666666666667%" +
 "Link: [16, 16, 1] @ 100.0%";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(4, 4, 5));
 expected.addLink(new StringLink(10, 10, 5));
 expected.addLink(new StringLink(13, 13, 5));
 expected.addLink(new StringLink(15, 15, 1));
 expected.addLink(new StringLink(16, 16, 1));
 String result = "";
 try {
 result = StringIncrementor.diff(a, b, true).getAbsolute();
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 assertEquals(expectedVerbose, result);
 assertEquals(expected, StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_errorOnInvalidInput() {
 String a = null;
 String b = "hello";
 try {
 StringIncrementor.diff(a, b, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 String a2 = "hello";
 String b2 = null;
 try {
 StringIncrementor.diff(a2, b2, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testPatch_emptyStrings() {
 String a = "";String b = "";
 String expected = "";
 assertEquals(expected, StringIncrementor.patch(a, new RelativeString(), false));
 }

 @Test
 public void testPatch_identicalStrings() {
 String a = "hello";
 String b = "hello";
 String expected = "hello";
 assertEquals(expected, StringIncrementor.patch(a, new RelativeString(), false));
 }

 @Test
 public void testPatch_singleCharacterDifference() {
 String a = "hello";
 String b = "helo";
 String expected = "helo";
 assertEquals(expected, StringIncrementor.patch(a, new StringLink(4, 3, 1), false));
 }

 @Test
 public void testPatch_multipleCharacterDifferences() {
 String a = "hello";
 String b = "helloworld";
 String expected = "helloworld";
 assertEquals(expected, StringIncrementor.patch(a, new RelativeString(Arrays.asList(new StringLink(4, 5, 1), new StringLink(6, 6, 1), new StringLink(10, 10, 6))), false));
 }

 @Test
 public void testPatch_verboseMode() {
 String a = "abcdefghijk";
 String b = "abcxyzdefghijk";
 String expected = "abcxyzdefghijk";
 String result = "";
 try {
 result = StringIncrementor.patch(a, new RelativeString(Arrays.asList(new StringLink(4, 4, 5), new StringLink(10, 10, 5), new StringLink(13, 13, 5), new StringLink(15, 15, 1), new StringLink(16, 16, 1))), true);
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_errorOnInvalidInput() {
 String a = null;
 String b = "hello";
 try {
 StringIncrementor.patch(a, new RelativeString(), false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 String a2 = "hello";
 RelativeString r = new RelativeString();
 try {
 StringIncrementor.patch(a2, r, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 RelativeString r2 = new RelativeString(null);
 try {
 StringIncrementor.patch(new String("hello"), r2, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}