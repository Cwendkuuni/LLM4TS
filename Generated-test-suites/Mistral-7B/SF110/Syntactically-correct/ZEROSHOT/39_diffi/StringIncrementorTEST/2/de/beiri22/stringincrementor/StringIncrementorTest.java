package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiff_sameStrings_returnsEmptyRelativeString() {
 String a = "test";
 String b = "test";
 RelativeString result = StringIncrementor.diff(a, b);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testDiff_simpleDifference_returnsCorrectRelativeString() {
 String a = "abcd";
 String b = "efgh";
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expected = new RelativeString();
 StringLink link1 = new StringLink(3, 1, 2);
 StringLink link2 = new StringLink(-1, 4, 1);
 expected.addLink(link1);
 expected.addLink(link2);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_complexDifference_returnsCorrectRelativeString() {
 String a = "abcdabcd";
 String b = "efghijhgfe";
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expected = new RelativeString();
 StringLink link1 = new StringLink(3, 1, 2);
 StringLink link2 = new StringLink(8, 4, 4);
 StringLink link3 = new StringLink(12, 9, 3);
 StringLink link4 = new StringLink(-1, 13, 1);
 expected.addLink(link1);
 expected.addLink(link2);
 expected.addLink(link3);
 expected.addLink(link4);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullInputs_throwsIllegalArgumentException() {
 StringIncrementor.diff(null, "test");
 StringIncrementor.diff("test", null);
 }

 @Test
 public void testPatch_emptyRelativeString_returnsOriginalString() {
 String a = "test";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals(a, result);
 }

 @Test
 public void testPatch_simpleDifference_returnsCorrectPatchedString() {
 String a = "abcdabcd";
 RelativeString r = new RelativeString();
 StringLink link1 = new StringLink(3, 1, 2);
 r.addLink(link1);
 String result = StringIncrementor.patch(a, r);
 String expected = "efcdefcd";
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_complexDifference_returnsCorrectPatchedString() {
 String a = "abcdabcd";
 RelativeString r = new RelativeString();
 StringLink link1 = new StringLink(3, 1, 2);
 StringLink link2 = new StringLink(8, 4, 4);
 StringLink link3 = new StringLink(12, 9, 3);
 StringLink link4 = new StringLink(-1, 13, 1);
 r.addLink(link1);
 r.addLink(link2);
 r.addLink(link3);
 r.addLink(link4);
 String result = StringIncrementor.patch(a, r);
 String expected = "efghijhgfe";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_nullInputs_throwsIllegalArgumentException() {
 StringIncrementor.patch(null, "test");
 StringIncrementor.patch("test", null);
 }
}