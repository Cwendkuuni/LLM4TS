import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 RelativeString expected = new RelativeString();
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "helloworld";
 String b = "helloworld";
 RelativeString expected = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "abcd";
 String b = "abce";
 RelativeString expected = new StringLink(3, 3, 1);
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffMultipleDifferences() {
 String a = "abcd";
 String b = "efghijkabcde";
 RelativeString expected = new RelativeString() {{
 addLink(new StringLink(3, 8, 5));
 addLink(new StringLink(11, 11, 1));
 }};
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStrings() {
 String a = null;
 String b = "hello";
 try {
 StringIncrementor.diff(a, b);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffNullStringParameter() {
 String a = "hello";
 String b = null;
 try {
 StringIncrementor.diff(a, b);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testPatchEmptyString() {
 String a = "";
 RelativeString r = new RelativeString();
 String expected = "";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "abcd";
 RelativeString r = new StringLink(3, 3, 1);
 String expected = "abce";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchMultipleDifferences() {
 String a = "abcd";
 RelativeString r = new RelativeString() {{
 addLink(new StringLink(3, 8, 5));
 addLink(new StringLink(11, 11, 1));
 }};
 String expected = "efghijkabcde";
 String result = StringIncrementor.patch(a, r);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchNullString() {
 String a = null;
 RelativeString r = new RelativeString();
 try {
 StringIncrementor.patch(a, r);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchNullRelativeString() {
 String a = "hello";
 RelativeString r = null;
 try {
 StringIncrementor.patch(a, r);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }
}