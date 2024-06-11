import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testStringDiff() {
 String string1 = "The quick brown fox jumps over the lazy dog";
 String string2 = "The quick brown fox jumps over the lazy dog and the lazy cat";
 RelativeString expectedResult = new RelativeString("3 jumps over the (2) lazy ", new StringLink(5, 21, 7), new StringLink(23, 34, 11));
 StringIncrementor.diff(string1, string2); // Calling the method without storing the result

 String result = new StringIncrementor().diff(string1, string2, true).getAbsolute(); // Testing the verbose mode
 assertEquals(expectedResult.getAbsolute(), result);
 }

 @Test
 public void testStringDiff_EmptyStrings() {
 String string1 = "";
 String string2 = "";
 RelativeString expectedResult = new RelativeString();
 StringIncrementor.diff(string1, string2, true); // Calling the method without storing the result
 String result = new StringIncrementor().diff(string1, string2, true).getAbsolute();
 assertEquals(expectedResult.getAbsolute(), result);
 }

 @Test
 public void testStringDiff_EmptyStringAndNonEmptyString() {
 String string1 = "";
 String string2 = "The quick brown fox jumps over the lazy dog";
 RelativeString expectedResult = new RelativeString("The quick brown fox jumps over the lazy dog");
 StringIncrementor.diff(string1, string2, true); // Calling the method without storing the result
 String result = new StringIncrementor().diff(string1, string2, true).getAbsolute();
 assertEquals(expectedResult.getAbsolute(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringDiff_StringIsNull() {
 String nullString = null;
 String string2 = "The quick brown fox jumps over the lazy dog";
 StringIncrementor.diff(nullString, string2); // Calling the method without storing the result
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringDiff_String2IsNull() {
 String string1 = "The quick brown fox jumps over the lazy dog";
 String nullString2 = null;
 StringIncrementor.diff(string1, nullString2); // Calling the method without storing the result
 }

 @Test
 public void testStringPatch() {
 String string1 = "The quick brown fox jumps over the lazy dog";
 RelativeString relativeString = new RelativeString("3 jumps over the (2) lazy ", new StringLink(5, 21, 7), new StringLink(23, 34, 11));
 String expectedResult = "The quick brown fox jumps over the lazy dog and the lazy cat";
 String result = new StringIncrementor().patch(string1, relativeString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testStringPatch_EmptyStringAndRelativeString() {
 String string1 = "";
 RelativeString relativeString = new RelativeString();
 String expectedResult = "";
 String result = new StringIncrementor().patch(string1, relativeString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testStringPatch_StringAndEmptyRelativeString() {
 String string1 = "The quick brown fox jumps over the lazy dog";
 RelativeString emptyRelativeString = new RelativeString();
 String expectedResult = "The quick brown fox jumps over the lazy dog";
 String result = new StringIncrementor().patch(string1, emptyRelativeString);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringPatch_StringIsNull() {
 String nullString = null;
 RelativeString relativeString = new RelativeString();
 StringIncrementor.patch(nullString, relativeString);
 }
}