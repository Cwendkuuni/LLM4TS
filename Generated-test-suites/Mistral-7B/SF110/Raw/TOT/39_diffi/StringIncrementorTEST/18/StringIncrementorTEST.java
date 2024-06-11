import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString result = StringIncrementor.diff("", "");
 assertNotNull(result);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffIdenticalStrings() {
 RelativeString result = StringIncrementor.diff("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz");
 assertNotNull(result);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 RelativeString result = StringIncrementor.diff("abcdefghijklmnopqrstuvwxyz", "abcdeffghijklmnopqrstuvwxyz");
 assertNotNull(result);
 StringLink expectedLink = new StringLink(5, 5, 2);
 assertEquals(expectedLink.toString(), result.getLink(0).toString());
 }

 @Test
 public void testDiffMultipleDifferences() {
 RelativeString result = StringIncrementor.diff("abcabcabcabc", "abcdabcdabcd");
 assertNotNull(result);
 StringLink link1 = new StringLink(1, 1, 1);
 StringLink link2 = new StringLink(4, 4, 1);
 StringLink link3 = new StringLink(7, 7, 1);
 StringLink[] expectedLinks = {link1, link2, link3};
 for (int i = 0; i < result.linksCount(); ++i) {
 StringLink link = result.getLink(i);
 assertTrue(Arrays.binarySearch(expectedLinks, link) >= 0);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullString() {
 StringIncrementor.diff(null, "abcdefghijklmnopqrstuvwxyz");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullSecondString() {
 StringIncrementor.diff("abcdefghijklmnopqrstuvwxyz", null);
 }

 @Test
 public void testPatchEmptyStrings() {
 String result = StringIncrementor.patch("", new RelativeString());
 assertNotNull(result);
 assertEquals("", result);
 }

 @Test
 public void testPatchIdenticalStrings() {
 String result = StringIncrementor.patch("abcdefghijklmnopqrstuvwxyz", new RelativeString());
 assertNotNull(result);
 assertEquals("abcdefghijklmnopqrstuvwxyz", result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String result = StringIncrementor.patch("abcdefghijklmnopqrstuvwxyz", new RelativeString(new StringLink(5, 5, 2), "ef"));
 assertNotNull(result);
 assertEquals("abcdeffghijklmnopqrstuvwxyz", result);
 }

 @Test
 public void testPatchMultipleDifferences() {
 String result = StringIncrementor.patch("abcabcabcabc", new RelativeString(new StringLink(1, 1, 1), new StringLink(4, 4, 1), new StringLink(7, 7, 1)));
 assertNotNull(result);
 assertEquals("abcdabcdabcd", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullString() {
 StringIncrementor.patch(null, new RelativeString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullRelativeString() {
 StringIncrementor.patch("abcdefghijklmnopqrstuvwxyz", null);
 }
}