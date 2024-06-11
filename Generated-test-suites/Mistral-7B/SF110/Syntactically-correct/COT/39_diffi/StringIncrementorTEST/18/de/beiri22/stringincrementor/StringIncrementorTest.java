package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Testing method: diff(a, b)
 @Test
 public void testDiff_givenTwoStrings_returnsCorrectRelativeString() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick brown fox jumps over the quick red dog";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(11, 16, 5)); // "quick red"
 assertRelativeStringEquals(expected, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_givenTwoIdenticalStrings_returnsEmptyRelativeString() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick brown fox jumps over the lazy dog";
 RelativeString expected = new RelativeString();
 assertRelativeStringEquals(expected, StringIncrementor.diff(a, b));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_givenNullString_throwsIllegalArgumentException() {
 String a = null;
 String b = "The quick brown fox jumps over the lazy dog";
 assertNull(StringIncrementor.diff(a, b));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_givenNullStringB_throwsIllegalArgumentException() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = null;
 assertNull(StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_givenVerbose_printsCorrectOutput() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick brown fox jumps over the quick red dog";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(11, 16, 5)); // "quick red"
 StringIncrementor.diff(a, b, true);
 }

 // Testing method: diff(a, b, verbose)
 @Test
 public void testDiff_givenTwoStringsWithVerbose_printsCorrectOutput() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick brown fox jumps over the quick red dog";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(11, 16, 5)); // "quick red"
 StringIncrementor.diff(a, b, true);
 }

 // Testing method: patch(a, r)
 @Test
 public void testPatch_givenStringAndEmptyRelativeString_returnsOriginalString() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 String expected = a;
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_givenCorrectStringAndRelativeString_returnsCorrectString() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(11, 16, 5)); // "quick red"
 String expected = "The quick brown fox jumps over the quick red dog";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_givenNullString_throwsIllegalArgumentException() {
 String a = null;
 RelativeString r = new RelativeString();
 assertNull(StringIncrementor.patch(a, r));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_givenNullRelativeString_throwsIllegalArgumentException() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = null;
 assertNull(StringIncrementor.patch(a, r));
 }

 @Test
 public void testPatch_givenVerbose_printsCorrectOutput() {
 String a = "The quick brown fox jumps over the lazy dog";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(11, 16, 5)); // "quick red"
 StringIncrementor.patch(a, r, true);
 }

}