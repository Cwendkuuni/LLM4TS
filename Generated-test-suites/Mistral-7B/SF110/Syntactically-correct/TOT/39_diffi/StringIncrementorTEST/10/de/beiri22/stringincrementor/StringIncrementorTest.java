import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expected = new RelativeString();
 assertTrue(result.equals(expected));
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expected = new RelativeString();
 assertTrue(result.equals(expected));
 }

 @Test
 public void testDiffOneCharDifference() {
 String a = "abcdef";
 String b = "abcdey";
 RelativeString result = StringIncrementor.diff(a, b);
 StringLink expectedLink = new StringLink(5, 5, 1);
 StringLink actualLink = result.getLink(0);
 assertEquals(expectedLink, actualLink);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullString() {
 String a = null;
 String b = "abcdef";
 try {
 StringIncrementor.diff(a, b);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStringB() {
 String a = "abcdef";
 String b = null;
 try {
 StringIncrementor.diff(a, b);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 String expected = "";
 assertTrue(result.equals(expected));
 }

 @Test
 public void testPatchOneLink() {
 String a = "abcabcabc";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 3));
 String result = StringIncrementor.patch(a, r);
 String expected = "abcxxxabc";
 assertEquals(expected, result);
 }

 @Test
 public void testPatchMultipleLinks() {
 String a = "abcabcabc";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 3, 3));
 r.addLink(new StringLink(6, 6, 4));
 String result = StringIncrementor.patch(a, r);
 String expected = "abcxxxabcxxxx";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullString() {
 String a = null;
 RelativeString r = new RelativeString();
 try {
 StringIncrementor.patch(a, r);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullStringResult() {
 String a = "abcabcabc";
 RelativeString r = null;
 try {
 StringIncrementor.patch(a, r);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}