import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.*;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

 @Test
 public void testDiff_emptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString(), result);
 }

 @Test
 public void testDiff_sameStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(new RelativeString(), result);
 }

 @Test
 public void testDiff_singleCharacterDifference() {
 String a = "hello";
 String b = "helo";
 RelativeString result = StringIncrementor.diff(a, b);
 StringLink expectedLink = new StringLink(4, 3, 1);
 assertEquals(expectedLink, result.getLink(0));
 }

 @Test
 public void testDiff_multipleCharacterDifferences() {
 String a = "hello";
 String b = "helohelo";
 RelativeString result = StringIncrementor.diff(a, b);
 StringLink link1 = new StringLink(4, 3, 1);
 StringLink link2 = new StringLink(10, 6, 3);
 assertEquals(link1, result.getLink(0));
 assertEquals(link2, result.getLink(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullString() {
 String a = null;
 String b = "hello";
 StringIncrementor.diff(a, b);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullStringB() {
 String a = "hello";
 String b = null;
 StringIncrementor.diff(a, b);
 }

 @Test
 public void testPatch_emptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String patchedString = StringIncrementor.patch(a, r);
 assertEquals("", patchedString);
 }

 @Test
 public void testPatch_singleCharacterDifference() {
 String a = "hello";
 RelativeString r = new StringLink(4, 3, 1);
 String patchedString = StringIncrementor.patch(a, r);
 assertEquals("helo", patchedString);
 }

 @Test
 public void testPatch_multipleCharacterDifferences() {
 String a = "hello";
 RelativeString r = new StringLink(4, 3, 1)
 + new StringLink(10, 6, 3);
 String patchedString = StringIncrementor.patch(a, r);
 assertEquals("helohelo", patchedString);
 }

 @Test
 public void testPatch_longStringWithManyLinks() {
 String a = "abcdefghijklmnopqrstuvwxyz";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(3, 2, 1));
 r.addLink(new StringLink(7, 5, 2));
 r.addLink(new StringLink(11, 9, 2));
 r.addLink(new StringLink(15, 13, 2));
 String patchedString = StringIncrementor.patch(a, r);
 String expected = "abcabcdefefefefefefefefefefefefefef";
 assertEquals(expected, patchedString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_nullString() {
 String a = null;
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_nullRelativeString() {
 String a = "hello";
 RelativeString r = null;
 StringIncrementor.patch(a, r);
 }
}