package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest {

 static final String STRING_A = "ABCDEFG";
 static final String STRING_B = "123456789ABCDEFG";
 static final String STRING_C = "1234567890ABCDEFG";
 static final String STRING_D = "ABCDEFG1234567890";

 @Test
 public void testDiff_basic() {
 RelativeString actual = StringIncrementor.diff(STRING_B, STRING_A);
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(7, 0, 3));
 expected.addLink(new StringLink(11, 3, 3));
 expected.setAbsolute("123");
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_verbose_noMatch() {
 StringIncrementor.diff("ABC", "DEF");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullString() {
 StringIncrementor.diff(null, "ABC");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_emptyString() {
 StringIncrementor.diff("", "ABC");
 }

 @Test
 public void testDiff_verbose() {
 System.out.println("Testing diff(STRING_B, STRING_A)");
 RelativeString actual = StringIncrementor.diff(STRING_B, STRING_A, true);
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(7, 0, 3));
 expected.addLink(new StringLink(11, 3, 3));
 expected.setAbsolute("123");
 System.out.println("Expected: " + expected);
 System.out.println("Actual : " + actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_basic() {
 String actual = StringIncrementor.patch(STRING_C, StringIncrementor.diff(STRING_B, STRING_A));
 String expected = STRING_B;
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_verbose() {
 System.out.println("Testing patch(STRING_C, StringIncrementor.diff(STRING_B, STRING_A))");
 String actual = StringIncrementor.patch(STRING_C, StringIncrementor.diff(STRING_B, STRING_A), true);
 String expected = STRING_B;
 System.out.println("Expected: " + expected);
 System.out.println("Actual : " + actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_nullString() {
 String actual = StringIncrementor.patch(STRING_C, null);
 assertNull(actual);
 }

 @Test
 public void testPatch_relativeStringWithNoLinks() {
 RelativeString rs = new RelativeString();
 String actual = StringIncrementor.patch(STRING_C, rs);
 String expected = STRING_C;
 assertEquals(expected, actual);
 }
}