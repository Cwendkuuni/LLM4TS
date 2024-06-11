package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString rs1 = StringIncrementor.diff("", "");
 assertEquals(new RelativeString(), rs1);
 }

 @Test
 public void testDiffSameStrings() {
 RelativeString rs1 = StringIncrementor.diff("foo", "foo");
 assertEquals(new RelativeString(), rs1);
 }

 @Test
 public void testDiffDifferentStringsWithSameLength() {
 RelativeString rs1 = StringIncrementor.diff("foo", "bar");
 assertEquals(new RelativeString(new StringLink(3, 3, 1), new StringLink(4, 4, 1)), rs1);
 }

 @Test
 public void testDiffDifferentStringsWithDifferentLengths() {
 RelativeString rs1 = StringIncrementor.diff("foo", "foobar");
 assertEquals(new RelativeString(new StringLink(3, 3, 1), new StringLink(0, 4, 1), new StringLink(5, 5, 2)), rs1);
 }

 @Test
 public void testDiffWithVerboseMode() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdexyzabcdefghijklmnopqrstuvwz";
 RelativeString rs1 = StringIncrementor.diff(a, b, true);
 String expected = "Link: [0, 6, 1] @ 0%   Link: [12, 15, 3] @ 92.3069%";
 StringBuilder sb = new StringBuilder();
 for (StringLink link : rs1.getLinks()) {
 sb.append(link.toString() + " ");
 }
 assertTrue(sb.toString().contains(expected));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStrings() {
 StringIncrementor.diff(null, "foobar");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullSecondString() {
 StringIncrementor.diff("foobar", null);
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString rs1 = new RelativeString();
 String patched = StringIncrementor.patch(a, rs1);
 assertEquals("", patched);
 }

 @Test
 public void testPatchEmptyRelativeString() {
 String a = "foobar";
 RelativeString rs1 = new RelativeString();
 String patched = StringIncrementor.patch(a, rs1);
 assertEquals(a, patched);
 }

 @Test
 public void testPatchSimpleString() {
 String a = "foobar";
 RelativeString rs1 = new RelativeString(new StringLink(3, 3, 1));
 String patched = StringIncrementor.patch(a, rs1);
 assertEquals("foobar", patched);
 }

 @Test
 public void testPatchRelativeStringWithOneLink() {
 String a = "foobar";
 RelativeString rs1 = new RelativeString(new StringLink(3, 3, 1));
 String patched = StringIncrementor.patch(a, rs1);
 assertEquals("foobar", patched);
 }

 @Test
 public void testPatchRelativeStringWithMultipleLinks() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString rs1 = new RelativeString(new StringLink(3, 3, 1), new StringLink(12, 15, 3));
 String patched = StringIncrementor.patch(a, rs1);
 String expected = "abcdefghijklmnopqrstuvwxyz";
 assertEquals(expected, patched);
 }

 @Test
 public void testPatchRelativeStringWithMultipleLinksAndVerboseMode() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString rs1 = new RelativeString(new StringLink(3, 3, 1), new StringLink(12, 15, 3));
 String patched = StringIncrementor.patch(a, rs1, true);
 String expected = "Link: [0, 6, 1] @ 0%   Link: [12, 15, 3] @ 92.3069%";
 StringBuilder sb = new StringBuilder();for (StringLink link : rs1.getLinks()) {
 sb.append(link.toString() + " ");
 }
 assertTrue(sb.toString().contains(expected));
 assertEquals("abcdexyzabcdefghijklmnopqrstuvwz", patched);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullString() {
 String patched = StringIncrementor.patch(null, new RelativeString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullRelativeString() {
 String patched = StringIncrementor.patch("foobar", null);
 }
}