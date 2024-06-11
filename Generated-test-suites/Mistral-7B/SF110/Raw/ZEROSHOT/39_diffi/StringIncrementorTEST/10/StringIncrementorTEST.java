package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

 @Test
 public void testDiff() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy dog";

 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(5, 5, 3));
 expected.addLink(new StringLink(10, 10, 3));
 expected.setAbsolute("red Fox jumps over the lazy dog");

 RelativeString result = StringIncrementor.diff(a, b, false);

 assertEquals(expected.getLinks().length, result.linksCount());
 for (int i = 0; i < result.linksCount(); i++) {
 StringLink stringLink = result.getLink(i);
 StringLink expectedLink = expected.getLink(i);
 assertEquals(expectedLink.getPosNew(), stringLink.getPosNew());
 assertEquals(expectedLink.getLen(), stringLink.getLen());
 assertEquals(expectedLink.getPosOrig(), stringLink.getPosOrig());
 }
 assertEquals(expected.getAbsolute(), result.getAbsolute());
 }

 @Test
 public void testDiff_verbose_true() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy dog";

 System.out.println("Testing StringIncrementor.diff(a = \"" + a + "\", b = \"" + b + "\")");

 RelativeString result = StringIncrementor.diff(a, b, true);

 assertEquals(result.getLinks().length, 2);
 for (int i = 0; i < result.linksCount(); i++) {
 StringLink link = result.getLink(i);
 System.out.println("Link [" + i + "]: posNew = " + link.getPosNew() + ", len = " + link.getLen() + ", posOrig = " + link.getPosOrig());
 }
 assertEquals(result.getAbsolute(), "red Fox jumps over the lazy dog");
 }

 @Test
 public void testDiff_verbose_false() {
 String a = "The quick brown fox jumps over the lazy dog";
 String b = "The quick red fox jumps over the lazy dog";

 StringIncrementor.diff(a, b, false); // no output
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_stringIsNull() {
 StringIncrementor.diff(null, "The quick brown fox jumps over the lazy dog");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_string2IsNull() {
 StringIncrementor.diff("The quick brown fox jumps over the lazy dog", null);
 }

 @Test
 public void testPatch() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patch = "red Fox jumps over the lazy dog";

 String expected = "The quick red fox jumps over the lazy dog";

 String result = StringIncrementor.patch(a, StringIncrementor.diff(a, patch, false));

 assertEquals(expected, result);
 }

 @Test
 public void testPatch_verbose_true() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patch = "red Fox jumps over the lazy dog";

 System.out.println("Testing StringIncrementor.patch(a = \"" + a + "\", patch = \"" + patch + "\")");

 String result = StringIncrementor.patch(a, StringIncrementor.diff(a, patch, true), true);

 assertEquals(expected, result);
 }

 @Test
 public void testPatch_verbose_false() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patch = "red Fox jumps over the lazy dog";

 StringIncrementor.patch(a, StringIncrementor.diff(a, patch, false), false); // no output
 }

 @Test
 public void testPatch_stringIsNull() {
 String a = null;
 String patch = "red Fox jumps over the lazy dog";

 try {
 StringIncrementor.patch(a, StringIncrementor.diff(a, patch, false), false);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPatch_patchIsNull() {
 String a = "The quick brown fox jumps over the lazy dog";
 String patch = null;

 String result = StringIncrementor.patch(a, StringIncrementor.diff(a, patch, false), false);

 assertEquals(a, result);
 }
}