import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

 private final static String STRING_A = "The quick brown fox jumps over the lazy dog.";
 private final static String STRING_B = "The quick brown fox jumps over the very lazy dog.";
 private final static String STRING_C = "The quick brown fox jumps over the dog.";
 private final static String STRING_D = "The quick brown fox jumps over the dog. The quick brown fox jumps over the dog.";
 private final static String STRING_E = "The quick brown fox jumps over the dog. The quick brown fox jumps over the dog.";
 private final static String STRING_F = "The quick brown fox jumps over the dog. The quick brown fox jumps over the dog. The quick brown fox jumps over the dog.";

 @Test
 public void testDiff_sameStrings() {
 RelativeString expected = new RelativeString();
 RelativeString actual = StringIncrementor.diff(STRING_A, STRING_A, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_emptyStrings() {
 RelativeString expected = new RelativeString();
 RelativeString actual = StringIncrementor.diff("", "", true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_nullStrings() {
 try {
 StringIncrementor.diff(null, null, true);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testDiff_shortStringA_longStringB() {
 RelativeString expected = new RelativeString("(13)" + StringLink.SEPARATOR + "0-12" + StringLink.SEPARATOR + "13");
 RelativeString actual = StringIncrementor.diff(STRING_A, STRING_B, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_longStringA_shortStringB() {
 RelativeString expected = new RelativeString("(13)" + StringLink.SEPARATOR + "0-12" + StringLink.SEPARATOR + "13");
 RelativeString actual = StringIncrementor.diff(STRING_B, STRING_A, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_sameSubstringAtDifferentPositions() {
 RelativeString expected = new RelativeString("(13)" + StringLink.SEPARATOR + "6-18" + StringLink.SEPARATOR + "10-22");
 RelativeString actual = StringIncrementor.diff(STRING_D, STRING_E, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDiff_differentSubstringLengths() {
 RelativeString expected = new RelativeString("(13)" + StringLink.SEPARATOR + "0-12" + StringLink.SEPARATOR + "13" + StringLink.SEPARATOR + "11-14" + StringLink.SEPARATOR + "13");
 RelativeString actual = StringIncrementor.diff(STRING_F, STRING_F, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_emptyString() {
 String expected = "";
 String actual = StringIncrementor.patch("", new RelativeString(), true);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_relativeString_emptyString() {
 String expected = "";
 String actual = StringIncrementor.patch(STRING_A, new RelativeString(), true);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_relativeString_string() {
 String expected = "The quick brown fox jumps over the very lazy dog.";
 String actual = StringIncrementor.patch(STRING_A, new RelativeString("(13)" + StringLink.SEPARATOR + "0-12" + StringLink.SEPARATOR + "13"), true);
 assertEquals(expected, actual);
 }

 @Test
 public void testPatch_string_relativeString() {
 String expected = "The quick brown fox jumps over the very lazy dog.";
 String actual = StringIncrementor.patch(STRING_B, new RelativeString("(13)" + StringLink.SEPARATOR + "0-12" + StringLink.SEPARATOR + "13"), true);
 assertEquals(expected, actual);
 }
}