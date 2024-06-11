package de.beiri22.stringincrementor.test;

import org.junit.Assert;
import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_SameStrings() {
 String a = "abcdabcd";
 String b = "abcdabcd";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(-1, -1, 0);
 expectedResult.addLink(link);
 String actualResult = StringIncrementor.diff(a, b);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 StringTestHelper.assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 @Test
 public void testDiff_OneCharacterDifference() {
 String a = "abcdabcd";
 String b = "abcdabce";
 RelativeString expectedResult = new RelativeString();
 StringLink link = new StringLink(7, 8, 1);
 expectedResult.addLink(link);
 String actualResult = StringIncrementor.diff(a, b);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDiff_LargeStrings() {
 String a = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
 String b = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabc";
 RelativeString expectedResult = new RelativeString();
 StringLink link1 = new StringLink(26, 53, 27);
 StringLink link2 = new StringLink(52, 53, 1);
 expectedResult.addLink(link1);
 expectedResult.addLink(link2);
 String actualResult = StringIncrementor.diff(a, b);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullStrings() {
 String a = null;
 String b = "abcdabcd";
 StringIncrementor.diff(a, b);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullStringLink() {
 String a = "abcdabcd";
 String b = "abcdabcd";
 RelativeString result = StringIncrementor.diff(a, b);
 StringLink nullLink = null;
 result.setAbsolute(nullLink.getAbsolute());
 }

 @Test
 public void testPatch_SameStrings() {
 String a = "abcdabcd";
 String b = "abcdabcd";
 String expectedResult = "abcdabcd";
 String actualResult = StringIncrementor.patch(a, StringIncrementor.diff(a, b));
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testPatch_EmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String expectedResult = "";
 String actualResult = StringIncrementor.patch(a, r);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testPatch_OneCharacterDifference() {
 String a = "abcdabcd";
 String b = "abcdabce";
 String expectedResult = "abcdabce";
 String actualResult = StringIncrementor.patch(a, StringIncrementor.diff(a, b));
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testPatch_LargeStrings() {
 String a = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
 String b = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabc";
 String expectedResult = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabc";
 String actualResult = StringIncrementor.patch(a, StringIncrementor.diff(a, b));
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testPatch_ErrorScenario_ZeroLengthString() {
 String a = "";
 RelativeString r = new RelativeString();
 String actualResult = StringIncrementor.patch(a, r);
 Assert.assertEquals("", actualResult);
 }

 @Test
 public void testPatch_ErrorScenario_NullString() {
 String a = "abcdabcd";
 String b = null;
 String actualResult = StringIncrementor.patch(a, StringIncrementor.diff(a, b));
 Assert.assertNotNull(actualResult);
 }
}