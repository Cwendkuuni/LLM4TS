package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilderExt;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiff_emptyStrings() {
 RelativeString expected = new RelativeString();
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_identicalStrings() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdefghijklmnopqrstuvwxyz";
 String expected = new String("");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_singleCharacterDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdeffghijklmnopqrstuvwxyz";
 RelativeString expected = new StringLink(4, 5, 2).addLink(new StringLink(11, 12, 2));
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_multipleCharacterDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdgijhklijlmnopqrstuvwxyz";
 String expected = new StringLink(3, 4, 2).addLink(new StringLink(6, 7, 2)).addLink(new StringLink(10, 11, 2));
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_verboseOutput() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdgijhklijlmnopqrstuvwxyz";
 StringBuilder outContent = new StringBuilder();
 PrintStream out = new ByteArrayOutputStream();
 System.setOut(out);
 RelativeString result = StringIncrementor.diff(a, b, true);
 String outString = out.toString(StandardCharsets.UTF_8);
 out.close();
 System.setOut(System.out);
 String expectedString = "Creating index...\nSearching links...\n@ 0%\nAdding Link: StringLink[posOrig=3, posNew=4, len=2]\n@ 33.333333333333336%\nAdding Link: StringLink[posOrig=10, posNew=11, len=2]\n";
 String expected = new StringLink(3, 4, 2).addLink(new StringLink(10, 11, 2));
 assertEquals(expected, result);
 assertEquals(expectedString, outString);
 }

 @Test
 public void testPatch_emptyString() {
 String a = "";
 RelativeString rs = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, rs, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_noLinks() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString rs = new RelativeString();
 String expected = "abcdefghijklmnopqrstuvwxyz";
 String result = StringIncrementor.patch(a, rs, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_singleCharacterDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdeffghijklmnopqrstuvwxyz";
 RelativeString rs = new StringLink(4, 5, 2).addLink(new StringLink(11, 12, 2));
 String expected = "abcdefghijklmnoeffghijklmnopqrstuvwxyz";
 String result = StringIncrementor.patch(a, rs, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_multipleCharacterDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdgijhklijlmnopqrstuvwxyz";
 RelativeString rs = new StringLink(3, 4, 2).addLink(new StringLink(6, 7, 2)).addLink(new StringLink(10, 11, 2));
 String expected = "abcdgijhklijlmnopqrstuvwxyz";
 String result = StringIncrementor.patch(a, rs, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_verboseOutput() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString rs = new StringLink(3, 4, 2).addLink(new StringLink(6, 7, 2)).addLink(new StringLink(10, 11, 2));
 StringBuilder outContent = new StringBuilder();
 PrintStream out = new ByteArrayOutputStream();
 System.setOut(out);
 String result = StringIncrementor.patch(a, rs, true);
 String outString = out.toString(StandardCharsets.UTF_8);
 out.close();
 System.setOut(System.out);
 String expectedString = "Adding abcdefghij from pos#0 to pos#6\nAdding gijhklijl from pos#9 to pos#16\n";
 String expected = "abcdefghijklmnopqrstuvwxyz".substring(0, 6) + "gijhklijl".substring(0, 8);
 assertEquals(expected, result);
 assertEquals(expectedString, outString);
 }
}