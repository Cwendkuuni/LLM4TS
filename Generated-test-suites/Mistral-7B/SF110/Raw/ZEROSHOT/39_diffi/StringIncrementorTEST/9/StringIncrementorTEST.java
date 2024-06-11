package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiff() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";
 RelativeString result = StringIncrementor.diff(a, b);

 assertEquals(1, result.linksCount());
 StringLink link = result.getLink(0);
 assertEquals(11, link.getLen());
 assertEquals("red", link.getRelative());
 assertEquals(9, link.getPosNew());
 assertEquals(11, link.getPosOrig());
 assertEquals("fox", link.getSourceString().substring(link.getPosOrig(), link.getPosOrig() + link.getLen()));

 String expectedAbsolute = "The quick red fox jumps over the lazy dog.";
 assertEquals(expectedAbsolute, result.getAbsolute());
 }

 @Test
 public void testDiff_verbose() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick red fox jumps over the lazy dog.";

 System.out.println("Testing diff(a, b) with verbose mode...");
 RelativeString result = StringIncrementor.diff(a, b, true);

 assertEquals(1, result.linksCount());
 StringLink link = result.getLink(0);
 assertEquals(11, link.getLen());
 assertEquals("red", link.getRelative());
 assertEquals(9, link.getPosNew());
 assertEquals(11, link.getPosOrig());
 assertEquals("fox", link.getSourceString().substring(link.getPosOrig(), link.getPosOrig() + link.getLen()));

 String expectedAbsolute = "The quick red fox jumps over the lazy dog.";
 assertEquals(expectedAbsolute, result.getAbsolute());

 System.out.println(result);
 }

 @Test
 public void testDiff_sameStrings() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick brown fox jumps over the lazy dog.";

 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(0, result.linksCount());
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiff_emptyString() {
 String a = "";
 String b = "The quick brown fox jumps over the lazy dog.";

 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(0, result.linksCount());
 assertEquals("The quick brown fox jumps over the lazy dog.", result.getAbsolute());
 }

 @Test
 public void testDiff_differentLengths() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String b = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the lazy dog.";

 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(1, result.linksCount());
 StringLink link = result.getLink(0);
 assertEquals(44, link.getLen());
 assertEquals(" The quick red fox jumps over the lazy dog.", link.getRelative());
 assertEquals(11, link.getPosNew());
 assertEquals(0, link.getPosOrig());
 assertEquals("The quick brown fox jumps over the lazy dog.", link.getSourceString());

 String expectedAbsolute = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the lazy dog.";
 assertEquals(expectedAbsolute, result.getAbsolute());
 }

 @Test
 public void testPatch() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String patch = "The quick red fox jumps over the lazy dog.";
 RelativeString relativePatch = StringIncrementor.diff(a, patch);
 String result = StringIncrementor.patch(a, relativePatch);

 assertEquals("The quick red fox jumps over the lazy dog.", result);
 }

 @Test
 public void testPatch_verbose() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String patch = "The quick red fox jumps over the lazy dog.";

 System.out.println("Testing patch(a, relativePatch) with verbose mode...");
 RelativeString relativePatch = StringIncrementor.diff(a, patch);
 String result = StringIncrementor.patch(a, relativePatch, true);

 assertEquals("The quick red fox jumps over the lazy dog.", result);

 System.out.println(result);
 }

 @Test
 public void testPatch_emptyString() {
 String a = "";String patch = "The quick brown fox jumps over the lazy dog.";
 RelativeString relativePatch = StringIncrementor.diff(a, patch);
 String result = StringIncrementor.patch(a, relativePatch);

 assertEquals("The quick brown fox jumps over the lazy dog.", result);
 }

 @Test
 public void testPatch_differentStrings_sameLength() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String patch = "The quick red fox jumps over the lazy dog.";

 RelativeString relativePatch = StringIncrementor.diff(a, patch);
 String result = StringIncrementor.patch(a, relativePatch);

 assertEquals("The quick red fox jumps over the lazy dog.", result);
 }

 @Test
 public void testPatch_differentStrings_differentLengths() {
 String a = "The quick brown fox jumps over the lazy dog.";
 String patch = "The quick red fox jumps over the lazy dog. The quick green fox jumps over the lazy cat.";

 RelativeString relativePatch = StringIncrementor.diff(a, patch);
 String result = StringIncrementor.patch(a, relativePatch);

 assertEquals("The quick red fox jumps over the lazy dog. The quick green fox jumps over the lazy cat.", result);
 }
}