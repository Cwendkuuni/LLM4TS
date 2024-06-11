package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 private static final String STRING_A = "The quick brown fox jumps over the lazy dog";
 private static final String STRING_B = "The quick brown fox jumps over the lazy dog and the quick red fox barks";

 @Test
 public void testDiff_SameStrings() {
 RelativeString expected = new RelativeString();
 StringIncrementor.diff(STRING_A, STRING_A, true); // Verbose mode to see output
 assertNotNull(expected);
 assertEquals(0, expected.linksCount());
 }

 @Test
 public void testDiff_EmptyStrings() {
 RelativeString expected = new RelativeString();
 StringIncrementor.diff("", "", true); // Verbose mode to see output
 assertNotNull(expected);
 assertEquals(0, expected.linksCount());
 }

 @Test
 public void testDiff_DifferentStrings() {
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(7, 17, 10));
 expected.addLink(new StringLink(18, 18, 1));
 expected.setAbsolute("dog and ");
 StringIncrementor.diff(STRING_A, STRING_B, true); // Verbose mode to see output
 RelativeString actual = StringIncrementor.diff(STRING_A, STRING_B, false);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullInput() {
 StringIncrementor.diff(null, STRING_B, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullInputB() {
 StringIncrementor.diff(STRING_A, null, false);
 }

 @Test
 public void testPatch_SameStrings() {
 String expected = STRING_A;
 String actual = StringIncrementor.patch(STRING_A, StringIncrementor.diff(STRING_A, STRING_A, false));
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_DifferentStrings() {
 String expected = "The quick brown fox jumps over the lazy dog and the quick red fox barks";
 String actual = StringIncrementor.patch(STRING_A, StringIncrementor.diff(STRING_A, STRING_B, false));
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_EmptyInput() {
 String expected = "";
 String actual = StringIncrementor.patch("", StringIncrementor.diff("", "", false));
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_EmptyLinkList() {
 String expected = STRING_A;
 String actual = StringIncrementor.patch(STRING_A, new RelativeString());
 assertEquals(expected, actual);
 }
}