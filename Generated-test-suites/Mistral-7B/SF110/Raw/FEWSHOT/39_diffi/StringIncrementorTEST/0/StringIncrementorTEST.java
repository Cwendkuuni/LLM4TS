package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.Relativestring.*;
import de.beiri22.stringincrementor.StringIncrementor.*;
import java.util.LinkedList;
import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString rs = diff("", "", false);
 assertEquals(new RelativeString(), rs);
 }

 @Test
 public void testDiffIdenticalStrings() {
 RelativeString rs = diff("abcdef", "abcdef", false);
 assertEquals(new RelativeString(), rs);
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 RelativeString rs = diff("abcdef", "abcdgh", false);
 StringLink link = rs.getLink(0);
 assertEquals("de", link.getString1());
 assertEquals(2, link.getLen());
 assertEquals(3, link.getPosNew());
 assertEquals(4, link.getPosOrig());
 }

 @Test
 public void testDiffMultipleCharacterDifferences() {
 RelativeString rs = diff("abcdef", "abcdghij", false);
 assertEquals(2, rs.linksCount());
 StringLink link1 = rs.getLink(0);
 StringLink link2 = rs.getLink(1);
 assertEquals("de", link1.getString1());
 assertEquals(2, link1.getLen());
 assertEquals(3, link1.getPosNew());
 assertEquals(4, link1.getPosOrig());
 assertEquals("ghij", link2.getString1());
 assertEquals(5, link2.getLen());
 assertEquals(6, link2.getPosNew());
 assertEquals(7, link2.getPosOrig());
 }

 @Test
 public void testDiffLongStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.";
 RelativeString rs = diff(a, b, false);
 assertEquals(1, rs.linksCount());
 StringLink link = rs.getLink(0);
 assertEquals(a.substring(a.length() - 10), link.getString1());
 assertEquals(10, link.getLen());
 assertEquals(b.length(), link.getPosNew());
 assertEquals(a.length(), link.getPosOrig());
 }

 @Test
 public void testDiffVerbose() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.";
 RelativeString rs = diff(a, b, true);
 String expected = "Link: de@ 62.96%\nLink: exercitation ullamco laboris@ 94.12%\n";
 StringBuilder actual = new StringBuilder();
 PrintStream printStream = new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String x) {
 actual.append(x);
 }
 };
 System.setOut(printStream);
 diff(a, b, true);
 System.setOut(System.out);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString rs = diff("abcdef", "abcdgh");
 String patched = patch(a, rs);
 assertEquals("", patched);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "abcdef";
 RelativeString rs = diff("abcdef", "abcdgh");
 String patched = patch(a, rs);
 assertEquals("cdgh", patched);
 }

 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "abcdef";
 RelativeString rs = diff("abcdef", "abcdghij");
 String patched = patch(a, rs);
 assertEquals("cdghij", patched);
 }

 @Test
 public void testPatchLongStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.";
 RelativeString rs = diff(a, b);
 String patched = patch(a, rs);
 assertEquals(b, patched);
 }

 @Test
 public void testPatchVerbose() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.";
 String expected = "Adding 48 absolute Bytes from pos#0\nAdding Link: de@ 62.96%\nAdding 12 absolute Bytes from pos#1\nAdding Link: exercitation ullamco laboris@ 94.12%\n";
 StringBuilder actual = new StringBuilder();
 PrintStream printStream = new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String x) {
 actual.append(x);
 }
 };
 System.setOut(printStream);
 patch(a, rs, true);
 System.setOut(System.out);
 assertEquals(expected, actual.toString());
 }
}