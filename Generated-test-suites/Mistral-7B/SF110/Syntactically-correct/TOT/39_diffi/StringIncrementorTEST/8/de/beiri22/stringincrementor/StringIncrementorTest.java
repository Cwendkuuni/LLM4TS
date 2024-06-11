import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

 @Test
 public void testDiff_EmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 Assert.assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiff_IdenticalStrings() {
 String a = "abcdefghijklmnopqrst";
 String b = "abcdefghijklmnopqrst";
 RelativeString result = StringIncrementor.diff(a, b);
 Assert.assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiff_SingleCharDifference() {
 String a = "abcdefghijklmnopqrst";
 String b = "abcdefghijklmnopqrstu";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = new StringLink(5, 5, 1).toString();
 Assert.assertEquals(expected, result.toString());
 }

 @Test
 public void testDiff_MultipleDifferences() {
 String a = "abcdefghijklmnopqrst";
 String b = "xyzabcdefghijklmnopqrstuvw";
 RelativeString result = StringIncrementor.diff(a, b);
 String expected = "Link(0, 3, 3) Link(7, 7, 1) Link(11, 12, 1) Link(13, 14, 1) Link(14, 15, 1) Link(15, 16, 1) Link(16, 17, 1)";
 Assert.assertEquals(expected, result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_NullStrings() {
 String a = null;
 String b = "abcdefghijklmnopqrst";
 StringIncrementor.diff(a, b);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDiff_OutOfBounds() {
 String a = "abcdefghijklmnopqrst";
 String b = "abcdefghijklmnopqrstuvwxyz";
 StringIncrementor.diff(a, b.substring(13));
 }

 @Test
 public void testPatch_EmptyStringAndEmptyLinkList() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r);
 Assert.assertEquals("", result);
 }

 @Test
 public void testPatch_StringAndLinkListWithSingleLink() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new RelativeString().addLink(new StringLink(5, 5, 1));
 String result = StringIncrementor.patch(a, r);
 String expected = "abcdefghijklmnopqrstu";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_StringAndLinkListWithMultipleLinks() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new RelativeString()
 .addLink(new StringLink(5, 5, 1))
 .addLink(new StringLink(11, 13, 2))
 .addLink(new StringLink(15, 15, 1));
 String result = StringIncrementor.patch(a, r);
 String expected = "abcdefghijklmnopqrstuvw";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPatch_StringAndLinkListWithOutOfBoundsLink() {
 String a = "abcdefghijklmnopqrst";
 RelativeString r = new RelativeString().addLink(new StringLink(15, 15, 1));
 String result = StringIncrementor.patch(a, r);
 Assert.assertEquals("abcdefghijklmnopqrst", result);
 }
}