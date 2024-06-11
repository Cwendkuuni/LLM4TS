package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString result = StringIncrementor.diff("", "");
 assertEquals("", result.getAbsolute());
 assertNull(result.linksCount());
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "foo";
 RelativeString result = StringIncrementor.diff(a, a);
 assertEquals("", result.getAbsolute());
 assertNull(result.linksCount());
 }

 @Test
 public void testDiffSingleCharacterChange() {
 String a = "foo";
 String b = "bar";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("b", result.getAbsolute());
 assertEquals(1, result.linksCount());
 }

 @Test
 public void testDiffMultipleCharacterChanges() {
 String a = "foo";
 String b = "bar";
 String c = "baz";
 RelativeString result = StringIncrementor.diff(a, c);
 assertEquals("baz", result.getAbsolute());
 assertEquals(2, result.linksCount());
 }

 @Test
 public void testDiffMultipleCharacterDeletions() {
 String a = "foo";
 String b = "o";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("", result.getAbsolute());
 assertEquals(2, result.linksCount());
 }

 @Test
 public void testDiffMultipleCharacterInsertions() {
 String a = "foo";
 String b = "foo bar";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(" bar", result.getAbsolute());
 assertEquals(1, result.linksCount());
 }

 @Test
 public void testDiffMultipleCharacterSubstitutions() {
 String a = "foo";
 String b = "bar";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("b", result.getAbsolute());
 assertEquals(1, result.linksCount());

 String c = "foo bar baz";
 RelativeString result2 = StringIncrementor.diff(a, c);
 assertEquals(" bar baz", result2.getAbsolute());
 assertEquals(2, result2.linksCount());
 }

 @Test
 public void testDiffWithVerboseMode() {
 String a = "foo";
 String b = "bar";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals("b", result.getAbsolute());
 assertEquals(1, result.linksCount());

 StringBuilder out = new StringBuilder();
 StringIncrementor.diff(a, b, true, out);
 String expectedOut = "Adding 1 absolute Bytes from pos#0\n" +
         "Adding Link: StringLink(posOrig=3, posNew=3, len=1)\n" +
         "Adding 1 absolute Bytes from pos#1\n";
 assertEquals(expectedOut, out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffWithNullStrings() {
 StringIncrementor.diff(null, null);
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 assertEquals("", result);
 }

 @Test
 public void testPatchSingleCharacterChange() {
 String a = "foo";
 String b = "bar";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 1));
 String result = StringIncrementor.patch(a, r);
 assertEquals("bar", result);
 }

 @Test
 public void testPatchMultipleCharacterChanges() {
 String a = "foo";
 String b = "bar";
 String c = "baz";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 1));
 r.addLink(new StringLink(6, 6, 2));
 String result = StringIncrementor.patch(a, r);
 assertEquals("baz", result);
 }

 @Test
 public void testPatchWithVerboseMode() {
 String a = "foo";
 String b = "bar";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 1));
 StringBuilder out = new StringBuilder();
 StringIncrementor.patch(a, r, true, out);
 String expectedOut = "Adding 1 absolute Bytes from pos#0\n" +
         "Adding Link: StringLink(posOrig=3, posNew=3, len=1)\n" +
         "Adding 1 absolute Bytes from pos#3\n" +
         "Adding Link: StringLink(posOrig=0, posNew=3, len=3)\n";
 assertEquals(expectedOut, out.toString());
 }

 @Test
 public void testPatchWithNegativeOffset() {
 String a = "foo";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(-1, 3, 1));
 String result = StringIncrementor.patch(a, r);
 assertNull(result);
 }

 @Test
 public void testPatchWithOffsetBeyondStringEnd() {
 String a = "foo";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, a.length(), 1));
 String result = StringIncrementor.patch(a, r);
 assertNull(result);
 }
}