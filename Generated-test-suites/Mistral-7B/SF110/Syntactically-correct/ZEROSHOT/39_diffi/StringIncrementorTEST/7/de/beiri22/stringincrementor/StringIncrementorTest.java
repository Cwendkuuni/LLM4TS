package de.beiri22.stringincrementor.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiffMethodWithTwoEmptyStrings() {
 RelativeString expectedResult = new RelativeString();
 RelativeString actualResult = StringIncrementor.diff("", "", false);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDiffMethodWithTwoSameStrings() {
 RelativeString expectedResult = new RelativeString();
 String testString = "testtest";
 RelativeString actualResult = StringIncrementor.diff(testString, testString, false);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDiffMethodWithDifferentStrings() {
 String testStringA = "testtest";
 String testStringB = "testtest1";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(5, 5, 1));
 StringLink link = expectedResult.getLink(0);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 RelativeString actualResult = StringIncrementor.diff(testStringA, testStringB, false);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDiffMethodWithVerboseMode() {
 String testStringA = "testtest";
 String testStringB = "testtest1";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(5, 5, 1));
 StringLink link = expectedResult.getLink(0);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 System.out.print("Expected Result: ");
 System.out.println(expectedResult);
 RelativeString actualResult = StringIncrementor.diff(testStringA, testStringB, true);
 System.out.print("Actual Result: ");
 System.out.println(actualResult);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffMethodWithNullString() {
 String testStringA = null;
 String testStringB = "testtest";
 StringIncrementor.diff(testStringA, testStringB, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffMethodWithNullStringB() {
 String testStringA = "testtest";
 String testStringB = null;
 StringIncrementor.diff(testStringA, testStringB, false);
 }

 @Test
 public void testPatchMethodWithEmptyStringAndEmptyRelativeString() {
 String expectedResult = "";
 String actualResult = StringIncrementor.patch("", new RelativeString());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testPatchMethodWithEmptyStringAndNonEmptyRelativeString() {
 String testString = "testtest";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(5, 5, 1);
 expectedResult.addLink(link);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 String actualResult = StringIncrementor.patch(testString, expectedResult, false);
 assertEquals(expectedResult.getAbsolute(), actualResult);
 }

 @Test
 public void testPatchMethodWithEmptyStringAndNonEmptyRelativeStringVerbose() {
 String testString = "testtest";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(5, 5, 1);
 expectedResult.addLink(link);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 System.out.print("Expected Result: ");
 System.out.println(expectedResult);
 String actualResult = StringIncrementor.patch(testString, expectedResult, true);
 System.out.print("Actual Result: ");
 System.out.println(actualResult);
 assertEquals(expectedResult.getAbsolute(), actualResult);
 }

 @Test
 public void testPatchMethodWithNonEmptyStringAndEmptyRelativeString() {
 String testString = "testtest";
 RelativeString expectedResult = new RelativeString();
 String actualResult = StringIncrementor.patch(testString, expectedResult, false);
 assertEquals(testString, actualResult);
 }

 @Test
 public void testPatchMethodWithNonEmptyStringAndNonEmptyRelativeString() {
 String testString = "testtest";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(5, 5, 1);
 expectedResult.addLink(link);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 String actualResult = StringIncrementor.patch(testString, expectedResult, false);
 assertEquals(expectedResult.getAbsolute(), actualResult);
 }

 @Test
 public void testPatchMethodWithNonEmptyStringAndNonEmptyRelativeStringVerbose() {
 String testString = "testtest";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(5, 5, 1);
 expectedResult.addLink(link);
 expectedResult.setAbsolute("testtest" + link.getOperation() + "testtest1");
 System.out.print("Expected Result: ");
 System.out.println(expectedResult);
 String actualResult = StringIncrementor.patch(testString, expectedResult, true);
 System.out.print("Actual Result: ");
 System.out.println(actualResult);
 assertEquals(expectedResult.getAbsolute(), actualResult);
 }
}