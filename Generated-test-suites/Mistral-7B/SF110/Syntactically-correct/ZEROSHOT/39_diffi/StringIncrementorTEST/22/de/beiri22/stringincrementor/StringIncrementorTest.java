package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

 @Test
 public void testDiff_sameStrings() {
 String a = "testtest";
 String b = "testtest";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertNotNull(result);
assertEquals(new RelativeString(), result);
 }

 @Test
 public void testDiff_emptyString() {
 String a = "";
 String b = "test";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertNotNull(result);
 assertEquals(new RelativeString(new StringLink(0, 0, 4)), result);
 }

 @Test
 public void testDiff_simpleAddition() {
 String a = "test";
 String b = "testhi";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertNotNull(result);
 assertEquals(new RelativeString(new StringLink(4, 0, 1)), result);
 }

 @Test
 public void testDiff_simpleDeletion() {
 String a = "test";
 String b = "tes";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertNotNull(result);
 assertEquals(new RelativeString(new StringLink(3, 3, 1)), result);
 }

 @Test
 public void testDiff_multipleChanges() {
 String a = "test";
 String b = "teshi";
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertNotNull(result);
 assertEquals(new RelativeString(new StringLink(3, 0, 1), new StringLink(5, 3, 1)), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_stringSizesMismatch() {
 String a = "test";
 String b = "testtesttest";
 StringIncrementor.diff(a, b, true);
 }

 @Test
 public void testPatch_simpleAddition() {
 String a = "test";
 RelativeString r = new RelativeString(new StringLink(4, 0, 1));
 String result = StringIncrementor.patch(a, r, true);
 assertNotNull(result);
 assertEquals("testhi", result);
 }

 @Test
 public void testPatch_simpleDeletion() {
 String a = "test";
 RelativeString r = new RelativeString(new StringLink(3, 3, 1));
 String result = StringIncrementor.patch(a, r, true);
 assertNotNull(result);
 assertEquals("test", result);
 }

 @Test
 public void testPatch_multipleChanges() {
 String a = "test";
 RelativeString r = new RelativeString(new StringLink(3, 0, 1), new StringLink(5, 3, 1));
 String result = StringIncrementor.patch(a, r, true);
 assertNotNull(result);
 assertEquals("teshi", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_relativeStringLinksCountMismatch() {
 String a = "test";
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r, true);
 }

 @Test
 public void testPatch_absoluteStringLongerThanResult() {
 String a = "test";
 RelativeString r = new RelativeString(new StringLink(3, 0, 1));
 String result = StringIncrementor.patch(a, r, true);
 String longerString = result.substring(0, result.length() - 3) + "x";
 StringIncrementor.patch(result, new RelativeString(new StringLink(result.length(), result.length(), 1)), true);
 String longerResult = StringIncrementor.patch(result, new RelativeString(new StringLink(result.length(), result.length(), 1)), true);
 assertNotNull(longerResult);
 assertEquals(longerString, longerResult);
 }

}