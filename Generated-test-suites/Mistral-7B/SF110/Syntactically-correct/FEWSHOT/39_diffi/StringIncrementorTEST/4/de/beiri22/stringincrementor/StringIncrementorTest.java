package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString rs = StringIncrementor.diff("", "");
 assertNotNull(rs);
 assertEquals("", rs.getAbsolute());
 assertEquals(0, rs.linksCount());
 }

 @Test
 public void testDiffIdenticalStrings() {
 RelativeString rs = StringIncrementor.diff("foo", "foo");
 assertNotNull(rs);
 assertEquals("", rs.getAbsolute());
 assertEquals(0, rs.linksCount());
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 RelativeString rs = StringIncrementor.diff("foo", "bar");
 assertNotNull(rs);
 assertFalse(rs.getAbsolute().isEmpty());
 assertEquals(1, rs.linksCount());
 }

 @Test
 public void testDiffMultipleCharacterDifferences() {
 RelativeString rs = StringIncrementor.diff("fooabcbaf", "foobadabgaf");
 assertNotNull(rs);
 assertFalse(rs.getAbsolute().isEmpty());
 assertEquals(3, rs.linksCount());
 }

 @Test
 public void testDiffWithVerboseMode() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "lmnopqrstuvwxyzabcdefghijklm";
 RelativeString rs = StringIncrementor.diff(a, b, true);
 assertNotNull(rs);
 assertFalse(rs.getAbsolute().isEmpty());
 assertEquals(13, rs.linksCount());
 }

 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString rs = new RelativeString();
 String result = StringIncrementor.patch(a, rs);
 assertEquals("", result);
 }

 @Test
 public void testPatchIdentity() {
 String a = "foo";
 RelativeString rs = new RelativeString();
 String result = StringIncrementor.patch(a, rs);
 assertEquals(a, result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "foo";
 RelativeString rs = new StringLink(0, 0, 1);
 rs.setLinkType("add");
 String result = StringIncrementor.patch(a, rs);
 assertEquals("foob", result);
 }

 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "fooabcbaf";
 RelativeString rs = new RelativeString();
 rs.addLink(new StringLink(3, 3, 3));
 rs.addLink(new StringLink(8, 8, 1));
 rs.addLink(new StringLink(10, 10, 2));
 String result = StringIncrementor.patch(a, rs);
 assertEquals("foobacdabgaf", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchInvalidLink() {
 String a = "foo";
 RelativeString rs = new RelativeString();
 rs.addLink(new StringLink(-1, 0, 1));
 String result = StringIncrementor.patch(a, rs);
 }
}