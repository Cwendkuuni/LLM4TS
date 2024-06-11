package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Testing method: diff(String a, String b)
 @Test
 public void testDiff() {
 String a = "hello world";
 String b = "hello there world";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(5, 5, 6, "there"));
 String result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Testing method: diff(String a, String b, boolean verbose)
 @Test
 public void testDiffVerbose() {
 String a = "hello world";
 String b = "hello there world";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(5, 5, 6, "there"));
 String result = StringIncrementor.diff(a, b, true);
 assertEquals(expected, result);
 }

 // Testing edge case: empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 String result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Testing edge case: identical strings
 @Test
 public void testDiffIdenticalStrings() {
 String a = "hello world";
 String b = "hello world";
 RelativeString expected = new RelativeString();
 String result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Testing edge case: one string empty, another not
 @Test
 public void testDiffOneEmpty() {
 String a = "";
 String b = "hello world";
 RelativeString expected = new RelativeString();
 String result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Testing edge case: one string longer, another shorter
 @Test
 public void testDiffOneLonger() {
 String a = "hello world";
 String b = "hello";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(6, 5, 1, ""));
 String result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 // Testing method: patch(String a, RelativeString r)
 @Test
 public void testPatch() {
 String a = "hello world hello there world";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 6, "there"));
 String expected = "hello world there world";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 // Testing method: patch(String a, RelativeString r, boolean verbose)
 @Test
 public void testPatchVerbose() {
 String a = "hello world hello there world";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 6, "there"));
 String expected = "hello world there world";
 String result = StringIncrementor.patch(a, r, true);
 assertEquals(expected, result);
 }

 // Testing edge case: empty string as input
 @Test(expected = IllegalArgumentException.class)
 public void testPatchEmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r);
 }

 // Testing edge case: null as input
 @Test(expected = NullPointerException.class)
 public void testPatchNullInput() {
 String a = null;
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r);
 }

 // Testing edge case: null as RelativeString input
 @Test(expected = NullPointerException.class)
 public void testPatchNullRelativeStringInput() {
 String a = "hello world";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }
}