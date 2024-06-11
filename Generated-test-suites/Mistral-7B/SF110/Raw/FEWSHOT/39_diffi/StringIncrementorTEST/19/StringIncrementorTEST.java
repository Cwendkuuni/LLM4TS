package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString result = StringIncrementor.diff("", "");
 assertEquals("", result.getAbsolute());
 assertEquals(0, result.linksCount());
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "foo";
 RelativeString result = StringIncrementor.diff(a, a);
 assertEquals("", result.getAbsolute());
 assertEquals(0, result.linksCount());
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "foo";
 String b = "bar";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("bar(1)@ 33.3%", result.getAbsolute());
 assertEquals(1, result.linksCount());
 StringLink link = result.getLink(0);
 assertEquals(3, link.getLen());
 assertEquals(1, link.getPosNew());
 assertEquals(0, link.getPosOrig());
 }

 @Test
 public void testDiffMultipleCharacterDifferences() {
 String a = "foo";
 String b = "barbazqux";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("barbazqux(3)@ 33.3% bar(3)@ 66.7%", result.getAbsolute());
 assertEquals(2, result.linksCount());
 StringLink link1 = result.getLink(0);
 assertEquals(3, link1.getLen());
 assertEquals(0, link1.getPosNew());
 assertEquals(0, link1.getPosOrig());
 StringLink link2 = result.getLink(1);
 assertEquals(5, link2.getLen());
 assertEquals(3, link2.getPosNew());
 assertEquals(0, link2.getPosOrig());
 }

 @Test
 public void testDiffVerboseMode() {
 String a = "foo";
 String b = "barbazqux";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals("barbazqux(3)@ 33.3% bar(3)@ 66.7%", result.getAbsolute());
 assertEquals(2, result.linksCount());
 StringLink link1 = result.getLink(0);
 assertEquals(3, link1.getLen());
 assertEquals(0, link1.getPosNew());
 assertEquals(0, link1.getPosOrig());
 StringLink link2 = result.getLink(1);
 assertEquals(5, link2.getLen());
 assertEquals(3, link2.getPosNew());
 assertEquals(0, link2.getPosOrig());
 System.out.flush(); // flush print statements for proper test execution
 }

 @Test(expected = NullPointerException.class)
 public void testDiffNullInput() {
 String a = null;
 String b = "barbazqux";
 RelativeString result = StringIncrementor.diff(a, b);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testDiffNullInputB() {
 String a = "foo";
 String b = null;
 RelativeString result = StringIncrementor.diff(a, b);
 fail("Expected NullPointerException");
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String patched = StringIncrementor.patch(a, r);
 assertEquals("", patched);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "foo";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(2, 1, 1));
 String patched = StringIncrementor.patch(a, r);
 assertEquals("foob", patched);
 }

 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "foo";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(2, 1, 1));
 r.addLink(new StringLink(5, 3, 3));
 String patched = StringIncrementor.patch(a, r);
 assertEquals("foobar", patched);
 }

 @Test
 public void testPatchVerboseMode() {
 String a = "foo";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(2, 1, 1));
 r.addLink(new StringLink(5, 3, 3));
 String patched = StringIncrementor.patch(a, r, true);
 assertEquals("foobar", patched);
 System.out.flush(); // flush print statements for proper test execution
 }

 @Test
 public void testPatchNullInputA() {
 String a = null;
 RelativeString r = new RelativeString();
 String patched = StringIncrementor.patch(a, r);
 fail("Expected NullPointerException");
 }

 @Test
 public void testPatchNullInputR() {
 String a = "foo";
 RelativeString r = null;
 String patched = StringIncrementor.patch(a, r);
 fail("Expected NullPointerException");
 }
}