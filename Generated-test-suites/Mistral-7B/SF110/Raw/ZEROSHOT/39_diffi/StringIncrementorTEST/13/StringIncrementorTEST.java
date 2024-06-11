package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringIncrementorTest {

 @BeforeClass
 public static void setUp() {
 System.out.println("### Test Setup ###");
 }

 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("", result.getAbsolute());
 assertFalse(result.linksCount() > 0);
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "TestString";
 String b = "TestString";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals("", result.getAbsolute());
 assertFalse(result.linksCount() > 0);
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "TestString";
 String b = "TestStringX";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new StringBuilder("TestString").append("X").toString();
 assertEquals(expected.toString(), result.getAbsolute());
 assertTrue(result.linksCount() > 0);
 }

 @Test
 public void testDiffMultipleDifferences() {
 String a = "TestString";
 String b = "TestStringXZ";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new StringBuilder("TestString").append("X").append("Z").toString();
 assertEquals(expected.toString(), result.getAbsolute());
 assertTrue(result.linksCount() > 0);
 }

 @Test
 public void testDiffVerbose() {
 String a = "TestString123";
 String b = "TestString456";
 System.setOut(new PrintStream(new ByteArrayOutputStream())); // Clear standard output to test verbose flag
 RelativeString result = StringIncrementor.diff(a, b, true);
 String expected = new StringBuilder("TestString123").append("456").toString();
 assertEquals(expected.toString(), result.getAbsolute());
 assertTrue(result.linksCount() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "TestString";
 try {
 StringIncrementor.diff(a, b);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test passed
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullRelativeString() {
 String a = "TestString";
 RelativeString r = null;
 try {
 StringIncrementor.patch(a, r);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test passed
 }
 }

 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "TestString";
 RelativeString r = new StringLink(4, 5, 1);
 String expected = "TestStringX";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchMultipleDifferences() {
 String a = "TestString";
 RelativeString r = new StringLink(4, 5, 1)
 .addLink(new StringLink(8, 10, 2))
 .addLink(new StringLink(12, 14, 2));
 String expected = "TestStringXZZ";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchVerbose() {
 String a = "TestString123";
 RelativeString r = new StringLink(4, 5, 1)
 .addLink(new StringLink(8, 10, 2))
 .addLink(new StringLink(12, 14, 2));
 System.setOut(new PrintStream(new ByteArrayOutputStream())); // Clear standard output to test verbose flag
 String result = StringIncrementor.patch(a, r, true);
 String expected = "TestString123XZZ";
 assertEquals(expected, result);
 }
}