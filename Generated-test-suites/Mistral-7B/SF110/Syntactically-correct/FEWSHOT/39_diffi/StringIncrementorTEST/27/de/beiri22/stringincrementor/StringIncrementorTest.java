package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import java.util.Random;

import org.junit.Test;

public class StringIncrementorTest {

 private static final int MAX_LENGTH = 100;
 private static final int MAX_TEST_ITERATIONS = 10;

 @Test
 public void testDiff() {
 for (int i = 0; i < MAX_TEST_ITERATIONS; ++i) {
 String a = generateRandomString(MAX_LENGTH);
 String b = generateRandomString(MAX_LENGTH);
 RelativeString rs = StringIncrementor.diff(a, b);
 assertEquals(a.length(), rs.getAbsolute().length() + rs.linksCount() * 13);
 }
 }

 @Test
 public void testDiff_SameString() {
 String a = "foo";
 RelativeString rs = StringIncrementor.diff(a, a);
 assertEquals(0, rs.linksCount());
 assertEquals("", rs.getAbsolute());
 }

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString rs = StringIncrementor.diff(a, b);
 assertEquals(0, rs.linksCount());
 assertEquals("", rs.getAbsolute());
 }

 @Test
 public void testDiff_SubStrings() {
 String a = "abcdefghij";
 String b = "ijkabcdefghij";
 RelativeString rs = StringIncrementor.diff(a, b);
 assertEquals(2, rs.linksCount());
 assertEquals("ij", rs.getLink(0).getStringOld());
 assertEquals(10, rs.getLink(0).getPosOrig());
 assertEquals(12, rs.getLink(0).getPosNew());
 assertEquals(1, rs.getLink(0).getLen());
 assertEquals("k", rs.getLink(1).getStringOld());
 assertEquals(0, rs.getLink(1).getPosOrig());
 assertEquals(11, rs.getLink(1).getPosNew());
 assertEquals(1, rs.getLink(1).getLen());
 }

 @Test
 public void testDiff_Verbose() {
 String a = "abcdefghij";
 String b = "ijkabcdefghij";
 StringIncrementor.diff(a, b, true);
 }

 @Test
 public void testPatch() {
 for (int i = 0; i < MAX_TEST_ITERATIONS; ++i) {
 String a = generateRandomString(MAX_LENGTH);
 RelativeString rs = StringIncrementor.diff(a, generateRandomString(MAX_LENGTH));
 String patched = StringIncrementor.patch(a, rs);
 assertEquals(a, patched);
 }
 }

 @Test
 public void testPatch_EmptyString() {
 String a = "";
 RelativeString rs = StringIncrementor.diff("", "");
 String patched = StringIncrementor.patch(a, rs);
 assertEquals("", patched);
 }

 @Test
 public void testPatch_SimpleString() {
 String a = "foo";
 RelativeString rs = StringIncrementor.diff(a, "bar");
 String patched = StringIncrementor.patch(a, rs);
 assertEquals("bar", patched);
 }

 @Test
 public void testPatch_ComplexString() {
 String a = "abcdefghij";
 String b = "ijkabcdefghij";
 RelativeString rs = StringIncrementor.diff(a, b);
 String patched = StringIncrementor.patch(a, rs);
 assertEquals(b, patched);
 }

 private static String generateRandomString(int length) {
 Random random = new Random();
 StringBuilder result = new StringBuilder(length);
 for (int i = 0; i < length; ++i) {
 result.append((char) (random.nextInt(128) + 32));
 }
 return result.toString();
 }
}