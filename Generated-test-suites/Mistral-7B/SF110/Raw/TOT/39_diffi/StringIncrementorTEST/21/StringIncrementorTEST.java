import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDiff_SameStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDiff_IdenticalSubstrings() {
 String a = "hello world hello";
 String b = "hello world hello world";
 RelativeString expected = new StringLink(5, 5, 5);
 RelativeString result = StringIncrementor.diff(a, b, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDiff_DifferentSubstrings() {
 String a = "hello world";
 String b = "world hello world";
 RelativeString expected = new StringLink(0, 5, 6)
 .concatenate(new StringLink(11, 11, 5));
 RelativeString result = StringIncrementor.diff(a, b, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDiff_Error_NullString() {
 String a = null;
 String b = "test";
 Assert.assertThrows(IllegalArgumentException.class, () -> StringIncrementor.diff(a, b, false));
 }

 @Test
 public void testDiff_Error_NullRelativeString() {
 String a = "test";
 RelativeString r = null;
 Assert.assertThrows(IllegalArgumentException.class, () -> StringIncrementor.patch(a, r, false));
 }

 @Test
 public void testPatch_EmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_SameStrings() {
 String a = "hello";
 RelativeString r = new RelativeString();
 String expected = "hello";
 String result = StringIncrementor.patch(a, r, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_IdenticalSubstrings() {
 String a = "hello world hello";
 RelativeString r = new StringLink(5, 5, 5);
 String expected = "hello world hello";
 String result = StringIncrementor.patch(a, r, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_DifferentSubstrings() {
 String a = "hello world";
 RelativeString r = new StringLink(0, 5, 6)
 .concatenate(new StringLink(11, 11, 5));
 String expected = "world world hello world";
 String result = StringIncrementor.patch(a, r, false);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_Error_NullString() {
 String a = null;
 RelativeString r = new RelativeString();
 Assert.assertThrows(IllegalArgumentException.class, () -> StringIncrementor.patch(a, r, false));
 }

 @Test
 public void testPatch_Error_NullPatch() {
 String a = "test";
 RelativeString r = null;
 Assert.assertThrows(IllegalArgumentException.class, () -> StringIncrementor.patch(a, r, false));
 }
}