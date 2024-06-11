package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

 // Testing all methods in StringIncrementor class

 // Testing method: diff(String a, String b)
 @Test
 public void testDiff() {
 String a = "abcdefghijklmno";
 String b = "xyzabcdefghijklmno";
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(4, 3, 4));
 assertEquals(expected, result);
 }

 // Testing method: diff(String a, String b, boolean verbose)
 @Test
 public void testDiffWithVerbose() {
 String a = "abcdefghijklmno";
 String b = "xyzabcdefghijklmno";
 RelativeString result = StringIncrementor.diff(a, b, true);
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(4, 3, 4));
 System.out.println("Expected Output:");
 System.out.println(expected);
 String actualOutput = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput);
 assertEquals(expected.toString(), actualOutput);
 }

 // Testing method: diff(String a, String b) with edge cases
 @Test
 public void testDiffEdgeCases() {
 String a1 = "";
 String b1 = "abc";
 RelativeString result1 = StringIncrementor.diff(a1, b1);
 RelativeString expected1 = new RelativeString();
 assertEquals(expected1, result1);

 String a2 = "abc";
 String b2 = "";
 RelativeString result2 = StringIncrementor.diff(a2, b2);
 RelativeString expected2 = new RelativeString();
 assertEquals(expected2, result2);

 String a3 = "ab";
 String b3 = "ab";
 RelativeString result3 = StringIncrementor.diff(a3, b3);
 RelativeString expected3 = new RelativeString();
 assertEquals(expected3, result3);
 }

 // Testing method: diff(String a, String b, boolean verbose) with edge cases
 @Test
 public void testDiffWithVerboseEdgeCases() {
 String a1 = "";
 String b1 = "abc";
 RelativeString result1 = StringIncrementor.diff(a1, b1, true);
 RelativeString expected1 = new RelativeString();
 assertEquals(expected1, result1);
 System.out.println("Expected Output:");
 System.out.println(expected1);
 String actualOutput1 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput1);
 assertEquals(expected1.toString(), actualOutput1);

 String a2 = "abc";
 String b2 = "";
 RelativeString result2 = StringIncrementor.diff(a2, b2, true);
 RelativeString expected2 = new RelativeString();
 assertEquals(expected2, result2);
 System.out.println("Expected Output:");
 System.out.println(expected2);
 String actualOutput2 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput2);
 assertEquals(expected2.toString(), actualOutput2);

 String a3 = "ab";
 String b3 = "ab";
 RelativeString result3 = StringIncrementor.diff(a3, b3, true);
 RelativeString expected3 = new RelativeString();
 assertEquals(expected3, result3);
 System.out.println("Expected Output:");
 System.out.println(expected3);
 String actualOutput3 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput3);
 assertEquals(expected3.toString(), actualOutput3);
 }

 // Testing method: patch(String a, RelativeString r)
 @Test
 public void testPatch() {
 String a = "abcdefghijklmno";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(4, 3, 4));
 String result = StringIncrementor.patch(a, r);
 String expected = "xyzabcdefghijklmno";
 assertEquals(expected, result);
 }

 // Testing method: patch(String a, RelativeString r, boolean verbose)
 @Test
 public void testPatchWithVerbose() {
 String a = "abcdefghijklmno";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(4, 3, 4));
 String result = StringIncrementor.patch(a, r, true);
 String expected = "xyzabcdefghijklmno";
 System.out.println("Expected Output:");
 System.out.println(expected);
 String actualOutput = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput);
 assertEquals(expected, actualOutput);
 }

 // Testing method: patch(String a, RelativeString r) with edge cases
 @Test
 public void testPatchEdgeCases() {
 String a1 = "";
 RelativeString r1 = new RelativeString();
 String result1 = StringIncrementor.patch(a1, r1);
 assertEquals("", result1);

 String a2 = "";
 RelativeString r2 = new RelativeString();
 r2.addLink(new StringLink(0, 0, 1));
 String result2 = StringIncrementor.patch(a2, r2);
 assertEquals("", result2);

 String a3 = "abc";
 RelativeString r3 = new RelativeString();
 r3.addLink(new StringLink(0, 2, 3));
 String result3 = StringIncrementor.patch(a3, r3);
 assertEquals("cdef", result3);

 String a4 = "abc";
 RelativeString r4 = new RelativeString();
 r4.addLink(new StringLink(1, 1, 1));
 String result4 = StringIncrementor.patch(a4, r4);
 assertEquals("bac", result4);
 }

 // Testing method: patch(String a, RelativeString r, boolean verbose) with edge cases
 @Test
 public void testPatchWithVerboseEdgeCases() {
 String a1 = "";
 RelativeString r1 = new RelativeString();
 String result1 = StringIncrementor.patch(a1, r1, true);
 assertEquals("", result1);
 System.out.println("Expected Output:");
 System.out.println("");
 String actualOutput1 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput1);
 assertEquals("", actualOutput1);

 String a2 = "";
 RelativeString r2 = new RelativeString();
 r2.addLink(new StringLink(0, 0, 1));
 String result2 = StringIncrementor.patch(a2, r2, true);
 assertEquals("", result2);
 System.out.println("Expected Output:");
 System.out.println("");
 String actualOutput2 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println("Actual Output:");
 System.out.println(actualOutput2);
 assertEquals("", actualOutput2);

 String a3 = "abc";
 RelativeString r3 = new RelativeString();
 r3.addLink(new StringLink(0, 2, 3));
 String result3 = StringIncrementor.patch(a3, r3, true);
 assertEquals("cdef", result3);
 System.out.println("Expected Output:");
 System.out.println("Adding 3 absolute Bytes from pos#0");
 System.out.println("Adding Link: def @ 0%");
 System.out.println("Actual Output:");
 String actualOutput3 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println(actualOutput3);
 assertEquals("Adding 3 absolute Bytes from pos#0\nAdding Link: def @ 0%", actualOutput3);

 String a4 = "abc";
 RelativeString r4 = new RelativeString();
 r4.addLink(new StringLink(1, 1, 1));
 String result4 = StringIncrementor.patch(a4, r4, true);
 assertEquals("bac", result4);
 System.out.println("Expected Output:");
 System.out.println("Adding 1 absolute Byte from pos#1");
 System.out.println("Adding Link: ac @ 1%");
 System.out.println("Actual Output:");
 String actualOutput4 = System.out.getHistory(System.out.size() - 1).replaceAll("\\[|\\]", "").trim();
 System.out.println(actualOutput4);
 assertEquals("Adding 1 absolute Byte from pos#1\nAdding Link: ac @ 1%", actualOutput4);
 }
}