import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString expected = new RelativeString();
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "example";
 String b = "example";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "hello";
 String b = "hellp";
 RelativeString expected = new StringLink(5, 5, 1).addTo(new RelativeString());
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffMultipleCharacterDifferences() {
 String a = "hello";
 String b = "hellWorld";
 RelativeString expected = new StringLink(5, 5, 1).addTo(new StringLink(10, 10, 6).addTo(new RelativeString()));
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "test";
 StringIncrementor.diff(a, b, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullRelativeString() {
 String a = "test";
 RelativeString r = null;
 StringIncrementor.patch(a, r, false);
 }

 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "hello";
 RelativeString r = new StringLink(5, 5, 1).addTo(new RelativeString());
 String expected = "hellp";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchMultipleCharacterDifferences() {
 String a = "hello";
 RelativeString r = new StringLink(5, 5, 1).addTo(new StringLink(10, 10, 6).addTo(new RelativeString()));
 String expected = "hellWorld";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullString() {
 String a = null;
 String patch = "test";
 StringIncrementor.patch(a, patch, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullPatch() {
 String a = "test";
 String patch = null;
 StringIncrementor.patch(a, patch, false);
 }
}