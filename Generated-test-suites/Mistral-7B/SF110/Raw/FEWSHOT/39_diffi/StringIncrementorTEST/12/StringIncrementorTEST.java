package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest {

 // Test for diff method with no arguments
 @Test
 public void testDiffNoArgs() {
 String a = "abcdef";
 String b = "xyzabcdef";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString("xyz-3 x 1 abcdef"), result);
 }

 // Test for diff method with verbose mode
 @Test
 public void testDiffVerbose() {
 String a = "abcdef";
 String b = "xyzabcdef";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals(new RelativeString("xyz-3 x 1 abcdef"), result);
 System.out.println("Expected verbose output:");
 System.out.println("-3, 0-3: abc to xyz @ 0%");
 System.out.println("1, 5-6: e to e @ 50%");
 }

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertNull(result);
 }

 // Test for diff method with null strings
 @Test (expected = NullPointerException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "abcdef";
 StringIncrementor.diff(a, b);
 }

 // Test for diff method with different lengths
 @Test
 public void testDiffDifferentLengths() {
 String a = "abcdef";
 String b = "xyzabcdefg";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString("xyz-3 x 1 abcdef g"), result);
 }

 // Test for patch method with no arguments
 @Test
 public void testPatchNoArgs() {
 String a = "abcdef";
 RelativeString r = new RelativeString("xyz-3 x 1 abcdef");
 String result = StringIncrementor.patch(a, r);
 assertEquals("xyzabcdef", result);
 }

 // Test for patch method with verbose mode
 @Test
 public void testPatchVerbose() {
 String a = "abcdef";
 RelativeString r = new RelativeString("xyz-3 x 1 abcdef");
 String result = StringIncrementor.patch(a, r, true);
 assertEquals("xyzabcdef", result);
 System.out.println("Expected verbose output:");
 System.out.println("Adding 3 absolute Bytes from pos#0");
 System.out.println("Adding Link: xyz-3");
 System.out.println("Adding 1 absolute Bytes from pos#5");
 System.out.println("Adding Link: 1, 5-6: e to e @ 50%");
 }

 // Test for patch method with empty absolute string
 @Test
 public void testPatchEmptyAbsoluteString() {
 String a = "abcdef";
 RelativeString r = new RelativeString("xyz-3 x 1 abcdef");
 String result = StringIncrementor.patch(a, r);
 assertEquals("xyzabcdef", result);
 }

 // Test for patch method with null relative string
 @Test (expected = NullPointerException.class)
 public void testPatchNullRelativeString() {
 String a = "abcdef";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }

 // Test for patch method with different lengths
 @Test
 public void testPatchDifferentLengths() {
 String a = "abcdef";
 RelativeString r = new RelativeString("xyz-3 x 1 abcdef g h ijk");
 String result = StringIncrementor.patch(a, r);
 assertEquals("xyzabcdef g h ijk", result);
 }
}