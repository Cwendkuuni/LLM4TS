package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 private static final String STRING_A = "The quick brown fox jumps over the lazy dog.";
 private static final String STRING_B = "The quick blue fox jumps over the lazy dog.";
 private static final String STRING_C = "The quick brown fox jumps over the lazy cat.";

 @Test
 public void testDiff_EqualStrings() {
     RelativeString result = StringIncrementor.diff(STRING_A, STRING_A);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testDiff_EmptyStrings() {
     RelativeString result = StringIncrementor.diff("", "");
     assertTrue(result.isEmpty());
 }

 @Test
 public void testDiff_NullStrings() {
     try {
         StringIncrementor.diff(null, STRING_B);
         fail("Exception expected");
     } catch (NullPointerException e) {
         // Expected
     }

     try {
         StringIncrementor.diff(STRING_A, null);
         fail("Exception expected");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 public void testDiff_EmptyStringBefore() {
     RelativeString result = StringIncrementor.diff("", STRING_A);
     assertEquals(STRING_A, result.getAbsolute());
 }

 @Test
 public void testDiff_EmptyStringAfter() {
     RelativeString result = StringIncrementor.diff(STRING_A, "");
     assertEquals(STRING_A, result.getAbsolute());
 }

 @Test
 public void testDiff_SingleCharacterChange() {
     RelativeString result = StringIncrementor.diff(STRING_A, STRING_B);
     assertEquals(1, result.linksCount());
     StringLink link = result.getLink(0);
     assertEquals(5, link.getPosOrig());
     assertEquals(6, link.getPosNew());
     assertEquals(1, link.getLen());
     assertEquals('b', link.getChar(0));
 }

 @Test
 public void testDiff_MultipleCharacterChange() {
     RelativeString result = StringIncrementor.diff(STRING_A, STRING_C);
     assertEquals(2, result.linksCount());
     StringLink link1 = result.getLink(0);
     assertEquals(5, link1.getPosOrig());
     assertEquals(6, link1.getPosNew());
     assertEquals(1, link1.getLen());
     assertEquals('c', link1.getChar(0));

     StringLink link2 = result.getLink(1);
     assertEquals(15, link2.getPosOrig());
     assertEquals(16, link2.getPosNew());
     assertEquals(1, link2.getLen());
     assertEquals('c', link2.getChar(0));
 }

 @Test
 public void testDiff_Verbose() {
     boolean verbose = true;
     RelativeString result = StringIncrementor.diff(STRING_A, STRING_C, verbose);
     assertEquals(2, result.linksCount());
     StringLink link1 = result.getLink(0);
     assertEquals(5, link1.getPosOrig());
     assertEquals(6, link1.getPosNew());
     assertEquals(1, link1.getLen());
     assertEquals('c', link1.getChar(0));

     StringLink link2 = result.getLink(1);
     assertEquals(15, link2.getPosOrig());
     assertEquals(16, link2.getPosNew());
     assertEquals(1, link2.getLen());
     assertEquals('c', link2.getChar(0));

     assertTrue(result.getAbsolute().contains("lazy cat"));
 }

 @Test
 public void testPatch_EmptyString() {
     String result = StringIncrementor.patch("", new RelativeString());
     assertEquals("", result);
 }

 @Test
 public void testPatch_SingleCharacterChange() {
     String result = StringIncrementor.patch(STRING_A, new RelativeString(new StringLink(5, 6, 1), new StringLink(15, 16, 1)));
     assertEquals(STRING_C, result);
 }

 @Test
 public void testPatch_MultipleCharacterChange() {
     String result = StringIncrementor.patch(STRING_A, new RelativeString(new StringLink(5, 6, 1), new StringLink(15, 16, 1), new StringLink(16, 17, 1), new StringLink(17, 18, 1)));
     assertEquals(STRING_C, result);
 }

 @Test
 public void testPatch_Verbose() {
     boolean verbose = true;
     String result = StringIncrementor.patch(STRING_A, new RelativeString(new StringLink(5, 6, 1), new StringLink(15, 16, 1), new StringLink(16, 17, 1), new StringLink(17, 18, 1)), verbose);
     assertEquals(STRING_C, result);
 }
}