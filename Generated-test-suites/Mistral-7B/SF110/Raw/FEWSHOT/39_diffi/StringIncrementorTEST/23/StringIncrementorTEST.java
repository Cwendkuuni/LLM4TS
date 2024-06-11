package de.beiri22.stringincrementor;

import static org.junit.Assert.*;

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
 String a = "hello";
 String b = "hello";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString(), result);
 }

 // Test for diff method with one character difference
 @Test
 public void testDiffOneCharacterDifference() {
 String a = "hello";
 String b = "helo";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new StringLink(5, 4, 1).toString();
 assertTrue(result.toString().contains(expected));
 }

 // Test for diff method with multiple character differences
 @Test
 public void testDiffMultipleCharacterDifferences() {
 String a = "hello world";
 String b = "hello earth";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new StringLink(11, 10, 1).toString() + " " + new StringLink(12, 11, 1).toString();
 assertTrue(result.toString().contains(expected));
 }

 // Test for diff method with verbose output
 @Test
 public void testDiffVerbose() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcxyzdefghijklmnopqrstuvwxyz";
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expected = "Link: [0, 3] @ 0%\n" +
                  "Link: [9, 12] @ 66.666667%\n";
 String actual = result.toString();
 assertTrue(actual.contains(expected));
 }

 // Test for patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("", result);
 }

 // Test for patch method with identical strings
 @Test
 public void testPatchIdenticalStrings() {
 String a = "hello";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals(a, result);
 }

 // Test for patch method with one character difference
 @Test
 public void testPatchOneCharacterDifference() {
 String a = "hello";
 RelativeString r = new StringLink(5, 4, 1).toString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("helo", result);
 }

 // Test for patch method with multiple character differences
 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "hello world";
 RelativeString r = new StringLink(11, 10, 1).toString() + " " + new StringLink(12, 11, 1).toString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("hello earth", result);
 }

 // Test for patch method with verbose output
 @Test
 public void testPatchVerbose() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(9, 10, 1));
 r.addLink(new StringLink(12, 11, 1));
 String result = StringIncrementor.patch(a, r, true);
 String expected = "abcxyzdefghijklmnopqrstuvwxyz";
 assertEquals(expected, result);
 }
}