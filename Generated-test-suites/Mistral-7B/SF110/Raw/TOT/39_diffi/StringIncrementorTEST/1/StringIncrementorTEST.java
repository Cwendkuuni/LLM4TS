import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_IdenticalStrings() {
 String a = "hello world";
 String b = "hello world";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_OneCharacterDifference() {
 String a = "hello world";
 String b = "hello wold";
 RelativeString expected = new StringLink(4, 4, 1);
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_LongerSequenceDifference() {
 String a = "abracadabra";
 String b = "abracadabrabrabrabrabra";
 RelativeString expected = new StringLink(4, 12, 8)
 .addLink(new StringLink(12, 13, 1))
 .addLink(new StringLink(13, 15, 2))
 .addLink(new StringLink(15, 17, 2))
 .addLink(new StringLink(17, 19, 2))
 .addLink(new StringLink(19, 21, 2))
 .addLink(new StringLink(21, 24, 3));
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiff_EdgeCase_EmptyStringB() {
 String a = "hello world";
 String b = "";
 RelativeString expected = new RelativeString(a);
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_Error_NullStringA() {
 String b = "hello world";
 RelativeString result = StringIncrementor.diff(null, b);
 fail("Expected IllegalArgumentException, but got: " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_Error_NullStringB() {
 String a = "hello world";
 RelativeString result = StringIncrementor.diff(a, null);
 fail("Expected IllegalArgumentException, but got: " + result);
 }

 @Test
 public void testPatch_EmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_Identity() {
 String a = "hello world";
 RelativeString r = new RelativeString();
 String expected = a;
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_SingleCharacterInsertion() {
 String a = "hello world";
 RelativeString r = new StringLink(4, 4, 1);
 String expected = "hello wold";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_MultipleCharacterInsertions() {
 String a = "hello world";
 RelativeString r = new StringLink(4, 12, 8)
 .addLink(new StringLink(12, 13, 1))
 .addLink(new StringLink(13, 15, 2))
 .addLink(new StringLink(15, 17, 2))
 .addLink(new StringLink(17, 19, 2))
 .addLink(new StringLink(19, 21, 2))
 .addLink(new StringLink(21, 24, 3));
 String expected = "abracadabrabrabrabrabrahello world";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_EdgeCase_EmptyStringResult() {
 String a = "hello world";
 RelativeString r = new RelativeString();
 String expected = a;
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatch_Error_NullStringA() {
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(null, r);
 fail("Expected NullPointerException, but got: " + result);
 }

 @Test
 public void testPatch_Error_NullStringR() {
 String a = "hello world";
 String result = StringIncrementor.patch(a, null);
 fail("Expected NullPointerException, but got: " + result);
 }

 @Test
 public void testPatch_Error_InvalidLink() {
 String a = "hello world";
 RelativeString r = new StringLink(10, 5, 1)
 .addLink(new StringLink(12, 13, 1));
 String result = StringIncrementor.patch(a, r);
 fail("Expected IllegalArgumentException, but got: " + result);
 }
}