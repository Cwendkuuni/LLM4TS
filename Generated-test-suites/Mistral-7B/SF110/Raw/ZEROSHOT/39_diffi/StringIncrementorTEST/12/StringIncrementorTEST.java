package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiff_emptyStrings() {
 RelativeString expected = new RelativeString();
 RelativeString actual = StringIncrementor.diff("", "");
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_identicalStrings() {
 String input = "testString";
 RelativeString expected = new RelativeString();
 RelativeString actual = StringIncrementor.diff(input, input);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_singleCharacterDifference() {
 String a = "abcdefg";
 String b = "abcdefh";
 RelativeString expected = new StringLink(4, 4, 1).addLink(new StringLink(7, 7, 1));
 RelativeString actual = StringIncrementor.diff(a, b);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_multipleCharacterDifferences() {
 String a = "abcdefg";
 String b = "abcdhijklm";
 RelativeString expected = new StringLink(3, 3, 1).addLink(new StringLink(6, 6, 3)).addLink(new StringLink(9, 9, 4));
 RelativeString actual = StringIncrementor.diff(a, b);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_verboseMode() {
 String a = "abcdefg";
 String b = "abcdhijklm";
 String verboseOutput = "@ 33.33% Adding 3 absolute Bytes from pos#0\n" +
 "@ 66.67% Adding Link: StringLink(3, 3, 1)\n" +
 "@ 75.00% Adding 3 absolute Bytes from pos#3\n" +
 "@ 93.33% Adding 4 absolute Bytes from pos#6\n" +
 "@ 97.50% Adding Link: StringLink(6, 6, 3)\n" +
 "@ 100.00% Adding 4 absolute Bytes from pos#9\n";
 System.out = new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String s) {
 assertEquals(verboseOutput, s);
 }
 };
 String result = StringIncrementor.diff(a, b);
 assertNull(result);
 }

 @Test
 public void testPatch_emptyStringAndEmptyRelativeString() {
 String input = "testString";
 RelativeString r = new RelativeString();
 String actual = StringIncrementor.patch(input, r);
 assertEquals(input, actual);
 }

 @Test
 public void testPatch_singleCharacterDifference() {
 String a = "abcdefg";
 String expected = "abcdefh";
 RelativeString r = new StringLink(4, 4, 1).addLink(new StringLink(7, 7, 1));
 String actual = StringIncrementor.patch(a, r);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_multipleCharacterDifferences() {
 String a = "abcdefg";
 String expected = "abcdhijklm";
 RelativeString r = new StringLink(3, 3, 1).addLink(new StringLink(6, 6, 3)).addLink(new StringLink(9, 9, 4));
 String actual = StringIncrementor.patch(a, r);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_verboseMode() {
 String a = "abcdefg";
 String expected = "abcdhijklm";
 RelativeString r = new StringLink(3, 3, 1).addLink(new StringLink(6, 6, 3)).addLink(new StringLink(9, 9, 4));
 System.out = new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String s) {
 // Empty output for verbose mode
 }
 };
 String actual = StringIncrementor.patch(a, r, true);
 assertEquals(expected, actual);
 }

}