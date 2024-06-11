package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilderExt;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertNotNull(result);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdefghijklmnopqrstuvwxyz";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffOneCharacterDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdefghijklmnopqrstuvwxyab";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertNotNull(result);
 StringLink link = result.getLink(0);
 assertEquals("a@ 50.0%", link.toString());
 }

 @Test
 public void testDiffMultipleDifferences() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdghijklmnopqrstuvxyzab";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertNotNull(result);
 StringLink link1 = result.getLink(0);
 assertEquals("a@ 4.736842105263158%", link1.toString());
 StringLink link2 = result.getLink(1);
 assertEquals("b@ 49.19230769473685%", link2.toString());
 }

 @Test
 public void testDiffWithVerboseOutput() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdghijklmnopqrstuvxyzab";
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expectedOutput = "Creating index...\nSearching links...\na@ 4.736842105263158% @ 4.736842105263158%\nb@ 49.19230769473685% @ 49.19230769473685%\n";
 StringBuilder actualOutput = new StringBuilder();
 try (PrintStream out = new PrintStream(new ByteArrayOutputStream())) {
 System.setOut(out);
 StringIncrementor.diff(a, b, true);
 }
 String output = actualOutput.toString();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String patched = StringIncrementor.patch(a, r);
 assertEquals("", patched);
 }

 @Test
 public void testPatchNoDifferences() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString r = new RelativeString();
 String patched = StringIncrementor.patch(a, r);
 assertEquals("abcdefghijklmnopqrstuvwxyz", patched);
 }

 @Test
 public void testPatchSingleDifference() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString r = new StringLink(5, 5, 1, "a");
 String patched = StringIncrementor.patch(a, r, false);
 assertEquals("ababcdefghijklmnopqrstuvwxyz", patched);
 }

 @Test
 public void testPatchMultipleDifferences() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdghijklmnopqrstuvxyzab";
 RelativeString r = StringIncrementor.diff(a, b, false);
 String patched = StringIncrementor.patch(a, r, false);
 assertEquals("abcdghijklmnopqrstuvxyzab", patched);
 }

 @Test
 public void testPatchWithVerboseOutput() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 String b = "abcdghijklmnopqrstuvxyzab";
 RelativeString r = StringIncrementor.diff(a, b, true);
 StringBuilder actualOutput = new StringBuilder();
 try (PrintStream out = new PrintStream(new ByteArrayOutputStream())) {
 System.setOut(out);
 StringIncrementor.patch(a, r, true);
 }
 String output = actualOutput.toString();
 String expectedOutput = "Adding Link: a@ 4.736842105263158% from pos#0\nAdding absolute Bytes: 1\nAdding Link: b@ 49.19230769473685% from pos#1\nAdding absolute Bytes: 1\n";
 assertEquals(expectedOutput, output);
 }
}