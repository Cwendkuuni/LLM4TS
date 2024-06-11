package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString rs = StringIncrementor.diff("", "");
 assertTrue(rs.isEqual(""));
 }

 @Test
 public void testDiffSameStrings() {
 RelativeString rs = StringIncrementor.diff("foo", "foo");
 assertTrue(rs.isEqual(""));
 }

 @Test
 public void testDiffOneCharacterDifference() {
 RelativeString rs = StringIncrementor.diff("foo", "bar");
 StringLink link = rs.getLink(0);
 assertEquals("foo", link.getOldString());
 assertEquals("bar", link.getNewString());
 assertEquals(1, link.getLength());
 assertEquals(3, rs.linksCount());
 }

 @Test
 public void testDiffMultipleDifferences() {
 RelativeString rs = StringIncrementor.diff("foo", "barfoo");
 StringLink link1 = rs.getLink(0);
 assertEquals("foo", link1.getOldString());
 assertEquals("bar", link1.getNewString());
 assertEquals(1, link1.getLength());

 StringLink link2 = rs.getLink(1);
 assertEquals("foo", link2.getOldString());
 assertEquals("foo", link2.getNewString());
 assertEquals(0, link2.getLength());

 assertEquals(3, rs.linksCount());
 }

 @Test
 public void testDiffMiddleOfString() {
 RelativeString rs = StringIncrementor.diff("abcdefg", "123abcdefg");
 StringLink link1 = rs.getLink(0);
 assertEquals("abcdefg", link1.getOldString());
 assertEquals("123abcdefg", link1.getNewString());
 assertEquals(6, link1.getLength());

 StringLink link2 = rs.getLink(1);
 assertEquals("abcdefg", link2.getOldString());
 assertEquals("abcdefg", link2.getNewString());
 assertEquals(0, link2.getLength());

 assertEquals(2, rs.linksCount());
 }

 @Test
 public void testDiffBeginningOfString() {
 RelativeString rs = StringIncrementor.diff("abcdefg", "ab123defg");
 StringLink link1 = rs.getLink(0);
 assertEquals("abcdefg", link1.getOldString());
 assertEquals("ab123defg", link1.getNewString());
 assertEquals(3, link1.getLength());

 assertEquals(1, rs.linksCount());
 }

 @Test
 public void testDiffEndOfString() {
 RelativeString rs = StringIncrementor.diff("abcdefg", "abcdefg123");
 StringLink link1 = rs.getLink(0);
 assertEquals("abcdefg", link1.getOldString());
 assertEquals("abcdefg123", link1.getNewString());
 assertEquals(3, link1.getLength());

 assertEquals(1, rs.linksCount());
 }

 @Test
 public void testDiffMultipleOccurrences() {
 RelativeString rs = StringIncrementor.diff("abcabcabc", "123abcabcabc");
 StringLink link1 = rs.getLink(0);
 assertEquals("abcabcabc", link1.getOldString());
 assertEquals("123abcabcabc", link1.getNewString());
 assertEquals(9, link1.getLength());

 StringLink link2 = rs.getLink(1);
 assertEquals("abc", link2.getOldString());
 assertEquals("abc", link2.getNewString());
 assertEquals(0, link2.getLength());

 assertEquals(3, rs.linksCount());
 }

 @Test
 public void testDiffVerbose() {
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String s) {
 System.out.println("TestConsole: " + s);
 }
 });
 String a = "abcdefg";
 String b = "123abcdefg";
 RelativeString rs = StringIncrementor.diff(a, b, true);
 assertEquals(2, rs.linksCount());
 StringLink link1 = rs.getLink(0);
 assertEquals("abcdefg", link1.getOldString());
 assertEquals("123abcdefg", link1.getNewString());
 assertEquals(3, link1.getLength());
 assertEquals(100.0, Math.round(link1.getPercentPos() * 100.0) / 10.0, 0.1);
 StringLink link2 = rs.getLink(1);
 assertEquals("abcdefg", link2.getOldString());
 assertEquals("abcdefg", link2.getNewString());
 assertEquals(0, link2.getLength());
 assertEquals(100.0, Math.round(link2.getPercentPos() * 100.0) / 10.0, 0.1);
 }

 @Test
 public void testPatchEmptyString() {
 String result = StringIncrementor.patch("", new RelativeString());
 assertEquals("", result);
 }

 @Test
 public void testPatchSingleLink() {
 String a = "foo";
 RelativeString rs = new RelativeString();
 rs.addLink(new StringLink(0, 0, 3, "foo"));
 String result = StringIncrementor.patch(a, rs);
 assertEquals("foo", result);
 }

 @Test
 public void testPatchMultipleLinks() {
 String a = "foo";
 RelativeString rs = new RelativeString();
 rs.addLink(new StringLink(0, 0, 3, "foo"));
 rs.addLink(new StringLink(3, 3, 3, "bar"));
 String result = StringIncrementor.patch(a, rs);
 assertEquals("foobar", result);
 }

 @Test
 public void testPatchVerbose() {
 System.setOut(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String s) {
 System.out.println("TestConsole: " + s);
 }
 });
 String a = "abcdefg";
 RelativeString rs = new RelativeString();
 rs.addLink(new StringLink(0, 0, 3, "123"));
 rs.addLink(new StringLink(6, 6, 2, "xyz"));
 String result = StringIncrementor.patch(a, rs, true);
 assertEquals("123abcdefgxyz", result);
 }
}