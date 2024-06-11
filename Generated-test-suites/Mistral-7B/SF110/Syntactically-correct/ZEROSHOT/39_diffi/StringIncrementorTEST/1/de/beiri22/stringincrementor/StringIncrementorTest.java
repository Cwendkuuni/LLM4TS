package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiff_EmptyStrings() {
 RelativeString result = StringIncrementor.diff("", "");
 assertFalse(result.linksCount() > 0);
 }

 @Test
 public void testDiff_IdenticalStrings() {
 RelativeString result = StringIncrementor.diff("abcdef", "abcdef");
 assertFalse(result.linksCount() > 0);
 }

 @Test
 public void testDiff_SameStringDifferentPosition() {
 RelativeString result = StringIncrementor.diff("abcdabcd", "cdabcdabcd");
 assertEquals(4, result.linksCount());
 StringLink[] links = result.getLinks();
 assertEquals("(5, 6, 2) @ 50.0%", links[0].toString());
 assertEquals("(6, 7, 1) @ 75.0%", links[1].toString());
 assertEquals("(12, 13, 1) @ 100.0%", links[2].toString());
 assertEquals("(13, 14, 1) @ 125.0%", links[3].toString());
 }

 @Test
 public void testDiff_DifferentStrings() {
 RelativeString result = StringIncrementor.diff("abcdabcd", "efghijklm");
 assertEquals(0, result.linksCount());
 }

 @Test
 public void testDiff_VerboseMode() {
 String a = "abc123def456";
 String b = "xyzabc123def456pqr";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals(2, result.linksCount());
 StringLink[] links = result.getLinks();
 assertEquals("(4, 4, 5) @ 25.0%", links[0].toString());
 assertEquals("(11, 11, 6) @ 75.0%", links[1].toString());
 }

 @Test
 public void testPatch_EmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("", result);
 }

 @Test
 public void testPatch_NoLinks() {
 String a = "abcdef";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("abcdef", result);
 }

 @Test
 public void testPatch_OneLink() {
 String a = "abc123def456";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 2, "xyz"));
 String result = StringIncrementor.patch(a, r);
 assertEquals("xyzabc123def456", result);
 }

 @Test
 public void testPatch_MultipleLinks() {
 String a = "abc123def456";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 2, "xyz"));
 r.addLink(new StringLink(11, 11, 6, "pqr"));
 String result = StringIncrementor.patch(a, r);
 assertEquals("xyzabc123defpqr456", result);
 }

 @Test
 public void testPatch_VerboseMode() {
 String a = "abc123def456";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 2, "xyz"));
 r.addLink(new StringLink(11, 11, 6, "pqr"));
 String result = StringIncrementor.patch(a, r, true);
 assertEquals("xyzabc123defpqr456", result);
 }

}