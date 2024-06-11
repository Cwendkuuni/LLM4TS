package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 private static final String BASIC_STRING_A = "The quick brown fox jumps over the lazy dog.";
 private static final String BASIC_STRING_B = "The quick red fox jumps over the lazy cat.";

 @Test
 public void testStringIncrementor_diff_basic() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 RelativeString relativeString = stringIncrementor.diff(BASIC_STRING_A, BASIC_STRING_B);

 assertEquals("Expected 1 link, received: " + relativeString.linksCount() + ".", 1, relativeString.linksCount());
 StringLink link = relativeString.getLink(0);
 assertEquals("Expected link from pos#1 to pos#15, received: [" + link.getPosOrig() + ", " + link.getPosNew() + "].", 1, link.getPosOrig());
 assertEquals("Expected link from pos#1 to pos#15, received: [" + link.getPosNew() + ", " + link.getLen() + "].", 15, link.getPosNew());
 assertEquals("Expected link length: [" + link.getLen() + "].", 12, link.getLen());
 assertEquals("Expected link content: [" + new String(link.getContent().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8) + "].", "brown", new String(link.getContent().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
 }

 @Test
 public void testStringIncrementor_diff_verbose() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 System.setOut(new PrintStream(new ByteArrayOutputStream()));
 RelativeString relativeString = stringIncrementor.diff(BASIC_STRING_A, BASIC_STRING_B, true);
 System.setOut(System.out);

 assertEquals("Expected 1 link, received: " + relativeString.linksCount() + ".", 1, relativeString.linksCount());
 StringLink link = relativeString.getLink(0);
 assertEquals("Expected link from pos#1 to pos#15, received: [" + link.getPosOrig() + ", " + link.getPosNew() + "].", 1, link.getPosOrig());
 assertEquals("Expected link from pos#1 to pos#15, received: [" + link.getPosNew() + ", " + link.getLen() + "].", 15, link.getPosNew());
 assertEquals("Expected link length: [" + link.getLen() + "].", 12, link.getLen());
 assertEquals("Expected link content: [" + new String(link.getContent().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8) + "].", "brown", new String(link.getContent().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringIncrementor_diff_nullStringA_throwsException() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 try {
 stringIncrementor.diff(null, BASIC_STRING_B);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringIncrementor_diff_nullStringB_throwsException() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 try {
 stringIncrementor.diff(BASIC_STRING_A, null);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStringIncrementor_patch_basic() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String patchedString = stringIncrementor.patch(BASIC_STRING_A, relativeString);
 assertEquals("Expected patched string: [" + BASIC_STRING_A + "].", BASIC_STRING_A, BASIC_STRING_A);
 assertEquals("Expected patched string: [" + patchedString + "].", "The quick red fox jumps over the lazy cat.", patchedString);
 }

 @Test
 public void testStringIncrementor_patch_verbose() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 System.setOut(new PrintStream(new ByteArrayOutputStream()));
 String patchedString = stringIncrementor.patch(BASIC_STRING_A, relativeString, true);
 System.setOut(System.out);
 assertEquals("Expected patched string: [" + BASIC_STRING_A + "].", BASIC_STRING_A, BASIC_STRING_A);
 assertEquals("Expected patched string: [" + patchedString + "].", "The quick red fox jumps over the lazy cat.", patchedString);
 }

 @Test
 public void testStringIncrementor_patch_nullRelativeString_throwsException() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 try {
 stringIncrementor.patch(BASIC_STRING_A, null);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}