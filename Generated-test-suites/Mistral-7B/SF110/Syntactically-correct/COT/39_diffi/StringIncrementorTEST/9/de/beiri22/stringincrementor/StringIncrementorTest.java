package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest {

 private static final String STRING_A = "abcdabcdabcdabcd";
 private static final String STRING_B = "efghabcdabcdabcdefgh";
 private static final String STRING_C = "ijklabcdabcdabcdijkl";

 @Test
 public void testDiffMethodWithTwoIdenticalStrings() {
 RelativeString expectedResult = new RelativeString();
 assertEquals(expectedResult, StringIncrementor.diff(STRING_A, STRING_A, false));
 }

 @Test
 public void testDiffMethodWithTwoDifferentStrings() {
 final RelativeString result = StringIncrementor.diff(STRING_A, STRING_B, false);
 final RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(7, 4, 3));
 expectedResult.addLink(new StringLink(15, 12, 3));
 expectedResult.setAbsolute("efghijkl");
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffMethodWithNullString() {
 StringIncrementor.diff(null, STRING_B, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffMethodWithNullSecondString() {
 StringIncrementor.diff(STRING_A, null, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testDiffMethodWithOutOfBoundsIndex() {
 StringIncrementor.diff(STRING_A, STRING_B, true);
 final int lengthB = STRING_B.length();
 final int index = lengthB + 1;
 StringIncrementor.diff(STRING_A, STRING_B.substring(0, index), true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffMethodWithVerboseFlagAndZeroLengthStringB() {
 StringIncrementor.diff("", STRING_B, true);
 }

 @Test
 public void testPatchMethodWithEmptyRelativeString() {
 final String result = StringIncrementor.patch(STRING_A, new RelativeString(), false);
 assertEquals(STRING_A, result);
 }

 @Test
 public void testPatchMethodWithSimpleRelativeString() {
 final RelativeString relativeString = new RelativeString();
 relativeString.addLink(new StringLink(7, 4, 3));
 relativeString.addLink(new StringLink(15, 12, 3));
 final String result = StringIncrementor.patch(STRING_A, relativeString, false);
 final String expectedResult = "efghabcdabcdabcdefgh";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testPatchMethodWithRelativeStringLongerThanOriginalString() {
 final RelativeString relativeString = new RelativeString();
 relativeString.addLink(new StringLink(0, 0, STRING_C.length()));
 final String result = StringIncrementor.patch(STRING_A, relativeString, false);
 final String expectedResult = "ijklabcdabcdabcdijkl";
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchMethodWithNullStringA() {
 StringIncrementor.patch(null, new RelativeString(), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchMethodWithNullRelativeString() {
 StringIncrementor.patch(STRING_A, null, false);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPatchMethodWithOutOfBoundsIndex() {
 final RelativeString relativeString = new RelativeString();
 final StringLink link = new StringLink(Integer.MAX_VALUE, 0, 1);
 relativeString.addLink(link);
 final String result = StringIncrementor.patch(STRING_A, relativeString, false);
 assertEquals(STRING_A, result);
 }

 @Test
 public void testPatchMethodWithRelativeStringAndVerboseFlag() {
 final RelativeString relativeString = new RelativeString();
 relativeString.addLink(new StringLink(7, 4, 3));
 relativeString.addLink(new StringLink(15, 12, 3));
 final String result = StringIncrementor.patch(STRING_A, relativeString, true);
 final String expectedResult = "efghabcdabcdabcd[Adding 7 absolute Bytes from pos#0]efg[Adding 3 absolute Bytes from pos#3]abcdabcd[Adding 3 absolute Bytes from pos#10]efgh";
 System.out.println(result);
 assertEquals(expectedResult, result);
 }
}